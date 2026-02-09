package com.amituofo.common.kit.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProperlyStopThread extends Thread {
	public final  AtomicBoolean requestStop = new AtomicBoolean(false);
	public final CountDownLatch latch = new CountDownLatch(1);

	public ProperlyStopThread() {
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	public ProperlyStopThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

}
