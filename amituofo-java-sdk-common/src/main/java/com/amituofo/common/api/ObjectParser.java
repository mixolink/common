package com.amituofo.common.api;

import java.io.InputStream;

import com.amituofo.common.ex.ParseException;

public interface ObjectParser<S, T> {
	T parse(S source) throws ParseException;
}
