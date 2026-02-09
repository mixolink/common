package com.amituofo.common.api;

public interface ActionStrategy<RESULT> {
	RESULT match();
}
