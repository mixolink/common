package com.amituofo.common.kit.datetime.accepter;

import com.amituofo.common.ex.InvalidParameterException;

public abstract class DatetimeAccepterBuilder {
	private DatetimeAccepters type;

	public abstract DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException;

	protected void setType(DatetimeAccepters type) {
		this.type=type;
	}

	public DatetimeAccepters getType() {
		return type;
	}
}
