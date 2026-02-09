package com.amituofo.common.kit.config;

import com.amituofo.common.api.ValueChangedListener;

public interface ConfigChangedListener extends ValueChangedListener {
	void changed(String key, Object newValue, Object oldValue);
}
