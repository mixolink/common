package com.amituofo.common.kit.config;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.ex.TestException;
import com.amituofo.common.ex.UnsupportedException;
import com.amituofo.common.util.FileUtils;
import com.amituofo.common.util.URLUtils;

public interface ConfigKeeper<CONFIG extends Config> extends Closeable {
//	void close();

	void addFloat(CONFIG setting) throws IOException;

	ConfigParser<CONFIG> getConfigParser();

	String getExt();

	void addListener(ConfigChangesListener<CONFIG> listener) throws UnsupportedException;

	void fireConfigChanged();

	boolean save(CONFIG setting, boolean overwrite) throws InvalidConfigException, IOException;

	boolean exist(String id) throws IOException;

	CONFIG get(String id) throws ParseException, IOException;

	CONFIG getByName(String name) throws ParseException, IOException;

	boolean delete(String id) throws IOException;

	CONFIG duplicate(String id) throws InvalidConfigException;

	List<CONFIG> list() throws ParseException, IOException;

//	List<CONFIGKEY> listKeys();

	void reload(ConfigLoadEvent<CONFIG> configLoadEvent);

	void deleteAll() throws IOException;

	int count() throws IOException;

	void test(CONFIG conf) throws TestException;

	public static Config parse(File configFile, Class<? extends Config> cls) throws IOException, InstantiationException, IllegalAccessException {
		String config = FileUtils.fileToString(configFile, "utf-8");

//		ObjectMapper objectMapper = new ObjectMapper();
//		HashMap<String, Object> configMap = objectMapper.readValue(config, new TypeReference<HashMap<String, Object>>() {
//		});

//		Map<String, Object> configMap = JSON.parseObject(config).getInnerMap();
		Map<String, Object> configMap = JSON.parseObject(config, HashMap.class);

		Config conf = parse(configMap, cls);
		return conf;
	}

	public static Config parse(Map<String, Object> configMap, Class<? extends Config> cls) throws IOException, InstantiationException, IllegalAccessException {
		Config conf = (Config) cls.newInstance();
		conf.resetTo(configMap);
		return conf;
	}

	public static void save(Config setting, String saveLocation, String ext) throws InvalidConfigException {
		try {
			String s = setting.toPrettyJsonString();
			String name = setting.getName();
			name = name.replace("\\", "").replace("/", "").replace(":", "").replace("?", "").replace("*", "").replace(">", "").replace("<", "").replace("|", "");
			File file = new File(URLUtils.catFilePath(saveLocation, name + (ext.charAt(0) == '.' ? ext : "." + ext)));
			FileUtils.writeToFile(s, file, "utf-8", false);
		} catch (Exception e) {
			throw new InvalidConfigException(e);
		}
	}

}
