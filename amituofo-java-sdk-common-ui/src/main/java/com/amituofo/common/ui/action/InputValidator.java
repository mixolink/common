package com.amituofo.common.ui.action;

import com.amituofo.common.ex.InvalidParameterException;

public interface InputValidator<T> {

	void validate(T value) throws InvalidParameterException;

}
