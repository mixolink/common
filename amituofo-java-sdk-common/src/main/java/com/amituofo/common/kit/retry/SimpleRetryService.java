package com.amituofo.common.kit.retry;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.kit.interrupt.InterruptableProcesser;
import com.amituofo.common.kit.thread.ThreadExecutor;
import com.amituofo.common.type.HandleFeedback;

public class SimpleRetryService<T> extends InterruptableProcesser implements RetryService<T> {
	private final Queue<RetryContext<T>> contextdata = new ConcurrentLinkedDeque<RetryContext<T>>();

	private int maxConcurrent = 1;
	private int idleTime = 1000 * 30;

	private ThreadExecutor executorService;
	private RetryPolicy<T> policy;
	private TryAction<T> retryAction;
	private String desc;

	public SimpleRetryService() {
		super();
	}

	@Override
	public RetryService<T> withTryAction(TryAction<T> retryAction, String actionDescription) {
		this.retryAction = retryAction;
		this.desc = actionDescription;
		return this;
	}

	@Override
	public RetryService<T> withLogger(Logger log) {
		this.log = log;
		return this;
	}

	@Override
	public RetryService<T> withRetryPolicy(RetryPolicy<T> retryPolicy) {
		this.policy = retryPolicy;
		return this;
	}

	@Override
	public void addRetryOn(T data) {
		contextdata.add(new RetryContext<T>(data));
	}

	@Override
	public RetryService<T> start() {
		if (executorService != null) {
			return this;
		}

		executorService = new ThreadExecutor(maxConcurrent);
		for (int i = 0; i < maxConcurrent; i++) {
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					while (!isInterrupted()) {
						RetryContext<T> context = contextdata.poll();

						if (context == null) {
							try {
								Thread.sleep(idleTime);
								continue;
							} catch (InterruptedException e) {
							}
						}

						if (policy.isMetRetryCondition(context)) {
							HandleFeedback hf;
							hf = retry(context);
							if (hf == HandleFeedback.failed) {
								if (policy.isNeedRetry(context)) {
									contextdata.add(context);
								}
							} else {

							}
						}

						if (policy.getEachTryIdelTime() > 0) {
							try {
								Thread.sleep(policy.getEachTryIdelTime());
								continue;
							} catch (InterruptedException e) {
							}
						}
					}
				}

				private HandleFeedback retry(RetryContext<T> context) {
					if (log != null) {
						log.warn("Retrying to [" + desc + "] on {} for {} times", context.data, context.getRetryTimes());
					}

					context.plusRetryTimes();

					long currenttime = System.currentTimeMillis();
					try {
						HandleFeedback hf = retryAction.retry(context, context.data);
						return hf;
					} catch (Exception e) {
						if (log != null) {
							log.fatal("Unknown error occured when retrying to [" + desc + "] on " + context.data, e);
						}

						return HandleFeedback.failed;
					} finally {
						context.lastUsedTime = System.currentTimeMillis() - currenttime;
						context.updateLastRetryTime();
					}
				}
			});
		}

		return this;
	}

	@Override
	public void stop() {
		try {
			super.interrupt();
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected boolean tryStop() throws InterruptedException {
		if (this.executorService != null) {
			this.executorService.shutdown();
		}
		return true;
	}
}
