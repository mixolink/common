package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterOption;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;
import com.amituofo.common.type.CompareOperator;

public class DatetimeBetweenDayOfMonthAccepter extends DatetimeAccepterBase {
	private int day1, day2;
	private boolean isThisMonth;
	private CompareOperator operator;

	public DatetimeBetweenDayOfMonthAccepter(DatetimeAccepterOption option, boolean isThisMonth) throws InvalidParameterException {
		this(option.getFromDay(), option.getToDay(), isThisMonth);

		this.setOption(option);
	}

	public DatetimeBetweenDayOfMonthAccepter(Integer fromDate, Integer toDate, boolean isThisMonth) throws InvalidParameterException {
		super(DatetimeAccepters.IS_BETWEEN_TIME);

		if (fromDate != null && toDate != null) {
			this.day1 = fromDate;
			this.day2 = toDate;
			this.operator = CompareOperator.between;
		} else if (fromDate == null && toDate != null) {
			this.day1 = toDate;
			this.operator = CompareOperator.less;
		} else if (fromDate != null && toDate == null) {
			this.day1 = fromDate;
			this.operator = CompareOperator.greater;
		} else {
			operator = null;

			throw new InvalidParameterException("From [DAY] and To [DAY] required.");
		}

		this.isThisMonth = isThisMonth;
	}

	@Override
	protected boolean accept0(long millisecond) {
		LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		if (isThisMonth) {
			// 将时间戳转换为 LocalDate
			LocalDate today = LocalDate.now();

			if (thedate.getYear() != today.getYear() || thedate.getDayOfMonth() != today.getDayOfMonth()) {
				return false;
			}
		}

		int day = thedate.getDayOfMonth();

		switch (operator) {
		case greater:
			return day >= day1;
		case less:
			return day <= day1;
		case between:
			return day >= day1 && day <= day2;
		default:
			return false;
		}
	}

}
