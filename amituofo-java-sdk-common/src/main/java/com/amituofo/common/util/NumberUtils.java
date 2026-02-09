package com.amituofo.common.util;

import java.util.concurrent.atomic.AtomicLong;

public class NumberUtils {
	private static AtomicLong startindex = new AtomicLong(System.currentTimeMillis());

	public static long toLong(Object o) {
		if (o == null) {
			return 0;
		}

		if (o instanceof String) {
			return Long.valueOf((String) o);
		}

		if (o instanceof Integer) {
			return ((Integer) o).longValue();
		}

		if (o instanceof Long) {
			return ((Long) o).longValue();
		}

		if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		return 0;
	}

	public static float round2(double num) {
		return (float) (Math.round(num * 100)) / 100f;
	}

	public static float round2(float num) {
		return (float) (Math.round(num * 100)) / 100f;
	}

	public static long next() {
		return startindex.incrementAndGet();
	}

//	public static int toInt(String num, int defaultValue) {
//		if (StringUtils.isNotEmpty(num)) {
//			return Integer.parseInt(num);
//		}
//
//		return defaultValue;
//	}

	public static int toInt(Object num, int defaultValue) {
		if (num != null) {
			String strnum = num.toString();
			int len = strnum.length();
			// 2147483647
			if (len > 0 && len < 11) {
				return Integer.parseInt(strnum);
			}
		}

		return defaultValue;
	}
}
