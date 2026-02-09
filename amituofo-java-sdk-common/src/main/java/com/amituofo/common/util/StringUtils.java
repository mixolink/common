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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtils {
	public final static String[] SPACES = new String[] { "", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ", "              ", "               ",
			"                ", "                 ", "                  ", "                   ", "                    ", "                     ", "                      ", "                       ", "                        ",
			"                         " };
	public final static String[] UNDERLINE = new String[] { "", "_", "__", "___", "____", "_____", "______", "_______", "________", "_________", "__________", "___________", "____________", "_____________", "______________",
			"_______________" };

	public final static String[] ZERO = new String[] { "", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000", "00000000000", "000000000000", "0000000000000", "00000000000000", "000000000000000" };
	public final static String FS = String.valueOf((char) 28);

	public static String repeat(char c, int len) {
		String cs = String.valueOf(c);
		String str = "";
		for (int i = 0; i < len; i++) {
			str += cs;
		}

		return str;
	}

	public static String firstCharUppercase(String str) {
		if (str != null && str.length() > 0) {
			String firstChar = String.valueOf(str.charAt(0)).toUpperCase();
			return firstChar + str.substring(1);
		}

		return str;
	}

	public static String printStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static boolean isEmpty(String[] ary) {
		return ary == null || ary.length == 0;
	}

	public static boolean isNotEmpty(String[] strs) {
		return strs != null && strs.length > 0;
	}

	public static boolean isNotEmpty(char[] strs) {
		return strs != null && strs.length > 0;
	}

	public static boolean isEmpty(char[] strs) {
		return strs == null || strs.length == 0;
	}

	public static boolean isEmpty(CharSequence input) {
		if (input != null) {
			return input.length() == 0;
		}

		return true;
	}

	public static boolean isNotEmpty(CharSequence input) {
		if (input != null) {
			return input.length() != 0;
		}

		return false;
	}

	public static boolean areNotEmpty(String... strings) {
		if (strings == null || strings.length == 0) {
			return false;
		}

		for (String string : strings) {
			if (isEmpty(string)) {
				return false;
			}
		}

		return true;
	}

	public static String notEmptyValue(String input, String defaultValue) {
		if (input != null && input.length() != 0) {
			return input;
		}

		return defaultValue;
	}

	public static boolean isAscii(String input) {
		if (StringUtils.isEmpty(input)) {
			return true;
		}
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) > 127) {
				return false;
			}
		}
		return true;
	}

	public static boolean isIPAddress(String ip) {
		// 0.0.0.0
		if (ip == null || ip.length() < 7) {
			return false;
		}

		String[] ips = ip.split(".");
		if (ips.length == 4) {
			try {
				for (String A : ips) {
					int ai = Integer.parseInt(A);
					if (ai < 0 || ai > 255) {
						return false;
					}
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		return false;
	}

	public static boolean isEqualsAny(String str, String[] arr, boolean ignoreCase) {
		if (str == null || arr == null || arr.length == 0) {
			return false;
		}

		if (ignoreCase) {
			for (String string : arr) {
				if (isEqualsIgnoreCase(string, str)) {
					return true;
				}
			}
		} else {
			for (String string : arr) {
				if (isEquals(string, str)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isEqual(String[] arr1, String[] arr2) {
		if (arr1 == null && arr2 == null) {
			return true;
		}

		if (arr1 == null && arr2 != null || arr1 != null || arr2 == null) {
			return false;
		}

		if (arr1.length != arr2.length) {
			return false;
		}

		HashSet<String> set = new HashSet<>();

		for (String element : arr1) {
			set.add(element);
		}

		for (String element : arr2) {
			if (!set.contains(element)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isEquals(String str1, String str2) {
		if (str1 == str2) {
			return true;
		}

		if (str1 != null && str2 != null) {
			return str1.equals(str2);
		}

		return false;
	}

	public static boolean isEqualsIgnoreCase(String str1, String str2) {
		if (str1 == str2) {
			return true;
		}

		if (str1 != null && str2 != null) {
			return str1.equalsIgnoreCase(str2);
		}

		return false;
	}

	public static String toString(Object value) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

	public static Long toLong(Object value) {
		if (value == null) {
			return null;
		}

		return Long.parseLong(toString(value));
	}

	public static String toString(Long value) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

	public static String toString(Integer value) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

	public static boolean wildcardMatchAny(String pattern, String[] strs, boolean ignoreCase) {
		if (strs == null || strs.length == 0) {
			return true;
		}

		for (String str : strs) {
			if (wildcardMatch(pattern, str, ignoreCase)) {
				return true;
			}
		}

		return false;
	}

//	public static boolean wildcardMatch(String pattern, String str) {
//		return wildcardMatch(pattern, false, str);
//	}
//
//	public static boolean wildcardMatch(String pattern, boolean ignoreCase, String str) {
//		if (StringUtils.isEmpty(pattern)) {
//			return true;
//		}
//
//		if ("*".equals(pattern) || "**".equals(pattern)) {
//			return true;
//		}
//
//		if (StringUtils.isEmpty(str)) {
//			return true;
//		}
//
//		if (ignoreCase) {
//			pattern = pattern.toLowerCase();
//			str = str.toLowerCase();
//		}
//
//		int patternLength = pattern.length();
//		int strLength = str.length();
//		int strIndex = 0;
//		char ch;
//		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
//			ch = pattern.charAt(patternIndex);
//			if (ch == '*') {
//				while (strIndex < strLength) {
//					if (wildcardMatch(pattern.substring(patternIndex + 1), false, str.substring(strIndex))) {
//						return true;
//					}
//					strIndex++;
//				}
//			} else if (ch == '?') {
//				strIndex++;
//				if (strIndex > strLength) {
//					return false;
//				}
//			} else {
//				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
//					return false;
//				}
//				strIndex++;
//			}
//		}
//		return (strIndex == strLength);
//	}

	public static boolean wildcardMatch(String pattern, String filename) {
		// 调用辅助方法，开始匹配
		return wildcardMatch(pattern, filename, 0, 0, true);
	}

	public static boolean wildcardMatch(String pattern, String filename, boolean caseSensitive) {
		// 调用辅助方法，开始匹配
		return wildcardMatch(pattern, filename, 0, 0, caseSensitive);
	}

	private static boolean wildcardMatch(String pattern, String filename, int i, int j, boolean caseSensitive) {
		int len = filename.length();
		// 如果模式已经匹配完，检查文件名是否也匹配完
		if (j == pattern.length()) {
			return i == len;
		}

		// 处理模式中的 * 通配符
		if (pattern.charAt(j) == '*') {
			// 试图匹配 0 个字符或者多个字符
			for (int k = 0; k <= len - i; k++) {
				if (wildcardMatch(pattern, filename, i + k, j + 1, caseSensitive)) {
					return true;
				}
			}
			return false;
		}

		// 处理模式中的 ? 通配符或字符匹配
		if (i < len) {
			char filenameChar = filename.charAt(i);
			char patternChar = pattern.charAt(j);

			// 如果大小写敏感，则直接比较；否则忽略大小写进行比较
			boolean charsMatch = caseSensitive ? (patternChar == filenameChar) : (Character.toLowerCase(patternChar) == Character.toLowerCase(filenameChar));

			if (charsMatch || patternChar == '?') {
				return wildcardMatch(pattern, filename, i + 1, j + 1, caseSensitive);
			}
		}

		// 如果字符不匹配
		return false;
	}

//	public static void main(String[] args) {
//		String pattern = "my_file_*_xxx_*.txt";
//		String filename = "my_file_test_xxx_12345_.txt";
//
//		 List<String> matchStringList=new ArrayList<String>();
//		boolean match = pickWildcardMatch(pattern, filename, 0, 0,matchStringList);
//		System.out.println("是否匹配: " + match); // 输出是否匹配
//		
//		System.out.println("匹配的*: " + matchStringList);
//	}

	public static boolean pickWildcardMatch(String pattern, String filename, List<String> matchStringList) {
		return pickWildcardMatch(pattern, filename, 0, 0, matchStringList);
	}

	private static boolean pickWildcardMatch(String pattern, String filename, int i, int j, List<String> matchStringList) {
		int len = filename.length();

		// 如果模式已经匹配完，检查文件名是否也匹配完
		if (j == pattern.length()) {
			return i == len;
		}

		// 处理模式中的 * 通配符
		if (pattern.charAt(j) == '*') {
			// 记录当前 * 的位置
			int starIndex = j;
			int matchIndex = i;

			// 尝试匹配不同长度的字符
			while (matchIndex <= len) {
				// 打印当前匹配的字符
				String matched = filename.substring(i, matchIndex);

				// 递归尝试
				if (pickWildcardMatch(pattern, filename, matchIndex, starIndex + 1, matchStringList)) {
					if (matchStringList != null) {
						matchStringList.add(0, matched);
					}

					return true;
				}
				matchIndex++;
			}
			return false;
		}

		// 如果模式字符和文件名字符匹配
		if (i < len && pattern.charAt(j) == filename.charAt(i)) {
			return pickWildcardMatch(pattern, filename, i + 1, j + 1, matchStringList);
		}

		// 如果字符不匹配
		return false;
	}

	public static Integer toInteger(String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return Integer.parseInt(value);
	}

	public static Boolean toBoolean(String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return Boolean.parseBoolean(value);
	}

	public static boolean toBoolean(String value, boolean defaultValue) {
		if (value == null || value.length() == 0) {
			return defaultValue;
		}

		return Boolean.parseBoolean(value);
	}

	public static Long toLong(String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return Long.parseLong(value);
	}

	public static String nullToString(Object str) {
		if (str == null) {
			return "";
		}

		return str.toString();
	}

	public static String nullToString(Object str, String defaultValue) {
		if (str == null) {
			return defaultValue;
		}

		return str.toString();
	}

	public static String nullToString(Boolean str, String defaultValue) {
		if (str == null) {
			return defaultValue;
		}

		return str == Boolean.TRUE ? "Yes" : "No";
	}

	public static String toString(String[] params, String separator) {
		if (params == null || params.length == 0) {
			return "";
		}

		if (separator == null) {
			separator = "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length - 1; i++) {
			String string = params[i];
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]).append(separator);

		return buf.toString();
	}

	public static <T> String toString(Collection<T> params, char separator) {
		if (params == null || params.size() == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (T string : params) {
			if (string != null) {
				buf.append(string.toString()).append(separator);
			} else {
				buf.append(separator);
			}
		}

		return buf.substring(0, buf.length() - 1);
	}

	public static <T> List<String> toGroupString(Collection<T> params, char separator, int maxItemsInGroup) {
		List<String> strings = new ArrayList<>();

		if (params == null || params.size() == 0) {
			return strings;
		}

		int n = 0;
		StringBuffer buf = new StringBuffer();
		for (T string : params) {
			if (string != null) {
				buf.append(string.toString()).append(separator);
			} else {
				buf.append(separator);
			}

			n++;

			if (n % maxItemsInGroup == 0) {
				strings.add(buf.substring(0, buf.length() - 1));
				buf.setLength(0);
			}
		}

		if (buf.length() > 0) {
			strings.add(buf.substring(0, buf.length() - 1));
		}

		return strings;
	}

//	public static String toString(Collection<String> params, char separator) {
//		if (params == null || params.size() == 0) {
//			return "";
//		}
//
//		StringBuffer buf = new StringBuffer();
//		for (String string : params) {
//			buf.append(string).append(separator);
//		}
//
//		return buf.substring(0, buf.length() - 2);
//	}

	public static String toString(List<String> params, char separator) {
		if (params == null) {
			return "";
		}

		int size = params.size();
		if (size == 0) {
			return "";
		}

		if (size == 1) {
			return params.get(0);
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.size() - 1; i++) {
			String string = params.get(i);
			buf.append(string).append(separator);
		}
		buf.append(params.get(params.size() - 1));

		return buf.toString();
	}

	public static String toString(String[] params, int fromindex, char separator) {
		if (params == null || params.length <= fromindex) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = fromindex; i < params.length - 1; i++) {
			String string = params[i];
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]);

		return buf.toString();
	}

	public static String toString(String[] params, char separator) {
		return toString(params, 0, separator);
	}

	public static String toString(Object[] params, char separator) {
		if (params == null || params.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length - 1; i++) {
			String string = params[i] == null ? "" : params[i].toString();
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]);

		return buf.toString();
	}

	public static String toString(int[] params, char separator) {
		if (params == null || params.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length - 1; i++) {
			String string = params[i] + "";
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]);

		return buf.toString();
	}

	public static String toString(long[] params, char separator) {
		if (params == null || params.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length - 1; i++) {
			String string = params[i] + "";
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]);

		return buf.toString();
	}

	public static String toString(boolean[] params, char separator) {
		if (params == null || params.length == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < params.length - 1; i++) {
			String string = params[i] + "";
			buf.append(string).append(separator);
		}
		buf.append(params[params.length - 1]);

		return buf.toString();
	}

	public static String emptyToString(String str, String defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		}

		return str;
	}

	public static String nvl(String str1, String str2) {
		if (str1 == null) {
			return str2;
		}

		return str1;
	}

	public static String evl(String str1, String str2) {
		if (str1 == null || str1.length() == 0) {
			return str2;
		}

		return str1;
	}

	public static boolean hasValue(String[] values, String value, boolean caseSense) {
		if (values == null) {
			return false;
		}

		if (value == null) {
			for (String string : values) {
				if (string == null) {
					return true;
				}
			}
		} else {
			if (caseSense) {
				for (String string : values) {
					if (value.equals(string)) {
						return true;
					}
				}
			} else {
				for (String string : values) {
					if (value.equalsIgnoreCase(string)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static String toLowerCase(String str) {
		return isEmpty(str) ? "" : str.toLowerCase();
	}

	public static String toUpperCase(String str) {
		return isEmpty(str) ? "" : str.toUpperCase();
	}

	public static String firstChart2LowerCase(String str) {
		if (isEmpty(str)) {
			return "";
		}

		char c = str.charAt(0);
		if (c >= 'A' && c <= 'Z') {
			c += 32;
		}
		return c + str.substring(1);
	}
	// public static byte[] toUtf8BytesFromGbk(String gbkStr) {
	// int n = gbkStr.length();
	// byte[] utfBytes = new byte[3 * n];
	// int k = 0;
	// for (int i = 0; i < n; i++) {
	// int m = gbkStr.charAt(i);
	// if (m < 128 && m >= 0) {
	// utfBytes[k++] = (byte) m;
	// continue;
	// }
	// utfBytes[k++] = (byte) (0xe0 | (m >> 12));
	// utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
	// utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
	// }
	// if (k < utfBytes.length) {
	// byte[] tmp = new byte[k];
	// System.arraycopy(utfBytes, 0, tmp, 0, k);
	// return tmp;
	// }
	// return utfBytes;
	// }
	//
	// public static String toUtf8StringFromGbk(String gbkStr) throws
	// UnsupportedEncodingException {
	// return new String(toUtf8BytesFromGbk(gbkStr), "UTF-8");
	// }

	public static String encodingConverter(String value, Charset fromCharset, Charset toCharset) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}

		String a = new String(value.getBytes(fromCharset), "iso-8859-1");
		String b = new String(a.getBytes("iso-8859-1"), toCharset);

		return b;
	}

//	public static String encodeString(String input) {
//		if (StringUtils.isEmpty(input)) {
//			return "";
//		}
////		 return input;
//		return Base64Utils.encode(input.getBytes(StandardCharsets.UTF_8));
//	}
//	
//
//	public static String encodeString(String input) {
//		if (StringUtils.isEmpty(input)) {
//			return "";
//		}
////		 return input;
//		return Base64Utils.encode(input.getBytes(StandardCharsets.UTF_8));
//	}

	public static String encodeBase64String(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
//		 return input;
		return Base64Utils.encode(input.getBytes(StandardCharsets.UTF_8));
	}

	public static String encodeBase64String(String input, Charset encoding) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
//		 return input;
		return Base64Utils.encode(input.getBytes(encoding), encoding.name());
	}

	public static String encodeBase64String(byte[] input) {
		if (input == null || input.length == 0) {
			return "";
		}
//		 return input;
		return Base64Utils.encode(input);
	}

	public static String encodeBase64String(byte[] input, String encoding) {
		if (input == null || input.length == 0) {
			return "";
		}
//		 return input;
		return Base64Utils.encode(input, encoding);
	}

	public static String decodeBase64ToString(String base64String) {
		if (StringUtils.isEmpty(base64String)) {
			return "";
		}
//		 return base64String;
		return new String(Base64Utils.decode(base64String), StandardCharsets.UTF_8);
	}

	public static byte[] decodeBase64String(String base64String) {
		if (StringUtils.isEmpty(base64String)) {
			return null;
		}
//		 return base64String;
		return Base64Utils.decode(base64String);
	}

	public static byte[] decodeBase64String(String base64String, String encoding) {
		if (StringUtils.isEmpty(base64String)) {
			return null;
		}
//		 return base64String;
		return Base64Utils.decode(base64String, encoding);
	}

	public static int countOf(String str, char c) {
		if (StringUtils.isEmpty(str)) {
			return 0;
		}

		int count = 0;
		int start = 0;
		int i = str.indexOf(c, start);
		while (i != -1) {
			count++;
			start = i + 1;
			i = str.indexOf(c, start);
		}
		return count;
	}

	public static int countOf(String str, String find) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(find)) {
			return 0;
		}

		int count = 0;
		int start = 0;
		int i = str.indexOf(find, start);
		while (i != -1) {
			count++;
			start = i + find.length();
			i = str.indexOf(find, start);
		}
		return count;
	}

	public static int checkPasswordStrength(String password) {

		int iPasswordScore = 0;

		if (password.length() < 8)
			return 0;
		else if (password.length() >= 10)
			iPasswordScore += 2;
		else
			iPasswordScore += 1;

		/*
		 * if password contains 2 digits, add 2 to score. if contains 1 digit add 1 to
		 * score
		 */
		if (password.matches("(?=.*[0-9].*[0-9]).*"))
			iPasswordScore += 2;
		else if (password.matches("(?=.*[0-9]).*"))
			iPasswordScore += 1;

		// if password contains 1 lower case letter, add 2 to score
		if (password.matches("(?=.*[a-z]).*"))
			iPasswordScore += 2;

		/*
		 * if password contains 2 upper case letters, add 2 to score. if contains only 1
		 * then add 1 to score.
		 */
		if (password.matches("(?=.*[A-Z].*[A-Z]).*"))
			iPasswordScore += 2;
		else if (password.matches("(?=.*[A-Z]).*"))
			iPasswordScore += 1;

		/*
		 * if password contains 2 special characters, add 2 to score. if contains only 1
		 * special character then add 1 to score.
		 */
		if (password.matches("(?=.*[~!@#$%^&*()_-].*[~!@#$%^&*()_-]).*"))
			iPasswordScore += 2;
		else if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
			iPasswordScore += 1;

		return iPasswordScore;
	}

	public static boolean containsLowerCase(String label) {
		if (StringUtils.isNotEmpty(label)) {
			char[] cs = label.toCharArray();
			for (char c : cs) {
				if (c >= 'a' && c <= 'z') {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(String srcstr, String findstr) {
		return contains(srcstr, findstr, true);
	}

	public static boolean contains(String srcstr, String findstr, boolean caseSensitive) {
		if (StringUtils.isEmpty(srcstr) || StringUtils.isEmpty(findstr)) {
			return false;
		}

		if (caseSensitive) {
			return srcstr.contains(findstr);
		} else {
			return srcstr.toLowerCase().contains(findstr.toLowerCase());
		}
	}

	public static boolean contains(String[] srcstrs, String findstr) {
		return contains(srcstrs, findstr, true);
	}

	public static boolean contains(String[] srcstrs, String findstr, boolean caseSensitive) {
		if (srcstrs == null || srcstrs.length == 0 || StringUtils.isEmpty(findstr)) {
			return false;
		}

		if (!caseSensitive) {
			findstr = findstr.toLowerCase();
		}

		for (String string : srcstrs) {
			if (string == null) {
				continue;
			}
			if (!caseSensitive) {
				string = string.toLowerCase();
			}
			if (string.equals(findstr)) {
				return true;
			}
		}

		return false;
	}

	public static boolean contains(List<String> srcstrs, String findstr, boolean caseSensitive) {
		if (srcstrs == null || srcstrs.size() == 0 || StringUtils.isEmpty(findstr)) {
			return false;
		}

		if (!caseSensitive) {
			findstr = findstr.toLowerCase();
		}

		for (String string : srcstrs) {
			if (string == null) {
				continue;
			}
			if (!caseSensitive) {
				string = string.toLowerCase();
			}
			if (string.equals(findstr)) {
				return true;
			}
		}

		return false;
	}

	public static boolean contains(String[] srcstrs, String[] findstrs, boolean caseSensitive) {
		if (srcstrs == null || srcstrs.length == 0 || findstrs == null || findstrs.length == 0) {
			return false;
		}

		for (String findstr : findstrs) {
			if (contains(srcstrs, findstr, caseSensitive)) {
				return true;
			}
		}

		return false;
	}

	public static boolean contains(String srcstr, String[] findstrs, boolean matchAllTrueOrAnyFalse, boolean caseSensitive) {
		if (srcstr == null || srcstr.length() == 0 || StringUtils.isEmpty(findstrs)) {
			return false;
		}

		if (!caseSensitive) {
			srcstr = srcstr.toLowerCase();
		}

		int matchcnt = 0;
		for (String findstr : findstrs) {
			if (findstr == null) {
				continue;
			}

			if (!caseSensitive) {
				findstr = findstr.toLowerCase();
			}

			if (srcstr.indexOf(findstr) != -1) {
				matchcnt++;
			}
		}

		if (matchAllTrueOrAnyFalse) {
			return matchcnt == findstrs.length;
		} else {
			return matchcnt > 0;
		}
	}

//	public static boolean containsAnyInString(String string, String[] find) {
//		// TODO Auto-generated method stub
//		return false;
//	}

//	public static <T> String[] toStringArray(List<T> collection, ) {
//		return (collection != null ? collection.toArray(new String[0]) : new String[0]);
//	}

	public static String[] toStringArray(Collection<String> collection) {
		return (collection != null ? collection.toArray(new String[0]) : new String[0]);
	}

	public static String[] toStringArray(Enumeration<String> enumeration) {
		return (enumeration != null ? toStringArray(Collections.list(enumeration)) : new String[0]);
	}

//	public static List<String> toStringList(String[] arrays) {
//		List<String> list = new ArrayList<>();
//		if (arrays != null) {
//			for (String string : arrays) {
//				list.add(string);
//			}
//		}
//		return list;
//	}

	public static String findStringStartWith(String[] fields, String string) {
		if (fields == null) {
			return null;
		}

		for (String field : fields) {
			if (field != null && field.indexOf(string) == 0) {
				return field;
			}
		}

		return null;
	}

	public static List<String> toStringList(String str, char seperator) {
		List<String> list = new ArrayList<>();

		if (StringUtils.isNotEmpty(str)) {
			String[] strs = str.split(String.valueOf(seperator));
			for (String string : strs) {
				list.add(string);
			}
		}
		return list;
	}

	public static List<String> toStringList(Object... strs) {
		List<String> list = new ArrayList<>();

		if (strs != null) {
			for (Object str : strs) {
				if (str != null) {
					list.add(str.toString());
				}
			}
		}
		return list;
	}

	public static String[] split(String strs, char seperator) {
		return split(strs, seperator, false);
	}

	public static String[] splitFirst(String strs, char seperator) {
		if (StringUtils.isEmpty(strs)) {
			return new String[0];
		}

		int i = strs.indexOf(seperator);
		if (i == -1) {
			return new String[0];
		}

		return new String[] { strs.substring(0, i), strs.substring(i + 1) };
	}

	public static String[] split(String strs, char seperator, boolean trim) {
		if (StringUtils.isEmpty(strs)) {
			return new String[0];
		}

		int[] indexs = indexsOfEachChar(strs, seperator);
		if (indexs.length == 0) {
			return new String[] { trim ? strs.trim() : strs };
		}

		int startIndex = 0;
		int endIndex = 0;
		String[] s = new String[indexs.length + 1];
		for (int i = 0; i < indexs.length; i++) {
			endIndex = indexs[i];

			if (startIndex == endIndex) {
				s[i] = "";
			} else {
				s[i] = strs.substring(startIndex, endIndex);
				if (trim) {
					s[i] = s[i].trim();
				}
			}
			startIndex = endIndex + 1;
		}

		if (trim) {
			s[indexs.length] = strs.substring(endIndex + 1).trim();
		} else {
			s[indexs.length] = strs.substring(endIndex + 1);
		}

		return s;
	}

	public static String[] split(String strs, boolean trim, char... separators) {
		// 处理 null 输入
		if (strs == null) {
			return new String[0];
		}

		// 处理空分隔符数组：返回整个字符串（trim 后）作为一个元素
		if (separators == null || separators.length == 0) {
			String result = trim ? strs.trim() : strs;
			return new String[] { result };
		}

		// 将分隔符放入 Set 以提高查找效率（虽然 char 数组通常很小）
		java.util.Set<Character> sepSet = new java.util.HashSet<>();
		for (char c : separators) {
			sepSet.add(c);
		}

		Collection<String> parts;
		if (trim) {
			parts = new HashSet<>();
		} else {
			parts = new java.util.ArrayList<>();
		}

		StringBuilder current = new StringBuilder();

		for (int i = 0; i < strs.length(); i++) {
			char c = strs.charAt(i);
			if (sepSet.contains(c)) {
				// 遇到分隔符，处理当前片段
				String part = current.toString();
				if (trim) {
					part = part.trim();
				}
				if (part.length() > 0) {
					parts.add(part);
				}
				current.setLength(0); // 清空 StringBuilder
			} else {
				current.append(c);
			}
		}

		// 添加最后一个片段
		String lastPart = current.toString();
		if (trim) {
			lastPart = lastPart.trim();
		}
		if (lastPart.length() > 0) {
			parts.add(lastPart);
		}

		return parts.toArray(new String[parts.size()]);
	}
	
//	public static void main(String[] args) {
//        System.out.println("=== 测试 1: trim = true（去重 + 无序） ===");
//        String[] r1 = split("apple, banana, apple, cherry, banana", true, ',');
//        System.out.println("输入: \"apple, banana, apple, cherry, banana\"");
//        System.out.println("输出: " + Arrays.toString(r1));
//        // 预期：包含 ["apple", "banana", "cherry"]，顺序不定，无重复
//
//        System.out.println("\n=== 测试 2: trim = false（保留重复 + 顺序） ===");
//        String[] r2 = split("apple, banana, apple, cherry", false, ',');
//        System.out.println("输入: \"apple, banana, apple, cherry\"");
//        System.out.println("输出: " + Arrays.toString(r2));
//        // 预期：["apple", " banana", " apple", " cherry"]（注意空格）
//
//        System.out.println("\n=== 测试 3: 多分隔符 + trim=true（去重） ===");
//        String[] r3 = split("a:b/a\\b:c", true, ':', '/', '\\');
//        System.out.println("输入: \"a:b/a\\\\b:c\"");
//        System.out.println("输出: " + Arrays.toString(r3));
//        // 预期：["a", "b", "c"]（"a" 和 "b" 重复出现，但被去重）
//
//        System.out.println("\n=== 测试 4: 全空片段（应返回空数组） ===");
//        String[] r4 = split(" , , \t , ", true, ',');
//        System.out.println("输入: \" , , \\t , \"");
//        System.out.println("输出: " + Arrays.toString(r4));
//        // 预期：[]
//
//        System.out.println("\n=== 测试 5: trim=false 但内容相同（保留重复） ===");
//        String[] r5 = split("x,x,x", false, ',');
//        System.out.println("输入: \"x,x,x\"");
//        System.out.println("输出: " + Arrays.toString(r5));
//        // 预期：["x", "x", "x"]
//
//        System.out.println("\n=== 测试 6: trim=true 且内容相同（去重） ===");
//        String[] r6 = split("x,x,x", true, ',');
//        System.out.println("输入: \"x,x,x\"");
//        System.out.println("输出: " + Arrays.toString(r6));
//        // 预期：["x"]
//    }

//	public static String[] split(String strs, char[] seperators, boolean trim) {
//		if (StringUtils.isEmpty(strs)) {
//			return new String[0];
//		}
//
//		Set<String> splitedStrs = new HashSet<>();
//		for (char seperator : seperators) {
//			String[] sparrays = split(strs, seperator, trim);
//		}
//		return s;
//	}

//	if (expips.contains(",")) {
//		ips = expips.split(",");
//	} else if (expips.contains(";")) {
//		ips = expips.split(";");
//	} else {
//		ips = new String[] { expips };
//	}
//
//	if (ips != null) {
//		for (String ip : ips) {
//			ip = ip.trim();
//			if (StringUtils.isNotEmpty(ip)) {
//				list.add(ip);
//			}
//		}
//	}

	private static int[] indexsOfEachChar(String str, char seperator) {
		if (StringUtils.isEmpty(str)) {
			return new int[0];
		}

		int index = str.indexOf(seperator);
		if (index == -1) {
			return new int[0];
		}

		List<Integer> indexsTmp = new ArrayList<>();
		while (index != -1) {
			indexsTmp.add(index);
			index = str.indexOf(seperator, index + 1);
		}

		int[] indexs = new int[indexsTmp.size()];
		for (int j = 0; j < indexs.length; j++) {
			indexs[j] = indexsTmp.get(j);
		}

		indexsTmp = null;

		return indexs;
	}

	public static String join(Object[] strs, String split) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder();

		int max = strs.length - 1;
		for (int i = 0; i < max; i++) {
			buf.append(strs[i]);
			buf.append(split);
		}

		buf.append(strs[max]);

		return buf.toString();
	}

	public static String join(List<String> strs, String split) {
		if (strs == null || strs.size() == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder();

		int max = strs.size() - 1;
		for (int i = 0; i < max; i++) {
			buf.append(strs.get(i));
			buf.append(split);
		}

		buf.append(strs.get(max));

		return buf.toString();
	}

	public static String[] appendArray(String[] ary1, String... ary2) {
		return mergeArray(ary1, ary2);
	}

	public static String[] mergeArray(String[] ary1, String[] ary2) {
		return mergeArray(ary1, ary2, false);
	}

	public static String[] mergeArray(String[] ary1, String[] ary2, boolean removeDuplicated) {
		if (ary1 == null || ary1.length == 0) {
			return ary2;
		}

		if (ary2 == null || ary2.length == 0) {
			return ary1;
		}

		Collection<String> list;
		if (removeDuplicated) {
			list = new HashSet<>();
		} else {
			list = new ArrayList<>();
		}

		for (String value : ary1) {
			list.add(value);
		}

		for (String arg : ary2) {
			list.add(arg);
		}

		return list.toArray(new String[list.size()]);
	}

	public static String[] removeDuplicated(String[] ary) {
		if (ary == null || ary.length == 0) {
			return ary;
		}

		Collection<String> list;
		list = new HashSet<>();

		for (String value : ary) {
			list.add(value);
		}

		return list.toArray(new String[list.size()]);
	}

	public static List<String> removeDuplicated(List<String> ary) {
		if (ary == null || ary.isEmpty()) {
			return ary;
		}

		Collection<String> list;
		list = new HashSet<>();

		for (String value : ary) {
			list.add(value);
		}

		return new ArrayList<String>(list);
	}

	public static boolean containsDuplicate(String[] strs) {
		if (strs == null || strs.length <= 1) {
			return false;
		}

		Set<String> set = new HashSet<>();
		for (String string : strs) {
			set.add(string);
		}

		return set.size() != strs.length;
	}

	public static Set<String> toStringSet(Set<?> objs) {
		Set<String> set = new HashSet<>();
		if (objs != null) {
			for (Object o : objs) {
				if (o != null) {
					set.add(o.toString());
				}
			}
		}
		return set;
	}

	public static List<String> toStringList(Set<?> objs) {
		List<String> list = new ArrayList<>();
		if (list != null) {
			for (Object o : objs) {
				if (o != null) {
					list.add(o.toString());
				}
			}
		}
		return list;
	}

	public static List<String> toStringList(List<?> objs) {
		List<String> list = new ArrayList<>();
		if (list != null) {
			for (Object o : objs) {
				if (o != null) {
					list.add(o.toString());
				}
			}
		}
		return list;
	}

	public static int indexOfFirstNumber(String str, int fromIndex) {
		if (str == null) {
			return -1;
		}

		int len = str.length();

		if (len == 0 || fromIndex >= len) {
			return -1;
		}

		for (int i = fromIndex; i < len; i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				return i;
			}
		}
		return -1;
	}

	public static String[] remove(String[] array, String target) {
		List<String> list = new ArrayList<>(Arrays.asList(array));
		list.remove(target);
		return list.toArray(new String[list.size()]);
	}

	public static String[] filterInclude(String[] srcarray, String[] findarray) {
		List<String> list = new ArrayList<>();

		if (srcarray != null && srcarray.length > 0) {
			for (String string : srcarray) {
				if (findarray != null && findarray.length > 0) {
					if (StringUtils.contains(findarray, string)) {
						list.add(string);
					}
				} else {
					list.add(string);
				}
			}
		}

		return list.toArray(new String[list.size()]);
	}

	public static String cat(String str1, String str2, char separator) {
		if (str1 == null || str1.length() == 0) {
			return str2;
		}

		if (str2 == null || str2.length() == 0) {
			return str1;
		}

		// 取得第一个路径中最后一个字符
		char lastChar = str1.charAt(str1.length() - 1);

		// 如果这个字符就是路径分隔符
		if (lastChar == separator) {
			return str1 + str2;
		} else {
			return str1 + separator + str2;
		}
	}

	public static String replacePathSeparator(String fileName, char pathSeparator) {
		if (fileName == null || fileName.length() == 0) {
			return fileName;
		}

		if (pathSeparator == '\\') {
			if (fileName.indexOf('/') != -1) {
				fileName = fileName.replace('/', pathSeparator);
			}
		} else if (pathSeparator == '/') {
			if (fileName.indexOf('\\') != -1) {
				fileName = fileName.replace('\\', pathSeparator);
			}
		}
		return fileName;
	}

	public static char lastChar(String str) {
		if (str == null) {
			return 0;
		}
		int len = str.length();
		if (len == 0) {
			return 0;
		}
		return str.charAt(len - 1);
	}

	public static boolean isLastCharSeperator(String str) {
		if (str == null) {
			return false;
		}
		int len = str.length();
		if (len == 0) {
			return false;
		}
		char c = str.charAt(len - 1);
		return c == '/' || c == '\\';
	}

	public static String trimSeparator(String str) {
		if (str == null) {
			return str;
		}
		int len = str.length();
		if (len == 0) {
			return str;
		}

		str = str.trim();

		char head = str.charAt(0);
		char foot = str.charAt(len - 1);

		if (head == '/' || head == '\\') {
			if (len == 1) {
				return "";
			}

			if (foot == '/' || foot == '\\') {
				return str.substring(1, len - 1);
			} else {
				return str.substring(1);
			}
		} else {
			if (foot == '/' || foot == '\\') {
				return str.substring(0, len - 1);
			} else {
				return str;
			}
		}
	}

	public static String propertyOf(String prop) {
		if (prop == null) {
			return null;
		}

		int len = prop.length();
		for (int i = 0; i < len; i++) {
			if (prop.charAt(i) == '.') {
				return prop.substring(i + 1);
			}
		}

		return null;
	}

	public static String[] addStringInEach(String[] strs, String headString, String footString) {
		if (strs == null || strs.length == 0) {
			return strs;
		}

		String[] newstrs = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			newstrs[i] = headString + strs[i] + footString;
		}

		return newstrs;
	}

	public static boolean isCertainlyNotJson(String str) {
		return !isMaybeJson(str);
	}

	public static boolean isMaybeJson(String str) {
		if (str == null) {
			return false;
		}

		str = str.trim();
		if (str.length() < 2) {
			return false;
		}

		char firstChar = str.charAt(0);
		char lastChar = str.charAt(str.length() - 1);

		// 快速检查
		if ((firstChar == '{' && lastChar == '}') || (firstChar == '[' && lastChar == ']')) {
			return true;
		}
		return false;
	}

	public static boolean isCertainlyNotJson(byte[] str) {
		return !isMaybeJson(str);
	}

	public static boolean isMaybeJson(byte[] str) {
		if (str == null) {
			return false;
		}

		if (str.length < 2) {
			return false;
		}

		char firstChar = (char) str[0];
		char lastChar = (char) str[str.length - 1];

		// 快速检查
		if ((firstChar == '{' && lastChar == '}') || (firstChar == '[' && lastChar == ']')) {
			return true;
		}
		return false;
	}

	public static boolean endWithChar(String str, char c) {
		return lastChar(str) == c;
	}

	public static boolean containsAsianCharacters(String text) {
		for (char c : text.toCharArray()) {
			Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
			if (isAsianBlock(block)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAsianBlock(Character.UnicodeBlock block) {
		return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || block == Character.UnicodeBlock.HIRAGANA || block == Character.UnicodeBlock.KATAKANA || block == Character.UnicodeBlock.HANGUL_SYLLABLES
				|| block == Character.UnicodeBlock.HANGUL_JAMO || block == Character.UnicodeBlock.BOPOMOFO || block == Character.UnicodeBlock.THAI || block == Character.UnicodeBlock.LAO || block == Character.UnicodeBlock.TIBETAN
				|| block == Character.UnicodeBlock.MYANMAR || block == Character.UnicodeBlock.GEORGIAN || block == Character.UnicodeBlock.ARMENIAN || block == Character.UnicodeBlock.HEBREW || block == Character.UnicodeBlock.ARABIC
				|| block == Character.UnicodeBlock.BENGALI || block == Character.UnicodeBlock.GURMUKHI || block == Character.UnicodeBlock.GUJARATI || block == Character.UnicodeBlock.TAMIL || block == Character.UnicodeBlock.TELUGU
				|| block == Character.UnicodeBlock.KANNADA || block == Character.UnicodeBlock.MALAYALAM || block == Character.UnicodeBlock.SINHALA || block == Character.UnicodeBlock.THAANA || block == Character.UnicodeBlock.ETHIOPIC
				|| block == Character.UnicodeBlock.CHEROKEE;
	}

	public static int compareBetween(String a, String b) {
		if (a == null && b == null) {
			return 0;
		}

		if (a == null) {
			return 1;
		}

		if (b == null) {
			return -1;
		}

		return a.compareTo(b);
	}

	public static String replaceLastString(String string, String find, String replaceWith) {
		if (string == null || find == null || find.isEmpty()) {
			return string; // 保持与标准库一致的行为
		}

		int lastIndex = string.lastIndexOf(find);
		if (lastIndex == -1) {
			return string; // 未找到，返回原字符串
		}

		StringBuilder sb = new StringBuilder();
		sb.append(string, 0, lastIndex); // 替换点之前的部分
		sb.append(replaceWith); // 替换内容
		sb.append(string, lastIndex + find.length(), string.length()); // 替换点之后的部分

		return sb.toString();
	}

	public static boolean looksBroken(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		return s.contains("�") || s.contains("Ã") || s.contains("Â­") || !s.chars().allMatch(c -> c >= 32);
	}

	/**
	 * 判断给定字符串是否可能是乱码
	 * 
	 * @param s 输入字符串，可能是 ISO-8859-1 创建的
	 * @return true 表示可能乱码，false 表示看起来正常
	 */
	public static boolean isGarbled(String s) {
		if (s == null || s.isEmpty())
			return false;

		int total = s.length();
		int printable = 0;
		int nonPrintable = 0;
		int replacementChar = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			// 替换字符，强烈乱码信号
			if (c == '\uFFFD') {
				replacementChar++;
				continue;
			}

			// 控制字符 / 非打印字符
			if (Character.isISOControl(c) && c != '\t' && c != '\n') {
				nonPrintable++;
				continue;
			}

			// Unicode 分类判断
			int type = Character.getType(c);
			switch (type) {
			case Character.UPPERCASE_LETTER:
			case Character.LOWERCASE_LETTER:
			case Character.TITLECASE_LETTER:
			case Character.MODIFIER_LETTER:
			case Character.OTHER_LETTER:
			case Character.DECIMAL_DIGIT_NUMBER:
			case Character.LETTER_NUMBER:
			case Character.OTHER_NUMBER:
			case Character.NON_SPACING_MARK:
			case Character.ENCLOSING_MARK:
			case Character.COMBINING_SPACING_MARK:
			case Character.OTHER_PUNCTUATION:
			case Character.DASH_PUNCTUATION:
			case Character.START_PUNCTUATION:
			case Character.END_PUNCTUATION:
			case Character.CONNECTOR_PUNCTUATION:
			case Character.MATH_SYMBOL:
			case Character.CURRENCY_SYMBOL:
			case Character.MODIFIER_SYMBOL:
			case Character.OTHER_SYMBOL:
				printable++;
				break;
			default:
				nonPrintable++;
			}
		}

		double replacementRatio = replacementChar / (double) total;
		double printableRatio = printable / (double) total;
		double nonPrintableRatio = nonPrintable / (double) total;

		// 判断规则，可调阈值
		if (replacementRatio > 0.05)
			return true; // 有替换字符就认为乱码
		if (printableRatio < 0.6)
			return true; // 可打印字符比例低
		if (nonPrintableRatio > 0.3)
			return true; // 非打印字符比例高

		return false;
	}

}
