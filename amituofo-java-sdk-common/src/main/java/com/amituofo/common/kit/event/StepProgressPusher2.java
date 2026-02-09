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

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.type.ReadProgressEvent;

public class StepProgressPusher2 {
	private final Queue<ProgressData> queue = new LinkedList<ProgressData>();
	private boolean loop = true;
	private CountDownLatch latch = new CountDownLatch(1);
	private final StepProgressListener progressListener;

	private ProgressData lastProgressStep = null;

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
			return (System.currentTimeMillis() - timestamp) > 500;
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

	public StepProgressPusher2(final StepProgressListener progressListener) {
		this.progressListener = progressListener;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int pushcnt = 0;
					do {
						pushcnt = pushRemains();
						if (pushcnt == 0) {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
							}
							if (lastProgressStep != null) {
								synchronized (lastProgressStep) {
									if (lastProgressStep != null) {
										if (lastProgressStep.isStepOn()) {
											if (lastProgressStep.hasStep()) {
												queue.add(lastProgressStep);
												lastProgressStep = null;
											} else {
												lastProgressStep.reset();
											}
										}
									}
								}
							}

						}
					} while (loop);

					// 处理剩余的
					pushRemains();
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}
		}).start();
	}

	public void push(ReadProgressEvent event, int step) {
		if (lastProgressStep == null) {
			lastProgressStep = new ProgressData();
		}

		synchronized (lastProgressStep) {
			if (lastProgressStep != null) {
				if (event == ReadProgressEvent.BYTE_READ_END_EVENT) {
					lastProgressStep.addStep(step);
					queue.add(lastProgressStep);
					queue.add(new ProgressData(ReadProgressEvent.BYTE_READ_END_EVENT));
					lastProgressStep = null;
				} else {
					lastProgressStep.addStep(step);
					if (lastProgressStep.isStepOn()) {
						if (lastProgressStep.hasStep()) {
							queue.add(lastProgressStep);
							lastProgressStep = null;
						} else {
							lastProgressStep.reset();
						}
					}
				}
			}
		}
	}

	public void stop() {
		this.loop = false;

		try {
			latch.await();
		} catch (Throwable e) {
		}
	}

	private int pushRemains() {
		if (queue.isEmpty()) {
			return 0;
		}

		int cnt = 0;
		try {
			ProgressData dt = queue.poll();
			while (dt != null) {
				progressListener.progressChanged(dt.event, dt.step);
				cnt++;

//				if (dt.event == ReadProgressEvent.BYTE_READ_END_EVENT) {
//					loop = false;
//					break;
//				}

				dt = queue.poll();
			}
		} catch (Throwable e) {
		}

		return cnt;
	}

}
