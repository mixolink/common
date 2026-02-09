package com.amituofo.common.kit.datetime.accepter;

public abstract class DatetimeAccepterBase implements DatetimeAccepter {
	protected DatetimeAccepters type;
	protected DatetimeAccepterOption option;

	public DatetimeAccepterBase(DatetimeAccepters type) {
		super();
		this.type = type;
	}

	public DatetimeAccepterBase(DatetimeAccepters type, DatetimeAccepterOption option) {
		super();
		this.type = type;
		this.option = option;
	}

	@Override
	public DatetimeAccepters type() {
		return type;
	}

	@Override
	public DatetimeAccepterOption getOption() {
		return option;
	}

	@Override
	public void setOption(DatetimeAccepterOption option) {
		this.option = option;
	}

	protected abstract boolean accept0(long millisecond);

	@Override
	public boolean accept(long millisecond) {
		return accept0(millisecond + option.getDatetimeOffset());
	}

}
