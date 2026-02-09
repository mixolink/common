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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.amituofo.common.api.ProgressListener;

public class EventPusher {
	private final static Map<String, ThreadPoolExecutor> tpe = new HashMap<String, ThreadPoolExecutor>();

	public void push(final Runnable runnable) {
		push("DEFAULT", runnable);
	}

	public void push(String channel, final Runnable runnable) {
		ThreadPoolExecutor t = null;
		synchronized (tpe) {
			t = tpe.get(channel);
			if (t == null) {
				t = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
				tpe.put(channel, t);
			}
		}

		t.execute(runnable);
	}

	public void reset() {
		Collection<ThreadPoolExecutor> ts = tpe.values();
		if (ts != null) {
			for (ThreadPoolExecutor threadPoolExecutor : ts) {
				threadPoolExecutor.shutdownNow();
			}
			tpe.clear();
		}
	}

}
