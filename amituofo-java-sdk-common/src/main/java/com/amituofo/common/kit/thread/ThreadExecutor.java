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
package com.amituofo.common.kit.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor extends ThreadPoolExecutor {
	private ExecuteHandler handler;

	public ThreadExecutor(int poolSize) {
		this(poolSize, poolSize);
	}

	public ThreadExecutor(int corePoolSize, int maxPoolSize) {
		super(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(maxPoolSize * 10), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void seal() {
		this.shutdown();
	}

	public int getQueueSize() {
		return this.getQueue().size();
	}

	public boolean isSealed() {
		return this.isShutdown();
	}

	public void setExecuteHandler(ExecuteHandler handler) {
		this.handler = handler;
	}

	public void waitForComplete() {
		try {
			super.awaitTermination(7, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (handler != null) {
			handler.finalExecute();
		}
	}
}
