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
package com.amituofo.common.define;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.amituofo.common.util.DateUtils;

public enum DatetimeFormat {
	/** ISO 8601 format */
	ISO8601_DATE_FORMAT(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")),
	RFC3339_DATE_FORMAT(new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssZ")),
	
	/** Alternate ISO 8601 format without fractional seconds */
	ALTERNATE_ISO8601_DATEFORMAT(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")),

	/** RFC 822 format */
	RFC822_DATE_FORMAT(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)),
	GMT_RFC822_DATE_FORMAT(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US), DateUtils.GMT_ZONE),

	/**
	 * This is another ISO 8601 format that's used in clock skew error response
	 */
	COMPRESSED_ISO8601_DATE_FORMAT(new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")),
	
	/**
	 * 
	 */
	YYYYMMDD(new SimpleDateFormat("yyyyMMdd")),
	YYYYMMDDHH(new SimpleDateFormat("yyyyMMddHH")),
	YYYYMMDDHHMM(new SimpleDateFormat("yyyyMMddHHmm")),
	YYYYMMDDHHMMSS(new SimpleDateFormat("yyyyMMddHHmmss")),
	YYYYMMDDHHMMSSS(new SimpleDateFormat("yyyyMMddHHmmsss")),
	/**
	 * 
	 */
	YYYY_MM_DD(new SimpleDateFormat("yyyy-MM-dd")),
	/**
	 * 
	 */
	YYYY_MM_DD_HHMM(new SimpleDateFormat("yyyy-MM-dd HH:mm")),
	YYYY_MM_DD_HHMMSS(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
	YYYY_MM_DD_HHMMSSS(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss")),
	
	
	HHMMSS(new SimpleDateFormat("HHmmss")),
	HHMMSSS(new SimpleDateFormat("HHmmsss"));


	private final SimpleDateFormat formater;

	DatetimeFormat(SimpleDateFormat formater) {
		this.formater = formater;
	}
	
	DatetimeFormat(SimpleDateFormat formater, TimeZone zone) {
		this.formater = formater;
		this.formater.setTimeZone(zone);
	}

	public SimpleDateFormat getFormater() {
		return formater;
	}

	public String format(Long time) {
		if (time == null) {
			return "";
		}

		return formater.format(time);
	}
	
	public String format(long time) {
		return formater.format(time);
	}

	public String format(Date date) {
		if (date == null) {
			return "";
		}

		return formater.format(date);
	}
	
	public String currentTime() {
		return formater.format(System.currentTimeMillis());
	}

	public static void setTimeZone(TimeZone tz) {
		DatetimeFormat[] vs = DatetimeFormat.values();
		for (DatetimeFormat datetimeFormat : vs) {
			datetimeFormat.getFormater().setTimeZone(tz);
		}
	}

	public static boolean isTimestampInMilliseconds(long timestamp) {
		// 以 1970 年至今的时间范围判断，10^10 约为 2001-09-09 的秒级时间戳
		return timestamp > 9999999999L;
	}
}
