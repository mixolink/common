package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepter;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;

public class DatetimeTodayAccepter extends DatetimeAccepterBase {
	public DatetimeTodayAccepter() throws InvalidParameterException {
		super(DatetimeAccepters.IS_TODAY);
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 获取当前日期（不含时间）
		LocalDate today = LocalDate.now();

		// 将时间戳转换为LocalDate
		LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		// 比较日期
		return today.isEqual(thedate);
	}

}
