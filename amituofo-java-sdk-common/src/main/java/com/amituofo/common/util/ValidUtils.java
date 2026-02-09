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

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.amituofo.common.ex.InvalidParameterException;

public class ValidUtils {

	public static void invalidHostResolution(String host, String msg) throws InvalidParameterException {
		if (!StringUtils.isIPAddress(host)) {
			try {
				InetAddress.getAllByName(host);
			} catch (UnknownHostException e) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidHostReachable(String host, int timeout) throws ConnectException {
		boolean status = false;
		try {
//			InetAddress[] ips = InetAddress.getAllByName(host);
//			if (ips != null) {
//				for (InetAddress inetAddress : ips) {
//
//				}
//			}
			InetAddress ip = InetAddress.getByName(host);
			// ping maybe disabled by server side
			status = ip.isReachable(timeout);
		} catch (Exception e) {
			throw new ConnectException("Host " + host + " unreachable");
		}

		if (!status) {
			throw new ConnectException("Host " + host + " unreachable");
		}
	}

	public static void invalidHostConnection(String host, int port, int timeout) throws ConnectException, UnknownHostException {
		Socket connect = new Socket();
		try {
			connect.connect(new InetSocketAddress(host, port), timeout);// 建立连接
			if (!connect.isConnected()) {
				throw new InvalidParameterException("Failed to connect to " + host + ":" + port);
			}
		} catch (UnknownHostException e) {
			throw e;
		} catch (ConnectException e) {
			throw e;
		} catch (Exception e) {
			throw new ConnectException(e.getMessage());
		} finally {
			try {
				connect.close();
			} catch (IOException e) {
			}
		}
	}

//	public static boolean invalidUDPConnection(String host, int port) {
//		try (DatagramSocket socket = new DatagramSocket()) {
//			socket.setSoTimeout(2000); // 设置超时为2秒
//
//			String message = "Test";
//			byte[] buffer = message.getBytes();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), port);
//			socket.send(packet);
//
//			// 监听响应（如果有的话）
//			DatagramPacket responsePacket = new DatagramPacket(new byte[buffer.length], buffer.length);
//			socket.receive(responsePacket); // 等待响应
//
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}

	public static void invalidIfNull(Object o, String msg) throws InvalidParameterException {
		if (o == null) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotNull(Object o, String msg) throws InvalidParameterException {
		if (o != null) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfTrue(boolean v, String msg) throws InvalidParameterException {
		if (v) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfFalse(boolean v, String msg) throws InvalidParameterException {
		if (!v) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfEmpty(Object o, String msg) throws InvalidParameterException {
		if (o == null) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfEmpty(String o, String msg) throws InvalidParameterException {
		if (o == null || o.length() == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfTrimEmpty(String o, String msg) throws InvalidParameterException {
		if (o == null || o.length() == 0 || o.trim().length() == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotNumber(String o, String msg) throws InvalidParameterException {
		invalidIfEmpty(o, msg);

		try {
			Double.parseDouble(o);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotRangeOfNumber(String o, double min, double max, String msg) throws InvalidParameterException {
		invalidIfEmpty(o, msg);

		double d;
		try {
			d = Double.parseDouble(o);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException(msg);
		}

		if (d < min || d > max) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotRangeOfLength(String o, int minLen, int maxLen, String msg) throws InvalidParameterException {
		invalidIfEmpty(o, msg);

		int len = o.length();
		if (len < minLen || len > maxLen) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotLettersNumbersHyphens(String o, String msg) throws InvalidParameterException {
		invalidIfEmpty(o, msg);

		// letters, numbers, and hyphens
		char[] cs = o.toCharArray();
		for (char c : cs) {
			if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '-' || c == '_') {
			} else {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfEmpty(List os, String msg) throws InvalidParameterException {
		if (os == null || os.size() == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfEmpty(Map<?, ?> map, String msg) throws InvalidParameterException {
		if (map == null || map.size() == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfNotEmpty(String o, String msg) throws InvalidParameterException {
		if (o != null || o.length() != 0) {
			throw new InvalidParameterException(msg);
		}
	}

	// public static void invalidIfContains(String o, String[] contains, String msg) throws InvalidParameterException {
	// if (o != null && o.length() >= 0 && contains != null) {
	// for (String string : contains) {
	// if (o.contains(string)) {
	// throw new InvalidParameterException(msg);
	// }
	// }
	// }
	// }

	public static void invalidIfContainsChar(String o, String chars, String msg) throws InvalidParameterException {
		if (o != null && o.length() >= 0 && chars != null) {
			char[] charArrays = chars.toCharArray();
			for (char c : charArrays) {
				if (o.contains(String.valueOf(c))) {
					throw new InvalidParameterException(msg);
				}
			}
		}
	}

	public static void invalidIfZero(int d, String msg) throws InvalidParameterException {
		if (d == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfZero(long d, String msg) throws InvalidParameterException {
		if (d == 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfLassThanZero(int d, String msg) throws InvalidParameterException {
		if (d < 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfLassThan(long d, final long threshold, String msg) throws InvalidParameterException {
		if (d < threshold) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfLassThan(float d, final float threshold, String msg) throws InvalidParameterException {
		if (d < threshold) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfGreaterThan(long d, final long threshold, String msg) throws InvalidParameterException {
		if (d > threshold) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfAGreaterThanB(Long a, Long b, String msg) throws InvalidParameterException {
		if (a != null && b != null) {
			invalidIfGreaterThan(a.longValue(), b.longValue(), msg);
		}
	}

	public static void invalidIfLengthGreaterThan(String value, int max, String msg) throws InvalidParameterException {
		if (value != null) {
			if (value.length() > max) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	// public static void invalidWhenNull(Object o, String msg) throws InvalidParameterException {
	// if (o == null) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void invalidWhenNotNull(Object o, String msg) throws InvalidParameterException {
	// if (o != null) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void invalidWhenTrue(boolean v, String msg) throws InvalidParameterException {
	// if (v) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void invalidWhenFalse(boolean v, String msg) throws InvalidParameterException {
	// if (!v) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void invalidWhenEmpty(String o, String msg) throws InvalidParameterException {
	// if (o == null || o.length() == 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }
	//
	// public static void invalidWhenEmpty(List os, String msg) throws InvalidParameterException {
	// if (os == null || os.size() == 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	public static void invalidIfAllEmpty(String msg, String... strs) throws InvalidParameterException {
		if (strs != null && strs.length != 0) {
			for (String o : strs) {
				if (o == null || o.length() == 0) {
					throw new InvalidParameterException(msg);
				}
			}

		}
	}

	// public static void exceptionWhenEmpty(Map<?, ?> map, String msg) throws InvalidParameterException {
	// if (map == null || map.size() == 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void exceptionWhenNotEmpty(String o, String msg) throws InvalidParameterException {
	// if (o != null || o.length() != 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	public static void invalidIfContains(String o, String[] contains, String msg) throws InvalidParameterException {
		if (o == null && contains != null && contains.length > 0) {
			throw new InvalidParameterException(msg);
		}

		for (String string : contains) {
			if (o.contains(string)) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfNotContains(String o, String[] contains, String msg) throws InvalidParameterException {
		if (o == null || o.length() == 0) {
			return;
		}

		int count = 0;
		if (contains != null) {
			for (String string : contains) {
				if (o.contains(string)) {
					count++;
				}
			}

			if (count < contains.length) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfNotA2zOr0to9(String o, String msg) throws InvalidParameterException {
		if (o != null && o.length() >= 0) {
			char[] cs = o.toCharArray();
			for (char c : cs) {
				if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
				} else {
					throw new InvalidParameterException(msg);
				}
			}
		}
	}
	
//	public static void invalidFileName(String o, String msg) throws InvalidParameterException {
//		if (o != null && o.length() >= 0) {
//			char[] cs = o.toCharArray();
//			for (char c : cs) {
//				if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '-' || c == '_') {
//				} else {
//					throw new InvalidParameterException(msg);
//				}
//			}
//		}
//	}

	// public static void exceptionWhenContainsChar(String o, String chars, String msg) throws InvalidParameterException {
	// if (o != null && o.length() >= 0 && chars != null) {
	// char[] charArrays = chars.toCharArray();
	// for (char c : charArrays) {
	// if (o.contains(String.valueOf(c))) {
	// throw new InvalidParameterException(msg);
	// }
	// }
	// }
	// }

	// public static void exceptionWhenZero(int d, String msg) throws InvalidParameterException {
	// if (d == 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void exceptionWhenZero(long d, String msg) throws InvalidParameterException {
	// if (d == 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	// public static void exceptionWhenLassThanZero(int d, String msg) throws InvalidParameterException {
	// if (d < 0) {
	// throw new InvalidParameterException(msg);
	// }
	// }
	//
	// public static void exceptionWhenLassThan(long d, final long threshold, String msg) throws InvalidParameterException {
	// if (d < threshold) {
	// throw new InvalidParameterException(msg);
	// }
	// }
	//
	// public static void exceptionWhenGreaterThan(long d, final long threshold, String msg) throws InvalidParameterException {
	// if (d > threshold) {
	// throw new InvalidParameterException(msg);
	// }
	// }

	public static void invalidIfNotEquals(String value1, String value2, String msg) throws InvalidParameterException {
		if (value1 == null && value2 != null || value1 != null && value2 == null) {
			throw new InvalidParameterException(msg);
		}

		if (!value1.equals(value2)) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfEqual(String value1, String value2, String msg) throws InvalidParameterException {
		if (value1 == null && value2 == null) {
			throw new InvalidParameterException(msg);
		}

		if (value1 != null && value2 != null) {
			if (value1.equals(value2)) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfEqualsIgnoreCase(String value1, String value2, String msg) throws InvalidParameterException {
		if (value1 == null && value2 == null) {
			throw new InvalidParameterException(msg);
		}

		if (value1 != null && value2 != null) {
			if (value1.equalsIgnoreCase(value2)) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfNotIPv4Address(String value, String msg) throws InvalidParameterException {
		// 1.1.1.1
		if (value == null || value.length() < 7) {
			throw new InvalidParameterException(msg);
		}

		String[] ipAs = value.split("\\.");

		if (ipAs.length != 4) {
			throw new InvalidParameterException(msg);
		}

		for (String ipA : ipAs) {
			int v = 0;
			try {
				v = Integer.parseInt(ipA);
			} catch (Exception e) {
				throw new InvalidParameterException(msg);
			}

			if (v > 255 || v < 0) {
				throw new InvalidParameterException(msg);
			}
		}
	}

	public static void invalidIfNotIPv4AddressOrSubNetworkMask(String value, String msg) throws InvalidParameterException {
		// 1.1.1.1
		if (value == null || value.length() < 7) {
			throw new InvalidParameterException(msg);
		}

		String[] ipAs = value.split("\\.");

		if (ipAs.length != 4) {
			throw new InvalidParameterException(msg);
		}

		for (int i = 0; i < 3; i++) {
			String ipA = ipAs[i];
			int v = 0;
			try {
				v = Integer.parseInt(ipA);
			} catch (Exception e) {
				throw new InvalidParameterException(msg);
			}

			if (v > 255 || v < 0) {
				throw new InvalidParameterException(msg);
			}
		}

		String ipA = ipAs[3];
		int submaski = ipA.indexOf('/');
		if (submaski != -1) {
			String lastIPA = ipA;
			ipA = lastIPA.substring(0, submaski);

			String mask = lastIPA.substring(submaski + 1);
			int v = 0;
			try {
				v = Integer.parseInt(mask);
			} catch (Exception e) {
				throw new InvalidParameterException(msg);
			}
			if (v > 32 || v < 1) {
				throw new InvalidParameterException(msg);
			}
		}

		int v = Integer.parseInt(ipA);
		if (v > 255 || v < 0) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfPasswordStrengthLessThan(String password, final int levelThreshold, String msg) throws InvalidParameterException {
		int level = StringUtils.checkPasswordStrength(password);
		if (level < levelThreshold) {
			throw new InvalidParameterException(msg);
		}
	}

	public static void invalidIfPasswordTooSimple(String password, String msg) throws InvalidParameterException {
		invalidIfPasswordStrengthLessThan(password, 5, msg);
	}

	public static void invalidIfFolderNotExist(String path, String msg) throws InvalidParameterException {
		if (path == null || path.length() == 0) {
			throw new InvalidParameterException(msg);
		}

		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			throw new InvalidParameterException(msg);
		}
	}
	
//	public static void main(String[] a) {
//		String[] ips = new String[] { "1.1.1./1", "1.1.1.1", "192.168.1.1", "0.0.0.0", "255.255.255.255", "256.1.1.1", "1.256.1.1", "1.1.256.1", "1.1.1.256", "-1.1.1.1", "1.-1.1.1", "1.1.-1.1",
//				"1.1.1.-1", "1.1.1.1/1", "1.1.1.251/32", "1.1.1.251/33", "1.1.1.251/-1", "", "q.1.1.1", "1.1.1", "1.2.3.q", "1.", "1.2.", "1.2.3.4.5", "1.2.3.4\\21", "1.2.3.4/q", "1.2.3./q", };
//
//		for (String ip : ips) {
//			try {
//				invalidIfNotIPv4Address(ip, ip);
//				// errorIfNotIPv4AddressOrSubNetworkMask(ip, ip);
//				System.out.println("valid ip " + ip);
//			} catch (InvalidParameterException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	public static void main(String[] args) throws IOException {
//		for (NetworkInterface ni : java.util.Collections.list(NetworkInterface.getNetworkInterfaces())) {
//			System.out.println(ni.getName() + "  " + ni.isUp() + "  " + ni.supportsMulticast());
//		}
//
//		String host = "192.168.0.118"; // Windows 的 IP
//		int port = 3501; // 或你的端口
//
//		Socket s = new Socket();
//		System.out.println("=== Before connect ===");
//		for (NetworkInterface ni : java.util.Collections.list(NetworkInterface.getNetworkInterfaces())) {
//			System.out.println(ni.getName() + "  " + ni.isUp() + "  " + ni.supportsMulticast());
//			for (InetAddress ia : java.util.Collections.list(ni.getInetAddresses())) {
//				System.out.println("   " + ia.getHostAddress());
//			}
//		}
//
//		s.connect(new InetSocketAddress(host, port), 3000);
//
//		System.out.println("\n=== After connect ===");
//		System.out.println("Local Address: " + s.getLocalAddress().getHostAddress());
//		System.out.println("Interface: " + NetworkInterface.getByInetAddress(s.getLocalAddress()));
//	}
}
