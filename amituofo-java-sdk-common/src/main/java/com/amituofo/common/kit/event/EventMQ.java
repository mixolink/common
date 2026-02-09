package com.amituofo.common.kit.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import com.amituofo.common.api.InterruptableRunnable;

public class EventMQ {
	private final Map<String, Queue<EventPayload>> eventmq = new HashMap<String, Queue<EventPayload>>();
	private final Map<String, List<EventHandler>> eventh = new HashMap<String, List<EventHandler>>();
	private final Map<String, InterruptableRunnable> evenpusher = new HashMap<String, InterruptableRunnable>();

	public EventMQ() {
	}

	public void push(String topic) {
		push(topic, null);
	}

	public void push(String topic, Object msg) {
		synchronized (eventmq) {
			Queue<EventPayload> msgq = eventmq.get(topic);
			if (msgq == null) {
				msgq = new LinkedList<EventPayload>();
				eventmq.put(topic, msgq);
			}
			msgq.add(new EventPayload(topic, msg));
		}
	}

	public void register(String topic, EventHandler eventHandler) {
		if (eventHandler == null) {
			return;
		}

		List<EventHandler> eventhandlers = eventh.get(topic);
		if (eventhandlers == null) {
			final List<EventHandler> ehs = new ArrayList<EventHandler>();
			eventh.put(topic, ehs);
			ehs.add(eventHandler);

			InterruptableRunnable runner = new InterruptableRunnable() {

				@Override
				public void run() {
					while (!isInterrupted()) {
						Queue<EventPayload> msgq = eventmq.get(topic);
						if (msgq != null && msgq.size() > 0) {
							EventPayload ep = msgq.poll();
							for (EventHandler eventHandler : ehs) {
								try {
								eventHandler.receive(ep.getKey(), ep.getValue());
								} catch (Throwable e) {
								}
							}
						} else {
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
							}
						}
					}
				}

				@Override
				public boolean interrupt() throws InterruptedException {
					return true;
				}
			};
			evenpusher.put(topic, runner);

			new Thread(runner, "EventMQ-" + topic).start();
		} else {
			eventhandlers.add(eventHandler);
		}
	}

	public void close() {
		if (evenpusher.size() != 0) {
			evenpusher.values().forEach(new Consumer<InterruptableRunnable>() {

				@Override
				public void accept(InterruptableRunnable t) {
					try {
						t.interrupt();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
