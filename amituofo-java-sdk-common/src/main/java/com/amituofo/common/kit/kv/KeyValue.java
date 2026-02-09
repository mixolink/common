package com.amituofo.common.kit.kv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.amituofo.common.util.StringUtils;

public class KeyValue<T> {
	private String key;
	private T value;

	public KeyValue() {
	}

	public KeyValue(String key) {
		this.key = key;
	}

	public KeyValue(String key, T value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void setKeyValue(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public String getStringValue() {
		return value == null ? "" : value.toString();
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyValue other = (KeyValue) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	public static List<KeyValue<Object>> toKeyValueList(Map<String, Object> map) {
		if (map == null || map.size() == 0) {
			return null;
		}

		List<KeyValue<Object>> list = new ArrayList<>(map.size());

		Set<String> keys = map.keySet();
		for (String key : keys) {
			list.add(new KeyValue<Object>(key, map.get(key)));
		}

		return list;
	}

	public static List<KeyValue<Object>> toKeyValueList(List<Map<String, Object>> maplist) {
		if (maplist == null || maplist.size() == 0) {
			return null;
		}

		List<KeyValue<Object>> list = new ArrayList<>(maplist.size());

		for (Map<String, Object> map : maplist) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				list.add(new KeyValue<Object>(key, map.get(key)));
			}
		}

		return list;
	}

	public static KeyValue<String> parse(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		String[] kv = StringUtils.splitFirst(str, '=');
		if (kv == null || kv.length != 2) {
			return null;
		}
		return new KeyValue<String>(kv[0], kv[1]);
	}

	public static List<KeyValue<String>> parse(String str, char expsplit, char optsplit) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		List<KeyValue<String>> list = new ArrayList<>();
		String[] exps = StringUtils.split(str, expsplit, true);
		for (String exp : exps) {
			String[] kv = StringUtils.splitFirst(exp, optsplit);
			if (kv.length == 2) {
				list.add(new KeyValue<String>(kv[0], kv[1]));
			}
		}

		return list;
	}

	public static Map<String, String> parseMap(String str, char expsplit, char optsplit) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		Map<String, String> list = new HashMap<>();
		String[] exps = StringUtils.split(str, expsplit, true);
		for (String exp : exps) {
			String[] kv = StringUtils.splitFirst(exp, optsplit);
			if (kv.length == 2) {
				list.put(kv[0], kv[1]);
			}
		}

		return list;
	}

}
