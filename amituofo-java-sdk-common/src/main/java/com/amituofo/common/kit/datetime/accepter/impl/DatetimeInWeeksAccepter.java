package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;

public class DatetimeInWeeksAccepter extends DatetimeAccepterBase {

	private DayOfWeek[] dayOfWeeks;

	public DatetimeInWeeksAccepter(DayOfWeek[] dayOfWeeks) throws InvalidParameterException {
		super(DatetimeAccepters.IS_IN_WEEKS);

		if (dayOfWeeks == null || dayOfWeeks.length == 0) {
			throw new InvalidParameterException("Day of weeks [MONDAY;TUESDAY;WEDNESDAY;THURSDAY;SATURDAY;SUNDAY] required!");
		}
		this.dayOfWeeks = dayOfWeeks;
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 将时间戳转换为 LocalDate
		LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(java.time.ZoneId.systemDefault()).toLocalDate();

		// 获取星期几
		DayOfWeek dayOfWeek0 = thedate.getDayOfWeek();

		for (DayOfWeek dayOfWeek : dayOfWeeks) {
			if (dayOfWeek == dayOfWeek0) {
				return true;
			}
		}

		return false;
	}

}
