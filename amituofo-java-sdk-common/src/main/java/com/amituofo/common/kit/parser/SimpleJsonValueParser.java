package com.amituofo.common.kit.parser;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.api.StringValueParser;
import com.amituofo.common.ex.ParseException;
import com.amituofo.common.util.StringUtils;

public class SimpleJsonValueParser<T> implements StringValueParser<T> {
	protected Class<T> cls;

	public SimpleJsonValueParser(Class<T> cls) {
		this.cls = cls;
	}

	@Override
	public T parse(String value) throws ParseException {
		if (StringUtils.isEmpty(value)) {
//			try {
//				return cls.newInstance();
//			} catch (Exception e) {
//				throw new ParseException(e);
//			}
			return null;
		}
		return JSON.parseObject(value, cls);
	}
};
