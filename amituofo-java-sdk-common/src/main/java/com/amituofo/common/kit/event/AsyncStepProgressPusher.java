/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package com.amituofo.common.kit.event;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.type.ReadProgressEvent;

public class AsyncStepProgressPusher {
	private Queue<ProgressData> queue = new ArrayBlockingQueue<ProgressData>(100);
	private volatile boolean loop = true;
	private CountDownLatch latch = new CountDownLatch(1);
	private StepProgressListener progressListener;

	private ProgressData lastProgressStep = new ProgressData();

	private class ProgressData {
		long timestamp = System.currentTimeMillis();
		ReadProgressEvent event = ReadProgressEvent.BYTE_READING_EVENT;
		int step = 0;

		public ProgressData() {
			super();
		}

		public ProgressData(int step) {
			super();
			this.step = step;
		}

		public ProgressData(ReadProgressEvent event) {
			super();
			this.event = event;
		}

		public boolean isStepOn() {
			return (System.currentTimeMillis() - timestamp) > 600;
		}

		public void addStep(int step) {
			this.step += step;
		}

		public void reset() {
			timestamp = System.currentTimeMillis();
		}

		public boolean hasStep() {
			return step > 0;
		}
	}

	public AsyncStepProgressPusher(final StepProgressListener progressListener) {
		this.progressListener = progressListener;

		Thread worker = new Thread(this::consumeLoop, "ProgressPusher-Worker");
		worker.setDaemon(true);
		worker.start();
	}

	private void consumeLoop() {
		try {
			do {
				if (queue.isEmpty()) {
					Thread.sleep(80);
				} else {
					push();
				}
			} while (loop);
		} catch (Throwable e) {
		} finally {
			// 处理剩余的
			push();

			latch.countDown();
		}
	}

	public void push(ReadProgressEvent event, int step) {
		lastProgressStep.addStep(step);

		if (event == ReadProgressEvent.BYTE_READ_END_EVENT) {
			queue.add(lastProgressStep);
			queue.add(new ProgressData(ReadProgressEvent.BYTE_READ_END_EVENT));
			lastProgressStep = new ProgressData();
		} else {
			if (lastProgressStep.isStepOn()) {
				if (lastProgressStep.hasStep()) {
					queue.add(lastProgressStep);
					lastProgressStep = new ProgressData();
				} else {
					lastProgressStep.reset();
				}
			}
		}
	}

	public void stop() {
		this.loop = false;

		try {
			latch.await();
		} catch (Throwable e) {
		} finally {
			progressListener = null;
			queue = null;
			lastProgressStep = null;
		}
	}

	private void push() {
		try {
			ProgressData dt;
			dt = queue.poll();
			while (dt != null) {
				progressListener.progressChanged(dt.event, dt.step);

//				if (dt.event == ReadProgressEvent.BYTE_READ_END_EVENT) {
//					loop = false;
//					break;
//				}

				dt = queue.poll();
			}
		} catch (Throwable e) {
		}
	}
}
