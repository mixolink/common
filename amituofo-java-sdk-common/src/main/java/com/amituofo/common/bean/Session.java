package com.amituofo.common.bean;

import java.util.HashMap;
import java.util.Map;

public class Session {
	private final Map<String, Object> session = new HashMap<String, Object>();

	public Session() {
	}

	public void setAttribute(String name, Object value) {
		session.put(name, value);
	}

	public Object getAttribute(String name) {
		return session.get(name);
	}

	public Boolean getBooleanAttribute(String name) {
		return (Boolean) session.get(name);
	}

	public String getStringAttribute(String name) {
		return (String) session.get(name);
	}

	public void removeAttribute(String name) {
		session.remove(name);
	}

	public boolean hasAttribute(String name) {
		return session.containsKey(name);
	}
}
