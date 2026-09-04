package com.amituofo.common.type;

public enum MessageType {
	INFO(400), WARN(300), ERROR(200);

	private final int intLevel;

	MessageType(final int val) {
		intLevel = val;
	}

	public int intLevel() {
		return intLevel;
	}

	public static MessageType valueOfLevel(String value) {
		for (MessageType s : MessageType.values())
			if (s.name().equals(value)) {
				return s;
			}
		return INFO;
	}
}
