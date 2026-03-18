package com.amituofo.common.kit.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.api.KeyValueHandler;
import com.amituofo.common.ex.EncryptorException;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.kit.counter.Counter;
import com.amituofo.common.kit.kv.KeyValue;
import com.amituofo.common.type.HandleFeedback;
import com.amituofo.common.type.Switch;
import com.amituofo.common.util.AESUtils;
import com.amituofo.common.util.DesUtils;
import com.amituofo.common.util.RandomUtils;
import com.amituofo.common.util.StringUtils;

public class Configuration implements Config, Serializable {
	private static final long serialVersionUID = 4727700807177134139L;

	private final static byte[] DEFAULT_DESKEY = new byte[] { 112, 111, 119, 101, 114, 101, 100, 45, 98, 121, 45, 114, 105, 115, 111, 110, 45, 104,
			97, 110, 45, 97, 109, 116, 102, 57, 57 };// "powered-by-rison-han-amtf99";

	public static final String _CONFIG_CATALOG_ID_ = "_<_CONFIG_CATALOG_ID_>_";
	public static final String _CONFIG_ID_ = "_<_CONFIG_ID_>_";
	public static final String _CONFIG_NAME_ = "_<_CONFIG_NAME_>_";
	public static final String _CONFIG_VERSION_ = "_<_CONFIG_VERSION_>_";
	public static final String _CONFIG_DESC_ = "_<_CONFIG_DESC_>_";

	protected final Map<String, Object> kv = new LinkedHashMap<String, Object>();

	private Map<String, List<ConfigChangedListener>> keylisteners = null;
//	private Map<String, List<ConfigChangedListener>> keyslisteners = null;

//	private SensitiveKeyValueHandler sensitiveKeyValueHandler;

	public Configuration() {
	}

//	@Override
//	public ConfigKey getConfigKey() {
//		return new ConfigKey(getId(), getName());
//	}

	public Configuration(Map<String, Object> configMap) {
		if (configMap != null)
			resetTo(configMap);
	}

	@Override
	public Configuration asConfiguration() {
		return this;
	}

	@Override
	public Map<String, Object> asMap() {
		return this.kv;
	}

	public <T extends Configuration> T buildConfigurationMapObject(String key, Class<T> cls) throws InstantiationException, IllegalAccessException {
		Object o = getObject(key);
		if (o != null) {
			if (o instanceof Map) {
				Configuration conf = cls.newInstance();
				conf.resetTo((Map) o);
				set(key, conf);
				o = conf;
//			} else if (o instanceof JSONObject) {
//				Configuration conf = cls.newInstance();
//				conf.resetTo((JSONObject) o);
//				set(key, conf);
//				o = conf;
			}
		} else {
			Configuration conf = cls.newInstance();
			set(key, conf);
			o = conf;
		}

		return (T) o;
	}

	public void clear() {
		kv.clear();
	}

	public Configuration clone() {
		Configuration newsc = new Configuration();
		newsc.resetTo(this);
		return newsc;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (kv == null) {
			if (other.kv != null)
				return false;
		} else if (!kv.equals(other.kv))
			return false;
		return true;
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

	public void forEachConfig(final KeyValueHandler<Configuration> kvHandler) {
		final Counter n = new Counter();
		Iterator<String> it = kv.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

			n.n++;

			try {
				Object val = kv.get(key);
				HandleFeedback hf = kvHandler.handle(n.n, key, new Configuration((Map) val));
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

	public void generateNewID() {
		this.setId(RandomUtils.randomString(6));
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

	public byte[] getBytes(String key) {
		return (byte[]) kv.get(key);
	}

	public String getCatalogId() {
		return this.getString(_CONFIG_CATALOG_ID_);
	}

	public char getChar(String key) {
		String v = (String) kv.get(key);
		if (v != null) {
			return v.charAt(0);
		}

		return 0;
	}

	public Configuration getConfig(String key) {
		Object conf = kv.get(key);
		if (conf != null) {
			return new Configuration((Map<String, Object>) conf);
		}

		return null;
	}

	public List<Configuration> getConfigs(String key) {
		List<Configuration> list = new ArrayList<Configuration>();

		Object conf = kv.get(key);
		if (conf != null) {
			List<Map<String, Object>> o = (List<Map<String, Object>>) conf;
			for (Map<String, Object> map : o) {
				list.add(new Configuration(map));
			}
		}

		return list;
	}

//	public String getConfigurationID() {
//		return this.configId;
//	}

	public Configuration getConfigurationMapObject(String key) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Object o = getObject(key);
		if (o != null) {
			String className = this.getString(key + "_CLASS_NAME_");

			if (className != null) {
				Class c = Class.forName(className);
				Configuration conf = (Configuration) c.newInstance();
				conf.resetTo(this);
				set(key, conf);
//				set(key + "_CLASS_NAME_", value.getClass().getName());
				set(key + "_CLASS_", c);
				return conf;
			}
		}

		return (Configuration) o;
	}

	public String getDescription() {
		return this.getString(_CONFIG_DESC_);
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

	public Float getFloat(String key) {
		return getFloat(key, null);
	}

//	public void set(String key, String value) {
//		configMap.put(key, value);
//	}

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

	public String getId() {
		return this.getString(_CONFIG_ID_);
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

//	public void set(String key, List<String> value) {
//		if (value == null || value.size() == 0) {
//			kv.remove(key);
//		} else {
//			String[] values = new String[value.size()];
//			int i = 0;
//			for (String string : value) {
//				values[i++] = StringUtils.encodeBase64String(string);
//			}
//			set0(key, StringUtils.toString(values, ','));
//		}
//	}

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

//	public void set(String key, String[] values) {
//		configMap.put(key, values);
//	}

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

	public String[] getKeys() {
		return kv.keySet().toArray(new String[kv.size()]);
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

	public Map<String, Object> getMap(String key) {
		return (Map<String, Object>) kv.get(key);
	}

	public String getName() {
		String name = this.getString(_CONFIG_NAME_);
		if (name == null) {
			// 兼容旧版本
			name = this.getString("_<_NAME_>_");
		}

		return name;
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

//	public String getSensitiveString(String key) throws EncryptorException {
//
//	}

	public String getSensitiveString(String key, char[] pepper) throws EncryptorException {
		String value = getString(key);
		if (StringUtils.isNotEmpty(value)) {
			try {
				if (pepper != null && pepper.length > 0) {
					return AESUtils.decrypt(value, pepper);
				} else {
					byte[] desKey = DEFAULT_DESKEY;
					return new String(DesUtils.decrypt(DesUtils.parseHexStr2Byte(value), desKey));
				}
			} catch (Exception e) {
				throw new EncryptorException(e);
			}
		}
		return value;
	}

	// public boolean hasNotNull(String key) {
	// return configMap.get(key) != null;
	// }

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

	public int[] getIntArray(String key) {
		Object o = kv.get(key);
		if (o != null) {
			if (o instanceof int[]) {
				return (int[]) o;
			}

			if (o instanceof String) {
				String str = (String) o;
				if (str.indexOf(',') != -1) {
					String[] arrays = str.split(",");
					int[] intarray = new int[arrays.length];
					for (int i = 0; i < arrays.length; i++) {
						intarray[i] = Integer.parseInt(arrays[i]);
					}
					return intarray;
				} else {
					if (str.length() > 0)
						return new int[] { Integer.parseInt(str) };
				}
			}
		}
		return null;
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
				} else {
					if (str.length() > 0)
						return new String[] { StringUtils.decodeBase64ToString(str) };
				}
			}
		}
		return null;
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
					if (str.length() > 0)
						return StringUtils.toStringList(StringUtils.decodeBase64ToString(str));
				}
			}
		}
		return null;
	}

	public Switch getSwitch(String key, Switch defaultValue) {
		Object v = kv.get(key);
		if (v != null) {
			if (v instanceof Switch) {
				return (Switch) v;
			} else {
				return Switch.valueOfSet(v.toString());
			}
		}

		return defaultValue;
	}

	public String getVersion() {
		return this.getString(_CONFIG_VERSION_);
	}

	public boolean has(String key) {
		return kv.get(key) != null;
	}

	@Override
	public int hashCode() {
		int result = 1;
//		System.out.println("------------------------------");

		Iterator<String> it = kv.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object o = kv.get(key);
			int kvhash = key.hashCode();
			if (o != null) {
				kvhash += o.hashCode();
			}

//			System.out.println(key + "=" + o + " (" + (o == null ? 0 : o.hashCode()) + ")");

			result += kvhash;

		}
//		System.out.println("------------------------------"+(int)result);
//		result = prime * result + ((configMap == null) ? 0 : configMap.hashCode());
		return result;
	}

	public boolean hasNotEmptyString(String key) {
		String v = this.getString(key);
		return StringUtils.isNotEmpty(v);
	}

	public boolean isKeyValueEquals(String key, int value) {
		int v = this.getInt(key);
		return v == value;
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

	public boolean hasKeysContains(String name) {
		Set<String> keys = kv.keySet();
		for (String key : keys) {
			int i = key.indexOf(name);
			if (i != -1) {
				return true;
			}
		}
		return false;
	}

	public void remove(String key) {
		kv.remove(key);
	}

	public void resetTo(Config config) {
		this.kv.clear();

		if (config == null) {
			return;
		}

		this.resetTo(config.asConfiguration());
	}

	public void resetTo(Configuration config) {
		this.kv.clear();
		this.kv.putAll(config.kv);
	}

	public void resetTo(Map<String, Object> configMap) {
		this.kv.clear();
		this.kv.putAll(configMap);
	}

	public void set(String key, boolean value) {
		set0(key, Boolean.toString(value));
	}

	public void set(String key, char value) {
		set0(key, String.valueOf(value));
	}

	public void set(String key, Collection<String> value) {
		if (value == null || value.size() == 0) {
			kv.remove(key);
		} else {
			String[] values = new String[value.size()];
			int i = 0;
			for (String string : value) {
				values[i++] = StringUtils.encodeBase64String(string);
			}
			set0(key, StringUtils.toString(values, ','));
		}
	}

	public void set(String key, Configuration value) {
		set0(key, value);
		set0(key + "_CLASS_NAME_", value.getClass().getName());
		set0(key + "_CLASS_", value.getClass());
	}

	public void set(String key, double value) {
		set0(key, Double.toString(value));
	}

	public void set(String key, float value) {
		set0(key, Float.toString(value));
	}

	public void set(String key, int value) {
		set0(key, Integer.toString(value));
	}

	public void set(String key, long value) {
		set0(key, Long.toString(value));
	}

	public void set(String key, Object value) {
		set0(key, value);
	}

	public void set(String key, byte[] value) {
		set0(key, value);
	}

	public void set(String key, String[] value) {
		if (value == null || value.length == 0) {
			kv.remove(key);
		} else {
			for (int i = 0; i < value.length; i++) {
				value[i] = StringUtils.encodeBase64String(value[i]);
			}

			set0(key, StringUtils.toString(value, ','));
		}
	}

	public void set(String key, int[] value) {
		if (value == null || value.length == 0) {
			kv.remove(key);
		} else {
			set0(key, StringUtils.toString(value, ','));
		}
	}

	public void setCatalogId(String id) {
		set0(_CONFIG_CATALOG_ID_, id);
	}

//	configMap.put(key, value);
//	configMap.put(key + "_CLASS_NAME_", value.getClass().getName());
//	configMap.put(key + "_CLASS_", value.getClass());

	public void setDescription(String name) {
		set0(_CONFIG_DESC_, name);
	}

	public void setId(String id) {
		set0(_CONFIG_ID_, id);
	}

	public void setName(String name) {
		set0(_CONFIG_NAME_, name);
	}

	public void setSensitiveString(String key, String value, char[] pepper) throws EncryptorException {
		if (StringUtils.isNotEmpty(value)) {
			try {
				if (pepper != null && pepper.length > 0) {
					value = AESUtils.encrypt(value, pepper);
				} else {
					byte[] desKey = DEFAULT_DESKEY;
					value = DesUtils.parseByte2HexStr(DesUtils.encrypt(value.getBytes(), desKey));
				}
			} catch (Exception e) {
				throw new EncryptorException(e);
			}
		}
		set0(key, value);
	}

	public void setVersion(String name) {
		set0(_CONFIG_VERSION_, name);
	}

	public int size() {
		return kv.size();
	}

	public String toJsonString() {
		return JSON.toJSONString(toMap());
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.writeValueAsString(toMap());
	}

	public Map<String, Object> toMap() {
		Map<String, Object> tmpConfigMap = new LinkedHashMap<String, Object>();

		kv.forEach(new BiConsumer<String, Object>() {

			public void accept(String key, Object value) {
				if (value instanceof Configuration) {
					value = ((Configuration) value).toMap();
				}
				tmpConfigMap.put(key, value);
			}
		});

		return tmpConfigMap;
	}

	private void set0(String key, Object value) {
		if (keylisteners != null) {
			List<ConfigChangedListener> listeners = keylisteners.get(key);
			if (listeners != null) {
				Object oldValue = kv.get(key);

				if (!Objects.equals(value, oldValue)) {
					for (ConfigChangedListener listener : listeners) {
						listener.changed(key, value, oldValue);
					}
				}
			}
		}

		kv.put(key, value);
	}

	public String toPrettyJsonString() {
		return JSON.toJSONString(toMap(), true);

//		ObjectMapper objectMapper = new ObjectMapper();
//		ObjectWriter ow = objectMapper.writerWithDefaultPrettyPrinter();
//
//		return ow.writeValueAsString(toMap());
	}

	public void validateConfig() throws InvalidConfigException {
	}

	public void removeAllKeyChangedListener(String key) {
		if (keylisteners == null || key == null) {
			return;
		}

		keylisteners.remove(key);
	}

	public void removeKeyChangedListener(String key, ConfigChangedListener listener) {
		if (keylisteners == null || listener == null || key == null) {
			return;
		}

		List<ConfigChangedListener> listeners = keylisteners.get(key);

		for (int i = 0; i < listeners.size(); i++) {
			if (listener == listeners.get(i)) {
				keylisteners.remove(i);
				return;
			}
		}
	}

	public void addKeyChangedListener(String key, ConfigChangedListener listener) {
		if (listener == null || key == null) {
			return;
		}

		if (keylisteners == null) {
			keylisteners = new HashMap<>();
		}

		List<ConfigChangedListener> listeners = keylisteners.get(key);

		if (listeners == null) {
			listeners = new ArrayList<>();
			keylisteners.put(key, listeners);
		}

		listeners.add(listener);
	}

//	public void addAnyKeysChangedListener(ConfigChangedListener listener, String... keys) {
//		if (listener == null || keys == null || keys.length == 0) {
//			return;
//		}
//
//		if (keyslisteners == null) {
//			keyslisteners = new HashMap<>();
//		}
//
//		String keystring = StringUtils.toString(keys, ',');
//		List<ConfigChangedListener> listeners = keyslisteners.get(keystring);
//
//		if (listeners == null) {
//			listeners = new ArrayList<>();
//			keyslisteners.put(keystring, listeners);
//		}
//
//		listeners.add(listener);
//	}
//
//	public void removeAnyKeysChangedListener(String... keys) {
//		if (keyslisteners == null || keys == null || keys.length == 0) {
//			return;
//		}
//
//		String keystring = StringUtils.toString(keys, ',');
//		keyslisteners.remove(keystring);
//	}

	public static Configuration parseJsonString(String config) throws ParseException {
		if (StringUtils.isEmpty(config)) {
			return null;
		}
		Map<String, Object> configMap = JSON.parseObject(config, HashMap.class);
		Configuration sc = new Configuration();
		sc.resetTo(configMap);
		return sc;
	}

	public static List<Configuration> toConfigurationMap(List<Map<String, Object>> listObj) {
		List<Configuration> list = new ArrayList<Configuration>();
		if (listObj != null) {
			for (Map<String, Object> map : listObj) {
				list.add(toConfigurationMap(map));
			}
		}
		return list;
	}

	public static Configuration toConfigurationMap(Map<String, Object> map) {
		return new Configuration(map);
	}

}
