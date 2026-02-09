package com.amituofo.common.kit.kv;

public class KV<T> extends KeyValue<T> {
	public static char DEFAULT_KEY_SEPERATOR = '/';

	public KV() {
		super();
	}

	public KV(String key, T value) {
		super(key, value);
	}

	public KV(String key) {
		super(key);
	}

	public String getLastRegionName() {
		String key = this.getKey();
		int li = key.lastIndexOf(DEFAULT_KEY_SEPERATOR, key.length() - 2);
		if (li != -1) {
			return key.substring(li + 1);
		}

		return key;
	}

}
