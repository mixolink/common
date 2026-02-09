package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;
import com.amituofo.common.util.StringUtils;

public class DatetimeDayOfMonthAccepter extends DatetimeAccepterBase {
	private int dayOfMonth;
	private boolean isThisMonth;

	private static DatetimeAccepters[] DAY_OF_MONTH_LIST = new DatetimeAccepters[] { DatetimeAccepters.IS_FIRST_DAY_OF_MONTH, DatetimeAccepters.IS_LAST_DAY_OF_MONTH,
			DatetimeAccepters.IS_NTH_DAY_OF_MONTH };
	private static DatetimeAccepters[] DAY_OF_THIS_MONTH_LIST = new DatetimeAccepters[] { DatetimeAccepters.IS_FIRST_DAY_OF_THIS_MONTH,
			DatetimeAccepters.IS_LAST_DAY_OF_THIS_MONTH, DatetimeAccepters.IS_NTH_DAY_OF_THIS_MONTH };

	public DatetimeDayOfMonthAccepter(DatetimeAccepters type) throws InvalidParameterException {
		this(type, 0);
	}

	public DatetimeDayOfMonthAccepter(DatetimeAccepters type, int dayOfMonth) throws InvalidParameterException {
		super(type);

		if (!DatetimeAccepters.isAnyOf(DAY_OF_MONTH_LIST, type) && !DatetimeAccepters.isAnyOf(DAY_OF_THIS_MONTH_LIST, type)) {
			throw new InvalidParameterException(
					"Only support " + StringUtils.toString(DAY_OF_MONTH_LIST, ',') + " or " + StringUtils.toString(DAY_OF_THIS_MONTH_LIST, ','));
		}

		if ((type == DatetimeAccepters.IS_NTH_DAY_OF_MONTH || type == DatetimeAccepters.IS_NTH_DAY_OF_THIS_MONTH) && (dayOfMonth > 31 || dayOfMonth < 1)) {
			throw new InvalidParameterException("Day of month must between 1~31");
		}

		this.dayOfMonth = dayOfMonth;
		this.isThisMonth = DatetimeAccepters.isAnyOf(DAY_OF_THIS_MONTH_LIST, type);
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 将时间戳转换为 LocalDate
		LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		if (isThisMonth) {
			// 将时间戳转换为 LocalDate
			LocalDate today = LocalDate.now();

			if (thedate.getYear() != today.getYear() || thedate.getDayOfMonth() != today.getDayOfMonth()) {
				return false;
			}
		}

		if (type == DatetimeAccepters.IS_FIRST_DAY_OF_MONTH) {
			return thedate.getDayOfMonth() == 1;
		} else if (type == DatetimeAccepters.IS_LAST_DAY_OF_MONTH) {
			// 获取本月的最后一天
			LocalDate lastDayOfMonth = thedate.with(TemporalAdjusters.lastDayOfMonth());
			return lastDayOfMonth.getDayOfMonth() == thedate.getDayOfMonth();
		} else if (type == DatetimeAccepters.IS_NTH_DAY_OF_MONTH) {
			return thedate.getDayOfMonth() == dayOfMonth;
		}

		return false;
	}


}
