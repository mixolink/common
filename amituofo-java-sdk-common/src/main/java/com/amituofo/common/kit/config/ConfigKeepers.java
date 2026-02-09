package com.amituofo.common.kit.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigKeepers {
	private final static Map<String, ConfigKeeper> cks = new HashMap<>();

	public ConfigKeepers() {
	}

	public static ConfigKeeper get(String id) {
		return cks.get(id);
	}

	public static void set(String id, ConfigKeeper ck) {
		cks.put(id, ck);
	}

	public static void remove(String id) {
		cks.remove(id);
	}
}
