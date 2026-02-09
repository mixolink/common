package com.amituofo.common.type;

public enum Level {
	INFO(400), WARN(300), ERROR(200), FATAL(100), DEBUG(500);

	private final int intLevel;

	Level(final int val) {
		intLevel = val;
	}

	public int intLevel() {
		return intLevel;
	}

	public static Level valueOfLevel(String value) {
		for (Level s : Level.values())
			if (s.name().equals(value)) {
				return s;
			}
		return INFO;
	}
}
