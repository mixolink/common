package com.amituofo.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class InetAddressUtil {
//	public static void main(String[] args) throws Exception {
////		System.out.println(getInnetIp());
//		System.out.println(StringUtils.toString(getLocalIPAddresses(), ','));
//		System.out.println(getHostname());
//		System.out.println(genIPAddresses("192.168.0.22", "192.168.0.90"));
//	}

	public static List<String> genIPAddresses(String fromIP, String toIP) {
		return genIPAddresses(fromIP, toIP, new String[0]);
	}

//	public static List<String> genIPAddresses(String fromIP, String toIP, List<String> exceptList) {
//		List<String> iplist = new ArrayList<>();
//
//		if (!isIPAddresses(fromIP) || !isIPAddresses(toIP)) {
//			return iplist;
//		}
//
//		int iA = Integer.parseInt(fromIP.substring(fromIP.lastIndexOf('.') + 1));
//		int iB = Integer.parseInt(toIP.substring(toIP.lastIndexOf('.') + 1));
//
//		int iAA = Math.min(iA, iB);
//		int iBB = Math.max(iA, iB);
//
//		String ipprefix = fromIP.substring(0, fromIP.lastIndexOf('.') + 1);
//		for (int i = iAA; i <= iBB; i++) {
//			iplist.add(ipprefix + i);
//		}
//
//		if (exceptList != null && exceptList.size() > 0) {
//			List<String> toberemoveiplist = new ArrayList<>();
//			for (String ip : iplist) {
//				for (String expip : exceptList) {
//					if (StringUtils.wildcardMatch(expip, ip)) {
//						toberemoveiplist.add(ip);
//						break;
//					}
//				}
//			}
//
//			if (toberemoveiplist.size() > 0) {
//				for (String string : toberemoveiplist) {
//					iplist.remove(string);
//				}
//			}
//		}
//
//		return iplist;
//	}

	public static List<String> genIPAddresses(String fromIP, String toIP, String[] exceptList) {
		List<String> iplist = new ArrayList<>();

		if (!isIPAddresses(fromIP) || !isIPAddresses(toIP)) {
			return iplist;
		}

		int iA = Integer.parseInt(fromIP.substring(fromIP.lastIndexOf('.') + 1));
		int iB = Integer.parseInt(toIP.substring(toIP.lastIndexOf('.') + 1));

		int iAA = Math.min(iA, iB);
		int iBB = Math.max(iA, iB);

		String ipprefix = fromIP.substring(0, fromIP.lastIndexOf('.') + 1);
		for (int i = iAA; i <= iBB; i++) {
			iplist.add(ipprefix + i);
		}

		if (exceptList != null && exceptList.length > 0) {
			List<String> toberemoveiplist = new ArrayList<>();
			for (String ip : iplist) {
				for (String expip : exceptList) {
					if (StringUtils.wildcardMatch(expip, ip)) {
						toberemoveiplist.add(ip);
						break;
					}
				}
			}

			if (toberemoveiplist.size() > 0) {
				for (String string : toberemoveiplist) {
					iplist.remove(string);
				}
			}
		}

		return iplist;
	}

	public static boolean isIPAddresses(String IP) {
		if (StringUtils.isEmpty(IP)) {
			return false;
		}

		String[] is = IP.split("\\.");
		if (is.length != 4) {
			return false;
		}

		for (String string : is) {
			try {
				int i = Integer.parseInt(string);
				if (i > 254 || i < 0) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 获取本机的内网ip地址
	 *
	 * @return
	 * @throws SocketException
	 */
	public static String[] getLocalIPAddresses() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		boolean finded = false;// 是否找到外网IP
		List<String> iplist = new ArrayList<>();

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				InetAddress ip = address.nextElement();
				if (!ip.getHostAddress().contains(":") && !ip.isLoopbackAddress()) {
//					System.out.println("ip=" + ip.getHostAddress() + " isSiteLocalAddress=" + ip.isSiteLocalAddress());
					if (!ip.isSiteLocalAddress()) {// 外网IP
						localip = netip = ip.getHostAddress();
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress()) {// 内网IP
						localip = ip.getHostAddress();
					}

					iplist.add(localip);
				}
			}
		}
		if (netip != null) {
			return new String[] { netip };
		} else {
			return ArrayUtils.asStringArray(iplist);
		}
	}

	public static String[] getAllIPAddresses() throws SocketException {
		boolean finded = false;// 是否找到外网IP
		List<String> iplist = new ArrayList<>();

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				InetAddress ip = address.nextElement();
				if(ip.isSiteLocalAddress()) {
					iplist.add(ip.getHostAddress());
				}
			}

		}
		return ArrayUtils.asStringArray(iplist);
	}

	public static String getHostname() {
		try {
			final InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (final UnknownHostException uhe) {
			try {
				final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					final NetworkInterface nic = interfaces.nextElement();
					final Enumeration<InetAddress> addresses = nic.getInetAddresses();
					while (addresses.hasMoreElements()) {
						final InetAddress address = addresses.nextElement();
						if (!address.isLoopbackAddress()) {
							final String hostname = address.getHostName();
							if (hostname != null) {
								return hostname;
							}
						}
					}
				}
			} catch (final SocketException se) {
				return null;
			}
			return null;
		}
	}

	public static boolean isIPV4Address(String host) {
		return StringUtils.countOf(host, '.') == 3;
	}
}
