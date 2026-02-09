package com.amituofo.common.kit.config;

import com.amituofo.common.ex.ParseException;

public interface ConfigParser<CONFIG> {
	CONFIG parse(String content, ClassLoader defaultClassLoader) throws ParseException;
}
