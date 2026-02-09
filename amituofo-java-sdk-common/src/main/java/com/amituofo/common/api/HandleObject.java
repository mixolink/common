package com.amituofo.common.api;

import com.amituofo.common.ex.HandleException;

public interface HandleObject<OBJ_META, OBJ, RESULT> {

	RESULT handle(OBJ_META meta, OBJ item) throws HandleException;
}
