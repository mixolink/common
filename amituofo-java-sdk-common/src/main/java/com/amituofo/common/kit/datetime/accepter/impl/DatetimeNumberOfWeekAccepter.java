package com.amituofo.common.kit.datetime.accepter.impl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepter;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;

public class DatetimeNumberOfWeekAccepter extends DatetimeAccepterBase {
	private int numberOfWeek;
	private DayOfWeek dayOfWeek;

	public DatetimeNumberOfWeekAccepter(int numberOfWeek, DayOfWeek dayOfWeek) throws InvalidParameterException {
		super(DatetimeAccepters.IS_NTH_OF_WEEK_OF_MONTH);

		if (numberOfWeek > 5 || 1 < 1) {
			throw new InvalidParameterException("Number of Week 1~5");
		}

		if (dayOfWeek == null) {
			throw new InvalidParameterException("Week value [MONDAY;TUESDAY;WEDNESDAY;THURSDAY;FRIDAY;SATURDAY;SUNDAY;] required!");
		}

		this.numberOfWeek = numberOfWeek;
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	protected boolean accept0(long millisecond) {
		// 将时间戳转换为LocalDate
		LocalDate thedate = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();

		return isSpecificWeekdayInMonth(thedate, numberOfWeek, dayOfWeek);
	}

	/**
	 * 检查给定日期是否是指定月份的第几个星期几
	 * 
	 * @param  localDate          要检查的日期(LocalDate)
	 * @param  expectedOccurrence 期望的第几个(1-5)
	 * @param  expectedDayOfWeek  期望的星期几(DayOfWeek)
	 * @return                    是否符合预期
	 */
	public static boolean isSpecificWeekdayInMonth(LocalDate localDate, int expectedOccurrence, DayOfWeek expectedDayOfWeek) {
		// 首先检查星期几是否匹配
		if (localDate.getDayOfWeek() != expectedDayOfWeek) {
			return false;
		}

		// 计算实际是该月的第几个星期几
		int actualOccurrence = (localDate.getDayOfMonth() - 1) / 7 + 1;

		return actualOccurrence == expectedOccurrence;
	}

//	public static void main(String[] args) {
//		// 测试2025年4月7日(应该是4月第1个星期一)
//		LocalDate testDate = LocalDate.of(2025, 4, 8);
//
//		// 获取星期几信息
////		System.out.println(getWeekdayOccurrence(date)); // 输出: 第1个星期一
//		{
//			// 检查是否是第1个星期一
//			boolean isFirstMonday = isSpecificWeekdayInMonth(testDate, 1, DayOfWeek.MONDAY);
//			System.out.println("2025/4/7是4月第1个星期一吗? " + isFirstMonday); // 输出: true
//		}
//		{
//			// 检查是否是第2个星期一(应该返回false)
//			boolean isSecondMonday = isSpecificWeekdayInMonth(testDate, 2, DayOfWeek.MONDAY);
//			System.out.println("2025/4/7是4月第2个星期一吗? " + isSecondMonday); // 输出: false
//		}
//		{
//			// 检查是否是第2个星期一(应该返回false)
//			boolean isSecondMonday = isSpecificWeekdayInMonth(testDate, 2, DayOfWeek.TUESDAY);
//			System.out.println("2025/4/8是4月第2个星期二吗? " + isSecondMonday); // 输出: false
//		}
//		{
//			// 检查是否是第2个星期一(应该返回false)
//			boolean isSecondMonday = isSpecificWeekdayInMonth(testDate, 1, DayOfWeek.TUESDAY);
//			System.out.println("2025/4/8是4月第2个星期一吗? " + isSecondMonday); // 输出: false
//		}
//	}

}
