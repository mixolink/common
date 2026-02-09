package com.amituofo.common.kit.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.amituofo.common.kit.kv.KeyValue;
import com.amituofo.common.type.TimeUnit;
import com.amituofo.common.util.FileUtils;

public class PropertiesConfig {
//	private final File CONFIG_FILE;

//	private final SimpleConfiguration config = new SimpleConfiguration();
//	private final Map<String, Object> cache = new HashMap<String, Object>();
	private final Properties properties = new Properties();
	private File CONFIG_FILE;

	public PropertiesConfig() {
//		CONFIG_FILE = new File(configFile);
	}

	public void loadConfig(String configFile) throws FileNotFoundException, IOException {
		CONFIG_FILE = new File(configFile);
		if (!CONFIG_FILE.exists()) {
			CONFIG_FILE.getParentFile().mkdirs();
		}

		if (!CONFIG_FILE.exists()) {
			throw new FileNotFoundException("Please copy [configuration] to : " + CONFIG_FILE.getAbsolutePath());
		}

//		s = FileUtils.readToString(CONFIG_FILE);
//		SimpleConfiguration setting = SimpleConfiguration.parseJsonString(s, SimpleConfiguration.class.getClass().getClassLoader());
//		properties.putSimpleConfiguration(setting);
//		System.out.println("Loading configuration from " + configFile);

		properties.clear();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(CONFIG_FILE));
		properties.load(bufferedReader);
	}

	public void save() throws IOException {
		StringBuilder sb = new StringBuilder();

		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			sb.append(key).append("=").append(value).append("\n");
		}
		FileUtils.writeToFile(sb.toString(), CONFIG_FILE, false);
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new LinkedHashMap<>();
		Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			map.put(key, value);
		}

		return map;
	}

	public void set(Map<String, String> kvmap) {
		if (kvmap == null || kvmap.size() == 0) {
			return;
		}

		for (String key : kvmap.keySet()) {
			properties.put(key, kvmap.get(key));
		}
	}

	public void setValue(String id, String value) {
		properties.put(id, value);
	}

//	public void setValue(String id, List<String> value) {
//		properties.put(id, value);
//	}

//	public void setValue(String id, String[] value) {
//		properties.put(id, value);
//	}

	public void setValue(String id, long value) {
		properties.put(id, Long.toString(value));
	}

	public void setValue(String id, boolean value) {
		properties.put(id, Boolean.toString(value));
	}

//	public void setValue(String id, Font value) {
//		properties.put(id, value.getFamily() + "," + value.getStyle() + "," + value.getSize());
//	}

	public void setValueIfNotExist(String id, String value) {
		if (!properties.containsKey(id)) {
			setValue(id, value);
		}
	}

//	public void setValueIfNotExist(String id, Font value) {
//		if (!properties.containsKey(id)) {
//			setValue(id, value);
//		}
//	}

	public void setValueIfNotExist(String id, long value) {
		if (!properties.containsKey(id)) {
			setValue(id, value);
		}
	}

	public void setValueIfNotExist(String id, boolean value) {
		if (!properties.containsKey(id)) {
			setValue(id, value);
		}
	}

	public String getString(String id) {
		return properties.getProperty(id);
	}

	public String getString(String id, String defualtValue) {
		String v = properties.getProperty(id);
		if (v == null) {
			return defualtValue;
		}

		return v;
	}

//	public List<String> getStringList(String key) {
//		return config.getStringList(key);
//	}

	public Long getLong(String id) {
		return Long.valueOf(properties.getProperty(id));
	}

	public Integer getInt(String id) {
		return Integer.valueOf(properties.getProperty(id));
	}

	public Integer getInt(String id, Integer defualtValue) {
		String o = properties.getProperty(id);
		if (o == null) {
			return defualtValue;
		} else {
			return Integer.valueOf(o);
		}
	}

	public long getMillisecondsProperty(String key, long defaultValue) {
		String o = properties.getProperty(key);
		if (o == null) {
			return defaultValue;
		} else {
			return TimeUnit.parse(o, defaultValue);
		}
	}

//	public SimpleDateFormat getDateFormat(String id) {
//		Object fmt = cache.get(id);
//		if (fmt != null) {
//			return (SimpleDateFormat) fmt;
//		}
//
//		String fmtdata = config.getString(id);
//		SimpleDateFormat dtfmt = null;
//		if (fmtdata == null) {
//			fmtdata = "yyyy/MM/dd hh:mm:ss";
//		}
//
//		dtfmt = new SimpleDateFormat(fmtdata);
//		// String tzvalue = Config.getString(ConfigKeys.TIME_ZONE);
//		// if (StringUtils.isNotEmpty(tzvalue)) {
//		// TimeZone tz = TimeZone.getTimeZone(tzvalue);
//		// dtfmt.setTimeZone(tz);
//		// }
//
//		cache.put(id, dtfmt);
//
//		return dtfmt;
//
//	}

	public Boolean getBoolean(String id) {
		return Boolean.valueOf(properties.getProperty(id));
	}

	public boolean isTrue(String id) {
		return "true".equals(properties.getProperty(id));
	}

	// public Boolean getBoolean(String id, Boolean defaultValue) {
	// Boolean val = config.getBoolean(id);
	// if (val == null) {
	// return defaultValue;
	// }
	// return val;
	// }

	public boolean has(String key) {
		return properties.contains(key);
	}

	// public boolean hasNotNull(String key) {
	// return properties.containsKeyNotNull(key);
	// }

	public List<KeyValue<String>> keysWithPerfix(String perfix, boolean removePerfix) {
		List<KeyValue<String>> list = new ArrayList<>();

		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			key = key.trim();
			int i = key.indexOf(perfix);
			if (i == 0) {
				String value = properties.getProperty(key);
				if (removePerfix) {
					key = key.substring(perfix.length());
				}
				list.add(new KeyValue<String>(key, value));
			}
		}
		return list;
	}

}
