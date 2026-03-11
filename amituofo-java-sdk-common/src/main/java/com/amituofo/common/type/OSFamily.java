package com.amituofo.common.type;

import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;

public enum OSFamily {
	LINUX, WINDOWS, MACOS, UNKNOWN;

	public String warpPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}

		if (path.indexOf(' ') != -1) {
			if (this == WINDOWS) {
				return "\"" + path + "\"";
			}

			return "'" + path + "'";

		}

		return path;
	}

	public boolean isWindows() {
		return this == WINDOWS;
	}

	public boolean isLinuxCore() {
		return this == LINUX || this == MACOS;
	}

	public static OSFamily getCurrentOSFamily() {
		if (SystemUtils.isWindows()) {
			return WINDOWS;
		}

		if (SystemUtils.isLinux()) {
			return LINUX;
		}

		if (SystemUtils.isMacOS()) {
			return MACOS;
		}

		return UNKNOWN;
	}

}
