package com.amituofo.common.kit.config;

public class CacheConfig<CONFIG extends Config> {
	private CONFIG config;
	private long lastAccessTime;
	private long lastModifiedTime;

	public CacheConfig(CONFIG config) {
		super();
		this.config = config;
		this.lastAccessTime = System.currentTimeMillis();
		this.lastAccessTime = System.currentTimeMillis();
	}

	public CONFIG getConfig() {
		return config;
	}

	public void setConfig(CONFIG config) {
		this.config = config;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

}
