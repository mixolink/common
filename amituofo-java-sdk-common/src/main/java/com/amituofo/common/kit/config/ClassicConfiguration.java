package com.amituofo.common.kit.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.ValidUtils;

public class ClassicConfiguration extends Configuration {
	public static final String _CONFIG_CLASS_NAME_ = "_<_CONFIG_CLASS_NAME_>_";

	private Class<?> defaultConfigClass = null;

	public ClassicConfiguration() {
		generateNewID();
	}

	public ClassicConfiguration(Class<? extends Config> clazz) {
		generateNewID();
		defaultConfigClass = clazz;
		set(_CONFIG_CLASS_NAME_, clazz.getName());
	}

	public ClassicConfiguration(Map<String, Object> configMap) {
		super(configMap);
		if (configMap != null && !configMap.containsKey(_CONFIG_CLASS_NAME_)) {
			set(_CONFIG_CLASS_NAME_, this.getClass().getName());
		}
	}

	public ClassicConfiguration clone() {
		ClassicConfiguration newsc = new ClassicConfiguration();
		newsc.resetTo(this);
		return newsc;
	}

	public ClassicConfiguration getClassicConfiguration(String key) {
		Map<String, Object> conf = (Map<String, Object>) kv.get(key);
		if (conf != null) {
			return new ClassicConfiguration(conf);
		}

		return null;
	}

	public List<ClassicConfiguration> getClassicConfigurationList(String key) {
		List<ClassicConfiguration> list = new ArrayList<ClassicConfiguration>();

		Object conf = kv.get(key);
		if (conf != null) {
			List<Map<String, Object>> o = (List<Map<String, Object>>) conf;
			for (Map<String, Object> map : o) {
				list.add(new ClassicConfiguration(map));
			}
		}

		return list;
	}

	public String getDefaultConfigClassName() {
		return this.getString(_CONFIG_CLASS_NAME_);
	}

	public Class<?> getDefaultConfigClass(ClassLoader defaultClassLoader) {
		if (defaultConfigClass == null) {
			defaultConfigClass = getClass(_CONFIG_CLASS_NAME_, defaultClassLoader);
		}

		return defaultConfigClass;
	}

	public Config newInstanceOfDefaultConfig() throws ParseException {
		return newInstanceOfDefaultConfig(null);
	}

	public Config newInstanceOfDefaultConfig(ClassLoader defaultClassLoader) throws ParseException {
		Class<?> clazz = getDefaultConfigClass(defaultClassLoader);
		if (clazz != null) {
			Object o = null;
			try {
				o = clazz.newInstance();
			} catch (Exception e) {
				throw new ParseException(e);
			}

			if (o != null && o instanceof Config) {
				((Config) o).resetTo(kv);
				return (Config) o;
			}
		} else {
//			return new ClassicConfiguration(configMap);
		}

		throw new ParseException("Default config class not found!");
	}

	public Class<?> getClass(String key, ClassLoader defaultClassLoader) {
		Object o = getObject(key);
		if (o != null) {
//			String className = (String) o;
//			if ("com.amituofo.common.kit.config.SimpleConfiguration".equals(className)) {
//				return null;
//			}

			Class<?> c = null;
			try {
				if (defaultClassLoader != null) {
					c = defaultClassLoader.loadClass((String) o);
				} else {
					c = Class.forName((String) o);
				}
				o = c;
				set(key, c);
				return c;
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}
		}

		try {
			return (Class<?>) o;
		} catch (Throwable e) {
//			e.printStackTrace();
			return null;
		}
	}

	public void validateConfig() throws InvalidConfigException {
		super.validateConfig();
		ValidUtils.invalidIfEmpty(getString(_CONFIG_CLASS_NAME_), "Configuration class name cound not be empty!");
	}

	public static ClassicConfiguration parseJsonString(String config) throws ParseException {
		if (StringUtils.isEmpty(config)) {
			return null;
		}
		Map<String, Object> configMap = JSON.parseObject(config, HashMap.class);
		ClassicConfiguration sc = new ClassicConfiguration();
		sc.resetTo(configMap);
		return sc;
	}

	public static Object parseJsonObject(String config, ClassLoader defaultClassLoader) throws ParseException {
		ClassicConfiguration conf = parseJsonString(config);
		if (conf == null) {
			return null;
		}

		return conf.newInstanceOfDefaultConfig(defaultClassLoader);
	}

	public static Object parseJsonObject(String config) throws ParseException {
		ClassicConfiguration conf = parseJsonString(config);
		if (conf == null) {
			return null;
		}

		return conf.newInstanceOfDefaultConfig(null);
	}

	public static ClassicConfiguration toSimpleConfiguration(Map<String, Object> map) {
		return new ClassicConfiguration(map);
	}

	public static List<ClassicConfiguration> toSimpleConfiguration(List<Map<String, Object>> listObj) {
		List<ClassicConfiguration> list = new ArrayList<ClassicConfiguration>();
		if (listObj != null) {
			for (Map<String, Object> map : listObj) {
				list.add(toSimpleConfiguration(map));
			}
		}
		return list;
	}

//	public static String toSerializableString(SimpleConfiguration simpleConfiguration) throws InvalidParameterException, JsonProcessingException, UnsupportedEncodingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		simpleConfiguration.validateConfig();
//		// return objectMapper.writeValueAsString(simpleConfiguration.configMap);
//
//		simpleConfiguration.configMap.put(CONFIG_ID, simpleConfiguration.getId());
//		byte[] buf = objectMapper.writeValueAsBytes(simpleConfiguration.configMap);
//		simpleConfiguration.configMap.remove(CONFIG_ID);
//
//		return new String(buf, "utf-8");
//	}

//	public static Object parseSerializableString(String config) throws JsonParseException, JsonMappingException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		HashMap<String, Object> configMap = objectMapper.readValue(config, new TypeReference<HashMap<String, Object>>() {
//		});
//		// Class<SimpleConfigurationEntry> cls = (Class<SimpleConfigurationEntry>) Class.forName((String) configMap.get(CONFIG_CLASS_NAME));
//		// SimpleConfigurationEntry sc = cls.newInstance();
//
//		SimpleConfiguration sc = new SimpleConfiguration();
//		sc.resetTo(configMap);
//		return sc;
//	}
//	public static SimpleConfiguration parseJsonStringToCustomSimpleConfiguration(String config, ClassLoader defaultClassLoader) throws ParseException {
//		try {
//			Map<String, Object> configMap = JSON.parseObject(config, HashMap.class);
//
//			SimpleConfiguration sc = null;
//
//			String clsname = (String) configMap.get(_CONFIG_CLASS_NAME_);
//			if (StringUtils.isNotEmpty(clsname)) {
//				Class<? extends SimpleConfiguration> cls = null;
//				if (defaultClassLoader != null) {
//					cls = (Class<? extends SimpleConfiguration>) defaultClassLoader.loadClass(clsname);
//				} else {
//					cls = (Class<? extends SimpleConfiguration>) Class.forName(clsname);
//				}
//				configMap.put(_CONFIG_CLASS_, cls);
//				sc = cls.newInstance();
//				sc.resetTo(configMap);
//				return sc;
//			} else {
////			sc = new SimpleConfiguration();
//			}
//
//		} catch (Exception e) {
//			throw new ParseException(e);
//		}
//
//		throw new ParseException("Configuration class not found!");
//	}
//
//	public static SimpleConfiguration toCustomSimpleConfiguration(Map<String, Object> configMap, ClassLoader defaultClassLoader) throws ParseException {
//		try {
//			SimpleConfiguration sc = null;
//
//			String clsname = (String) configMap.get(_CONFIG_CLASS_NAME_);
//			if (StringUtils.isNotEmpty(clsname)) {
//				Class<? extends SimpleConfiguration> cls = null;
//				if (defaultClassLoader != null) {
//					cls = (Class<? extends SimpleConfiguration>) defaultClassLoader.loadClass(clsname);
//				} else {
//					cls = (Class<? extends SimpleConfiguration>) Class.forName(clsname);
//				}
//				configMap.put(_CONFIG_CLASS_, cls);
//				sc = cls.newInstance();
//				sc.resetTo(configMap);
//				return sc;
//			} else {
////			sc = new SimpleConfiguration();
//			}
//
//		} catch (Exception e) {
//			throw new ParseException(e);
//		}
//
//		throw new ParseException("Configuration class not found!");
//	}

}
