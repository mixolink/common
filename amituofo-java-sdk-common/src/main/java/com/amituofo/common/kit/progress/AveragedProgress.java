package com.amituofo.common.kit.progress;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.type.ReadProgressEvent;

public class AveragedProgress implements StepProgressListener {
	private final StepProgressListener delegate;
	private final int targetCount;
	private long workerBytes;
	private long reportedBytes;

	public AveragedProgress(StepProgressListener delegate, int targetCount) {
		this.delegate = delegate;
		this.targetCount = Math.max(1, targetCount);
	}

	@Override
	public synchronized void progressChanged(ReadProgressEvent event, int len) {
		if (len > 0) {
			workerBytes += len;
			emitUntil(workerBytes / targetCount);
		}
	}

	public synchronized void complete(long expectedBytes) {
		emitUntil(Math.max(0, expectedBytes));
		delegate.progressChanged(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
	}

	private void emitUntil(long targetBytes) {
		while (reportedBytes < targetBytes) {
			int delta = (int) Math.min(Integer.MAX_VALUE, targetBytes - reportedBytes);
			delegate.progressChanged(ReadProgressEvent.BYTE_READING_EVENT, delta);
			reportedBytes += delta;
		}
	}
}