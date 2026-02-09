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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import com.amituofo.common.api.ProgressListener;

public class ProgressPusher<EVENT_TYPE, DATA_TYPE> {
	private final Queue<Object[]> queue = new ConcurrentLinkedQueue<Object[]>();
	private boolean loop = true;
	private CountDownLatch latch = new CountDownLatch(1);
	private final ProgressListener<EVENT_TYPE, DATA_TYPE> progressListener;

	public ProgressPusher(final ProgressListener<EVENT_TYPE, DATA_TYPE> progressListener) {
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

	public void push(EVENT_TYPE eventType, DATA_TYPE data) {
		queue.offer(new Object[] { eventType, data });
	}

	public void stop() {
		this.loop = false;

		try {
			latch.await();
		} catch (Throwable e) {
//			e.printStackTrace();
		}
	}

	private int pushRemains() {
		int cnt = 0;
		try {
			Object[] dt = queue.poll();
			while (dt != null) {
				EVENT_TYPE eventType = (EVENT_TYPE) dt[0];
				DATA_TYPE data = (DATA_TYPE) dt[1];
				progressListener.progressChanged(eventType, data);
				cnt++;

				dt = queue.poll();
			}
		} catch (Throwable e) {
//			e.printStackTrace();
		}

		return cnt;
	}
	
//	private int pushRemains() {
//		int cnt = 0;
//		try {
//			int sum=0;
//			Object[] dt = queue.poll();
//			while (dt != null) {
//				EVENT_TYPE eventType = (EVENT_TYPE) dt[0];
//				DATA_TYPE data = (DATA_TYPE) dt[1];
//				
//				cnt++;
//
//				sum+=((Integer)data).intValue();
//				
//				dt = queue.poll();
//			}
//			
//			progressListener.progressChanged(eventType, (DATA_TYPE)sum);
//			
//		} catch (Throwable e) {
////			e.printStackTrace();
//		}
//
//		return cnt;
//	}

}
