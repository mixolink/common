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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amituofo.common.define.Constants;
import com.amituofo.common.define.DatetimeFormat;
import com.amituofo.common.type.SizeUnit;

public class FormatUtils {

	// public static String getPrintSize(Long size) {
	// if (size == null) {
	// return "";
	// }
	//
	// //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
	// if (size < 1024) {
	// return String.valueOf(size) + " B";
	// } else {
	// size = size / 1024;
	// }
	// //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
	// //因为还没有到达要使用另一个单位的时候
	// //接下去以此类推
	// if (size < 1024) {
	// return String.valueOf(size) + " KB";
	// } else {
	// size = size / 1024;
	// }
	// if (size < 1024) {
	// //因为如果以MB为单位的话，要保留最后1位小数，
	// //因此，把此数乘以100之后再取余
	// size = size * 100;
	// return String.valueOf((size / 100)) + "."
	// + String.valueOf((size % 100)) + " MB";
	// } else {
	// //否则如果要以GB为单位的，先除于1024再作同样的处理
	// size = size * 100 / 1024;
	// return String.valueOf((size / 100)) + "."
	// + String.valueOf((size % 100)) + " GB";
	// }
	// }

	private final static DecimalFormat FLOAT_FORMAT_WITH_COMMA_1 = new DecimalFormat(",###,###.0");
	private final static DecimalFormat FLOAT_FORMAT_WITH_COMMA_2 = new DecimalFormat(",###,###.00");
	private final static DecimalFormat INT_FORMAT_WITH_COMMA = new DecimalFormat(",###,###");
	private final static DecimalFormat FLOAT_FORMAT_1 = new DecimalFormat("0.0");
	
	public final static DatetimeFormat YYYYMMDD = DatetimeFormat.YYYYMMDD;
	public final static DatetimeFormat YYYYMMDDHH = DatetimeFormat.YYYYMMDDHH;
	public final static DatetimeFormat YYYYMMDDHHMM = DatetimeFormat.YYYYMMDDHHMM;
	public final static DatetimeFormat YYYYMMDDHHMMSS = DatetimeFormat.YYYYMMDDHHMMSS;
	public final static DatetimeFormat YYYYMMDDHHMMSSS = DatetimeFormat.YYYYMMDDHHMMSSS;
	public final static DatetimeFormat YYYY_MM_DD = DatetimeFormat.YYYY_MM_DD;
	public final static DatetimeFormat YYYY_MM_DD_HHMM = DatetimeFormat.YYYY_MM_DD_HHMM;
	public final static DatetimeFormat YYYY_MM_DD_HHMMSS = DatetimeFormat.YYYY_MM_DD_HHMMSS;
	public final static DatetimeFormat YYYY_MM_DD_HHMMSSS = DatetimeFormat.YYYY_MM_DD_HHMMSSS;

	public static String formatDatetime(Long time, SimpleDateFormat formater) {
		Date date = new Date(time);
		return formater.format(date);
	}

	public static String getPrintSize(Integer size) {
		if (size == null) {
			return Constants.NO_DATA_MARK;
		}

		return getPrintSize(size.longValue(), false);
	}

	public static String getPrintSize(Long size) {
		if (size == null) {
			return Constants.NO_DATA_MARK;
		}

		return getPrintSize(size.longValue(), false);
	}

	public static String formatNumber(long num) {
		return INT_FORMAT_WITH_COMMA.format(num);
	}

	// public static String formatNumber(long num) {
	// return format2.format(num);
	// }

	public static String formatNumber(int num) {
		return INT_FORMAT_WITH_COMMA.format(num);
	}

	public static String formatNumber(double num) {
		return FLOAT_FORMAT_1.format(num);
	}

	public static String getPercent(double number, int dig) {
		NumberFormat formater = NumberFormat.getPercentInstance();
		formater.setMinimumFractionDigits(dig);
		return formater.format(number);
	}

	public static String getPercent(long used, long total, int dig) {
		NumberFormat formater = NumberFormat.getPercentInstance();
		formater.setMinimumFractionDigits(dig);
		return formater.format((double) used / total);
	}

	public static String getPrintSize(Long size, boolean withOrginalSize) {
		if (size == null) {
			return Constants.NO_DATA_MARK;
		}

		return getPrintSize(size.longValue(), withOrginalSize);
	}

	public static String getPrintSize(long size, boolean withOrginalSize) {
		if (size < 0) {
			return Constants.NO_DATA_MARK;
		} else if (size == 0) {
			return "0Byte";
		}

		size = Math.abs(size);

		StringBuilder bytes = new StringBuilder();

		SizeUnit unit = SizeUnit.kindOf(size);
		float unitsize = unit.toUnitSize(size);
		bytes.append(FLOAT_FORMAT_WITH_COMMA_2.format(unitsize)).append(unit.name());
		if (withOrginalSize && unit != SizeUnit.Bytes) {
			bytes.append(" ( " + INT_FORMAT_WITH_COMMA.format(size)).append("bytes ) ");
		}

		return bytes.toString();
	}

	public static String getPrintBps(Long bytesSize) {
		if (bytesSize == null) {
			return "";
		}

		StringBuilder bytes = new StringBuilder();
		if (bytesSize <= 0) {
			bytes.append("0Kbps");
		} else {
			bytesSize *= 8;
			if (bytesSize >= 1000 * 1000 * 1000) {
				double i = (bytesSize / (1000.0 * 1000.0 * 1000.0));
				bytes.append(FLOAT_FORMAT_WITH_COMMA_1.format(i)).append("Gbps");
			} else if (bytesSize >= 1000 * 1000) {
				double i = (bytesSize / (1000.0 * 1000.0));
				bytes.append(FLOAT_FORMAT_WITH_COMMA_1.format(i)).append("Mbps");
			} else if (bytesSize >= 1000) {
				double i = (bytesSize / (1000.0));
				bytes.append(FLOAT_FORMAT_WITH_COMMA_1.format(i)).append("Kbps");
			}
		}
		return bytes.toString();
	}

	public static Long toBytes(String size) {
		if (StringUtils.isEmpty(size)) {
			return null;
		}

		size = size.toLowerCase().trim();
//		if (size.contains("b")) {
//			return Long.valueOf(size.substring(0, size.length() - 1));
//		}
		if (size.length() == 0) {
			return null;
		}
		if (size.contains("byte")) {
			return Long.valueOf(size.substring(0, size.length() - 4));
		}
		if (size.contains("kb")) {
			return (long) (Double.parseDouble(size.substring(0, size.length() - 2)) * SizeUnit.KB.toBytes());
		}
		if (size.contains("mb")) {
			return (long) (Double.parseDouble(size.substring(0, size.length() - 2)) * SizeUnit.MB.toBytes());
		}
		if (size.contains("gb")) {
			return (long) (Double.parseDouble(size.substring(0, size.length() - 2)) * SizeUnit.GB.toBytes());
		}
		if (size.contains("tb")) {
			return (long) (Double.parseDouble(size.substring(0, size.length() - 2)) * SizeUnit.TB.toBytes());
		}
		if (size.contains("pb")) {
			return (long) (Double.parseDouble(size.substring(0, size.length() - 2)) * SizeUnit.PB.toBytes());
		}
		return Long.valueOf(size);
	}

//	public static void main(String[] args) {
//		System.out.println(toBytes("12.0 gb"));
//		System.out.println(getPercent(1.23D, 2));
//	}

}
