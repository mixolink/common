package com.amituofo.common.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class SystemFingerprint {

	public static Map<String, String> getStableSystemInfo() {
		Map<String, String> info = new LinkedHashMap<>();

		// 操作系统信息
		info.put("os.name", safeGet("os.name"));
		info.put("os.version", safeGet("os.version"));
		info.put("os.arch", safeGet("os.arch"));

		// 用户信息
		info.put("user.name", safeGet("user.name"));
		info.put("user.home", safeGet("user.home"));

		// 主机信息
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			info.put("hostname", localHost.getHostName());
			info.put("host.address", localHost.getHostAddress());
		} catch (UnknownHostException e) {
			info.put("hostname", "unknown");
			info.put("host.address", "unknown");
		}

		// MAC 地址（取第一个有效非回环）
		info.put("mac.address", getFirstMacAddress());

		// Java 信息
		info.put("java.version", safeGet("java.version"));
		info.put("java.vendor", safeGet("java.vendor"));

		// 路径信息
//		info.put("file.separator", File.separator);
//		info.put("path.separator", File.pathSeparator);

		return info;
	}

	public static String safeGet(String key) {
		return System.getProperty(key, "unknown");
	}

	public static String getFirstMacAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface nic = interfaces.nextElement();
				if (nic.isLoopback() || nic.isVirtual() || !nic.isUp()) {
					continue;
				}
				byte[] mac = nic.getHardwareAddress();
				if (mac != null && mac.length == 6) {
					StringBuilder sb = new StringBuilder();
					for (byte b : mac) {
						sb.append(String.format("%02X", b));
					}
					return sb.toString();
				}
			}
		} catch (Exception ignored) {
		}
		return "unknown";
	}

	public static String getSystemInfoAsString() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> info = getStableSystemInfo();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			sb.append(entry.getValue()).append("|");
		}
		return sb.toString().trim(); // 去掉末尾换行
	}

	// 打印示例
	public static void main(String[] args) {
		System.out.printf(getSystemInfoAsString());
	}
}