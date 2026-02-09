package com.amituofo.common.kit.datetime.accepter;

public interface DatetimeAccepter {
	boolean accept(long millisecond);
	
	DatetimeAccepters type();
	
	DatetimeAccepterOption getOption();

	void setOption(DatetimeAccepterOption option);

}
