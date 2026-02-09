package com.amituofo.common.kit.lock;

import java.util.concurrent.ConcurrentHashMap;

public class LockManager {
	// key 是 id，value 是唯一的锁对象
	private final ConcurrentHashMap<String, String> lockMap = new ConcurrentHashMap<>();

	public String getLock(String id) {
		// 如果 id 对应的锁不存在，就创建一个新的 Object 作为锁对象
		return lockMap.computeIfAbsent(id, key -> id);
	}

	public void clear() {
		lockMap.clear();
	}
}