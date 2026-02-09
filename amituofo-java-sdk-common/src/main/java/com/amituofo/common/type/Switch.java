package com.amituofo.common.type;

import com.amituofo.common.util.StringUtils;

public enum Switch {
	auto, on, off;

	public static Switch valueOfSet(String value) {
		return valueOfSet(value, Switch.auto);
	}

	public static Switch valueOfSet(Boolean value) {
		return valueOfSet(value == null ? null : value.toString(), Switch.auto);
	}

	public static Switch valueOfSet(YesNo value) {
		if (value == null) {
			return Switch.auto;
		}

		if (value == YesNo.No) {
			return Switch.off;
		}

		return Switch.on;
	}

	/**
	 * @param  value on=on/true/1/yes off=off/false/0/no auto=null/""
	 * @return
	 */
	public static Switch valueOfSet(String value, Switch defaultValue) {
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}

		if (on.name().equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "enable".equalsIgnoreCase(value) || "enabled".equalsIgnoreCase(value)
				|| "1".equals(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)) {
			return on;
		}

		if (off.name().equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) || "disable".equalsIgnoreCase(value) || "disabled".equalsIgnoreCase(value)
				|| "0".equals(value) || "no".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value)) {
			return off;
		}

		if (auto.name().equalsIgnoreCase(value)) {
			return auto;
		}

		return defaultValue;
	}

	public static Switch valueOf(boolean value) {
		return value == true ? on : off;
	}

	public Boolean toBoolean() {
		if (this == on) {
			return Boolean.TRUE;
		}
		if (this == off) {
			return Boolean.FALSE;
		}
		return null;
	}

	public boolean toBooleanPreferFalse() {
		return (this == on);
	}

	public boolean toBooleanPreferTrue() {
		return (this != off);
	}

	public boolean toBoolean(boolean defaultValue) {
		if (this == auto) {
			return defaultValue;
		}

		return toBoolean();
	}

}
