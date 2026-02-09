package com.amituofo.common.type;

import com.amituofo.common.define.Constants;
import com.amituofo.common.util.StringUtils;

public enum TimeUnit {
	MS(1), Second(1000L), Minute(60 * Second.toMilliseconds()), Hour(60 * Minute.toMilliseconds()), Day(24 * Hour.toMilliseconds());
//	Year(365 * Day.toMilliseconds());

	private long ms;

	TimeUnit(long ms) {
		this.ms = ms;
	}

	public long toMilliseconds() {
		return ms;
	}

	public long toMilliseconds(float quota) {
		return (long) (toMilliseconds() * quota);
	}

	public long toMilliseconds(long quota) {
		return (long) (toMilliseconds() * quota);
	}

	public long toMilliseconds(int quota) {
		return (long) (toMilliseconds() * quota);
	}

	public float toTimeUnit(long ms) {
		float a = ((float) (ms / ((double) this.ms)));
		return (float) (Math.round(a * 100)) / 100;
	}

	public static String toReadable(long ms) {
		if (ms <= 0) {
			return "0 ms";
		}

		if (ms <= Minute.ms) {
			if (ms < 1000) {
				return ms + " ms";
			} else {
				return (int) (((double) ms / (double) Second.ms)) + " sec";
			}
		} else if (ms <= Hour.ms) {
			return (ms / Minute.ms) + " mins " + toReadableTime(ms % Minute.ms);
		} else if (ms <= Day.ms) {
			return (ms / Hour.ms) + " hours " + toReadableTime(ms % Hour.ms);
		} else {
			return (ms / Day.ms) + " days " + toReadableTime(ms % Day.ms);
		}
	}
	
	private static String toReadableTime(long ms) {
		if (ms <= 0) {
			return "";
		}

		if (ms <= Minute.ms) {
			if (ms < 1000) {
				return "";
			} else {
				return (int) (((double) ms / (double) Second.ms)) + " sec";
			}
		} else if (ms <= Hour.ms) {
			return (ms / Minute.ms) + " mins " + toReadableTime(ms % Minute.ms);
		} else if (ms <= Day.ms) {
			return (ms / Hour.ms) + " hours " + toReadableTime(ms % Hour.ms);
		} else {
			return (ms / Day.ms) + " days " + toReadableTime(ms % Day.ms);
		}
	}

	public static TimeUnit kindOf(long ms) {
		if (ms < Second.toMilliseconds()) {
			return MS;
		}

		if (ms < Minute.toMilliseconds()) {
			return Second;
		}

		if (ms < Hour.toMilliseconds()) {
			return Minute;
		}

		if (ms < Day.toMilliseconds()) {
			return Hour;
		}

		return Day;
	}

	public static long parse(String time, final long defaultValue) throws NumberFormatException {
		if (StringUtils.isEmpty(time)) {
			return defaultValue;
		}

		time = time.trim().toUpperCase();

		String value = time;
		TimeUnit unit = TimeUnit.MS;

		int unitStartIndex = time.lastIndexOf("S");
		if (unitStartIndex != -1) {
			unit = Second;
		} else {
			unitStartIndex = time.lastIndexOf("M");
			if (unitStartIndex != -1) {
				unit = Minute;
			} else {
				unitStartIndex = time.lastIndexOf("H");
				if (unitStartIndex != -1) {
					unit = Hour;
				} else {
					unitStartIndex = time.lastIndexOf("D");
					if (unitStartIndex != -1) {
						unit = Day;
					}
				}
			}
		}

		if (unitStartIndex > 0) {
			value = time.substring(0, unitStartIndex).trim();
		}

		long longvalue = Long.valueOf(value);
		return longvalue * unit.ms;
	}

//	/**
//	 * @param dateStr 1Y2M3D8H 3D
//	 * @return
//	 */
//	public static long toMilliseconds(String dateStr) {
//		long milliseconds = 0;
//		dateStr = dateStr.toUpperCase();
//		String[] parts = dateStr.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
//		// 将字符串按数字和非数字分割，例如 "2D3H" 分割为 ["2", "D", "3", "H"]
//		try {
//			for (int i = 0; i < parts.length; i += 2) {
//				float number = Float.parseFloat(parts[i]);
//				char unit = parts[i + 1].charAt(0);
//
//				switch (unit) {
//				case 'Y':
//					milliseconds += number * Constants.TIME_MILLISECONDS_1_DAY * 365L; // 每年有 31536000000 毫秒
//					break;
//				case 'M':
//					milliseconds += number * Constants.TIME_MILLISECONDS_30_DAY; // 每天有 86400000 毫秒
//					break;
//				case 'D':
//					milliseconds += number * Constants.TIME_MILLISECONDS_1_DAY; // 每天有 86400000 毫秒
//					break;
//				case 'H':
//					milliseconds += number * Constants.TIME_MILLISECONDS_1_HOUR; // 每小时有 3600000 毫秒
//					break;
//				default:
//					System.out.println("Invalid input.");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return -1;
//		}
//
//		return milliseconds;
//	}

	/**
	 * @param  dateStr 1Y2M3D8H 3D
	 * @return
	 */
	public static long toMilliseconds(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return -1;
		}
		long milliseconds = 0;
		dateStr = dateStr.toUpperCase();
		int len = dateStr.length();
		int lastStart = 0;
		boolean foundunit = false;
		for (int i = 0; i < len; i++) {
			char c = dateStr.charAt(i);
			c = Character.toUpperCase(c);
			if (c == 'Y' || c == 'M' || c == 'D' || c == 'H') {
				foundunit=true;
				String num = dateStr.substring(lastStart, i).trim();
				lastStart = i + 1;

				// 将字符串按数字和非数字分割，例如 "2D3H" 分割为 ["2", "D", "3", "H"]
				try {
					double number = Double.parseDouble(num);
					char unit = c;

					switch (unit) {
					case 'Y':
						milliseconds += number * Constants.TIME_MILLISECONDS_1_DAY * 365L; // 每年有 31536000000 毫秒
						break;
					case 'D':
						milliseconds += number * Constants.TIME_MILLISECONDS_1_DAY; // 每天有 86400000 毫秒
						break;
					case 'H':
						milliseconds += number * Constants.TIME_MILLISECONDS_1_HOUR; // 每小时有 3600000 毫秒
						break;
					case 'M':
						milliseconds += number * Constants.TIME_MILLISECONDS_1_MIN; 
						break;
					default:
//						System.out.println("Invalid input.");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
		}

		if (foundunit) {
			return milliseconds;
		} else {
			return Long.parseLong(dateStr);
		}
	}

	public static void main(String[] r) {
		System.out.println(toMilliseconds("1.3H") + " ? " + Constants.TIME_MILLISECONDS_1_HOUR * 1.3);
		System.out.println(toMilliseconds("20D") + " ? " + Constants.TIME_MILLISECONDS_1_DAY * 20);
		System.out.println(toMilliseconds("20D 3H ") + " ? " + (Constants.TIME_MILLISECONDS_1_DAY * 20 + Constants.TIME_MILLISECONDS_1_HOUR * 3));
		System.out.println(toMilliseconds("3D20H") == Constants.TIME_MILLISECONDS_1_DAY * 3 + 20 * Constants.TIME_MILLISECONDS_1_HOUR);
		System.out.println(toMilliseconds("10Y20D 9M3H ") + " ? " + (Constants.TIME_MILLISECONDS_1_DAY * 365 * 10 + Constants.TIME_MILLISECONDS_1_DAY * 20
				+ Constants.TIME_MILLISECONDS_30_DAY * 9 + Constants.TIME_MILLISECONDS_1_HOUR * 3));
		System.out.println(parse("5s", 1) == Constants.TIME_MILLISECONDS_1_SECOND * 5);
		System.out.println(parse("6m", 1) == Constants.TIME_MILLISECONDS_1_MIN * 6);
		System.out.println(parse("1H", 1) == Constants.TIME_MILLISECONDS_1_HOUR);
		System.out.println(parse("1 D", 1) == Constants.TIME_MILLISECONDS_1_DAY);
		System.out.println(parse("10000 ", 1) == Constants.TIME_MILLISECONDS_10_SECOND);

		System.out.println(toReadable(System.currentTimeMillis()));
		System.out.println(toReadable(123456));
		System.out.println(TimeUnit.kindOf(123456));
	}
}
