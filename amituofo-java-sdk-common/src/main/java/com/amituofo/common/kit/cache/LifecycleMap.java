package com.amituofo.common.kit.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amituofo.common.api.Callback;
import com.amituofo.common.define.Constants;
import com.amituofo.common.ex.IDConflictException;
import com.amituofo.common.kit.retry.RetryHelper;
import com.amituofo.common.kit.retry.ReturnValue;
import com.amituofo.common.kit.retry.ReturnableTry;
import com.amituofo.common.util.RandomUtils;

public class LifecycleMap<K, V> {

	private class LifecycleData<V> {
		V data;
		long lastAccessTime;
		long injectTime;
		long expireDuration;
		long maxLifetime;
		Callback<V> expiredListener;
	}

	private Map<K, LifecycleData<V>> map = new HashMap<>();
	private final String TID = "lifecyclemap-autoclean-" + RandomUtils.randomString(8);

//	private long defaultCheckperiod = Constants.TIME_MILLISECONDS_15_SECOND;
	private long defaultMaxLifetime = -1;
	private long defaultExpireDuration = -1;

	private List<Callback<V>> listeners = null;
//	private Listeners<Callback<V>> listeners = new Listeners<>();

	public LifecycleMap() {
		this(Constants.TIME_MILLISECONDS_15_SECOND);
	}

	public LifecycleMap(long defaultCheckperiod) {
		try {
			RetryHelper.asyncRetryForever(TID, new ReturnableTry<Void>() {
				List<K> toberemoved = new ArrayList<>();

				@Override
				protected ReturnValue<Void> execute(int retryTimes, long time) {
					try {
						synchronized (map) {
							if (map.size() == 0) {
								return null;
							}

							Iterator<K> it = map.keySet().iterator();
							while (it.hasNext()) {
								K id = it.next();

								LifecycleData<V> cache = map.get(id);
								if (cache == null) {
									continue;
								}
//								System.out.println("SID-Detect expired-" + id + " " + (time - cache.lastAccessTime) + " " + cache.expireDuration + " "
//										+ (time - cache.injectTime) + " " + cache.maxLifetime);

								if (isExpired(cache, time)) {
									toberemoved.add(id);
								}
							}

							if (toberemoved.size() > 0) {
								for (K id : toberemoved) {
									expired(id);
								}
								toberemoved.clear();
							}
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}

					return null;
				}
			}, defaultCheckperiod);
		} catch (IDConflictException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		RetryHelper.stopRetry(TID);
		map.clear();
	}

	public void expireNow() {
		synchronized (map) {
			if (map.size() == 0) {
				return;
			}

			Map<K, LifecycleData<V>> tempmap = new HashMap<>();
			tempmap.putAll(map);
			map.clear();
			map = new HashMap<>();

			List<K> toberemoved = new ArrayList<>();

			Iterator<K> it = tempmap.keySet().iterator();
			while (it.hasNext()) {
				K id = it.next();

				LifecycleData<V> cache = tempmap.get(id);
				if (cache == null) {
					continue;
				}

				toberemoved.add(id);
			}

			if (toberemoved.size() > 0) {
				for (K id : toberemoved) {
					expired(id);
				}
				toberemoved.clear();
			}
		}
	}

	private boolean isExpired(LifecycleData<V> cache, long time) {
		boolean expired = false;

		if (cache.expireDuration > 0) {
			expired = ((time - cache.lastAccessTime) > cache.expireDuration);
		}
		if (!expired && cache.maxLifetime > 0) {
			expired = ((time - cache.injectTime) > cache.maxLifetime);
		}

		return expired;
	}

	public void addExpiredListener(Callback<V> listener) {
		if (listener == null) {
			return;
		}

		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(listener);
	}

	public long getDefaultMaxLifetime() {
		return defaultMaxLifetime;
	}

	public void setDefaultMaxLifetime(long defaultMaxLifetime) {
		this.defaultMaxLifetime = defaultMaxLifetime;
	}

	public long getDefaultExpireDuration() {
		return defaultExpireDuration;
	}

	public void setDefaultExpireDuration(long defaultExpireDuration) {
		this.defaultExpireDuration = defaultExpireDuration;
	}

	public boolean contains(K id) {
		synchronized (map) {
			return map.containsKey(id);
		}
	}

	public V get(K id) {
		synchronized (map) {
			LifecycleData<V> cache = map.get(id);
			if (cache == null) {
				return null;
			}

			long nowtime = System.currentTimeMillis();
			if (cache.expireDuration <= 0) {
				cache.lastAccessTime = nowtime;
				return (V) cache.data;
			} else {
				if (isExpired(cache, nowtime)) {
					expired(id);
					return null;
				}
				cache.lastAccessTime = nowtime;
				return (V) cache.data;
			}
		}
	}

	public long getInjectTime(K id) {
		synchronized (map) {
			LifecycleData<V> cache = map.get(id);
			if (cache == null) {
				return -1;
			}

			return cache.injectTime;
		}
	}

	private void expired(K id) {
		LifecycleData<V> cd = map.remove(id);
//		System.out.println("SID-Expired-" + id);
		if (cd == null) {
			return;
		}

		if (cd.expiredListener != null) {
			cd.expiredListener.callback(cd.data);
		}

		if (listeners != null) {
			for (Callback<V> callback : listeners) {
				callback.callback(cd.data);
			}
		}
	}

	/**
	 * @param id
	 * @param data
	 * @param expireDuration The maximum duration after the last access(get)
	 */
	public void put(K id, V data) {
		put(id, data, defaultExpireDuration, defaultMaxLifetime, null);
	}

	/**
	 * @param id
	 * @param data
	 * @param expireDuration The maximum duration after the last access(get)
	 * @param maxLifetime    The maximum duration after first time injection
	 */
	public void put(K id, V data, int maxLifetime) {
		put(id, data, defaultExpireDuration, maxLifetime, null);
	}

	/**
	 * @param id
	 * @param data
	 * @param expireDuration The maximum duration after the last access(get)
	 * @param maxLifetime    The maximum duration after first time injection
	 */
	public void put(K id, V data, int expireDuration, int maxLifetime) {
		put(id, data, expireDuration, maxLifetime, null);
	}

	/**
	 * @param id
	 * @param data
	 * @param expireDuration The maximum duration after the last access(get)
	 * @param maxLifetime    The maximum duration after first time injection
	 */
	public void put(K id, V data, long expireDuration, long maxLifetime, Callback<V> expiredListener) {
		synchronized (map) {
			LifecycleData<V> cache = new LifecycleData<V>();
			cache.data = data;
			cache.injectTime = System.currentTimeMillis();
			cache.lastAccessTime = cache.injectTime;
			cache.expireDuration = expireDuration;
			cache.maxLifetime = maxLifetime;
			cache.expiredListener = expiredListener;
			map.put(id, cache);
//			System.out.println("SID-Put-" + id);
		}
	}

	public V remove(K id) {
		synchronized (map) {
			LifecycleData<V> cd = map.remove(id);
//			System.out.println("SID-Remove-" + id);
			if (cd != null) {
				return cd.data;
			}

			return null;
		}
	}

	public void clear() {
		Map<K, LifecycleData<V>> map0 = new HashMap<>();
		synchronized (map) {
			map.clear();
		}
		map = map0;
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		List<V> list = new ArrayList<>();
		synchronized (map) {
			Collection<LifecycleMap<K, V>.LifecycleData<V>> values = map.values();
			for (LifecycleData<V> lifecycleData : values) {
				list.add(lifecycleData.data);
			}
		}
		return list;
	}

}
