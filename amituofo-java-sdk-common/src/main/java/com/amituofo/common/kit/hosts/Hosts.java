package com.amituofo.common.kit.hosts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amituofo.common.util.FileUtils;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;

public class Hosts {
	private static Hosts instance = null;

	private boolean loaded = false;
	private File hostfile;
	private Map<String, Set<String>> ip2hostMap = new LinkedHashMap<>();
	private Map<String, String> host2ipMap = new LinkedHashMap<>();
	private List<String> commentLines = new ArrayList<>();

	private final static String[] COMMENTS = new String[] { "# Commects:", "# Customized:" };
	private final static String[] ALLOW_DUPLICATE_DOMAINS = new String[] { "localhost", "localhost.localdomain", "localhost4", "localhost4.localdomain4",
			"localhost6", "localhost6.localdomain6" };
	private final static String[] LOCALHOST_DOMAINS = ALLOW_DUPLICATE_DOMAINS;
	private final static String[] LOCALHOST_IPS = new String[] { "127.0.0.1", "::1", "0.0.0.0" };

	private final static String SYS_LOCAL = "127.0.0.1	localhost localhost.localdomain localhost4 localhost4.localdomain4\n"
			+ "::1	localhost localhost.localdomain localhost6 localhost6.localdomain6\n\n";

	private Hosts() {
	}

	public static Hosts os() {
		if (instance == null) {
			instance = new Hosts();

			instance.reset();

			try {
				instance.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	public void reset() {
		ip2hostMap.clear();
		host2ipMap.clear();
		commentLines.clear();

//		put(false, "127.0.0.1", "localhost localhost.localdomain localhost4 localhost4.localdomain4");
//		put(false, "::1", "localhost localhost.localdomain localhost6 localhost6.localdomain6");
	}

	public boolean contains(String ip, String domain) {
		String ipadd = host2ipMap.get(domain);
		if (ipadd == null) {
			return false;
		}
		if (!ipadd.equals(ip)) {
			return false;
		}

		Set<String> hostnameSet = ip2hostMap.get(ip);
		if (hostnameSet == null) {
			return false;
		}
		return hostnameSet.contains(domain);
	}

	public synchronized void removeDomain(String domain) {
		if (StringUtils.isEmpty(domain)) {
			return;
		}

		domain = domain.trim();

		Collection<Set<String>> values = ip2hostMap.values();
		for (Set<String> set : values) {
			set.remove(domain);
		}
		
		host2ipMap.remove(domain);
	}

	public synchronized void put(String ip, String... domain) {
		put(true, ip, domain);
	}

	public synchronized void put(String ip, String domain) {
		put(true, ip, domain);
	}

	public synchronized void put(boolean wipeDuplicateDomain, String ip, String... domains) {
		if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(domains)) {
			return;
		}

		for (String domain : domains) {
			put(wipeDuplicateDomain, ip, domain);
		}
	}

	public synchronized void put(boolean wipeDuplicateDomain, String ip, String domain) {
		if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(domain)) {
			return;
		}

//		if (!StringUtils.isIPAddress(ip)) {
//			return;
//		}

		ip = ip.trim();
		domain = domain.trim();

		Set<String> hostnameSet = ip2hostMap.get(ip);
		if (hostnameSet == null) {
			hostnameSet = new LinkedHashSet<>();
			ip2hostMap.put(ip, hostnameSet);
		}

		String[] hostnames = domain.split(" ");
		for (String hostname : hostnames) {
			hostname = hostname.trim();
			if (hostname.length() == 0) {
				continue;
			}

			host2ipMap.put(hostname, ip);

			if (wipeDuplicateDomain) {
				if (!StringUtils.contains(ALLOW_DUPLICATE_DOMAINS, hostname)) {
					for (Set<String> set : ip2hostMap.values()) {
						set.remove(hostname);
					}
				}
			}

			hostnameSet.add(hostname);
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		Iterator<String> it = ip2hostMap.keySet().iterator();
		while (it.hasNext()) {
			String ip = it.next();

			int customizedCnt = 0;
			StringBuffer bufCustomized = new StringBuffer();
			bufCustomized.append(ip).append("\t");

			Set<String> hostnames = ip2hostMap.get(ip);

			for (String hostname : hostnames) {
				if (!isLocalhost(hostname)) {
					bufCustomized.append(hostname).append(" ");
					customizedCnt++;
				}
			}

			if (customizedCnt > 0) {
				buf.append(bufCustomized).append("\n");
			}
		}

		buf.append("\n");
		if (commentLines.size() > 0) {
			buf.append("# Commects:\n");
			for (String comment : commentLines) {
				buf.append(comment).append("\n");
			}
		}

		String str = SYS_LOCAL + "# Customized:\n" + buf.toString();

		return str;
	}

	private void load() throws IOException {
		String hostfilepath = SystemUtils.getOSHostsPath();

		hostfile = new File(hostfilepath);
		String content = null;
		try {
			content = FileUtils.readToString(hostfile);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}

		String[] lines = content.split("\n");
		for (String line : lines) {
			line = line.trim();

			if (line.startsWith("#")) {
				if (!StringUtils.contains(COMMENTS, line)) {
					commentLines.add(line);
				}
				continue;
			}

			int i1 = line.indexOf(' ');
			int i2 = line.indexOf('\t');
			int i = Math.min(i1, i2);
			if (i < 3) {
				i = Math.max(i2, i1);
			}

			if (i > 2) {
				String ip = line.substring(0, i).trim();
				String hostname = line.substring(i + 1).trim();

				put(ip, hostname);
//				System.out.println("["+hostname + "]\t[" + ip+"]");
			}
		}

//		127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
//		::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
//		172.16.1.231 N01
//		172.16.1.232 N02
//		172.16.1.233 N03
//		172.16.1.234 N04
//		172.16.1.231 oeos1.com
//		172.16.1.232 oeos2.com
//		172.16.1.233 oeos3.com
//		172.16.1.234 oeos4.com
//		172.16.1.233 oeos.com

		loaded = true;
	}

	public boolean flush() throws IOException {
		if (!loaded) {
			return false;
		}

//		System.out.println("---------");
//		System.out.println(ip2hostMap);
//		System.out.println("---------");
//		System.out.println(host2ipMap);
//		System.out.println("---------");
//		System.out.println(this.toString());
//		System.out.println("---------");
//		System.out.println(hostfile);

		FileUtils.writeToFile(this.toString(), hostfile, false);

		return true;
	}

	private boolean isLocalhostIP(String ip) {
		return StringUtils.contains(LOCALHOST_IPS, ip);
	}

	private boolean isLocalhost(String domain) {
		return StringUtils.contains(LOCALHOST_DOMAINS, domain);
	}

//	public static void main(String[] arg) throws IOException {
//		Hosts.os().put("1.1.1.1", "zzzzzzzzzzzzzzzz internalstb1.datacenter.internal dtspace1.datacenter.internal");
//		Hosts.os().put("2.2.2.2", "xxxxxxxxx");
//		System.out.println(Hosts.os());
//	}

}
