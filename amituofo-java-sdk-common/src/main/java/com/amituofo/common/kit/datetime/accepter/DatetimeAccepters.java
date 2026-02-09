package com.amituofo.common.kit.datetime.accepter;

import com.amituofo.common.define.Constants;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeBetweenAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeBetweenDayOfMonthAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeDayOfMonthAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeExceedDurationAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeInWeeksAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeMonthOfYearAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeNumberOfWeekAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeTodayAccepter;
import com.amituofo.common.kit.datetime.accepter.impl.DatetimeWeekOfMonthAccepter;

public enum DatetimeAccepters {
	//
	IS_BETWEEN_TIME(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeBetweenAccepter itemDatetimeAccepter = new DatetimeBetweenAccepter(option.getFromMillisecond(), option.getToMillisecond());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_BETWEEN_DAY_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeBetweenDayOfMonthAccepter itemDatetimeAccepter = new DatetimeBetweenDayOfMonthAccepter(option.getFromDay(), option.getToDay(), false);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_BETWEEN_DAY_OF_THIS_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeBetweenDayOfMonthAccepter itemDatetimeAccepter = new DatetimeBetweenDayOfMonthAccepter(option.getFromDay(), option.getToDay(), true);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_IN_WEEKS(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeInWeeksAccepter itemDatetimeAccepter = new DatetimeInWeeksAccepter(option.getDayOfWeeks());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_WEEK(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeInWeeksAccepter itemDatetimeAccepter = new DatetimeInWeeksAccepter(option.getDayOfWeeks());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_FIRST_DAY_OF_THIS_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_LAST_DAY_OF_THIS_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_NTH_DAY_OF_THIS_MONTH(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int dayOfMonth;
			if (option.getTimeOffset().intValue() <= 31) {
				dayOfMonth = (int) option.getTimeOffset().intValue();
			} else {
				dayOfMonth = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
			}
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType(), dayOfMonth);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_FIRST_DAY_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_LAST_DAY_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_NTH_DAY_OF_MONTH(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int dayOfMonth;
			if (option.getTimeOffset().intValue() <= 31) {
				dayOfMonth = (int) option.getTimeOffset().intValue();
			} else {
				dayOfMonth = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
			}
			DatetimeDayOfMonthAccepter itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(getType(), dayOfMonth);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}), //
	IS_FIRST_WEEK_OF_THIS_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), 1);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_LAST_WEEK_OF_THIS_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), -1);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_NTH_WEEK_OF_THIS_MONTH(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int weekOfMonth = (int) option.getTimeOffset().intValue();
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), weekOfMonth);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}), 
	//
	IS_FIRST_WEEK_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), 1);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_LAST_WEEK_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), -1);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_NTH_WEEK_OF_MONTH(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int weekOfMonth = (int) option.getTimeOffset().intValue();
			DatetimeWeekOfMonthAccepter itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(getType(), weekOfMonth);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_MONTH(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int monthOfYear;
			if (option.getTimeOffset().intValue() <= 12) {
				monthOfYear = (int) option.getTimeOffset().intValue();
			} else {
				monthOfYear = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
			}
			DatetimeMonthOfYearAccepter itemDatetimeAccepter = new DatetimeMonthOfYearAccepter(monthOfYear);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	IS_TODAY(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeTodayAccepter itemDatetimeAccepter = new DatetimeTodayAccepter();
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
//	IS_MONTH_OFFSET_FROM_TODAY(true, new DatetimeAccepterBuilder() {
//
//		@Override
//		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
//			int monthOffset;
//			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_30_DAY) {
//				monthOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_30_DAY);
//			} else {
//				monthOffset = (int) option.getTimeOffset().intValue();
//			}
//			DatetimeMonthOffsetFromTodayAccepter itemDatetimeAccepter = new DatetimeMonthOffsetFromTodayAccepter(monthOffset);
//			itemDatetimeAccepter.setOption(option);
//			return itemDatetimeAccepter;
//		}
//	}),
	//
//	IS_DAY_OFFSET_FROM_TODAY(true, new DatetimeAccepterBuilder() {
//
//		@Override
//		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
//			int dayOffset;
//			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_1_DAY) {
//				dayOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
//			} else {
//				dayOffset = (int) option.getTimeOffset().intValue();
//			}
//			DatetimeDayOffsetFromTodayAccepter itemDatetimeAccepter = new DatetimeDayOffsetFromTodayAccepter(dayOffset);
//			itemDatetimeAccepter.setOption(option);
//			return itemDatetimeAccepter;
//		}
//	}),
	// 偏移N天后是星期几
//	IS_WEEK_OF_DAY_OFFSET(false, new DatetimeAccepterBuilder() {
//
//		@Override
//		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
//			int dayOffset;
//			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_1_DAY) {
//				dayOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
//			} else {
//				dayOffset = (int) option.getTimeOffset().intValue();
//			}
//			DatetimeDayOffsetIsWeekAccepter itemDatetimeAccepter = new DatetimeDayOffsetIsWeekAccepter(dayOffset, option.getDayOfWeeks()[0]);
//			itemDatetimeAccepter.setOption(option);
//			return itemDatetimeAccepter;
//		}
//	}),
	//判断给定时间戳是不是那个月的第几个星期几，比如2025/4/7日是2025年4月第一个星期1
	IS_NTH_OF_WEEK_OF_MONTH(false, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			int dayOffset;
			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_1_DAY) {
				dayOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
			} else {
				dayOffset = (int) option.getTimeOffset().intValue();
			}
			DatetimeNumberOfWeekAccepter itemDatetimeAccepter = new DatetimeNumberOfWeekAccepter(dayOffset, option.getDayOfWeeks()[0]);
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	}),
	//
	IS_EXCEEDS_DURATION(true, new DatetimeAccepterBuilder() {

		@Override
		public DatetimeAccepter build(DatetimeAccepterOption option) throws InvalidParameterException {
			DatetimeExceedDurationAccepter itemDatetimeAccepter = new DatetimeExceedDurationAccepter(option.getTimeOffset(), option.getTimeOffsetUnit());
			itemDatetimeAccepter.setOption(option);
			return itemDatetimeAccepter;
		}
	});

	private DatetimeAccepterBuilder datetimeAccepterBuilder;
	private boolean isSingleDateTimeParam;

	DatetimeAccepters(boolean isSingleDateTimeParam, DatetimeAccepterBuilder datetimeAccepterBuilder) {
		this.datetimeAccepterBuilder = datetimeAccepterBuilder;
		this.datetimeAccepterBuilder.setType(this);
		this.isSingleDateTimeParam = isSingleDateTimeParam;
	}

	public boolean isSingleDateTimeParam() {
		return isSingleDateTimeParam;
	}

	public DatetimeAccepter buildDatetimeAccepter(DatetimeAccepterOption option) throws InvalidParameterException {
		return datetimeAccepterBuilder.build(option);
	}

	public static boolean isAnyOf(DatetimeAccepters[] types, DatetimeAccepters type) {
		for (DatetimeAccepters datetimeAccepters : types) {
			if (type == datetimeAccepters) {
				return true;
			}
		}

		return false;
	}
	
//	public static void main(String[] args) {
//		for(DatetimeAccepters a:DatetimeAccepters.values()) {
//			System.out.println(a);
//		}
//	}

//	public static DatetimeAccepter buildDefaultFunction(String funcExpression) throws InvalidParameterException, ParseException {
//		DatetimeAccepter itemDatetimeAccepter = null;
//		FunctionDesc funcDescOfMatcher;
//		funcDescOfMatcher = FunctionParser.parse(funcExpression);
//
//		funcDescOfMatcher.trimParams();
//
//		DatetimeAccepters compareMethod = DatetimeAccepters.valueOf(funcDescOfMatcher.getName());
//
//		DatetimeAccepterOption option = new DatetimeAccepterOption();
//
//		switch (compareMethod) {
//		// -----------------------------------------------------------------------------------------------------------------------------
//		case IS_EXCEEDS_DURATION:
//			itemDatetimeAccepter = new DatetimeExceedDurationAccepter(option.getTimeOffset(), option.getTimeOffsetUnit());
//			break;
//		// case TIME_OFFSET_FROM_NOW:
//		// this.itemDatetimeAccepter = new ItemDatetimeOffsetAccepter(option.timeOffset, option.timeOffsetUnit);
//		// break;
//		// case AFTER_TIME:
//		// case BEFORE_TIME:
//		// this.itemDatetimeAccepter = new ItemDatetimeOffsetAccepter(compareMethod, option.timeOffset, option.timeOffsetUnit);
//		// break;
//		case IS_IN_WEEKS:
//			itemDatetimeAccepter = new DatetimeInWeeksAccepter(option.getDayOfWeeks());
//			break;
//		case IS_BETWEEN_TIME:
//			itemDatetimeAccepter = new DatetimeBetweenAccepter(option);
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------------
//		case IS_FIRST_DAY_OF_THIS_MONTH:
//		case IS_LAST_DAY_OF_THIS_MONTH:
//			itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(compareMethod);
//			break;
//		case IS_DAY_OF_THIS_MONTH:
//			int dayOfMonth;
//			if (option.getTimeOffset().intValue() <= 31) {
//				dayOfMonth = (int) option.getTimeOffset().intValue();
//			} else {
//				dayOfMonth = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
//			}
//			itemDatetimeAccepter = new DatetimeDayOfMonthAccepter(compareMethod, dayOfMonth);
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------------
//		case IS_FIRST_WEEK_OF_THIS_MONTH:
//		case IS_LAST_WEEK_OF_THIS_MONTH:
//			itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(compareMethod);
//			break;
//		case IS_WEEK_OF_THIS_MONTH:
//			int weekOfMonth = (int) option.getTimeOffset().intValue();
//			itemDatetimeAccepter = new DatetimeWeekOfMonthAccepter(weekOfMonth);
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------------
//		case IS_MONTH_OF_THIS_YEAR:
//			int monthOfYear;
//			if (option.getTimeOffset().intValue() <= 12) {
//				monthOfYear = (int) option.getTimeOffset().intValue();
//			} else {
//				monthOfYear = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
//			}
//			itemDatetimeAccepter = new DatetimeMonthOfYearAccepter(monthOfYear);
//			break;
//		// -----------------------------------------------------------------------------------------------------------------------------
//		case IS_DAY_OFFSET_FROM_TODAY:
//			int dayOffset;
//			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_1_DAY) {
//				dayOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_1_DAY);
//			} else {
//				dayOffset = (int) option.getTimeOffset().intValue();
//			}
//			itemDatetimeAccepter = new DatetimeDayOffsetOfTodayAccepter(dayOffset);
//			break;
//		case IS_MONTH_OFFSET_FROM_TODAY:
//			int monthOffset;
//			if (option.getTimeOffset().longValue() >= Constants.TIME_MILLISECONDS_30_DAY) {
//				monthOffset = (int) (option.getTimeOffsetInMillisecond() / Constants.TIME_MILLISECONDS_30_DAY);
//			} else {
//				monthOffset = (int) option.getTimeOffset().intValue();
//			}
//			itemDatetimeAccepter = new DatetimeMonthOffsetOfTodayAccepter(monthOffset);
//			break;
//		}
//
//		return itemDatetimeAccepter;
//	}

}
