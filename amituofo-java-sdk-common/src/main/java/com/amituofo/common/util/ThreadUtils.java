package com.amituofo.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.alibaba.fastjson.util.Function;
import com.amituofo.common.kit.counter.Counter;

public class ThreadUtils {
	public static class ThreadGroupRunner {
		List<Thread> list = new ArrayList<>();
		CountDownLatch latch = null;

		public ThreadGroupRunner add(Runnable run) {
			list.add(new Thread(new Runnable() {

				@Override
				public void run() {
					run.run();
				}
			}));
			return this;
		}

		public ThreadGroupRunner start() {
			latch = new CountDownLatch(list.size());
			for (Thread thread : list) {
				thread.start();
			}
			return this;
		}

		public void awaitSecond(long timeoutInSecond) {
			if (latch != null) {
				try {
					if (timeoutInSecond > 0) {
						latch.await(timeoutInSecond, TimeUnit.SECONDS);
					} else {
						latch.await();
					}
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static ThreadGroupRunner newThreadGroupRunner() {
		return new ThreadGroupRunner();
	}

	public static void execute(Runnable... runnables) {
		try {
			execute(false, 0, runnables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void execute(final boolean await, int maxWaitMS, Runnable... runnables) throws InterruptedException {
		if (runnables == null || runnables.length == 0) {
			return;
		}

		if (runnables.length == 1) {
			if (await) {
				runnables[0].run();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							runnables[0].run();
						} finally {
						}
					}
				}).start();
			}
			return;
		}

		int totalruncnt = 0;
		for (final Runnable runnable : runnables) {
			if (runnable != null) {
				totalruncnt++;
			}
		}

		final CountDownLatch latch = new CountDownLatch(totalruncnt);

		for (final Runnable runnable : runnables) {
			if (runnable != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							runnable.run();
						} finally {
							latch.countDown();
						}
					}
				}).start();
			}
		}

		if (await) {
			if (maxWaitMS <= 0) {
				latch.await();
			} else {
				latch.await(maxWaitMS, TimeUnit.MICROSECONDS);
			}
		}
	}

	public static void executeAwait(Runnable... runnables) throws InterruptedException {
		if (runnables == null || runnables.length == 0) {
			return;
		}

		final Counter c = new Counter();
		final CountDownLatch latch = new CountDownLatch(runnables.length);

		for (final Runnable runnable : runnables) {
			if (runnable != null) {
				c.n++;
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							runnable.run();
						} finally {
							latch.countDown();
						}
					}
				}).start();
			}
		}

		if (c.n > 0) {
			for (int i = 0; i < (runnables.length - c.n); i++) {
				latch.countDown();
			}
			latch.await();
		} else {
		}
	}
	
	public static <T> void executeAwait(Consumer<T> action, T... inputs) throws InterruptedException {
	    ExecutorService pool = Executors.newFixedThreadPool(inputs.length);
	    List<Future<?>> futures = new ArrayList<>();

	    for (T input : inputs) {
	        futures.add(pool.submit(() -> action.accept(input)));
	    }

	    pool.shutdown();
	    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

	    for (Future<?> f : futures) {
	        try {
	            f.get(); // 确保异常抛出
	        } catch (ExecutionException e) {
	            throw new RuntimeException(e);
	        }
	    }
	}

	@SafeVarargs
    public static <T, R> List<R> executeAwait(Function<T, R> func, T... inputs) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(inputs.length);
        List<Future<R>> futures = new ArrayList<>();

        for (T input : inputs) {
            futures.add(pool.submit(() -> func.apply(input)));
        }

        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        List<R> results = new ArrayList<>();
        for (Future<R> f : futures) {
            try {
                results.add(f.get());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

	public static void executeAwait(final Runnable runnable1, final Runnable runnable2) throws InterruptedException {
		final Counter c = new Counter();
		final CountDownLatch latch = new CountDownLatch(2);

		if (runnable1 != null) {
			c.n++;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						runnable1.run();
					} finally {
						latch.countDown();
					}
				}
			}).start();
		}

		if (runnable2 != null) {
			c.n++;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						runnable2.run();
					} finally {
						latch.countDown();
					}
				}
			}).start();
		}

		if (c.n > 0) {
			for (int i = 0; i < (2 - c.n); i++) {
				latch.countDown();
			}
			latch.await();
		} else {
		}
	}

	public static void laterRun(final Runnable runnable1, long delay) {
		if (delay <= 0) {
			new Thread(runnable1).start();
		} else {
			final Timer timer = new Timer("Later-Run");
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					timer.cancel();
					runnable1.run();
				}
			}, delay);
		}
	}

	private final static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	public static void mergeRun(final String runid, final Runnable runnable1, long delay) {
		synchronized (timerMap) {
			Timer timer = timerMap.get(runid);
			if (timer != null) {
				timer.cancel();
				timerMap.remove(runid);
			}

			final Timer newTimer = new Timer("Merge-Run");
			timerMap.put(runid, newTimer);
			newTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					synchronized (timerMap) {
						// Timer may canceled by another thread
						if (timerMap.containsKey(runid)) {
							newTimer.cancel();
							timerMap.remove(runid);
							runnable1.run();
						}
					}
				}
			}, delay);
		}
	}

	public static boolean cancelMergeRun(String timerId) {
		synchronized (timerMap) {
			Timer timer = timerMap.get(timerId);
			if (timer != null) {
				timerMap.remove(timerId);
				timer.cancel();
				return true;
			}
		}
		return false;
	}

	public static void sleepSecond(int sec) {
		try {
			Thread.sleep(1000 * sec);
		} catch (InterruptedException e) {
		}
	}

	public static void sleepMinute(int min) {
		try {
			Thread.sleep(1000 * 60 * min);
		} catch (InterruptedException e) {
		}
	}

	public static void sleepMillis(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	public static void run(Runnable runnable) {
		new Thread(runnable).start();
	}
}
