package com.amituofo.common.type;

import com.amituofo.common.util.StringUtils;

public enum YesNo {
	Yes, No, Unknown;

	public static YesNo valueOfSet(String value) {
		return valueOfSet(value, YesNo.Unknown);
	}

	public static YesNo valueOfSet(Boolean value) {
		return valueOfSet(value == null ? null : value.toString(), YesNo.Unknown);
	}

	public static YesNo valueOfSet(Switch value) {
		if (value == null || value == Switch.auto) {
			return YesNo.Unknown;
		}

		if (value == Switch.off) {
			return YesNo.No;
		}

		return YesNo.Yes;
	}

	/**
	 * @param  value on=on/true/1/yes off=off/false/0/no auto=null/""
	 * @return
	 */
	public static YesNo valueOfSet(String value, YesNo defaultValue) {
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}

		if (Yes.name().equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "enable".equalsIgnoreCase(value) || "enabled".equalsIgnoreCase(value)
				|| "1".equals(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)) {
			return Yes;
		}

		if (No.name().equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) || "disable".equalsIgnoreCase(value) || "disabled".equalsIgnoreCase(value)
				|| "0".equals(value) || "no".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value)) {
			return No;
		}

		if (Unknown.name().equalsIgnoreCase(value)) {
			return Unknown;
		}

		return defaultValue;
	}

	public static YesNo valueOf(boolean value) {
		return value == true ? Yes : No;
	}

	public Boolean toBoolean() {
		if (this == Yes) {
			return Boolean.TRUE;
		}
		if (this == No) {
			return Boolean.FALSE;
		}
		return null;
	}

	public boolean toBooleanPreferFalse() {
		return (this == Yes);
	}

	public boolean toBooleanPreferTrue() {
		return (this != No);
	}

	public boolean toBoolean(boolean defaultValue) {
		if (this == Unknown) {
			return defaultValue;
		}

		return toBoolean();
	}

}
