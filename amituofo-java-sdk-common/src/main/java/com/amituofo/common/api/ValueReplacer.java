package com.amituofo.common.api;

import com.amituofo.common.ex.InvalidParameterException;

public interface ValueReplacer<T> {
	default T[] replace(T[] value) throws InvalidParameterException {
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				value[i] = replace(value[i]);
			}
		}

		return value;
	}

	T replace(T value, Object... params) throws InvalidParameterException;
}
