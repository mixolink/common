package com.amituofo.common.kit.event;

import com.amituofo.common.kit.kv.KeyValue;

public class EventPayload extends KeyValue<Object> {

//	public SysEventMessage() {
//		super();
//		// TODO Auto-generated constructor stub
//	}

	public EventPayload(String key, Object value) {
		super(key, value);
		// TODO Auto-generated constructor stub
	}

	public EventPayload(String key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

}
