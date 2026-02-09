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
import java.util.concurrent.LinkedBlockingDeque;

import com.amituofo.common.api.ProcessEvent;

public class AsynchronousProcess<T> {
	private final Queue<Object[]> queue = new LinkedBlockingDeque<Object[]>();
	private boolean loop = true;
	private boolean isLooping = true;
	private final ProcessEvent<T> process;

	public AsynchronousProcess(final ProcessEvent<T> process) {
		this.process = process;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				isLooping = true;
				while (loop) {
					Object[] dt = queue.poll();
					if (dt != null) {
						int event = ((Integer)dt[0]).intValue();
						T data = (T) dt[1];
						AsynchronousProcess.this.process.valueChanged(event, data);
					} else {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}
				}
				
				
				// 处理剩余的
				Object[] dt = queue.poll();
				while (dt != null) {
					int event = ((Integer)dt[0]).intValue();
					T data = (T) dt[1];
					process.valueChanged(event, data);
					
					dt = queue.poll();
				}
				
				isLooping = false;
			}
		}).start();
	}

	public void push(int event, T data) {
		queue.add(new Object[] { Integer.valueOf(event), data });
	}

	public synchronized void stopWhenFinished() {
		loop = false;

		while (isLooping) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
