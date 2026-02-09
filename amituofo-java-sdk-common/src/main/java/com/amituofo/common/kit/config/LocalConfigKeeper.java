package com.amituofo.common.kit.config;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.amituofo.common.ex.ParseException;
import com.amituofo.common.ex.TestException;
import com.amituofo.common.ex.UnsupportedException;
import com.amituofo.common.kit.event.EventPusher;
import com.amituofo.common.util.FileUtils;
import com.amituofo.common.util.StringUtils;

public class LocalConfigKeeper<CONFIG extends Config> extends ConfigKeeperBase<CONFIG> {
	protected String configLocation = "./conf/";
	protected ConfigParser<CONFIG> parser;
	protected ClassLoader defaultClassLoader;

	protected Map<String, CONFIG> cache = new LinkedHashMap<>();

	protected final List<ConfigChangesListener<CONFIG>> listeners = new ArrayList<ConfigChangesListener<CONFIG>>();
//	private EventPusher ep = null;

	public LocalConfigKeeper(String configLocation, String ext, ConfigParser<CONFIG> parser, ClassLoader defaultClassLoader) {
		super(ext);
		this.configLocation = configLocation;
		this.parser = parser;
		this.defaultClassLoader = defaultClassLoader;

		File conndir = new File(configLocation);
		if (!conndir.exists()) {
			conndir.mkdirs();
		}
	}

	public LocalConfigKeeper(File configLocation, String ext, ConfigParser<CONFIG> parser, ClassLoader defaultClassLoader) {
		this(configLocation.getPath(), ext, parser, defaultClassLoader);
	}

	protected boolean fireSettingEvent(int eventcode, CONFIG conf) {
//		synchronized (listeners) {
			for (final ConfigChangesListener<CONFIG> listener : listeners) {
//				ep.push(new Runnable() {
//					@Override
//					public void run() {
//						listener.valueChanged(eventcode, conf);
//					}
//				});

				if (!listener.acceptChanges(eventcode, conf)) {
					return false;
				}
			}
//		}

		return true;
	}

	@Override
	public void addListener(ConfigChangesListener<CONFIG> listener) {
		listeners.add(listener);
//		if (ep == null) {
//			ep = new EventPusher();
//		}
	}

	@Override
	public void fireConfigChanged() {
		fireSettingEvent(ConfigChangesListener.ALL_CHANGED_EVENT, null);
	}

	@Override
	protected boolean storeSetting(CONFIG setting) throws IOException {
		if (setting == null) {
			return false;
		}

		if (!fireSettingEvent(ConfigChangesListener.SAVING_EVENT, setting)) {
			return false;
		}

		cache.put(setting.getId(), setting);

		String s = setting.toPrettyJsonString();

		File file = new File(configLocation + File.separator + setting.getId() + extension);
		FileUtils.writeToFile(s, file, "utf-8", false);

		fireSettingEvent(ConfigChangesListener.SAVED_EVENT, setting);

		return true;
	}

	@Override
	protected boolean deleteSetting(String id) throws IOException {
		CONFIG setting;
		try {
			setting = getSetting(id);
		} catch (ParseException e) {
			throw new IOException(e);
		}

		if (setting == null) {
			return false;
		}

		if (!fireSettingEvent(ConfigChangesListener.DELETING_EVENT, setting)) {
			return false;
		}

		cache.remove(id);

		boolean deleted = new File(configLocation + File.separator + id + extension).delete();
		if (deleted) {
			fireSettingEvent(ConfigChangesListener.DELETED_EVENT, setting);
		}
		return deleted;
	}

	@Override
	protected CONFIG getSetting(String id) throws ParseException, IOException {
		CONFIG entryConfigCache = cache.get(id);
		if (entryConfigCache != null) {
			return entryConfigCache;
		}
		File configFile = new File(configLocation + File.separator + id + extension);
		CONFIG entryConfig = parse(configFile);
		return entryConfig;
	}

	@Override
	protected CONFIG duplicateSetting(String id) throws ParseException, IOException {
		File configFile = new File(configLocation + File.separator + id + extension);
		CONFIG entryConfig = parse(configFile);
		entryConfig.generateNewID();

		int i = 1;
		final String newNameTemplate = entryConfig.getName() + "_Copy";
		String newName = newNameTemplate;
		while (getByName(newName) != null) {
			newName = (newNameTemplate + (i++));
		}
		entryConfig.setName(newName);
		storeSetting(entryConfig);
		return entryConfig;
	}

	@Override
	protected List<CONFIG> listSettings() {
		synchronized (configLocation) {
			List<CONFIG> confs = new LinkedList<>();
			File[] configFiles = null;
			configFiles = new File(configLocation).listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if (!name.endsWith(extension)) {
						return false;
					}

					String id = name.substring(0, name.length() - extension.length());

					if (cache.containsKey(id)) {
						return false;
					}

					return true;
				}
			});

			for (CONFIG confcache : cache.values()) {
				confs.add(confcache);
			}

			if (configFiles != null) {
				for (int i = 0; i < configFiles.length; i++) {
					try {
						CONFIG conf = parse(configFiles[i]);
						confs.add(conf);

						cache.put(conf.getId(), conf);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			return confs;
		}
	}

//	@Override
//	protected void deleteAllSettings() {
//		File[] configFiles = new File(configLocation).listFiles(new FilenameFilter() {
//
//			@Override
//			public boolean accept(File dir, String name) {
//				return name.endsWith(ext);// || name.endsWith("hcc");
//			}
//		});
//		for (File file : configFiles) {
//			
//			CONFIG setting = getSetting(id);
//			boolean deleted = file.delete();
//			if (deleted) {
//				fireSettingEvent(ConfigEvent.SAVED_EVEND, setting);
//			}
//			
//		}
//	}

	private CONFIG parse(File configFile) throws ParseException, IOException {
		CONFIG config = null;
		String configLocationContent = FileUtils.fileToString(configFile, "utf-8");

		if (parser != null) {
			config = parser.parse(configLocationContent, defaultClassLoader);
//		} else {
//			try {
//				config = (CONFIG) ClassicConfiguration.parseJsonObject(configLocationContent, defaultClassLoader);
//			} catch (ParseException e) {
//				throw e;
//			} catch (Exception e) {
//				throw new ParseException(e);
//			}
		}

		return config;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	@Override
	public void test(CONFIG conf) throws TestException {
	}

	@Override
	public ConfigParser<CONFIG> getConfigParser() {
		return parser;
	}

	@Override
	protected boolean containsSetting(String id) throws IOException {
		return new File(configLocation + File.separator + id + extension).exists();
	}

	@Override
	protected int countOfSettings() throws IOException {
		String[] configFiles = new File(configLocation).list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(extension);// || name.endsWith("hcc");
			}
		});
		return configFiles.length;
	}

	@Override
	public void close() {
		cache.clear();
		listeners.clear();
//		if (ep != null) {
//			ep.reset();
//		}
	}
}
