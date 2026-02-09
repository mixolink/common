package com.amituofo.common.ui.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.kit.kv.KeyValue;

public interface DataSavingListener<META, T> {
	boolean savingData(META meta, T data);

	public static Map<String, String> toMap(List<KeyValue<String>> data) {
		Map<String, String> map = new HashMap<String, String>();
		if (data != null) {
			for (KeyValue<String> keyValue : data) {
				map.put(keyValue.getKey(), keyValue.getValue());
			}
		}
		return map;
	}
}
