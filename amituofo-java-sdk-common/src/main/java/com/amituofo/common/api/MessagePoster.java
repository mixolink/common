package com.amituofo.common.api;

import com.amituofo.common.type.Level;

public interface MessagePoster {
//	void postEvent(String catalog, Level level, String message);

	void post(Level level, String catalog, String message, Object... args);

}
