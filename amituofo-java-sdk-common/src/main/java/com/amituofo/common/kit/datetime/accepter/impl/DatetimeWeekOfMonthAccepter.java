package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;
import com.amituofo.common.util.StringUtils;

public class DatetimeWeekOfMonthAccepter extends DatetimeAccepterBase {
	private int weekOfMonth;
	private boolean isThisMonth = false;

	private static DatetimeAccepters[] WEEK_OF_MONTH_LIST = new DatetimeAccepters[] { DatetimeAccepters.IS_FIRST_WEEK_OF_MONTH, DatetimeAccepters.IS_LAST_WEEK_OF_MONTH,
			DatetimeAccepters.IS_NTH_WEEK_OF_MONTH };
	private static DatetimeAccepters[] WEEK_OF_THIS_MONTH_LIST = new DatetimeAccepters[] { DatetimeAccepters.IS_FIRST_WEEK_OF_THIS_MONTH,
			DatetimeAccepters.IS_LAST_WEEK_OF_THIS_MONTH, DatetimeAccepters.IS_NTH_WEEK_OF_THIS_MONTH };

	public DatetimeWeekOfMonthAccepter(DatetimeAccepters type, int weekOfMonth) throws InvalidParameterException {
		super(type);

		if (!DatetimeAccepters.isAnyOf(WEEK_OF_MONTH_LIST, type) && !DatetimeAccepters.isAnyOf(WEEK_OF_THIS_MONTH_LIST, type)) {
			throw new InvalidParameterException(
					"Only support " + StringUtils.toString(WEEK_OF_MONTH_LIST, ',') + " or " + StringUtils.toString(WEEK_OF_THIS_MONTH_LIST, ','));
		}

		if (type == DatetimeAccepters.IS_FIRST_WEEK_OF_MONTH) {
			this.type = DatetimeAccepters.IS_NTH_WEEK_OF_MONTH;
			this.weekOfMonth = 1;

			if (weekOfMonth > 5 || weekOfMonth < 1) {
				throw new InvalidParameterException("Week of month must between 1~5");
			}
		} else if (type == DatetimeAccepters.IS_FIRST_WEEK_OF_THIS_MONTH) {
			this.type = DatetimeAccepters.IS_NTH_WEEK_OF_MONTH;
			this.weekOfMonth = 1;
			this.isThisMonth = true;

			if (weekOfMonth > 5 || weekOfMonth < 1) {
				throw new InvalidParameterException("Week of month must between 1~5");
			}
		}
	}

	@Override
	protected boolean accept0(long millisecond) {
		if (isThisMonth) {
			LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();
			// 将时间戳转换为 LocalDate
			LocalDate today = LocalDate.now();

			if (thedate.getYear() != today.getYear() || thedate.getDayOfMonth() != today.getDayOfMonth()) {
				return false;
			}
		}

		if (type == DatetimeAccepters.IS_LAST_WEEK_OF_MONTH || type == DatetimeAccepters.IS_LAST_WEEK_OF_THIS_MONTH) {
			return isLastWeekOfMonth(millisecond);
		} else {
			// 创建Calendar实例并设置时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(millisecond));

			// 获取该日期在当月中的周数
			int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
			return weekOfMonth == this.weekOfMonth;
		}
	}

	public static boolean isLastWeekOfMonth(long timestamp) {
		ZonedDateTime date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault());

		// 获取当月最后一天
		ZonedDateTime lastDay = date.with(TemporalAdjusters.lastDayOfMonth());

		// 计算最后一天所在周的第一天和最后一天
		ZonedDateTime startOfLastWeek = lastDay.with(WeekFields.ISO.dayOfWeek(), 1);
		ZonedDateTime endOfLastWeek = lastDay.with(WeekFields.ISO.dayOfWeek(), 7);

		// 判断当前日期是否在这个范围内
		return !date.isBefore(startOfLastWeek) && !date.isAfter(endOfLastWeek);
	}

//	public static void main(String[] args) {
//		// 测试2023年4月最后一周（4月24日-4月30日）
//		long test1 = 1682294400000L; // 2023-04-24（周一）
//		long test2 = 1682812800000L; // 2023-04-30（周日）
//		long test3 = 1681689600000L; // 2023-04-17（不在最后一周）
//
//		System.out.println("2023-04-24 " + (isLastWeekOfMonth(test1) ? "是" : "不是") + "最后一周");
//		System.out.println("2023-04-30 " + (isLastWeekOfMonth(test2) ? "是" : "不是") + "最后一周");
//		System.out.println("2023-04-17 " + (isLastWeekOfMonth(test3) ? "是" : "不是") + "最后一周");
//	}

//	public static long createTimestamp(int year, int month, int day) {
//		return LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//	}
//
//	public static void main(String[] args) {
//		// 测试2023年2月（28天）
//		int year = 2023;
//		int month = 2;
//
//		long feb20 = createTimestamp(year, month, 20);
//		long feb26 = createTimestamp(year, month, 26);
//		long feb27 = createTimestamp(year, month, 27);
//		long feb28 = createTimestamp(year, month, 28);
//
//		System.out.println("2023年2月20日 " + (isLastWeekOfMonth(feb20) ? "是" : "不是") + "最后一周");
//		System.out.println("2023年2月26日 " + (isLastWeekOfMonth(feb26) ? "是" : "不是") + "最后一周");
//		System.out.println("2023年2月27日 " + (isLastWeekOfMonth(feb27) ? "是" : "不是") + "最后一周");
//		System.out.println("2023年2月28日 " + (isLastWeekOfMonth(feb28) ? "是" : "不是") + "最后一周");
//
//		// 测试2024年2月（闰年，29天）
//		year = 2024;
//		long feb29 = createTimestamp(year, month, 29);
//		System.out.println("\n2024年2月29日 " + (isLastWeekOfMonth(feb29) ? "是" : "不是") + "最后一周");
//	}
}
