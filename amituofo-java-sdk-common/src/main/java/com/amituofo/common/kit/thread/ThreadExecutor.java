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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.amituofo.common.kit.counter.Counter;

public class ThreadExecutor extends ThreadPoolExecutor {
//	private boolean seal = false;
	private ExecuteHandler handler;
//	private Counter count = new Counter();
//	private CountDownLatch latch = new CountDownLatch(1);

	public ThreadExecutor(int poolSize) {
		this(poolSize, poolSize);
	}

	public ThreadExecutor(int corePoolSize, int maxPoolSize) {
		super(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public void seal() {
//		seal = true;

//		synchronized (count) {
//			// 如果count是0代表没有正在运行的线程，直接结束；
//			if (count.n == 0) {
//				// System.out.println(id+" Found all finished");
//				latch.countDown();
//			}
//		}

		this.shutdown();
	}

	public int getQueueSize() {
		return this.getQueue().size();
	}

	public boolean isSealed() {
		return this.isShutdown();
//		return seal;
	}

	public void setExecuteHandler(ExecuteHandler handler) {
		this.handler = handler;
	}

//	@Override
//	public void execute(Runnable command) {
//		if (isSealed()) {
//			return;
//		}
//
//		synchronized (count) {
//			count.n++;
//		}
//
//		super.execute(command);
//	}

	// @Override
	// protected void beforeExecute(Thread t, Runnable r) {
	// }

//	@Override
//	protected void afterExecute(Runnable r, Throwable t) {
//		synchronized (count) {
//			count.n--;
//			// System.out.println("isSealed=" + this.isSealed() + " getActiveCount=" + getActiveCount() + " getQueue().size()=" + getQueue().size() + "
//			// count=" + count);
//			// System.out.println(Thread.currentThread().getId()+" isSealed=" + isSealed() + " count=" + count.i);
//
//			// 每个线程运行完count-1，如果为0不代表为最后一个，有可能还在添加exec
//			// 如果封口了并且count=0代表此为最后一个，即结束线程池。
//			// 也有可能所有线程都已运行完毕带还没有封口，此时将不会结束线程池。此种情况参见seal()
////			if (isSealed() && count.n == 0) {
////				latch.countDown();
////			}
//		}
//	}

	public void waitForComplete() {
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//		}
		// System.out.println(id+" waitForComplete...Completed.");

//		this.shutdownNow();
		try {
			super.awaitTermination(7, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (handler != null) {
			handler.finalExecute();
		}
	}

//	@Override
//	public void shutdown() {
////		latch.countDown();
//		super.shutdown();
//	}

//	@Override
//	public List<Runnable> shutdownNow() {
//		latch.countDown();
//		return super.shutdownNow();
//	}
}
