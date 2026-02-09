package com.amituofo.common.kit.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.util.StringUtils;

public abstract class ConfigKeeperBase<CONFIG extends Config> implements ConfigKeeper<CONFIG> {
	private final Comparator<CONFIG> COMPARE = new Comparator<CONFIG>() {
		@Override
		public int compare(CONFIG o1, CONFIG o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}
			if (o1 == null && o2 != null) {
				return 1;
			}
			if (o1 != null && o2 == null) {
				return -1;
			}
			return o1.getName().compareTo(o2.getName());
		}
	};

	private final List<CONFIG> floatConfList = new ArrayList<CONFIG>();
	private final Map<String, CONFIG> floatConfMap = new LinkedHashMap<String, CONFIG>();
	protected String extension;

	public ConfigKeeperBase(String defaultExt) {
		if (StringUtils.isNotEmpty(defaultExt)) {
			extension = defaultExt.trim().toLowerCase();
			if (defaultExt.charAt(0) != '.') {
				extension = "." + defaultExt;
			} else {
				extension = defaultExt;
			}
		} else {
			this.extension = ".hsjc";
		}
	}

	protected abstract boolean storeSetting(CONFIG setting) throws IOException;

	protected abstract boolean deleteSetting(String id) throws IOException;

	protected abstract CONFIG getSetting(String id) throws ParseException, IOException;

	protected abstract CONFIG duplicateSetting(String id) throws ParseException, IOException;

	protected abstract List<CONFIG> listSettings() throws ParseException, IOException;

	protected abstract boolean containsSetting(String id) throws IOException;

	protected abstract int countOfSettings() throws IOException;

	@Override
	public void close() {
		floatConfMap.clear();
		floatConfList.clear();
	}

	@Override
	public void addFloat(CONFIG setting) throws IOException {
		if (setting == null || floatConfMap.containsKey(setting.getId())) {
			return;
		}

//		if (containsSetting(setting.getId())) {
//			return;
//		}

		floatConfMap.put(setting.getName(), setting);
		floatConfMap.put(setting.getId(), setting);
		floatConfList.add(setting);
	}

	@Override
	public boolean save(CONFIG setting, boolean overwrite) throws InvalidConfigException, IOException {
//		if (floatConfMap.containsKey(setting.getId())) {
//			return;
//		}

		if (!overwrite) {
			CONFIG existConCfg;
			try {
				existConCfg = getByName(setting.getName());
			} catch (ParseException e) {
				throw new InvalidConfigException(e);
			}
			if (existConCfg != null && !existConCfg.getId().equals(setting.getId())) {
				throw new InvalidConfigException("Setting with the same name [" + setting.getName() + "] already exists!");
			}
		}

		return storeSetting(setting);
	}

	@Override
	public boolean exist(String id) throws IOException {
		return containsSetting(id) || floatConfMap.containsKey(id);
	}

	@Override
	public CONFIG get(String id) throws ParseException, IOException {
		CONFIG conf = null;
		conf = getSetting(id);
		if (conf == null) {
			conf = floatConfMap.get(id);
		}

		return conf;
	}

	@Override
	public CONFIG getByName(String name) throws ParseException, IOException {
		CONFIG conf = null;
		Collection<CONFIG> entrys = list();
		for (CONFIG conf0 : entrys) {
			if (conf0.getName().equalsIgnoreCase(name)) {
				conf = conf0;
				break;
			}
		}

		if (conf == null) {
			conf = floatConfMap.get(name);
		}
		return conf;
	}

	@Override
	public boolean delete(String id) throws IOException {
		boolean deleted = false;
		deleted = deleteSetting(id);

		return deleted;
//		if (deleted) {
//			CONFIG setting = cache.remove(id);
//			if (setting != null) {
//				for (ConfigEvent<CONFIG> entryConfigEvent : events) {
//					EventPusher.push(new Runnable() {
//						@Override
//						public void run() {
//							entryConfigEvent.valueChanged(ConfigEvent.DELETED_EVEND, setting);
//						}
//					});
//				}
//			}
//		}
	}

	@Override
	public CONFIG duplicate(String id) throws InvalidConfigException {
		try {
			CONFIG entryConfig = duplicateSetting(id);
			return entryConfig;
		} catch (Exception e) {
			throw new InvalidConfigException(e);
		}
	}

	@Override
	public List<CONFIG> list() throws ParseException, IOException {
		List<CONFIG> list = listSettings();
		if (list != null && list.size() != 0) {
			Collections.sort(list, COMPARE);
		} else if (list == null) {
			list = new ArrayList<CONFIG>();
		}

		if (!floatConfList.isEmpty()) {
			list.addAll(0, floatConfList);
		}

		return list;
	}

	@Override
	public void reload(ConfigLoadEvent<CONFIG> configLoadEvent) {
	}

	@Override
	public void deleteAll() throws IOException {
		try {
			List<CONFIG> list = listSettings();
			if (list != null && list.size() != 0) {
				for (CONFIG config : list) {
					deleteSetting(config.getId());
				}
			}
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getExt() {
		return extension;
	}

	@Override
	public int count() throws IOException {
		return countOfSettings() + floatConfList.size();
	}

}
