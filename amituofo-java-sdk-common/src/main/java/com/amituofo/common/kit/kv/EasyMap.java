package com.amituofo.common.kit.kv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.api.KeyValueHandler;
import com.amituofo.common.ex.DecodeException;
import com.amituofo.common.ex.EncodeException;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.kit.config.Config;
import com.amituofo.common.kit.counter.Counter;
import com.amituofo.common.type.HandleFeedback;
import com.amituofo.common.util.DesUtils;
import com.amituofo.common.util.StringUtils;

public class EasyMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 4727700807177134139L;

	private Map<String, Object> kv = this;

	public EasyMap() {
	}

	public EasyMap(Map<String, Object> configMap) {
		if (configMap != null)
			resetTo(configMap);
	}

	public void resetTo(Map<String, Object> configMap) {
		this.kv.clear();
		this.kv.putAll(configMap);
	}

	public void resetTo(EasyMap config) {
		this.kv.clear();
		this.kv.putAll(config.kv);
	}

	public void resetTo(Config config) {
		this.kv.clear();

		if (config == null) {
			return;
		}

		this.resetTo(config.asConfiguration());
	}

	public String[] getKeys() {
		return kv.keySet().toArray(new String[kv.size()]);
	}

	public EasyMap clone() {
		EasyMap newsc = new EasyMap();
		newsc.resetTo(this);
		return newsc;
	}

	public void remove(String key) {
		kv.remove(key);
	}

	public void set(String key, Object value) {
		kv.put(key, value);
	}

	public void set(String key, String[] value) {
		if (value == null || value.length == 0) {
			kv.remove(key);
		} else {
			for (int i = 0; i < value.length; i++) {
				value[i] = StringUtils.encodeBase64String(value[i]);
			}

			kv.put(key, StringUtils.toString(value, ','));
		}
	}

	public void set(String key, List<String> value) {
		if (value == null || value.size() == 0) {
			kv.remove(key);
		} else {
			String[] values = new String[value.size()];
			int i = 0;
			for (String string : value) {
				values[i++] = StringUtils.encodeBase64String(string);
			}
			kv.put(key, StringUtils.toString(values, ','));
		}
	}

//	public void set(String key, String[] values) {
//		configMap.put(key, values);
//	}

	public void set(String key, boolean value) {
		kv.put(key, Boolean.toString(value));
	}

	public void set(String key, int value) {
		kv.put(key, Integer.toString(value));
	}

	public void set(String key, long value) {
		kv.put(key, Long.toString(value));
	}

	public void set(String key, float value) {
		kv.put(key, Float.toString(value));
	}

	public void set(String key, double value) {
		kv.put(key, Double.toString(value));
	}

	public void set(String key, char value) {
		kv.put(key, String.valueOf(value));
	}

	public boolean has(String key) {
		return kv.get(key) != null;
	}

	public boolean hasNotEmptyString(String key) {
		String v = this.getString(key);
		return StringUtils.isNotEmpty(v);
	}

	public boolean isKeyValueEquals(String key, int value) {
		int v = this.getInt(key);
		return v == value;
	}

	// public boolean hasNotNull(String key) {
	// return configMap.get(key) != null;
	// }

	public char getChar(String key) {
		String v = (String) kv.get(key);
		if (v != null) {
			return v.charAt(0);
		}

		return 0;
	}

	public Object getObject(String key) {
		return kv.get(key);
	}

	public Object getObject(String key, Object defaultValue) {
		Object val = kv.get(key);
		if (val == null) {
			return defaultValue;
		}

		return val;
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defaultValue) {
		Object val = kv.get(key);
		if (val != null) {
			if (val instanceof String) {
				return (String) val;
			} else {
				return val.toString();
			}
		}

		return defaultValue;
	}

	public byte[] getBytes(String key) {
		return (byte[]) kv.get(key);
	}

	public boolean getBoolean(String key) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Boolean) {
				return (boolean) v;
			} else {
				return "true".equalsIgnoreCase(v.toString());
			}
		}

		return false;
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Boolean) {
				return (boolean) v;
			} else {
				return "true".equalsIgnoreCase(v.toString());
			}
		}

		return defaultValue;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		Integer n = getInteger(key, null);

		if (n == null) {
			return defaultValue;
		}

		return n.intValue();
	}

	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).intValue();
			}
			if (StringUtils.isNotEmpty(v.toString())) {
				return Integer.parseInt(v.toString());
			}
		}

		return defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).longValue();
			}
			if (StringUtils.isNotEmpty(v.toString())) {
				return Long.valueOf(v.toString());
			}
		}

		return defaultValue;
	}

	public Float getFloat(String key) {
		return getFloat(key, null);
	}

	public Float getFloat(String key, Float defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).floatValue();
			}
			if (StringUtils.isNotEmpty(v.toString())) {
				return Float.parseFloat(v.toString());
			}
		}

		return defaultValue;
	}

	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	public Double getDouble(String key, Double defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).doubleValue();
			}
			if (StringUtils.isNotEmpty(v.toString())) {
				return Double.parseDouble(v.toString());
			}
		}

		return defaultValue;
	}

	public List<String> getStringList(String key) {
		Object o = kv.get(key);
		if (o != null) {
			if (o instanceof List) {
				return (List<String>) o;
			}

			if (o instanceof String[]) {
				List<String> list = new ArrayList<String>();
				String[] vs = (String[]) o;
				for (String string : vs) {
					list.add(string);
				}

				return list;
			}

			if (o instanceof String) {
				String str = (String) o;
				if (StringUtils.isEmpty(str)) {
					return null;
				}

				if (str.indexOf(',') != -1) {
					String[] arrays = str.split(",");
					for (int i = 0; i < arrays.length; i++) {
						arrays[i] = StringUtils.decodeBase64ToString(arrays[i]);
					}
					return StringUtils.toStringList(arrays);
				} else {
					return StringUtils.toStringList(StringUtils.decodeBase64ToString(str));
				}
			}
		}
		return null;
	}

	public List<EasyMap> getConfigs(String key) {
		List<EasyMap> list = new ArrayList<EasyMap>();

		Object conf = kv.get(key);
		if (conf != null) {
			List<Map<String, Object>> o = (List<Map<String, Object>>) conf;
			for (Map<String, Object> map : o) {
				list.add(new EasyMap(map));
			}
		}

		return list;
	}

	public String[] getStringArray(String key) {
		Object o = kv.get(key);
		if (o != null) {
			if (o instanceof List) {
				return ((List<String>) o).toArray(new String[((List<String>) o).size()]);
			}

			if (o instanceof String[]) {
				return (String[]) o;
			}

			if (o instanceof String) {
				String str = (String) o;
				if (str.indexOf(',') != -1) {
					String[] arrays = str.split(",");
					for (int i = 0; i < arrays.length; i++) {
						arrays[i] = StringUtils.decodeBase64ToString(arrays[i]);
					}
					return arrays;
				}
			}
		}
		return null;
	}

	public Map<String, Object> getMap(String key) {
		return (Map<String, Object>) kv.get(key);
	}

	public EasyMap getEasyMap(String key) {
		Object conf = kv.get(key);
		if (conf != null) {
			return new EasyMap((Map<String, Object>) conf);
		}

		return null;
	}

	public void forEachConfig(final KeyValueHandler<EasyMap> kvHandler) {
		final Counter n = new Counter();
		Iterator<String> it = kv.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

			n.n++;

			try {
				Object val = kv.get(key);
				HandleFeedback hf = kvHandler.handle(n.n, key, new EasyMap((Map) val));
				if (hf == HandleFeedback.interrupted) {
					return;
				}
			} catch (HandleException e) {
				e.printStackTrace();
			}
		}

//		configMap.forEach(new BiConsumer<String, Object>() {
//
//			public void accept(String t, Object u) {
//				n.n++;
//
//				if (u instanceof Map) {
//					try {
//						kvHandler.handle(n.n, t, new Configuration((Map) u));
//					} catch (HandleException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
	}

	public List<KeyValue<Object>> keysWithPerfix(String perfix, boolean removePerfix) {
		List<KeyValue<Object>> list = new ArrayList<>();

		Set<String> keys = kv.keySet();
		for (String key : keys) {
			key = key.trim();
			int i = key.indexOf(perfix);
			if (i == 0) {
				Object value = kv.get(key);
				if (removePerfix) {
					key = key.substring(perfix.length());
				}
				list.add(new KeyValue<Object>(key, value));
			}
		}
		return list;
	}

	public void forEach(final KeyValueHandler<Object> kvHandler) {
		final Counter n = new Counter();
		Iterator<String> it = kv.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

			n.n++;

			try {
				Object val = kv.get(key);
				HandleFeedback hf = kvHandler.handle(n.n, key, val);
				if (hf == HandleFeedback.interrupted) {
					return;
				}
			} catch (HandleException e) {
				e.printStackTrace();
			}
		}

//		configMap.forEach(new BiConsumer<String, Object>() {
//
//			public void accept(String t, Object u) {
//				n.n++;
//
//				try {
//					HandleFeedback hf = kvHandler.handle(n.n, t, u);
//					if(hf== HandleFeedback.interrupted) {
//						
//					}
//				} catch (HandleException e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	public Map<String, Object> toMap() {
		Map<String, Object> tmpConfigMap = new LinkedHashMap<String, Object>();

		kv.forEach(new BiConsumer<String, Object>() {

			public void accept(String key, Object value) {
				if (value instanceof EasyMap) {
					value = ((EasyMap) value).toMap();
				}
				tmpConfigMap.put(key, value);
			}
		});

		return tmpConfigMap;
	}

	public String toJsonString() {
		return JSON.toJSONString(kv);
	}

	public String toPrettyJsonString() {
		return JSON.toJSONString(kv, true);
	}

	public static EasyMap parseJsonString(String config) throws ParseException {
		if (StringUtils.isEmpty(config)) {
			return null;
		}
		return JSON.parseObject(config, EasyMap.class);
	}

	public static EasyMap toConfigurationMap(Map<String, Object> map) {
		return new EasyMap(map);
	}

//	public static List<EasyMap> toConfigurationMap(List<Map<String, Object>> listObj) {
//		List<EasyMap> list = new ArrayList<EasyMap>();
//		if (listObj != null) {
//			for (Map<String, Object> map : listObj) {
//				list.add(toConfigurationMap(map));
//			}
//		}
//		return list;
//	}

}
