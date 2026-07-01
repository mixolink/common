package com.amituofo.common.kit.config;

import com.amituofo.common.api.ObjectHandler;
import com.amituofo.common.type.HandleFeedback;

public interface ConfigHandler<CONFIG> extends ObjectHandler<Integer, CONFIG> {
	HandleFeedback handle(Integer meta, CONFIG config);
}
