package com.amituofo.common.kit.datetime.accepter.impl;

import java.util.Calendar;
import java.util.Date;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterOption;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;
import com.amituofo.common.type.CompareOperator;
import com.amituofo.common.util.DateUtils;
import com.amituofo.common.util.StringUtils;

public class DatetimeBetweenAccepter extends DatetimeAccepterBase {
	private long datetime1, datetime2;
	private CompareOperator operator;

	public DatetimeBetweenAccepter(DatetimeAccepterOption option) throws InvalidParameterException {
		this(option.getFromMillisecond(), option.getToMillisecond());

		this.setOption(option);
	}

	public DatetimeBetweenAccepter(Long fromDate, Long toDate) throws InvalidParameterException {
		super(DatetimeAccepters.IS_BETWEEN_TIME);

		if (fromDate != null && toDate != null) {
			this.datetime1 = fromDate;
			this.datetime2 = toDate;
			this.operator = CompareOperator.between;
		} else if (fromDate == null && toDate != null) {
			this.datetime1 = toDate;
			this.operator = CompareOperator.less;
		} else if (fromDate != null && toDate == null) {
			this.datetime1 = fromDate;
			this.operator = CompareOperator.greater;
		} else {
			operator = null;

			throw new InvalidParameterException("From datetime[Millisecond] and To datetime[Millisecond] required!");
		}
	}

	@Override
	protected boolean accept0(long millisecond) {
//		if (operator == null) {
//			return false;
//		}

		switch (operator) {
		case greater:
			return millisecond >= datetime1;
		case less:
			return millisecond <= datetime1;
		case between:
			return millisecond >= datetime1 && millisecond <= datetime2;
		default:
			return false;
		}
	}

	public static Long toMillisecond(String value) {
		if (StringUtils.isNotEmpty(value)) {
			if (value.indexOf('-') != -1) {
				value = value.replace("-", "_");
			}
			if (value.indexOf('/') != -1) {
				value = value.replace("/", "_");
			}
			if (value.indexOf(':') != -1) {
				value = value.replace(":", "_");
			}
			if (value.indexOf('_') != -1) {
				// 肯定时时间格式
				String[] fields = value.split("_");
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].length() % 2 != 0) {
						fields[i] = "0" + fields[i];
					}
				}

				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < fields.length; i++) {
					buf.append(fields[i]);
				}

				value = buf.toString();
			} else {
				if (value.length() == 13) {
					// 时间戳
					return Long.parseLong(value);
				}
			}

			String YYYYMMDDHHMMSSS = value + StringUtils.ZERO[14 - value.length()];

			int year, month, day, hour, min, sec;

			year = Integer.parseInt(YYYYMMDDHHMMSSS.substring(0, 4));
			month = Integer.parseInt(YYYYMMDDHHMMSSS.substring(4, 6));
			day = Integer.parseInt(YYYYMMDDHHMMSSS.substring(6, 8));

			if (!DateUtils.isDate(year, month, day)) {
				String millisecond = value + StringUtils.ZERO[13 - value.length()];
				return Long.parseLong(millisecond);
			}

			hour = Integer.parseInt(YYYYMMDDHHMMSSS.substring(8, 10));
			min = Integer.parseInt(YYYYMMDDHHMMSSS.substring(10, 12));
			sec = Integer.parseInt(YYYYMMDDHHMMSSS.substring(12, 14));

			if (hour == 24) {
				hour = 23;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month - 1); // 月份从 0 开始，所以需要减去 1
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.SECOND, sec);

			// 获取 Date 对象
			Date date = calendar.getTime();

			// 将 Date 对象转换为时间戳（毫秒级）
			return date.getTime();

//			try {
//				// yyyymmdd=8 yyyymmddhh=10 yyyymmddhhmm=12 yyyymmddhhmmss=14 yyyymmddhhmmsss=15
//				LocalDateTime dateTime = null;
//				int len = value.length();
//				if (len == 8) {
//					dateTime = LocalDateTime.parse(value, YYYYMMDD);
//				} else if (len == 10) {
//					dateTime = LocalDateTime.parse(value, YYYYMMDDHH);
//				} else if (len == 12) {
//					dateTime = LocalDateTime.parse(value, YYYYMMDDHHMM);
//				} else if (len == 14) {
//					dateTime = LocalDateTime.parse(value, YYYYMMDDHHMMSS);
////				} else if (len == 15) {
////					dateTime = LocalDateTime.parse(value, YYYYMMDDHHMMSSS);
//				}
//
//				if (dateTime == null) {
//					return null;
//				}
//
//				return dateTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli();
//			} catch (Exception e) {
//				throw new ParseException(e);
//			}
		}
		return null;
	}
}
