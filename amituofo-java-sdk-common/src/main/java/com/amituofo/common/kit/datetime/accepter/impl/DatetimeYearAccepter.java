package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;

public class DatetimeYearAccepter extends DatetimeAccepterBase {
	private int year;

	public DatetimeYearAccepter(int year) throws InvalidParameterException {
		super(DatetimeAccepters.IS_YEAR);
		if (year > 2900 || year < 1900) {
			throw new InvalidParameterException("Year must between 1900~2900");
		}
		this.year = year;
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 将时间戳转换为 LocalDate
		LocalDate currentDate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		return currentDate.getYear() == year;
//		return LocalDate.now().getYear() == currentDate.getYear() && currentDate.getMonthValue() == monthOfYear;
	}

}
