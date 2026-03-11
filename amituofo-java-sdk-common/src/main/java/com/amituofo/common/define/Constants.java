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

import com.amituofo.common.util.DigestUtils;

public class Constants {
	public final static String[] SPACES = new String[] { "", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ",
			"             ", "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    ",
			"                     ", "                      ", "                       ", "                        ", "                         " };
	public final static String[] UNDERLINE = new String[] { "", "_", "__", "___", "____", "_____", "______", "_______", "________", "_________", "__________", "___________", "____________",
			"_____________", "______________", "_______________" };

	public final static String[] ZERO = new String[] { "", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000", "00000000000", "000000000000",
			"0000000000000", "00000000000000", "000000000000000" };
	public final static String FS = String.valueOf((char) 28);

	public final static String INVALID_NAME_CHARS_STR = "\\/:*?\"<>|";
	public final static String[] INVALID_NAME_CHARS = new String[] { "\\", "/", ":", "*", "?", "\"", "<", ">", "|" };
	public final static String[] INVALID_PATH_CHARS = new String[] { ":", "*", "?", "\"", "<", ">", "|" };

	public final static String SYSTEM_SHORT_NAME = "OCTSYS";
	public final static String SYSTEM_AUTH_NAME = SYSTEM_SHORT_NAME + "JA";

	public final static char URL_SEPARATOR = '/';
	public static final String NO_DATA_MARK = "-";

	public final static String TRUE = "true";
	public final static String FALSE = "false";

	public final static String DEFAULT_URL_ENCODE = "UTF-8";

	public final static String DT_FMT_ISO_8601 = "yyyy-MM-ddThh:mm:ssZ";
	public final static String DT_FMT_ISO_8601_BASIC = "yyyyMMddThh:mm:ssZ";

	public final static String TITLE_NO_INFO = "-";

//	public final static SimpleDateFormat DATE_FORMATTER_ISO_8601 = new SimpleDateFormat(DT_FMT_ISO_8601);

	public static final long TIME_2020_SECOND = 1577808000L;
	public static final long TIME_2020_MILLISECONDS = TIME_2020_SECOND * 1000;
	public static final long TIME_1981_MILLISECONDS = 3769344000L * 1000;

	public static final int SIZE_1KB = 1024;
	public static final int SIZE_5KB = SIZE_1KB * 5;
	public static final int SIZE_32KB = SIZE_1KB * 32;
	public static final int SIZE_64KB = SIZE_1KB * 64;
	public static final int SIZE_96KB = SIZE_1KB * 96;
	public static final int SIZE_128KB = SIZE_1KB * 128;
	public static final int SIZE_256KB = SIZE_1KB * 256;
	public static final int SIZE_512KB = SIZE_1KB * 512;
	public static final int SIZE_1MB = SIZE_1KB * 1024;
	public static final int SIZE_4MB = SIZE_1MB * 4;
	public static final int SIZE_5MB = SIZE_1MB * 5;
	public static final int SIZE_10MB = SIZE_1MB * 10;
	public static final int SIZE_15MB = SIZE_1MB * 15;
	public static final int SIZE_20MB = SIZE_1MB * 20;
	public static final int SIZE_25MB = SIZE_1MB * 25;
	public static final int SIZE_30MB = SIZE_1MB * 30;
	public static final int SIZE_35MB = SIZE_1MB * 35;
	public static final int SIZE_50MB = SIZE_1MB * 50;
	public static final int SIZE_64MB = SIZE_1MB * 64;
	public static final int SIZE_128MB = SIZE_1MB * 128;
	public static final int SIZE_100MB = SIZE_1MB * 100;
	public static final long SIZE_500MB = SIZE_1MB * 500;
	public static final long SIZE_512MB = SIZE_1MB * 512;
	public static final long SIZE_1GB = SIZE_1MB * 1024;
	public static final long SIZE_5GB = SIZE_1GB * 5L;
	public static final long SIZE_10GB = SIZE_1GB * 10L;
	public static final long SIZE_32GB = SIZE_1GB * 32L;
	public static final long SIZE_64GB = SIZE_1GB * 64L;
	public static final long SIZE_128GB = SIZE_1GB * 128L;
	public static final long SIZE_1024GB = SIZE_1GB * 1024L;
	public static final long SIZE_1500GB = SIZE_1GB * 1500L;
	public static final long SIZE_1TB = SIZE_1024GB;

	public static final int TIME_MILLISECONDS_1_SECOND = 1000;
	public static final int TIME_MILLISECONDS_2_SECOND = TIME_MILLISECONDS_1_SECOND * 2;
	public static final int TIME_MILLISECONDS_3_SECOND = TIME_MILLISECONDS_1_SECOND * 3;
	public static final int TIME_MILLISECONDS_4_SECOND = TIME_MILLISECONDS_1_SECOND * 4;
	public static final int TIME_MILLISECONDS_5_SECOND = TIME_MILLISECONDS_1_SECOND * 5;
	public static final int TIME_MILLISECONDS_6_SECOND = TIME_MILLISECONDS_1_SECOND * 6;
	public static final int TIME_MILLISECONDS_7_SECOND = TIME_MILLISECONDS_1_SECOND * 7;
	public static final int TIME_MILLISECONDS_8_SECOND = TIME_MILLISECONDS_1_SECOND * 8;
	public static final int TIME_MILLISECONDS_9_SECOND = TIME_MILLISECONDS_1_SECOND * 9;
	public static final int TIME_MILLISECONDS_10_SECOND = TIME_MILLISECONDS_1_SECOND * 10;
	public static final int TIME_MILLISECONDS_15_SECOND = TIME_MILLISECONDS_1_SECOND * 15;
	public static final int TIME_MILLISECONDS_30_SECOND = TIME_MILLISECONDS_1_SECOND * 30;
	public static final int TIME_MILLISECONDS_60_SECOND = TIME_MILLISECONDS_1_SECOND * 60;
	public static final int TIME_MILLISECONDS_1_MIN = TIME_MILLISECONDS_60_SECOND;
	public static final int TIME_MILLISECONDS_2_MIN = TIME_MILLISECONDS_1_MIN * 2;
	public static final int TIME_MILLISECONDS_3_MIN = TIME_MILLISECONDS_1_MIN * 3;
	public static final int TIME_MILLISECONDS_4_MIN = TIME_MILLISECONDS_1_MIN * 4;
	public static final int TIME_MILLISECONDS_5_MIN = TIME_MILLISECONDS_1_MIN * 5;
	public static final int TIME_MILLISECONDS_6_MIN = TIME_MILLISECONDS_1_MIN * 6;
	public static final int TIME_MILLISECONDS_7_MIN = TIME_MILLISECONDS_1_MIN * 7;
	public static final int TIME_MILLISECONDS_8_MIN = TIME_MILLISECONDS_1_MIN * 8;
	public static final int TIME_MILLISECONDS_9_MIN = TIME_MILLISECONDS_1_MIN * 9;
	public static final int TIME_MILLISECONDS_10_MIN = TIME_MILLISECONDS_1_MIN * 10;
	public static final int TIME_MILLISECONDS_15_MIN = TIME_MILLISECONDS_1_MIN * 15;
	public static final int TIME_MILLISECONDS_20_MIN = TIME_MILLISECONDS_1_MIN * 20;
	public static final int TIME_MILLISECONDS_25_MIN = TIME_MILLISECONDS_1_MIN * 25;
	public static final int TIME_MILLISECONDS_30_MIN = TIME_MILLISECONDS_1_MIN * 30;
	public static final int TIME_MILLISECONDS_45_MIN = TIME_MILLISECONDS_1_MIN * 45;
	public static final long TIME_MILLISECONDS_60_MIN = TIME_MILLISECONDS_1_MIN * 60;
	public static final long TIME_MILLISECONDS_90_MIN = TIME_MILLISECONDS_60_MIN + TIME_MILLISECONDS_30_MIN;
	public static final long TIME_MILLISECONDS_1_HOUR = TIME_MILLISECONDS_60_MIN;
	public static final long TIME_MILLISECONDS_2_HOUR = TIME_MILLISECONDS_1_HOUR * 2;
	public static final long TIME_MILLISECONDS_3_HOUR = TIME_MILLISECONDS_1_HOUR * 3;
	public static final long TIME_MILLISECONDS_4_HOUR = TIME_MILLISECONDS_1_HOUR * 4;
	public static final long TIME_MILLISECONDS_5_HOUR = TIME_MILLISECONDS_1_HOUR * 5;
	public static final long TIME_MILLISECONDS_6_HOUR = TIME_MILLISECONDS_1_HOUR * 6;
	public static final long TIME_MILLISECONDS_7_HOUR = TIME_MILLISECONDS_1_HOUR * 7;
	public static final long TIME_MILLISECONDS_8_HOUR = TIME_MILLISECONDS_1_HOUR * 8;
	public static final long TIME_MILLISECONDS_9_HOUR = TIME_MILLISECONDS_1_HOUR * 9;
	public static final long TIME_MILLISECONDS_10_HOUR = TIME_MILLISECONDS_1_HOUR * 10;
	public static final long TIME_MILLISECONDS_11_HOUR = TIME_MILLISECONDS_1_HOUR * 11;
	public static final long TIME_MILLISECONDS_12_HOUR = TIME_MILLISECONDS_1_HOUR * 12;
	public static final long TIME_MILLISECONDS_13_HOUR = TIME_MILLISECONDS_1_HOUR * 13;
	public static final long TIME_MILLISECONDS_14_HOUR = TIME_MILLISECONDS_1_HOUR * 14;
	public static final long TIME_MILLISECONDS_15_HOUR = TIME_MILLISECONDS_1_HOUR * 15;
	public static final long TIME_MILLISECONDS_16_HOUR = TIME_MILLISECONDS_1_HOUR * 16;
	public static final long TIME_MILLISECONDS_17_HOUR = TIME_MILLISECONDS_1_HOUR * 17;
	public static final long TIME_MILLISECONDS_18_HOUR = TIME_MILLISECONDS_1_HOUR * 18;
	public static final long TIME_MILLISECONDS_19_HOUR = TIME_MILLISECONDS_1_HOUR * 19;
	public static final long TIME_MILLISECONDS_20_HOUR = TIME_MILLISECONDS_1_HOUR * 20;
	public static final long TIME_MILLISECONDS_21_HOUR = TIME_MILLISECONDS_1_HOUR * 21;
	public static final long TIME_MILLISECONDS_22_HOUR = TIME_MILLISECONDS_1_HOUR * 22;
	public static final long TIME_MILLISECONDS_23_HOUR = TIME_MILLISECONDS_1_HOUR * 23;
	public static final long TIME_MILLISECONDS_24_HOUR = TIME_MILLISECONDS_1_HOUR * 24;
	public static final long TIME_MILLISECONDS_1_DAY = TIME_MILLISECONDS_24_HOUR;
	public static final long TIME_MILLISECONDS_2_DAY = TIME_MILLISECONDS_1_DAY * 2;
	public static final long TIME_MILLISECONDS_3_DAY = TIME_MILLISECONDS_1_DAY * 3;
	public static final long TIME_MILLISECONDS_4_DAY = TIME_MILLISECONDS_1_DAY * 4;
	public static final long TIME_MILLISECONDS_5_DAY = TIME_MILLISECONDS_1_DAY * 5;
	public static final long TIME_MILLISECONDS_6_DAY = TIME_MILLISECONDS_1_DAY * 6;
	public static final long TIME_MILLISECONDS_7_DAY = TIME_MILLISECONDS_1_DAY * 7;
	public static final long TIME_MILLISECONDS_8_DAY = TIME_MILLISECONDS_1_DAY * 8;
	public static final long TIME_MILLISECONDS_9_DAY = TIME_MILLISECONDS_1_DAY * 9;
	public static final long TIME_MILLISECONDS_10_DAY = TIME_MILLISECONDS_1_DAY * 10;
	public static final long TIME_MILLISECONDS_30_DAY = TIME_MILLISECONDS_1_DAY * 30L;

	public static final Integer INTEGER_1 = new Integer(1);
	public static final Integer INTEGER_0 = new Integer(0);
	public static final Long LONG_1 = new Long(1L);
	public static final Long LONG_0 = new Long(0L);

	public static String GLOBAL_DEFAULT_ADMIN_USERNAME = "admin";
	public static String INTERNAL_COMMUNICATION_AK1 = DigestUtils.encodeBase64("anonymous-admin");
	public static String INTERNAL_COMMUNICATION_SK1 = "a60de6363a6365955135eb2065551ad2";
	public static String INTERNAL_INIT_SK1 = "b9b7f25013c4680d91458da1617cb51e";

}
