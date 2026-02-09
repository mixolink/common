package com.amituofo.common.kit.config;

import java.util.Map;

import com.amituofo.common.ex.InvalidConfigException;

public interface Config {

	void generateNewID();

	String getId();

	String getCatalogId();

	String getName();

	void setName(String name);

	void validateConfig() throws InvalidConfigException;

	Configuration asConfiguration();

	Map<String, Object> asMap();

	Map<String, Object> toMap();

	String toPrettyJsonString();

	String toJsonString();

	void resetTo(Map<String, Object> configMap);

//	void resetTo(Configuration config);

	void resetTo(Config config);

}
