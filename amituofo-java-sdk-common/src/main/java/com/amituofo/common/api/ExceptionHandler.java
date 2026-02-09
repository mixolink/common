package com.amituofo.common.api;

public interface ExceptionHandler<OBJ> {

	void exceptionCaught(OBJ data, Throwable e);

}
