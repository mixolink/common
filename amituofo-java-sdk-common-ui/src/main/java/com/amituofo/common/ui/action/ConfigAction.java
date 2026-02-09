package com.amituofo.common.ui.action;

import com.amituofo.common.ex.InvalidConfigException;

public interface ConfigAction<T> {
	void validateConfig() throws InvalidConfigException;

	void showConfig(T config);

	void updateConfig(T config);
}
