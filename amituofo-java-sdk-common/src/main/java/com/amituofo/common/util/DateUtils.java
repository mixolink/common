/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package com.amituofo.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.TimerTask;

import javax.swing.Timer;

import com.amituofo.common.define.Constants;

/**
 * Utilities for parsing and formatting dates.
 */
public class DateUtils {

	public static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
	public static final TimeZone UTC_ZONE = TimeZone.getTimeZone("UTC");

	public static final Calendar GMT_CALENDAR = Calendar.getInstance(GMT_ZONE);

	// private final static DecimalFormat SEC = new DecimalFormat("0");

	// /** ISO 8601 format */
	// public static final SimpleDateFormat ISO8601_DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");;
	//
	// /** Alternate ISO 8601 format without fractional seconds */
	// public static final SimpleDateFormat ALTERNATE_ISO8601_DATEFORMAT = new
	// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	//
	// /** RFC 822 format */
	// public static final SimpleDateFormat RFC822_DATE_FORMAT = new
	// SimpleDateFormat("EEE, dd
	// MMM yyyy HH:mm:ss 'GMT'", Locale.US);//
	// .withZone(GMT);
	//// protected static final DateTimeFormatter RFC822_DATE_FORMAT =
	// DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss
	// 'GMT'").withLocale(Locale.US).withZone(GMT);
	//
	// /**
	// * This is another ISO 8601 format that's used in clock skew error response
	// */
	// public static final SimpleDateFormat COMPRESSED_ISO8601_DATE_FORMAT = new
	// SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
	//
	// public static class GMT_ZONE_FMT {
	// /** ISO 8601 format */
	// public static final SimpleDateFormat ISO8601_DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");;
	//
	// /** Alternate ISO 8601 format without fractional seconds */
	// public static final SimpleDateFormat ALTERNATE_ISO8601_DATEFORMAT = new
	// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	//
	// /** RFC 822 format */
	// public static final SimpleDateFormat RFC822_DATE_FORMAT = new
	// SimpleDateFormat("EEE, dd
	// MMM yyyy HH:mm:ss 'GMT'", Locale.US);//
	// .withZone(GMT);
	//// protected static final DateTimeFormatter RFC822_DATE_FORMAT =
	// DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss
	// 'GMT'").withLocale(Locale.US).withZone(GMT);
	//
	// /**
	// * This is another ISO 8601 format that's used in clock skew error response
	// */
	// public static final SimpleDateFormat COMPRESSED_ISO8601_DATE_FORMAT = new
	// SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
	//
	// static {
	// ISO8601_DATE_FORMAT.setTimeZone(GMT_ZONE);
	// ALTERNATE_ISO8601_DATEFORMAT.setTimeZone(GMT_ZONE);
	// RFC822_DATE_FORMAT.setTimeZone(GMT_ZONE);
	// COMPRESSED_ISO8601_DATE_FORMAT.setTimeZone(GMT_ZONE);
	// }
	// }

	private static long timeOffset = 0;

	public static long getTimeOffset() {
		return timeOffset;
	}

	public static void setTimeOffset(long timeOffset) {
		DateUtils.timeOffset = timeOffset;
	}

	public static double getSecond(long t1, long t2) {
		double sec = (double) Math.abs(t1 - t2) / 1000;

		return sec;
	}

	public static boolean isDate(int yyyy, int mm, int dd) {
		return yyyy >= 1900 && yyyy <= 2100 && mm >= 1 && mm <= 12 && dd >= 1 && dd <= 31;// && hh >= 0 && hh <= 24;
	}

	public static String usedTime(long startTime) {
		return usedTime(System.currentTimeMillis(), startTime);
	}

	public static String usedTime(Long t1, Long t2) {
		if (t1 == null || t2 == null) {
			return "";
		}

		return usedTime(t1, t2);
	}

	public static String usedTime(long t1, long t2) {
		long t = Math.abs(t2 - t1);

		return toReadableTimeMillis(t);

		// if (t <= 0) {
		// return "0 sec";
		// }
		//
		// if (t <= 1000 * 60) {
		// return ((int) ((double) t / (double) 1000)) + " sec";
		// } else if (t <= 1000 * 60 * 60) {
		// return (t / (1000 * 60)) + " mins " + usedTime(t1, t1 + (t % (1000 * 60)));
		// } else {
		// return (t / (1000 * 60 * 60)) + " hours " + usedTime(t1, t1 + (t % (1000 * 60
		// * 60)));
		// }
	}

	public final static int oneSecondInTimeMillis = 1000;
	public final static int oneMinInTimeMillis = oneSecondInTimeMillis * 60;
	public final static int oneHourInTimeMillis = oneMinInTimeMillis * 60;
	public final static int oneDayInTimeMillis = oneHourInTimeMillis * 24;

	public static String toReadableSimpleTimeMillis(long t) {
		if (t <= oneMinInTimeMillis) {
			return "";
		}

		if (t <= oneHourInTimeMillis) {
			return (t / oneMinInTimeMillis) + " Min ";
		} else if (t <= oneDayInTimeMillis) {
			return (t / oneHourInTimeMillis) + " H " + toReadableTimeMillis(t % oneHourInTimeMillis);
		} else {
			return (t / oneDayInTimeMillis) + " D " + toReadableTimeMillis(t % oneDayInTimeMillis);
		}
	}

	public static String toReadableTimeMillis(long t) {
		if (t <= 0) {
			return "0 ms";
		}

		if (t <= oneMinInTimeMillis) {
			if (t < 1000) {
				return t + " ms";
			} else {
				return (int) (((double) t / (double) 1000)) + " sec";
			}
		} else if (t <= oneHourInTimeMillis) {
			return (t / oneMinInTimeMillis) + " Min " + toReadableTimeMillis(t % oneMinInTimeMillis);
		} else if (t <= oneDayInTimeMillis) {
			return (t / oneHourInTimeMillis) + " Hour " + toReadableTimeMillis(t % oneHourInTimeMillis);
		} else {
			return (t / oneDayInTimeMillis) + " Day " + toReadableTimeMillis(t % oneDayInTimeMillis);
		}
	}

	public static int toDays(long remainExpireInterval) {
		return (int) (remainExpireInterval / Constants.TIME_MILLISECONDS_1_DAY);
	}

	public static int toHours(long remainExpireInterval) {
		return (int) (remainExpireInterval / Constants.TIME_MILLISECONDS_1_HOUR);
	}

	public static String toReadableCHSTimeMillis(long t) {
		if (t <= 0) {
			return "0毫秒";
		}

		if (t <= oneMinInTimeMillis) {
			if (t < 1000) {
				return t + "毫秒";
			} else {
				return (int) (((double) t / (double) 1000)) + "秒";
			}
		} else if (t <= oneHourInTimeMillis) {
			return (t / oneMinInTimeMillis) + "分钟" + toReadableCHSTimeMillis(t % oneMinInTimeMillis);
		} else if (t <= oneDayInTimeMillis) {
			return (t / oneHourInTimeMillis) + "小时" + toReadableCHSTimeMillis(t % oneHourInTimeMillis);
		} else {
			return (t / oneDayInTimeMillis) + "天" + toReadableCHSTimeMillis(t % oneDayInTimeMillis);
		}
	}

	// public static void setTimeZone(TimeZone tz) {
	// ISO8601_DATE_FORMAT.setTimeZone(tz);
	// ALTERNATE_ISO8601_DATEFORMAT.setTimeZone(tz);
	// RFC822_DATE_FORMAT.setTimeZone(tz);
	// COMPRESSED_ISO8601_DATE_FORMAT.setTimeZone(tz);
	// }

	public static long getTime() {
		return GMT_CALENDAR.getTimeInMillis() - timeOffset;
	}

	/**
	 * Return the days of specific month <br>
	 * 
	 * @param yyyy year
	 * @param mm   month
	 * 
	 * @return days（28,29,30,31）
	 */
	public static int amountOfMonth(int yyyy, int mm) {

		switch (mm) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (isLeapYear(yyyy)) {
				return 29;
			} else {
				return 28;
			}
		default:
			return -1;
		}
	}

	/**
	 * Return the days of specific month <br>
	 * 
	 * @param yyyy year
	 * @param mm   month
	 * 
	 * @return days（28,29,30,31）
	 */
	public static String amountOfMonth(String yyyy, String mm) {
		int iYear = Integer.parseInt(yyyy);
		int iMonth = Integer.parseInt(mm);
		return Integer.toString(amountOfMonth(iYear, iMonth));
	}

	/**
	 * Return the days between two days <BR>
	 * 
	 * @param from begin day
	 * @param to   end day
	 * @return days
	 */
	public static int calculateDays(Calendar from, Calendar to) {
		// (24*60*60*1000)
		final long DATE_VALUE = 86400000;

		long days = 0;

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		start.setLenient(false);
		end.setLenient(false);

		start.set(Calendar.YEAR, from.get(Calendar.YEAR));
		start.set(Calendar.MONTH, from.get(Calendar.MONTH));
		start.set(Calendar.DATE, from.get(Calendar.DATE));

		end.set(Calendar.YEAR, to.get(Calendar.YEAR));
		end.set(Calendar.MONTH, to.get(Calendar.MONTH));
		end.set(Calendar.DATE, to.get(Calendar.DATE));

		start.clear(Calendar.HOUR_OF_DAY);
		start.clear(Calendar.HOUR);
		start.clear(Calendar.MINUTE);
		start.clear(Calendar.SECOND);

		end.clear(Calendar.HOUR_OF_DAY);
		end.clear(Calendar.HOUR);
		end.clear(Calendar.MINUTE);
		end.clear(Calendar.SECOND);

		days = end.getTime().getTime() - start.getTime().getTime();

		days /= DATE_VALUE;

		return (int) days;
	}

	// -------------------------------------------------------------------------------------------------------------------

	/**
	 * Return the days between two days <BR>
	 * 
	 * @param from begin day
	 * @param to   end day
	 * @return days
	 */
	public static int calculateDays(Date from, Date to) {

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		start.setLenient(false);
		end.setLenient(false);

		start.setTime(from);
		end.setTime(to);

		return calculateDays(start, end);
	}

	/**
	 * Return the days between two days <BR>
	 * 
	 * @param fromYear
	 * @param fromMonth
	 * @param fromDay
	 * @param toYear
	 * @param toMonth
	 * @param toDay
	 * @return days
	 */
	public static int calculateDays(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		start.setLenient(false);
		end.setLenient(false);

		start.set(fromYear, fromMonth - 1, fromDay);
		end.set(toYear, toMonth - 1, toDay);

		return calculateDays(start, end);
	}

	/**
	 * Return the days between two days <BR>
	 * 
	 * @param from
	 * @param to
	 * @return days
	 * @see Calendar
	 */
	public static int calculateDays(String from, String to) {

		int ymdfr[] = splitDateToInt(from);
		int ymdto[] = splitDateToInt(to);

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		start.setLenient(false);
		end.setLenient(false);

		start.set(ymdfr[0], ymdfr[1] - 1, ymdfr[2]);
		end.set(ymdto[0], ymdto[1] - 1, ymdto[2]);

		return calculateDays(start, end);
	}

	public static int calculateMinutes(Date smdate, Date bdate) {
		// smdate=yyyyMMdd.parse(yyyyMMdd.format(smdate));
		// bdate=yyyyMMdd.parse(yyyyMMdd.format(bdate));
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(smdate);
		// long time1 = cal.getTimeInMillis();
		// cal.setTime(bdate);
		// long time2 = cal.getTimeInMillis();
		// long between_days=(time2-time1)/(1000*3600*24);
		// return Integer.parseInt(String.valueOf(between_days));

		long stateTimeLong = smdate.getTime();
		long endTimeLong = bdate.getTime();
		// 结束时间-开始时间 = 天数
		int day = (int) ((endTimeLong - stateTimeLong) / (60 * 1000));

		return day;
	}

	public static int compareDate(String yyyymmdd1, String yyyymmdd2) {
		int ymd1[];
		int ymd2[];

		ymd1 = splitDateToInt(yyyymmdd1);
		ymd2 = splitDateToInt(yyyymmdd2);

		try {
			Calendar calendar1 = new GregorianCalendar(ymd1[0], ymd1[1] - 1, ymd1[2]);
			Calendar calendar2 = new GregorianCalendar(ymd2[0], ymd2[1] - 1, ymd2[2]);

			if (calendar1.equals(calendar2)) {
				return 0;
			}
			if (calendar1.after(calendar2)) {
				return 1;
			}
			if (calendar1.before(calendar2)) {
				return -1;
			}

		} catch (RuntimeException e) {
			throw new IllegalArgumentException("yyyymmdd length != 8");
		}

		return -1;
	}

	public static Date createDateAt23h59m59s(int yyyy, int mm, int dd) {
		GregorianCalendar gc = new GregorianCalendar(yyyy, mm - 1, dd, 23, 59, 59);
		gc.setLenient(false);
		return gc.getTime();
	}

	public static Date createDate(int yyyy, int mm, int dd) {
		GregorianCalendar gc = new GregorianCalendar(yyyy, mm - 1, dd);
		gc.setLenient(false);
		return gc.getTime();
	}

	public static Date createDate(String yyyy, String mm, String dd) {
		if (yyyy == null || mm == null || dd == null || yyyy.length() != 4 || mm.length() != 2 || dd.length() != 2) {
			throw new IllegalArgumentException("argument is wrong");
		}

		return createDate(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
	}

	public static String dayFlow(String yyyymmdd, int dd) {

		if (null == yyyymmdd) {
			return "";
		} else if (yyyymmdd.length() != 8) {
			return yyyymmdd;
		}

		int ymd[] = splitDateToInt(yyyymmdd);

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
		calendar.add(Calendar.DATE, dd);

		int returnY = calendar.get(Calendar.YEAR);
		int returnM = calendar.get(Calendar.MONTH) + 1;
		int returnD = calendar.get(Calendar.DATE);
		int returnAfterDays = returnY * 10000 + returnM * 100 + returnD;

		return String.valueOf(returnAfterDays);

	}

	public static Date minuteFlow(Date date, int mm) {

		if (mm <= 0) {
			throw new IllegalArgumentException("Minute must be specified.");
		}

		Calendar calendar = GregorianCalendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}

		calendar.add(Calendar.MINUTE, mm);

		return calendar.getTime();
	}

	public static String extractDay(Date ymd) {
		String dd = "";
		dd = formatDate(ymd, "dd");

		return dd;
	}

	public static String extractMonth(Date ymd) {
		String mm = "";
		mm = formatDate(ymd, "MM");

		return mm;
	}

	public static String extractYear(Date ymd) {
		String yyyy = "";
		yyyy = formatDate(ymd, "yyyy");

		return yyyy;
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String datenewformat = formatter.format(date);
		return datenewformat;
	}

	public static String formatDate(Long date, String format) {
		if (date == null || date == 0) {
			return "";
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String datenewformat = formatter.format(c.getTime());
		return datenewformat;
	}

	public static int indicateWeek(String yyyymmdd) {

		if (yyyymmdd.length() != 8) {
			throw new IllegalArgumentException("argDate length != 8");
		}

		int ymd[] = splitDateToInt(yyyymmdd);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
		int returnDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		return returnDayOfWeek;
	}

	public static boolean isAvailableDateFormat(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() != 8)
			return false;

		int ymd[] = splitDateToInt(yyyymmdd);

		try {
			Calendar calendar = new GregorianCalendar(ymd[0], ymd[1] - 1, ymd[2]);
			calendar.setLenient(false);
			calendar.getTime();
		} catch (Throwable ex) {
			return false;
		}
		return true;
	}

	public static boolean isAvailableDateRange(String fromYYYYMMDD, String toYYYYMMDD) {

		if (compareDate(fromYYYYMMDD, toYYYYMMDD) > 0) {
			return false;
		}
		return true;
	}

	public static boolean isAvailableTimeFormat(String hhmm) {

		if (hhmm == null || hhmm.length() != 4) {
			return false;
		}

		try {
			int intArgH = Integer.parseInt(hhmm.substring(0, 2));
			int intArgM = Integer.parseInt(hhmm.substring(2));

			Calendar calendar = GregorianCalendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, intArgH);
			calendar.set(Calendar.MINUTE, intArgM);
			if (calendar.get(Calendar.HOUR_OF_DAY) == intArgH && calendar.get(Calendar.MINUTE) == intArgM) {
				return true;
			} else {
				return false;
			}

		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean isLeapYear(int yyyy) {
		String strYear = Integer.toString(yyyy);
		return isLeapYear(strYear);
	}

	public static boolean isLeapYear(String yyyy) {
		if (StringUtils.isEmpty(yyyy) == false) {

			int year = Integer.parseInt(yyyy);
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			GregorianCalendar gcalendar = new GregorianCalendar();
			if (gcalendar.isLeapYear(calendar.get(Calendar.YEAR))) {
				return true;
			}
		}
		return false;
	}

	public static int leftDaysOfMonth(String yyyymmdd) {

		if (yyyymmdd.length() != 8) {
			throw new IllegalArgumentException("argDate length != 8");
		}

		int ymd[] = splitDateToInt(yyyymmdd);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
		int intMaxDayOfMonth = calendar.getActualMaximum(Calendar.DATE);

		return (intMaxDayOfMonth - calendar.get(Calendar.DATE));
	}

	public static String monthFlow(String yyyymmdd, int mm) {

		if (null == yyyymmdd) {
			return "";
		} else if (yyyymmdd.length() != 8) {
			return yyyymmdd;
		}

		int ymd[] = splitDateToInt(yyyymmdd);

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(ymd[0], ymd[1] - 1, ymd[2]);
		calendar.add(Calendar.MONTH, mm);

		int returnY = calendar.get(Calendar.YEAR);
		int returnM = calendar.get(Calendar.MONTH) + 1;
		int returnD = calendar.get(Calendar.DATE);
		int returnAfterMonth = returnY * 10000 + returnM * 100 + returnD;

		return String.valueOf(returnAfterMonth);

	}

	public static String nextMonth(String mm) {
		int numCurrentMonth = Integer.parseInt(mm == null ? "0" : mm);
		if (numCurrentMonth >= 12) {
			numCurrentMonth = 1;
		} else {
			numCurrentMonth++;
		}

		return (numCurrentMonth < 10 ? "0" : "") + String.valueOf(numCurrentMonth);
	}

	public static String nextYearMonth(String yyyymm) {
		if (StringUtils.isEmpty(yyyymm) || yyyymm.length() != 6) {
			throw new IllegalArgumentException("WrongFormat. Please YYYYMM");
		}
		int year = Integer.parseInt(yyyymm.substring(0, 4));
		int month = Integer.parseInt(yyyymm.substring(4, 6));
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		cal.add(Calendar.MONTH, +1);
		String newYear = String.valueOf(cal.get(Calendar.YEAR));
		int numCurrentMonth = cal.get(Calendar.MONTH) + 1;
		String newMonth = (numCurrentMonth < 10 ? "0" : "") + String.valueOf(numCurrentMonth);
		return newYear + newMonth;
	}

	public static String pevMonth(String mm) {

		int numCurrentMonth = Integer.parseInt(mm == null ? "0" : mm);
		if (numCurrentMonth <= 1) {
			numCurrentMonth = 12;
		} else {
			numCurrentMonth--;
		}

		return (numCurrentMonth < 10 ? "0" : "") + String.valueOf(numCurrentMonth);
	}

	public static String prevYearMonth(String yyyymm) {
		if (StringUtils.isEmpty(yyyymm) || yyyymm.length() != 6) {
			throw new IllegalArgumentException("WrongFormat. Please YYYYMM");
		}
		int year = Integer.parseInt(yyyymm.substring(0, 4));
		int month = Integer.parseInt(yyyymm.substring(4, 6));
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		cal.add(Calendar.MONTH, -1);
		String newYear = String.valueOf(cal.get(Calendar.YEAR));
		int numCurrentMonth = cal.get(Calendar.MONTH) + 1;
		String newMonth = (numCurrentMonth < 10 ? "0" : "") + String.valueOf(numCurrentMonth);
		return newYear + newMonth;
	}

	public static String[] splitDate(String yyyymmdd) {
		String[] ymd = new String[3];
		ymd[0] = "";
		ymd[1] = "";
		ymd[2] = "";

		if (yyyymmdd == null || 8 != yyyymmdd.length()) {
			throw new IllegalArgumentException("yyyymmdd length != 8");
		}

		ymd[0] = yyyymmdd.substring(0, 4);
		ymd[1] = yyyymmdd.substring(4, 6);
		ymd[2] = yyyymmdd.substring(6, 8);
		return ymd;
	}

	public static String[] splitDate(String yyyymmdd, String separator) {
		String[] ymd = new String[3];
		ymd[0] = "";
		ymd[1] = "";
		ymd[2] = "";

		ymd = yyyymmdd.split(separator);

		return ymd;
	}

	public static int[] splitDateToInt(String yyyymmdd) {
		int[] ymd = new int[3];
		ymd[0] = ymd[1] = ymd[2] = 0;

		if (yyyymmdd == null || 8 != yyyymmdd.length()) {
			throw new IllegalArgumentException("yyyymmdd length != 8");
		}

		ymd[0] = Integer.parseInt(yyyymmdd.substring(0, 4));
		ymd[1] = Integer.parseInt(yyyymmdd.substring(4, 6));
		ymd[2] = Integer.parseInt(yyyymmdd.substring(6, 8));
		return ymd;
	}

	public static int[] splitDateToInt(String strDate, String separator) {
		if (strDate == null || strDate.length() < 6) {
			return null;
		}

		String[] ymd = null;

		ymd = strDate.split(separator);
		if (ymd.length == 3) {
			int[] nymd = new int[3];
			nymd[0] = Integer.parseInt(ymd[0]);
			nymd[1] = Integer.parseInt(ymd[1]);
			nymd[2] = Integer.parseInt(ymd[2]);
			return nymd;
		} else {
			return null;
		}
	}

	public static String systemTime() {
		String hhmmss = "";

		hhmmss = formatDate(new Date(), "hh:mm:ss");

		return hhmmss;
	}

	public static String timeFlow(String yyyymmddhhmm, String hhmm) {

		if (yyyymmddhhmm.length() != 12) {
			throw new IllegalArgumentException("argDate length != 12");
		}
		if (hhmm.length() != 4) {
			throw new IllegalArgumentException("argNum length != 4");
		}

		int startY = Integer.parseInt(yyyymmddhhmm.substring(0, 4));
		int startM = Integer.parseInt(yyyymmddhhmm.substring(4, 6)) - 1;
		int startD = Integer.parseInt(yyyymmddhhmm.substring(6, 8));
		int startH = Integer.parseInt(yyyymmddhhmm.substring(8, 10));
		int startMin = Integer.parseInt(yyyymmddhhmm.substring(10));
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(startY, startM, startD, startH, startMin);

		int intArgH = Integer.parseInt(hhmm.substring(0, 2));
		int intArgMin = Integer.parseInt(hhmm.substring(2));
		calendar.add(Calendar.HOUR_OF_DAY, intArgH);
		calendar.add(Calendar.MINUTE, intArgMin);

		long returnY = (long) calendar.get(Calendar.YEAR);
		long returnM = (long) calendar.get(Calendar.MONTH) + 1;
		long returnD = (long) calendar.get(Calendar.DATE);
		long returnH = (long) calendar.get(Calendar.HOUR_OF_DAY);
		long returnMin = (long) calendar.get(Calendar.MINUTE);
		long returnReplyTime = returnY * 100000000 + returnM * 1000000 + returnD * 10000 + returnH * 100 + returnMin;
		return String.valueOf(returnReplyTime);
	}

	public static Calendar toCalendar(String yyyymmdd) {
		int[] ymd = splitDateToInt(yyyymmdd);
		int yy = ymd[0];
		int mm = ymd[1] - 1;
		int dd = ymd[2];
		Calendar calendar = Calendar.getInstance();
		calendar.set(yy, mm, dd, 0, 0, 0);
		return calendar;
	}

	public static Date toDate(String yyyymmdd) {
		int[] ymd = splitDateToInt(yyyymmdd);

		return createDate(ymd[0], ymd[1], ymd[2]);
	}

	public static Date toDateAt23h59m59s(String yyyymmdd) {
		int[] ymd = splitDateToInt(yyyymmdd);

		return createDateAt23h59m59s(ymd[0], ymd[1], ymd[2]);
	}

	public static Date toDate(String yyyymmdd, String delimiter) {
		int[] ymd = null;
		if (StringUtils.isEmpty(delimiter)) {
			ymd = splitDateToInt(yyyymmdd);
		} else {
			ymd = splitDateToInt(yyyymmdd, delimiter);
		}

		return createDate(ymd[0], ymd[1], ymd[2]);
	}

	public static Date toDateAt23h59m59s(String yyyymmdd, String delimiter) {
		int[] ymd = null;
		if (StringUtils.isEmpty(delimiter)) {
			ymd = splitDateToInt(yyyymmdd);
		} else {
			ymd = splitDateToInt(yyyymmdd, delimiter);
		}

		return createDateAt23h59m59s(ymd[0], ymd[1], ymd[2]);
	}

	public static boolean passMoreThan(long lastTime, long maxideltime) {
		return (System.currentTimeMillis() - lastTime) > maxideltime;
	}

	public static long currentDateTimeMillis() {
		return (System.currentTimeMillis() / Constants.TIME_MILLISECONDS_1_DAY) * Constants.TIME_MILLISECONDS_1_DAY;
	}
	
//	public static void scheduleAtMidnight(Runnable task) {
//        Timer timer = new Timer("Midnight-Timer", true); // daemon = true
//
//        // 获取当前时间
//        Calendar now = Calendar.getInstance();
//
//        // 设置为明天 00:00:00
//        Calendar firstRun = (Calendar) now.clone();
//        firstRun.add(Calendar.DAY_OF_MONTH, 1);
//        firstRun.set(Calendar.HOUR_OF_DAY, 0);
//        firstRun.set(Calendar.MINUTE, 0);
//        firstRun.set(Calendar.SECOND, 0);
//        firstRun.set(Calendar.MILLISECOND, 0);
//
//        long delay = firstRun.getTimeInMillis() - now.getTimeInMillis();
//        long period = 24 * 60 * 60 * 1000L; // 24 小时（毫秒）
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                task.run();
//            }
//        }, delay, period);
//    }
	
	
//
//	// 初始化（静态块确保首次使用前有值）
//	static {
//		refreshDateRanges();
//	}
//
//	// 缓存的时间戳边界（毫秒）
//	private static volatile long YESTERDAY_START = 0;
//	private static volatile long TODAY_START = 0;
//	private static volatile long TOMORROW_START = 0;
//
//	/**
//	 * 手动刷新日期范围（线程安全）
//	 */
//	public static synchronized void refreshDateRanges() {
//		ZoneId ZONE = ZoneId.systemDefault();
//		LocalDate today = LocalDate.now(ZONE);
//		YESTERDAY_START = today.minusDays(1).atStartOfDay(ZONE).toInstant().toEpochMilli();
//		TODAY_START = today.atStartOfDay(ZONE).toInstant().toEpochMilli();
//		TOMORROW_START = today.plusDays(1).atStartOfDay(ZONE).toInstant().toEpochMilli();
//	}
//
//	/**
//	 * 判断是否是今天
//	 */
//	public static boolean isToday(long timestamp) {
//		return timestamp >= TODAY_START && timestamp < TOMORROW_START;
//	}
//
//	/**
//	 * 判断是否是昨天
//	 */
//	public static boolean isYesterday(long timestamp) {
//		return timestamp >= YESTERDAY_START && timestamp < TODAY_START;
//	}
}
