package com.amituofo.common.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder {

	private Map<String, Object> map;

	private MapBuilder(Map<String, Object> map) {
		super();
		this.map = map;
	}

	public static MapBuilder map() {
		return new MapBuilder(new HashMap<>());
	}

	public static MapBuilder linkedmap() {
		return new MapBuilder(new LinkedHashMap<>());
	}

	public MapBuilder put(String name, Object value) {
		map.put(name, value);
		return this;
	}

	public Map<String, Object> build() {
		return map;
	}

}
