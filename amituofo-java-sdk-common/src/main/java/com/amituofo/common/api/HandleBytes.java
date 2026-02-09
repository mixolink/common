package com.amituofo.common.api;

import com.amituofo.common.ex.HandleException;
import com.amituofo.common.type.HandleFeedback;

public interface HandleBytes {

	HandleFeedback handle(long remainLen, int dataLen, byte[] data) throws HandleException;

}
