package com.amituofo.common.kit.interrupt;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.api.Interruptable;

public abstract class InterruptableProcesser implements Interruptable {
	protected ProcesserStatus status = ProcesserStatus.Idel;
	private Interrupter interrupter = new Interrupter();
	private String name = null;

	protected Logger log = null;

	public boolean isRunning() {
		return status == ProcesserStatus.Running;
	}

	public boolean isRunnable() {
		return !isInterrupted() && status != ProcesserStatus.Running && status != ProcesserStatus.Running;
	}

	public boolean isInterrupted() {
		return interrupter.isInterrupted();// || status == ProcesserStatus.Stoped;
	}

	public ProcesserStatus getProcesserStatus() {
		return status;
	}

	public void setProcesserStatus(ProcesserStatus status) {
		this.status = status;
	}

	public boolean interrupt() throws InterruptedException {
		interrupter.markInterrupted(true);

		if (log != null && log.isDebugEnabled() && name == null) {
			name = this.getClass().getSimpleName() + "@" + Thread.currentThread().getName();
		}

		boolean stoped = tryStop();
		if (stoped) {
			while (isRunning()) {
				if (name != null) {
					log.info("Waitting Processer [" + name + "] stop...");
				}
				Thread.sleep(300);
			}
		}

		if (name != null) {
			if (stoped) {
				log.info("Processer [" + name + "] stopped.");
			} else {
				log.warn("Processer [" + name + "] stop NG.");
			}
		}

		return stoped;
	}

	protected abstract boolean tryStop() throws InterruptedException;

}
