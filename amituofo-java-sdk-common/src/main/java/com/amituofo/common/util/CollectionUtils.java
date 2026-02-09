package com.amituofo.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

	public CollectionUtils() {
	}

	public static String[] keys2List(Map<String, ?> map) {
		if (map == null) {
			return new String[0];
		}

		Iterator<String> it = map.keySet().iterator();
		String[] keys = new String[map.size()];
		int i = 0;
		while (it.hasNext()) {
			keys[i++] = it.next();
		}

		return keys;
	}

	public static <T> List<T> values2List(Map<?, T> map) {
		List<T> list = new ArrayList<T>();
		if (map == null) {
			return list;
		}
		
		Collection<T> col = map.values();
		for (T t : col) {
			list.add(t);
		}

		return list;
	}

}
