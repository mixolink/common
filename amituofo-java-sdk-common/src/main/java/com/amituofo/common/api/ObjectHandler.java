package com.amituofo.common.api;

import com.amituofo.common.type.HandleFeedback;

public interface ObjectHandler<OBJ_META, OBJ> extends ExceptionHandler<OBJ>{

	HandleFeedback handle(OBJ_META meta, OBJ item);
}
