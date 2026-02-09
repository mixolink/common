package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;

public class DatetimeMonthOfYearAccepter extends DatetimeAccepterBase {
	private int monthOfYear;

	public DatetimeMonthOfYearAccepter(int monthOfYear) throws InvalidParameterException {
		super(DatetimeAccepters.IS_MONTH);
		if (monthOfYear > 12 || monthOfYear < 1) {
			throw new InvalidParameterException("Month of year must between 1~12");
		}
		this.monthOfYear = monthOfYear;
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 将时间戳转换为 LocalDate
		LocalDate currentDate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		return currentDate.getMonthValue() == monthOfYear;
//		return LocalDate.now().getYear() == currentDate.getYear() && currentDate.getMonthValue() == monthOfYear;
	}

}
