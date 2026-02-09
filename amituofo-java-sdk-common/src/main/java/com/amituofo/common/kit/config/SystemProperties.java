package com.amituofo.common.kit.config;

import com.amituofo.common.type.Switch;
import com.amituofo.common.type.TimeUnit;
import com.amituofo.common.util.StringUtils;

public class SystemProperties {
	public static final String SYS_SHORT_NAME = "system.name";
	public static final String SYS_FULL_NAME = "system.fullname";
	public static final String SYS_VERSION = "system.version";
	public static final String SYS_BUILD = "system.build";

	public static final String SYS_ROOT_PATH = "system.root.dir";

	public static final String SYS_CONF_LOG_PATH = "system.conf.log.dir";
	public static final String SYS_CONF_TEMP_PATH = "system.conf.temp.dir";
	public static final String SYS_CONF_PATH = "system.conf.dir";
	public static final String SYS_CONF_BIN_PATH = "system.conf.bin.dir";
	public static final String SYS_CONF_APPDATA_PATH = "system.conf.appdata.dir";
	public static final String SYS_CONF_APPCONF_PATH = "system.conf.appconf.dir";
	public static final String SYS_CONF_APP_PATH = "system.conf.app.dir";
	public static final String SYS_CONF_APP_ID = "system.conf.app.id";
	public static final String SYS_CONF_NAME = "system.conf.name";

	public static final String SYS_DEBUG = "system.debug";

	public static void setProperty(String key, String value) {
		System.setProperty(key, value);
	}

	public static void setProperty(String key, boolean value) {
		System.setProperty(key, value ? "1" : "0");
	}

	public static void setProperty(String key, long value) {
		System.setProperty(key, String.valueOf(value));
	}

	public static void setPropertyIfEmpty(String key, String value) {
		if (StringUtils.isEmpty(System.getProperty(key))) {
			System.setProperty(key, value);
		}
	}

	public static void setPropertyIfEmpty(String key, long value) {
		if (StringUtils.isEmpty(System.getProperty(key))) {
			System.setProperty(key, String.valueOf(value));
		}
	}

	public static String getStringProperty(String key) {
		return System.getProperty(key);
	}

	public static String getStringProperty(String key, String defaultValue) {
		return System.getProperty(key, defaultValue);
	}

	public static long getMillisecondsProperty(String key, long defaultValue) {
		String v = System.getProperty(key);
		return TimeUnit.parse(v, defaultValue);
	}

//	public static boolean getBooleanProperty(String key, boolean defaultValue) {
//		String v = System.getProperty(key);
//		if (StringUtils.isNotEmpty(v)) {
//			return Switc;
//		}
//
//		return defaultValue;
//	}
	public static int getIntProperty(String key, int defaultValue) {
		String v = System.getProperty(key);
		if (StringUtils.isEmpty(v)) {
			return defaultValue;
		}

		return Integer.parseInt(v);
	}

	public static long getLongProperty(String key, long defaultValue) {
		String v = System.getProperty(key);
		if (StringUtils.isEmpty(v)) {
			return defaultValue;
		}

		return Long.parseLong(v);
	}

	public static Switch getSwitchProperty(String key, Switch defaultValue) {
		String v = System.getProperty(key);
		if (StringUtils.isEmpty(v)) {
			return defaultValue;
		}

		return Switch.valueOfSet(v);
	}

	public static Switch getSwitchProperty(String key) {
		String v = System.getProperty(key);

		return Switch.valueOfSet(v);
	}

	public static void setPropertyIfEmpty(String key, boolean value) {
		if (StringUtils.isEmpty(System.getProperty(key))) {
			setProperty(key, value);
		}
	}
}
