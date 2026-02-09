package com.amituofo.common.util;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amituofo.common.define.Constants;

public class URLUtils {
//	public static void  main(String[] a) {
//		String[] urls = new String[] {"http://www.a.com",
//				"https://www.a.com",
//				"HTTPS://www.a.com",
//				"http://www.a.com/request/",
//				"HTTP://www.a.com/request/test",
//				"http://www.a.com/request/test?param=111",
//				"http://1.1.1.1?param=111",
//				"https://www.baidu.com/?p=1",
//				"https://www.baidu.com:81/?p=1:1",
//				"http://www.a.com:8080",
//				"http://www.a.com:8080/request",
//				"http://www.a.com:8080/request/eeee",
//				"http://www.a.com:8080/request/wewew?param=222",
//				"http://1.1.1.1?param=111",
//				"www.a.com",
//				"www.a.com/request/",
//				"www.a.com/request/test",
//				"www.a.com/request/test?param=111",
//				"1.1.1.1?param=111",
//				"www.baidu.com/?p=1",
//				"www.baidu.com:81/?p=1:1",
//				"www.a.com:8080",
//				"www.a.com:8080/request",
//				"www.a.com:8080/request/eeee",
//				"www.a.com:8080/request/wewew?param=222",
//				"1.1.1.1?param=111",
//				};
//		for (String string : urls) {
//			System.out.println(string+"\t\t\t"+URLUtils.getRequestHostName(string));
//			
//		}
//		System.out.println("----------");
//		
//		for (String string : urls) {
//			System.out.println(string+"\t\t\t"+URLUtils.getRequestHostPort(string, 88));
//			
//		}
//	}

	public static final char QP_SEP_A = '&';
	public static final char QP_SEP_S = ';';
	public static final String NAME_VALUE_SEPARATOR = "=";

	private static final String[] cs = new String[] { "+", "%7E", "*", "%2F" };
	private static final String[] replacements = new String[] { "%20", "~", "%2A", "/" };

	public static final Pattern ENCODED_PATTERN;
	static {
		StringBuilder pattern = new StringBuilder();

		pattern.append(Pattern.quote("+")).append("|").append(Pattern.quote("*")).append("|").append(Pattern.quote("%7E")).append("|").append(Pattern.quote("%2F"));// .append(Pattern.quote("%3A"));

		ENCODED_PATTERN = Pattern.compile(pattern.toString());
	}

	public static String getRequestHostName(String url) {
		int start, end;
		int i1 = url.indexOf("://");
		if (i1 == -1) {
			start = 0;
		} else {
			start = i1 + 3;
		}

		int i2 = url.indexOf(':', start);
		if (i2 == -1) {
			i2 = url.indexOf('/', start);
			if (i2 == -1) {
				i2 = url.indexOf('?', start);
			}
		}

		if (i2 == -1) {
			end = url.length();
		} else {
			end = i2;
		}

		if (end == start) {
			return "";
		}

		return url.substring(start, end).trim();
	}

	public static String replaceHost(String url, String hostname) {
		String httpSchema = "";
		String portetc = "";

		int i1 = url.indexOf("://");
		if (i1 != -1) {
			httpSchema = url.substring(0, i1 + 3);
		}

		i1 = url.lastIndexOf(':');
		if (i1 != -1) {
			portetc = url.substring(i1);
		}

		return httpSchema + hostname + portetc;
	}

	public static String resetHttpRequestUrl(String url, boolean ssl) {
		if (StringUtils.isNotEmpty(url)) {
			int i = url.indexOf("://");
			if (i != -1) {
				url = url.substring(i + 3);
				return ssl ? "https://" + url : "http://" + url;
			}
		}

		return "";
	}

	public static int getRequestHostPort(String url, int defaultPort) {
		// www.sss.com:8080
		// www.sss.com:8080/aaa/bbb
		// www.sss.com:8080/aaa/bbb?p=11
		// www.sss.com
		// http://www.ss/aaa
		// http://www.aa.com?tt=1
		// http://www.aa.com?tt=1:

		int start, end;
		int i1 = url.indexOf("://");
		if (i1 == -1) {
			i1 = 0;
		} else {
			i1 += 3;
		}

		int i2 = url.indexOf(':', i1);
		if (i2 == -1) {
			return defaultPort;
		}

		start = i2 + 1;

		int i3 = url.indexOf('/', start);
		if (i3 == -1) {
			// i3 = url.indexOf('?', start);
			i3 = url.length();
		}

		if (i2 > i3) {
			return defaultPort;
		}

		end = i3;

		if (end == start) {
			return defaultPort;
		}

		String port = url.substring(start, end).trim();
		return Integer.parseInt(port);
	}

	public static String getRequestPath(String url) {
		int istart;
		int i1 = url.indexOf("://");
		if (i1 != -1) {
			istart = url.indexOf('/', i1 + 4);
		} else {
			istart = url.indexOf('/');
		}

		if (istart == -1) {
			return "";
		}

		int i2 = url.indexOf('?', istart + 1);

		if (i2 != -1) {
			return url.substring(istart + 1, i2);
		} else {
			return url.substring(istart + 1);
		}
	}

//	public static String getRequestBasePath(String path) {
//		String requestName = null;
//		int i1 = path.lastIndexOf('/');
//		if (i1 == -1) {
//			i1 = path.lastIndexOf('\\');
//		}
//
//		if (i1 == 0) {
//			requestName = "/";
//		} else if (i1 > 0) {
//			requestName = path.substring(0, i1);
//		} else {
//			requestName = "/";
//		}
//
//		return requestName;
//	}

	// public static String getLastNameFromPath(String fileName, char pathSeparator) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * a/b/c -> c a/b/ -> b
	 *
	 * @param  path
	 * @return
	 */
	public static String getLastNameFromPath(String path) {
		if (path == null) {
			return "";
		}
		int len = path.length();
		if (len == 0) {
			return "";
		}
		if (len == 1) {
			return path;
		}

		int lastIndex = len - 1;
		char lastchar = path.charAt(lastIndex);
		if (lastchar == '/' || lastchar == '\\') {
			lastIndex--;
		}

		int start = path.lastIndexOf('/', lastIndex);
		if (start == -1) {
			start = path.lastIndexOf('\\', lastIndex);
			if (start == -1) {
				return path.substring(0, lastIndex + 1);
			} else {
				return path.substring(start + 1, lastIndex + 1);
			}
		} else {
			return path.substring(start + 1, lastIndex + 1);
		}
	}

	public static String getLastNameFromPath(String path, char pathSeparator) {
		if (path == null) {
			return "";
		}
		int len = path.length();
		if (len == 0) {
			return "";
		}
		if (len == 1 && path.charAt(0) == pathSeparator) {
			return path;
		}

		int lastIndex = len - 1;
		char lastchar = path.charAt(lastIndex);
		if (lastchar == pathSeparator) {
			lastIndex--;

			int start = path.lastIndexOf(pathSeparator, lastIndex);
			if (start == -1) {
				return path.substring(0, lastIndex + 1);
			} else {
				return path.substring(start + 1, lastIndex + 1);
			}
		} else {
			int start = path.lastIndexOf(pathSeparator, lastIndex);
			if (start == -1) {
				return path;
			} else {
				return path.substring(start + 1);
			}
		}
	}

	public static String getFirstNameFromPath(String path, char pathSeparator) {
		if (path == null) {
			return "";
		}
		int len = path.length();
		if (len == 0) {
			return "";
		}
		if (len == 1 && path.charAt(0) == pathSeparator) {
			return "";
		}

		int index = path.indexOf(pathSeparator);
		if (index == -1) {
			return path;
		} else {
			return path.substring(0, index);
		}
	}

	/**
	 * @param  path
	 * @return      [null,path] ["/root", "file.txt"]
	 */
	public static String[] getTopParentAndSubPath(final String path) {
		if (path == null) {
			return new String[] { null, "" };
		}

		int len = path.length();
		if (len == 0) {
			return new String[] { null, "" };
		}

		if (len == 1 && (path.charAt(0) == '/' || path.charAt(0) == '\\')) {
			return new String[] { null, "" };
		}

		int fromindex = 0;
		if (path.charAt(0) == '/' || path.charAt(0) == '\\') {
//			path = path.substring(1);
			fromindex++;
		}

		int end = path.indexOf('/', fromindex);
		if (end == -1) {
			end = path.indexOf('\\', fromindex);
		}

		if (end == -1) {
			return new String[] { null, path };
		} else {
			if (end == len - 1) {
				return new String[] { null, path };
			}
			return new String[] { path.substring(0, end), path.substring(end + 1) };
		}
	}

	public static String getParentPath(String path, char seperator, String defaultRoot) {
//		if (path == null) {
//			return null;
//		}
//		int len = path.length();
//		if (len == 0) {
//			return null;
//		}
//
//		int lastIndex = len - 1;
//		int start = path.lastIndexOf(seperator, lastIndex - 1);
//		if (start == -1) {
//			if (path.charAt(0) != seperator) {
//				return defaultRoot;
//			}
//
//			// start = path.lastIndexOf('\\', lastIndex - 1);
//			// if (start == -1) {
//			return null;
//			// } else {
//			// return path.substring(0, start + 1);
//			// }
//		} else {
//			return path.substring(0, start + 1);
//		}

		return getParentPath(path, seperator, false, defaultRoot);
	}

	public static String getParentPath(String path, char seperator, boolean includeSeparator, String defaultRoot) {
		if (path == null) {
			return null;
		}
		int len = path.length();
		if (len == 0) {
			return null;
		}

		int lastIndex = len - 1;
		int start = path.lastIndexOf(seperator, lastIndex - 1);
		if (start == -1) {
			if (path.charAt(0) != seperator) {
				return defaultRoot;
			}

			return null;
		} else {
			return path.substring(0, start + (includeSeparator ? 1 : 0));
		}
	}

	public static String getLastRequestPath(String url) {
		// Intercepting the original file name
		String requestName = null;
		int i1 = url.lastIndexOf('/');
		if (i1 == -1) {
			i1 = url.lastIndexOf('\\');
		}

		if (i1 == -1) {
			return "";
		}

		int i2 = url.lastIndexOf('/', i1 - 1);
		if (i2 == -1) {
			i2 = url.lastIndexOf('\\', i1 - 1);
		}

		if (i2 != -1) {
			requestName = url.substring(i2 + 1, i1);
		} else {
			requestName = url.substring(0, i1);
		}

		return requestName;
	}

	public static String tidyUrlPath(String url) {
		// Intercepting the original file name
		char lastChar = url.charAt(url.length() - 1);
		if (lastChar != Constants.URL_SEPARATOR) {
			return url + Constants.URL_SEPARATOR;
		}

		return url;
	}

	// return new URI(String.format("%s://%s.%s", endpoint.getScheme(), bucketName,
	// endpoint.getAuthority()));

	public static String catPath(String path1, String path2, char pathSeparator) {
		String origPath1 = path1;
		if (path1 == null || path1.length() == 0) {
			if (path2 != null && path2.length() != 0) {
				if (pathSeparator == '\\') {
					path2 = path2.replace('/', pathSeparator);
				} else if (pathSeparator == '/') {
					path2 = path2.replace('\\', pathSeparator);
				}
				if (path2.charAt(0) == pathSeparator) {
					return path2.substring(1);
				}
			}

			return path2;
		} else {
			if (pathSeparator == '\\') {
				path1 = path1.replace('/', pathSeparator);
			} else if (pathSeparator == '/') {
				path1 = path1.replace('\\', pathSeparator);
			}
			// 取得第一个路径中最后一个字符
			char lastChar = path1.charAt(path1.length() - 1);

			// 如果这个字符就是路径分隔符
			if (lastChar == pathSeparator) {
				path1 = path1.substring(0, path1.length() - 1);
			}
		}

		// 如果没有要加的路径就返回第一个
		if (path2 == null || path2.length() == 0) {
			return origPath1;
		} else {
			if (pathSeparator == '\\') {
				path2 = path2.replace('/', pathSeparator);
			} else if (pathSeparator == '/') {
				path2 = path2.replace('\\', pathSeparator);
			}

			char firstChar = path2.charAt(0);

			if (firstChar == pathSeparator) {
				if (path2.length() == 1) {
					path2 = "";
				} else {
					path2 = path2.substring(1);
				}
			}
		}

		return path1 + pathSeparator + path2;
	}

	public static String catPathSeparator(String path, char pathSeparator) {
		if (path == null || path.length() == 0) {
			return String.valueOf(pathSeparator);
		} else {
			// 取得第一个路径中最后一个字符
			char lastChar = path.charAt(path.length() - 1);

			// 如果这个字符就是路径分隔符
			if (lastChar == pathSeparator) {
				return path;
			} else {
				return path + pathSeparator;
			}
		}
	}

	public static String catFilePath(String path1, String path2) {
		if (path1 == null || path1.length() == 0) {
			return path2;
		} else {
			char lastChar = path1.charAt(path1.length() - 1);

			if (lastChar == '/' || lastChar == '\\') {
				path1 = path1.substring(0, path1.length() - 1);
			}
		}

		if (path2 == null || path2.length() == 0) {
			return path1;
		} else {
			char firstChar = path2.charAt(0);

			if (firstChar == '/' || firstChar == '\\') {
				path2 = path2.substring(1);
			}
		}

		return path1 + File.separator + path2;
	}

	public static String catPath(String path1, String path2) {
		if (path1 == null || path1.length() == 0) {
			return path2;
		}

		if (path2 == null || path2.length() == 0) {
			return path1;
		}

		{
			char lastChar = path1.charAt(path1.length() - 1);

			if (lastChar == '/' || lastChar == '\\') {
				path1 = path1.substring(0, path1.length() - 1);
			}
		}

		{
			char firstChar = path2.charAt(0);

			if (firstChar == '/' || firstChar == '\\') {
				path2 = path2.substring(1);
			}
		}

		return path1 + Constants.URL_SEPARATOR + path2;
	}

	public static String catPaths(String[] paths, int start, int end) {
		StringBuilder fullPath = new StringBuilder();

		if (start < 0) {
			start = 0;
		}
		if (end >= paths.length) {
			end = paths.length - 1;
		}

		if (paths != null) {
			for (int i = start; i <= end; i++) {
				String path = trimPath(paths[i]);
				if (path.length() > 0) {
					fullPath.append(path);

					if (i != end) {
						fullPath.append('/');
					}
				}
			}
		}

		return fullPath.toString();
	}

	// public static void main(String[] s) {
	// String sa = URLUtils.trimPath("//rte/te/rt//");
	// System.out.println(sa);
	// String sa1 = URLUtils.catPaths(new String[] { "", "/", "sdd", "//rte/te/rt//", "/dd", "ppp/", "/ddd/" }, 0, 99);
	// System.out.println(sa1);
	// }

	public static String encode(String input, String encode) {
		if (input == null) {
			return "";
		}

		if (encode == null || encode.length() == 0) {
			return input;
		}

		// TODO XXX
		// input = input.replace(" ", "%20");
		try {
			String url = URLEncoder.encode(input, encode);
			// TODO
			// return url.replace("%2F", "/").replace("%3A", ":");
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return input;
	}

	public static String[] splitDomainAndPath(String url) {
		// http://www.google.com:9090/path/service
		int i = url.indexOf("://");
		if (i != -1) {
			i = url.indexOf('/', i + 3);
			if (i != -1) {
				// http://www.google.com:9090/path/service
				// /path/service
				return new String[] { url.substring(0, i), url.substring(i) };
			} else {
				// http://www.google.com:9090
				return new String[] { url, "" };
			}

		} else {
			return new String[] { "", url };
		}
	}

	public static String urlEncode1(String value, String encode) {
		if (value == null || value.length() == 0) {
			return "";
		}

		String[] splitedUrl = splitDomainAndPath(value);

		String encoded = null;
		try {
			encoded = URLEncoder.encode(splitedUrl[1], encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Matcher matcher = ENCODED_PATTERN.matcher(encoded);
		StringBuffer buffer = new StringBuffer(value.length());
		buffer.append(splitedUrl[0]);

		while (matcher.find()) {
			String replacement = matcher.group(0);

			if ("+".equals(replacement)) {
				replacement = "%20";
			} else if ("%7E".equals(replacement)) {
				replacement = "~";
			} else if ("*".equals(replacement)) {
				replacement = "%2A";
			} else if ("%2F".equals(replacement)) {
				replacement = "/";
				// } else if ("%3A".equals(replacement)) {
				// replacement = ":";
			}

			matcher.appendReplacement(buffer, replacement);
		}

		matcher.appendTail(buffer);

		return buffer.toString();
	}

	public static String urlEncode(String value, String encode) {
		if (value == null || value.length() == 0) {
			return "";
		}

		String[] splitedUrl = splitDomainAndPath(value);

		String encoded = null;
		try {
			encoded = URLEncoder.encode(splitedUrl[1], encode);
		} catch (UnsupportedEncodingException e) {
			encoded = splitedUrl[1];
		}

		for (int i = 0; i < cs.length; i++) {
			String c = cs[i];
			int position = encoded.indexOf(c);
			if (position != -1) {
				encoded = encoded.replace(c, replacements[i]);
			}
		}

		return splitedUrl[0] + encoded;
	}

	public static String requestPathEncode(String requestPath, String encode) {
		if (requestPath == null || requestPath.length() == 0) {
			return "";
		}

		String encoded = null;
		try {
			encoded = URLEncoder.encode(requestPath, encode);
		} catch (UnsupportedEncodingException e) {
			encoded = requestPath;
		}

		for (int i = 0; i < cs.length; i++) {
			String c = cs[i];
			int position = encoded.indexOf(c);
			if (position != -1) {
				encoded = encoded.replace(c, replacements[i]);
			}
		}

		return encoded;
	}

	public static String xmlKeywordDecode(String value) {
		// TODO
		// &lt; <
		// &gt; >
		// &amp; &
		// &apos; '
		// &quot; "
		// &copy; ©
		// &reg ®
		int iAnd = value.indexOf(QP_SEP_A);
		if (iAnd != -1) {
			int iQut = value.indexOf(QP_SEP_S, iAnd + 1);
			if (iQut != -1) {
				return convert(value, iAnd, iQut);
			}
		}

		return value;
	}

	private static String convert(String value, int iAnd, int iQut) {
		int nextBegin = iQut + 1;
		do {
			String xmlKey = value.substring(iAnd + 1, iQut).toLowerCase();
			if ("lt".equals(xmlKey)) {
				value = value.replace("&lt;", "<");
			} else if ("gt".equals(xmlKey)) {
				value = value.replace("&gt;", ">");
			} else if ("amp".equals(xmlKey)) {
				value = value.replace("&amp;", "&");
			} else if ("apos".equals(xmlKey)) {
				value = value.replace("&apos;", "'");
			} else if ("quot".equals(xmlKey)) {
				value = value.replace("&quot;", "\"");
			} else if ("copy".equals(xmlKey)) {
				value = value.replace("&copy;", "©");
			} else if ("reg".equals(xmlKey)) {
				value = value.replace("&reg;", "®");
			} else {
				nextBegin = iQut + 1;
			}

			iAnd = value.indexOf(QP_SEP_A, nextBegin);
			if (iAnd != -1) {
				iQut = value.indexOf(QP_SEP_S, iAnd + 1);
			}
		} while (iAnd != -1 && iQut != -1);

		return value;
	}

	/**
	 * Decode a string for use in the path of a URL; uses URLDecoder.decode, which decodes a string for use in the query portion of a URL.
	 *
	 * @param  value The value to decode
	 * @return       The decoded value if parameter is not null, otherwise, null is returned.
	 */
	public static String urlDecode(final String value, String encode) {
		if (value == null) {
			return null;
		}

		try {
			return URLDecoder.decode(value, encode);

		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean isPathEquals(String path1, String path2) {
		if (StringUtils.isNotEmpty(path1) && StringUtils.isEmpty(path2)) {
			return false;
		} else if (StringUtils.isEmpty(path1) && StringUtils.isNotEmpty(path2)) {
			return false;
		} else if (StringUtils.isNotEmpty(path1) && StringUtils.isNotEmpty(path2)) {
			char c1, c2;

			c1 = path1.charAt(0);
			if (c1 == '/' || c1 == '\\') {
				path1 = path1.substring(1);
			}
			if (path1.length() > 1) {
				c2 = path1.charAt(path1.length() - 1);
				if (c2 == '/' || c2 == '\\') {
					path1 = path1.substring(0, path1.length() - 1);
				}
			}

			c1 = path2.charAt(0);
			if (c1 == '/' || c1 == '\\') {
				path2 = path2.substring(1);
			}
			if (path2.length() > 1) {
				c2 = path2.charAt(path2.length() - 1);
				if (c2 == '/' || c2 == '\\') {
					path2 = path2.substring(0, path2.length() - 1);
				}
			}

			return path1.equalsIgnoreCase(path2);
		} else {
			return true;
		}
	}

	public static String simpleCatPaths(String... paths) {
		String retPath = "";

		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];

			retPath = catPath(retPath, path, '/');
		}

		return retPath;
	}

	public static String trimPath(String fullpath) {
		return trimPathHead(trimPathFoot(fullpath));
	}

//	public static String trimPath(String path) {
//		if (StringUtils.isNotEmpty(path)) {
//			while (path.length() > 0 && path.charAt(0) == '/') {
//				path = path.substring(1);
//			}
//
//			while (path.length() > 0 && path.charAt(path.length() - 1) == '/') {
//				path = path.substring(0, path.length() - 1);
//			}
//
//			return path;
//		} else {
//			return "";
//		}
//	}

	public static String wrapPath(String fullpath, char warpchar) {
		if (fullpath == null || fullpath.length() == 0) {
			return String.valueOf(warpchar);
		}

		char c = fullpath.charAt(0);
		if (c != warpchar) {
			fullpath = warpchar + fullpath;
		}

		c = fullpath.charAt(fullpath.length() - 1);
		if (c != warpchar) {
			fullpath = fullpath + warpchar;
		}

		return fullpath;
	}

	public static String catPathHead(String fullpath, char head) {
		if (fullpath == null || fullpath.length() == 0) {
			return String.valueOf(head);
		}

		char c = fullpath.charAt(0);
		if (c == head) {
			return fullpath;
		}

		return head + trimPathHead(fullpath);
	}

	public static String catPathFoot(String fullpath, char foot) {
		if (fullpath == null || fullpath.length() == 0) {
			return String.valueOf(foot);
		}

		char c = fullpath.charAt(fullpath.length() - 1);
		if (c == foot) {
			return fullpath;
		}

		return fullpath + foot;
	}

	public static String trimPathHead(String fullpath) {
		if (fullpath != null) {
			while (fullpath.length() > 0) {
				char c = fullpath.charAt(0);
				if (c == '/' || c == '\\') {
					fullpath = fullpath.substring(1);
				} else {
					return fullpath;
				}
			}

			return fullpath;
		}

		return "";
	}

	public static String trimPathFoot(String fullpath) {
		if (fullpath != null) {
			int len = fullpath.length();
			while (len > 0) {
				int lastindex = len - 1;
				char c = fullpath.charAt(lastindex);
				if (c == '/' || c == '\\') {
					fullpath = fullpath.substring(0, lastindex);
				} else {
					return fullpath;
				}

				len = fullpath.length();
			}

			return fullpath;
		}

		return "";
	}
}
