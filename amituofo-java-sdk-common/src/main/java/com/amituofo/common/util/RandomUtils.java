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

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomUtils {
	private final static Random rand = new Random();
	private final static char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static String randomMailAddress() {
		int a = 97;
		int z = 122;

		StringBuffer mailaddress = new StringBuffer();
		int maxCount = randomInt(3, 9);
		for (int i = 0; i < maxCount; i++) {
			mailaddress.append((char) randomInt(a, z));
		}
		mailaddress.append('@');
		maxCount = randomInt(3, 9);
		for (int i = 0; i < maxCount; i++) {
			mailaddress.append((char) randomInt(a, z));
		}
		mailaddress.append(".com");

		return mailaddress.toString();
	}

	public static String randomString() {
//		int a = 97;
//		int z = 122;

		int maxCount = randomInt(8, 15);
		StringBuffer str = new StringBuffer(maxCount);
		for (int i = 0; i < maxCount; i++) {
//			str.append((char) randomInt(a, z));
			int index = rand.nextInt(chars.length);
			str.append(chars[index]);
		}

		return str.toString();
	}

	public static String randomString(int minlen, int maxlen) {
		int len = randomInt(minlen, maxlen);
		StringBuffer str = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
//			str.append((char) randomInt(97, 122));
			int index = rand.nextInt(chars.length);
			str.append(chars[index]);
		}

		return str.toString();
	}

//	public static String randomString(final int lenth) {
//		StringBuffer str = new StringBuffer();
//		for (int i = 0; i < lenth; i++) {
//			str.append((char) randomInt(97, 122));
//		}
//
//		return str.toString();
//	}

	public static String randomString(int length) {
		// 字符集包含数字和小写字母
		StringBuilder result = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(chars.length);
			result.append(chars[index]);
		}

		return result.toString();
	}

	public static String randomChineseString(final int lenth) {
		StringBuffer str = new StringBuffer(lenth);
		for (int i = 0; i < lenth; i++) {
			int hightPos = (176 + Math.abs(rand.nextInt(39)));
			int lowPos = (161 + Math.abs(rand.nextInt(93)));

			byte[] b = new byte[2];
			b[0] = (Integer.valueOf(hightPos)).byteValue();
			b[1] = (Integer.valueOf(lowPos)).byteValue();

			String one;
			try {
				one = new String(b, "GB2312");
				str.append(one);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return str.toString();
	}

	public static long randomLong(long from, long to) {
		return from + (long) (Math.random() * (to - from + 1));
	}

	public static int randomInt(int from, int to) {
		if (from == to) {
			return to;
		}

		return rand.nextInt(to - from + 1) + from;
	}

	public static byte[] randomBytes(final int lenth) {
		int start = randomInt(0, 255);

		byte[] buf = new byte[lenth];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) (start++);
		}

		return buf;
	}

//	public static long randomLong(long from, long to) {
//		long v = rand.nextLong();
//		do {
//			if(v>=from &&)
//		}
//		return v;
//	}

//	public static long randomInt(long from, long to) {
//		if(from == to) {
//			return to;
//		}
//		return rand.nextLong() + from;
//	}

}
