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
package com.amituofo.common.kit.value;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import com.amituofo.common.api.ObjectHandler;

public class ValuePusher<T> {
	private final Queue<T> queue = new LinkedList<>();
	private boolean loop = true;
	private CountDownLatch latch = new CountDownLatch(1);
	private final ObjectHandler<Void, T> handler;

	public ValuePusher(final ObjectHandler<Void, T> handler) {
		this.handler = handler;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int pushcnt = 0;
					do {
						pushcnt = pushRemains();
						if (pushcnt == 0) {
							try {
								Thread.sleep(500);
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

	public void push(T value) {
		queue.add(value);
	}

	public void stop() {
		this.loop = false;

		try {
			latch.await();
		} catch (Throwable e) {
		}
	}

	private int pushRemains() {
		if (queue.size() == 0) {
			return 0;
		}

		int cnt = 0;
		try {
			T dt = queue.poll();
			while (dt != null) {
				handler.handle(null, dt);
				cnt++;

				dt = queue.poll();
			}
		} catch (Throwable e) {
		}

		return cnt;
	}

}
