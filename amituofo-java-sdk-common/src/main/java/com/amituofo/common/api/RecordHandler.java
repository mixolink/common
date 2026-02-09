package com.amituofo.common.api;

import com.amituofo.common.type.HandleFeedback;

public interface RecordHandler<OBJ_META, OBJ> extends ObjectHandler<OBJ_META, OBJ> {
//	default void exceptionCaught(String data, Throwable e) {
//	}

	HandleFeedback handle(OBJ_META meta, OBJ obj);

	void finished();
}
