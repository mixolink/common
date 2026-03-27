package com.amituofo.common.kit.retry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.InterruptableRunnable;
import com.amituofo.common.define.Constants;
import com.amituofo.common.define.DatetimeFormat;
import com.amituofo.common.ex.IDConflictException;
import com.amituofo.common.ex.InitializeException;
import com.amituofo.common.type.HandleFeedback;
import com.amituofo.common.util.NumberUtils;
import com.amituofo.common.util.RandomUtils;
import com.amituofo.common.util.SystemUtils;
import com.amituofo.common.util.ThreadUtils;

public class RetryHelper {

//	private final static Map<String, Timer> TIMERS = new HashMap<>();
	private final static Map<String, TH> THREADS = new HashMap<>();

	public static int DEFAULT_RETRY_INTERVAL = 300;
	public static int DEFAULT_RETRY_TIMES = 3;

	static {
		SystemUtils.addShutdownHook(new Runnable() {

			@Override
			public void run() {
//				System.info("Shutdown log system...");
				RetryHelper.stopAllRetrys();
			}
		});
	}

	public static interface TryBody {
		void setRetryInterval(long retryInterval);
	}

	private static class TH {
		Thread thread;
		InterruptableRunnable runnable;

		public TH(Thread thread, InterruptableRunnable runnable) {
			super();
			this.thread = thread;
			this.runnable = runnable;
		}
	}

	private abstract static class TryerBase<T> extends InterruptableRunnable implements TryBody {
		int retrytimes = 1;
		ReturnValue<T> returnval = null;

		final String id;

		final int maxRetryTimes;
		final long waitIntervalFirst;

		long retryInterval;

		final ReturnableTry<T> action;
		final Callback<ReturnValue<T>> resultCallback;
		final boolean forever;

		public TryerBase(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, int maxRetryTimes, long waitIntervalFirst, long retryInterval,
				boolean forever) {
			super();
			this.id = id;
			this.maxRetryTimes = maxRetryTimes;
			this.waitIntervalFirst = waitIntervalFirst;
			this.retryInterval = retryInterval;
			this.action = action;
			this.resultCallback = resultCallback;
			this.forever = forever;

			this.action.trybody = this;
		}

		@Override
		public void setRetryInterval(long retryInterval) {
			this.retryInterval = retryInterval;
		}

		protected void initAction() throws InitializeException {
			action.init();
		}

		protected void finalAction() {
			synchronized (THREADS) {
				THREADS.remove(id);
			}

			try {
				action.release();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

		protected void executeAction() {
			if (isInterrupted()) {
				return;
			}

			try {
				returnval = action.run();
			} catch (Throwable e1) {
				e1.printStackTrace();
				returnval = ReturnValue.failed(null);
			}

			try {
				if (returnval == null || returnval.getResult() == HandleFeedback.failed) {
					retrytimes++;

					if (maxRetryTimes > 0 && retrytimes > maxRetryTimes) {
						stopCycle();
						return;
					}
				} else {
					stopCycle();
					return;
				}
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

		private void stopCycle() {
			if (forever) {
				return;
			}

			try {
				interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (resultCallback != null) {
					resultCallback.callback(returnval == null ? ReturnValue.failed(null) : returnval);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

//	private static class ThreadSleepTryer<T> extends TryerBase<T> {
//		public ThreadSleepTryer(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, int maxRetryTimes,
//				long waitIntervalFirst, long retryInterval, boolean forever) {
//			super(id, action, resultCallback, maxRetryTimes, waitIntervalFirst, retryInterval, forever);
//		}
//
//		@Override
//		public void run() {
//			if (waitIntervalFirst > 0) {
//				try {
//					Thread.sleep(waitIntervalFirst);
//				} catch (InterruptedException e) {
//				}
//
//				if (isInterrupted()) {
//					return;
//				}
//			}
//
//			initAction();
//
//			while (!isInterrupted()) {
//				executeAction();
//
//				try {
//					Thread.sleep(retryInterval);
//				} catch (InterruptedException e) {
//				}
//			}
//
//			finalAction();
//		}
//	};

	private static class ThreadSleepTryer<T> extends TryerBase<T> {
		public ThreadSleepTryer(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, int maxRetryTimes, long waitIntervalFirst,
				long retryInterval, boolean forever) {
			super(id, action, resultCallback, maxRetryTimes, waitIntervalFirst, retryInterval, forever);
		}

		@Override
		public void run() {
			if (waitIntervalFirst > 0) {
				if (waitIntervalFirst > 500) {
					long remain = waitIntervalFirst;
					long sleepTime = 500;
					do {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
						}

						if (isInterrupted()) {
							return;
						}

						remain -= 500;

						if (remain <= 0) {
							break;
						}

						if (remain < 500) {
							sleepTime = remain;
						}
					} while (true);
				} else {
					try {
						Thread.sleep(waitIntervalFirst);
					} catch (InterruptedException e) {
					}
				}

				if (isInterrupted()) {
					return;
				}
			}

			try {
				initAction();
			} catch (InitializeException e1) {
				e1.printStackTrace();
				return;
			}

			try {
				while (true) {
					executeAction();

					if (isInterrupted()) {
						break;
					}

					if (retryInterval > 500) {
						long remain = retryInterval;
						long sleepTime = 500;
						do {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
							}

							if (isInterrupted()) {
								break;
							}

							remain -= 500;

							if (remain <= 0) {
								break;
							}

							if (remain < 500) {
								sleepTime = remain;
							}
						} while (true);
					} else {
						try {
							Thread.sleep(waitIntervalFirst);
						} catch (InterruptedException e) {
						}
					}
				}
			} finally {
				finalAction();
			}
		}
	};

	private static class TimerTryer<T> extends TryerBase<T> {
		private Timer timer;

		public TimerTryer(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, int maxRetryTimes, long waitIntervalFirst, long retryInterval,
				boolean forever) {
			super(id, action, resultCallback, maxRetryTimes, waitIntervalFirst, retryInterval, forever);
			this.timer = new Timer("Timer-Retry-" + id);
		}

		@Override
		public void run() {
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						initAction();
					} catch (InitializeException e1) {
						e1.printStackTrace();
						timer.cancel();
						return;
					}

					try {
						executeAction();
					} catch (Throwable e1) {
						e1.printStackTrace();
					} finally {
						finalAction();
					}

				}
			}, waitIntervalFirst, retryInterval);
		}

		@Override
		public boolean interrupt() throws InterruptedException {
			timer.cancel();
			return super.interrupt();
		}
	};

	private static class FixTimerTryer<T> extends TryerBase<T> {
		private Timer timer;
		private Date firstTimeAt;

		public FixTimerTryer(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, int maxRetryTimes, long retryInterval, Date firstTimeAt,
				boolean forever) {
			super(id, action, resultCallback, maxRetryTimes, -1, retryInterval, forever);
			this.timer = new Timer("FixRateTimer-Retry-" + id);
			this.firstTimeAt = firstTimeAt;
		}

		@Override
		public void run() {
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					try {
						initAction();
					} catch (InitializeException e1) {
						e1.printStackTrace();
						timer.cancel();
						return;
					}

					try {
						executeAction();
					} catch (Throwable e1) {
						e1.printStackTrace();
					} finally {
						finalAction();
					}

				}
			}, firstTimeAt, retryInterval);
		}

		@Override
		public boolean interrupt() throws InterruptedException {
			timer.cancel();
			return super.interrupt();
		}
	};

	private static AtomicLong SEQ = new AtomicLong(2000);

	public static String generateID(String prefix) {
		synchronized (SEQ) {
			return prefix + "@" + SEQ.incrementAndGet();
		}
	}

	public static <T> RetryService<T> buildContinuallyRetryService(TryAction<T> retryAction, String actionDescription, RetryPolicy<T> retryPolicy, Logger log) {
		RetryService<T> rs = new SimpleRetryService<T>().withTryAction(retryAction, actionDescription).withRetryPolicy(retryPolicy).withLogger(log);
		return rs;
	}

	public static <T> ReturnValue<T> waitingRetry3Times(ReturnableTry<T> action) {
		return waitingRetry(action, 3, DEFAULT_RETRY_INTERVAL, 0);
	}

	public static <T> ReturnValue<T> waitingRetry3Times(ReturnableTry<T> action, long retryInterval) {
		return waitingRetry(action, 3, retryInterval, 0);
	}

	public static <T> ReturnValue<T> waitingRetry(ReturnableTry<T> action, int retryTimes) {
		return waitingRetry(action, retryTimes, DEFAULT_RETRY_INTERVAL, 0);
	}

	public static <T> ReturnValue<T> waitingRetry(ReturnableTry<T> action, final int maxRetryTimes, long retryInterval) {
		return waitingRetry(action, maxRetryTimes, retryInterval, 0);
	}

	public static <T> ReturnValue<T> waitingRetry(ReturnableTry<T> action, final int maxRetryTimes, long retryInterval, long waitIntervalFirst) {
		ReturnValue<T> returnval = null;
		int retrytimes = maxRetryTimes;

		if (waitIntervalFirst > 0) {
			try {
				Thread.sleep(waitIntervalFirst);
			} catch (InterruptedException e) {
			}
		}

		while (maxRetryTimes <= 0 || retrytimes > 0) {
			try {
				returnval = action.run();
			} catch (Throwable e1) {
				e1.printStackTrace();
				returnval = ReturnValue.failed(null);
			}

			if (returnval == null || returnval.getResult() == HandleFeedback.failed) {
				retrytimes--;

				if (retryInterval > 0) {
					try {
						Thread.sleep(retryInterval);
					} catch (InterruptedException e) {
					}
				}
			} else {
				break;
			}
		}

		return returnval == null ? ReturnValue.failed(null) : returnval;
	}

	public static void stopRetry(String id) {
		synchronized (THREADS) {
			TH th = THREADS.remove(id);
			if (th != null) {
				try {
					th.runnable.interrupt();
//					th.thread.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void asyncStopRetry(String id) {
		asyncStopRetry(id, null);
	}

	public static void asyncStopRetry(String id, Callback<InterruptableRunnable> interruptCallback) {
		synchronized (THREADS) {
			TH th = THREADS.remove(id);
			if (th == null) {
				if (interruptCallback != null) {
					interruptCallback.callback(null);
				}
				return;
			}

			ThreadUtils.run(new Runnable() {
				@Override
				public void run() {
					try {
						th.runnable.interrupt();
//						th.thread.interrupt();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (interruptCallback != null) {
							interruptCallback.callback(th.runnable);
						}
					}
				}
			});
		}
	}

	public static void stopRetryIfIdContains(String word) {
		List<String> list = new ArrayList<>();
		synchronized (THREADS) {
			Iterator<String> it = THREADS.keySet().iterator();
			while (it.hasNext()) {
				String id = it.next();
				if (id.contains(word)) {
					list.add(id);
				}
			}
		}

		for (String id : list) {
			stopRetry(id);
		}
	}

	public synchronized static void stopRetrys(String... ids) {
		if (ids == null || ids.length == 0) {
			return;
		}

		CountDownLatch latch = new CountDownLatch(ids.length);
		for (String id : ids) {
			asyncStopRetry(id, new Callback<InterruptableRunnable>() {

				@Override
				public void callback(InterruptableRunnable data) {
					latch.countDown();
				}
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
	}

	public synchronized static void stopAllRetrys() {
		List<String> list = null;
		synchronized (THREADS) {
			if (THREADS.size() == 0) {
				return;
			}

			list = new ArrayList<>();
			Iterator<String> it = THREADS.keySet().iterator();
			while (it.hasNext()) {
				String id = it.next();
				list.add(id);
			}
		}

		CountDownLatch latch = new CountDownLatch(list.size());
		for (String id : list) {
			asyncStopRetry(id, new Callback<InterruptableRunnable>() {

				@Override
				public void callback(InterruptableRunnable data) {
					latch.countDown();
				}
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
	}

	public synchronized static void asyncStopAllRetrys() {
		List<String> list = null;
		synchronized (THREADS) {
			if (THREADS.size() == 0) {
				return;
			}

			list = new ArrayList<>();
			Iterator<String> it = THREADS.keySet().iterator();
			while (it.hasNext()) {
				String id = it.next();
				list.add(id);
			}
		}

		for (String id : list) {
			asyncStopRetry(id);
		}
	}

	public static <T> void asyncRetryUntilSucceed(ReturnableTry<T> action, long retryInterval, long waitIntervalFirst) throws IDConflictException {
		asyncRetry("tempmount-" + RandomUtils.randomString(10), action, null, -1, retryInterval, waitIntervalFirst, false);
	}

	public static <T> void asyncRetryUntilSucceed(String id, ReturnableTry<T> action, long retryInterval, long waitIntervalFirst) throws IDConflictException {
		asyncRetry(id, action, null, -1, retryInterval, waitIntervalFirst, false);
	}

	public static <T> void asyncRetryUntilSucceed(String id, ReturnableTry<T> action, long retryInterval) throws IDConflictException {
		asyncRetry(id, action, null, -1, retryInterval, 0, false);
	}

	public static <T> void asyncRetryUntilSucceed(String id, ReturnableTry<T> action) throws IDConflictException {
		asyncRetry(id, action, null, -1, DEFAULT_RETRY_INTERVAL, 0, false);
	}

	public static <T> void asyncRetryUntilSucceed(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback) throws IDConflictException {
		asyncRetry(id, action, resultCallback, -1, DEFAULT_RETRY_INTERVAL, 0, false);
	}

	public static void asyncRetryForever(String id, ReturnableTry<Void> action, long retryInterval) throws IDConflictException {
		asyncRetry(id, action, null, -1, retryInterval, 0, true);
	}

	public static void asyncRetryForever(String id, ReturnableTry<Void> action, long retryInterval, long waitIntervalFirst) throws IDConflictException {
		asyncRetry(id, action, null, -1, retryInterval, waitIntervalFirst, true);
	}

	public static void asyncRetryForever(ReturnableTry<Void> action, long retryInterval, long waitIntervalFirst) {
		try {
			asyncRetry("ID_" + UUID.randomUUID().toString(), action, null, -1, retryInterval, waitIntervalFirst, true);
		} catch (IDConflictException e) {
		}
	}

	public static <T> void asyncRetry(String id, ReturnableTry<T> action, final int maxRetryTimes, long retryInterval, long waitIntervalFirst)
			throws IDConflictException {
		asyncRetry(id, action, null, maxRetryTimes, retryInterval, waitIntervalFirst, false);
	}

	public static <T> void asyncRetry(String id, ReturnableTry<T> action, final int maxRetryTimes, long retryInterval) throws IDConflictException {
		asyncRetry(id, action, null, maxRetryTimes, retryInterval, 0, false);
	}

	public static <T> void asyncRetry(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, final int maxRetryTimes) throws IDConflictException {
		asyncRetry(id, action, resultCallback, maxRetryTimes, DEFAULT_RETRY_INTERVAL, 0, false);
	}

	public static <T> void asyncRetry(String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, final int maxRetryTimes, long retryInterval)
			throws IDConflictException {
		asyncRetry(id, action, resultCallback, maxRetryTimes, retryInterval, 0, false);
	}

	public static <T> void asyncRetry3Times(ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, long retryInterval, long waitIntervalFirst) {
		try {
			asyncRetry("ID_" + UUID.randomUUID().toString(), action, resultCallback, 3, retryInterval, waitIntervalFirst, false);
		} catch (IDConflictException e) {
		}
	}

	public static <T> void asyncRetry3Times(ReturnableTry<T> action, long retryInterval) {
		asyncRetry3Times(action, null, retryInterval, 0);
	}

	public static <T> void asyncRetry3Times(ReturnableTry<T> action, long retryInterval, long waitIntervalFirst) {
		asyncRetry3Times(action, null, retryInterval, waitIntervalFirst);
	}

	public static <T> void asyncRetry3Times(ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback) {
		asyncRetry3Times(action, resultCallback, DEFAULT_RETRY_INTERVAL, 0);
	}

	public static <T> void asyncTryOnce(ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback) {
		try {
			asyncRetry("ID_" + UUID.randomUUID().toString(), action, resultCallback, 1, DEFAULT_RETRY_INTERVAL, 0, false);
		} catch (IDConflictException e) {
		}
	}

	public static <T> void asyncTryOnce(ReturnableTry<T> action) {
		asyncTryOnce(action, null);
	}

	public static void timerRetryForever(String id, ReturnableTry<Void> action, long retryInterval) throws IDConflictException {
		timerRetry(id, action, null, -1, retryInterval, 0);
	}

	public static void timerRetryForever(String id, ReturnableTry<Void> action, long retryInterval, long waitIntervalFirst) throws IDConflictException {
		timerRetry(id, action, null, -1, retryInterval, waitIntervalFirst);
	}

	public static void timerRetryAt(String id, ReturnableTry<Void> action, int maxRetryTimes, long retryInterval, Date retryAtTime) throws IDConflictException {
		timerRetry(id, action, null, maxRetryTimes, retryInterval, retryAtTime);
	}

	public static void timerRetryAt(String id, ReturnableTry<Void> action, long retryInterval, Date retryAtTime) throws IDConflictException {
		timerRetry(id, action, null, -1, retryInterval, retryAtTime);
	}

	public synchronized static <T> void asyncRetry(final String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, final int maxRetryTimes,
			final long retryInterval, final long waitIntervalFirst, boolean forever) throws IDConflictException {

		synchronized (THREADS) {
			if (THREADS.containsKey(id)) {
				throw new IDConflictException("ID " + id + " already exist.");
			}

			InterruptableRunnable runnable = new ThreadSleepTryer<T>(id, action, resultCallback, maxRetryTimes, waitIntervalFirst, retryInterval, forever);
			final Thread thread = new Thread(runnable, "Async Retryer " + id);
			thread.start();

			THREADS.put(id, new TH(thread, runnable));
		}
	}

	public static <T> void timerRetry(final String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, final int maxRetryTimes,
			final long retryInterval, final long waitIntervalFirst) throws IDConflictException {
		synchronized (THREADS) {
			if (THREADS.containsKey(id)) {
				throw new IDConflictException("ID " + id + " already exist.");
			}

			InterruptableRunnable runnable = new TimerTryer<T>(id, action, resultCallback, maxRetryTimes, waitIntervalFirst, retryInterval, true);
			final Thread thread = new Thread(runnable, "Async Retryer " + id);
			thread.start();

			THREADS.put(id, new TH(thread, runnable));
		}
	}

	public static <T> void timerRetry(final String id, ReturnableTry<T> action, Callback<ReturnValue<T>> resultCallback, final int maxRetryTimes,
			final long retryInterval, Date retryAtTime) throws IDConflictException {

		synchronized (THREADS) {
			if (THREADS.containsKey(id)) {
				throw new IDConflictException("ID " + id + " already exist.");
			}

			InterruptableRunnable runnable = new FixTimerTryer<T>(id, action, resultCallback, maxRetryTimes, retryInterval, retryAtTime, true);
			final Thread thread = new Thread(runnable, "Async Retryer " + id);
			thread.start();

			THREADS.put(id, new TH(thread, runnable));
		}
	}

	public static boolean has(String key) {
		synchronized (THREADS) {
			return THREADS.containsKey(key);
		}
	}

	public static TryBody get(String key) {
		synchronized (THREADS) {
			TH th = THREADS.get(key);
			if (th != null) {
				return (TryBody) th.runnable;
			}
		}
		return null;
	}

//	public static void main(String[] args) throws IDConflictException {
//		Calendar calendar = Calendar.getInstance();
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		int day = calendar.get(Calendar.DATE);
//		Calendar c = Calendar.getInstance();
//		c.set(year, month - 1, day, 16, 55, 0);
//		RetryHelper.timerRetryAt("act7019", new ReturnableTry<Void>() {
//
//			@Override
//			protected ReturnValue<Void> execute(int retryTime, long tryTime) {
//				System.out.println("xx" + DatetimeFormat.YYYY_MM_DD_HHMMSS.format(tryTime));
//				return null;
//			}
//
//		}, 4, Constants.TIME_MILLISECONDS_30_SECOND, c.getTime());
//
//		RetryHelper.timerRetryForever("act222", new ReturnableTry<Void>() {
//
//			@Override
//			protected ReturnValue<Void> execute(int retryTimes, long time) {
//				System.out.println("act222 " + DatetimeFormat.YYYY_MM_DD_HHMMSS.format(time));
//				return null;
//			}
//		}, Constants.TIME_MILLISECONDS_30_SECOND, 0);
//
//		RetryHelper.asyncRetryForever("act111", new ReturnableTry<Void>() {
//
//			@Override
//			protected ReturnValue<Void> execute(int retryTimes, long time) {
//				System.out.println("act111 " + DatetimeFormat.YYYY_MM_DD_HHMMSS.format(time));
//				return null;
//			}
//		}, Constants.TIME_MILLISECONDS_1_MIN, 0);
//
//		try {
//			Thread.sleep(Constants.TIME_MILLISECONDS_1_SECOND * 32);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long t1 = System.currentTimeMillis();
//		System.out.println("stoping");
//		RetryHelper.stopRetry("act111");
//		RetryHelper.stopRetry("act222");
//		RetryHelper.stopAllRetrys();
//		System.out.println("stoped" + (System.currentTimeMillis() - t1));
//
////		try {
////			Thread.sleep(100000);
////		} catch (InterruptedException e) {
////		}
//	}

}
