package com.amituofo.common.kit.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.define.DatetimeFormat;
import com.amituofo.common.kit.kv.KeyValue;
import com.amituofo.common.util.FormatUtils;

public class OSInfo implements Serializable {
	private String osName;
	private String osVersion;
	private String osArch;
	private String osPatchLevel;

	private String sysFileEncoding;
	private String sysFileSeparator;
	private String sysLineSeparator;

	private String sysTimeZoneId;
	private String sysTimeZoneDisplayName;

	private long sysCurrentTime;
//	private long sysStartTime;
//	private long sysUpTime;

	private String sysUser;
	private String sysUserHome;
	private String sysUserLanguage;

	private String sysHostname;

	private String vmName;
	private String vmVersion;
	private long vmStartTime;

	private double cpuLoad;
	private int processors;

	private long totalPhysicalMemory;
	private long freeMemory;
	private long availableMemory;

	private List<DiskInfo> diskInfos;
	private List<NetworkAdapterInfo> networkAdapterInfos;

//	private List<String> installed

	private Map<String, String> customInfo = new HashMap<>();

	public static class NetworkAdapterInfo implements Serializable {
		private String name;
		private boolean isUp;
		private boolean isVirtual;
		private String[] ips;

		public String getName() {
			return name;
		}

		public void setName(String displayName) {
			this.name = displayName;
		}

		public boolean isUp() {
			return isUp;
		}

		public void setUp(boolean isUp) {
			this.isUp = isUp;
		}

		public boolean isVirtual() {
			return isVirtual;
		}

		public void setVirtual(boolean isVirtual) {
			this.isVirtual = isVirtual;
		}

		public String[] getIps() {
			return ips;
		}

		public void setIps(String[] ips) {
			this.ips = ips;
		}

//		@Override
		public String toString(String head) {
			StringBuffer buf = new StringBuffer();
			buf.append(head + "Name       ").append(name).append("\n");
			buf.append(head + "Up         ").append(isUp).append("\n");
			buf.append(head + "Virtual    ").append(isVirtual).append("\n");
			buf.append(head + "IP Address ").append(Arrays.toString(ips));
			return buf.toString();
		}
		
		public List<KeyValue> toKeyValue(String head) {
			List<KeyValue> kvlist = new ArrayList<KeyValue>();
			kvlist.add(new KeyValue(head + "Name       ", name));
			kvlist.add(new KeyValue(head + "Up         ", isUp));
			kvlist.add(new KeyValue(head + "Virtual    ", isVirtual));
			kvlist.add(new KeyValue(head + "IP Address ", Arrays.toString(ips)));
			return kvlist;
		}
	}

	public static class DiskInfo implements Serializable {
		private String name;
		private long totalSpace;
		private long freeSpace;
		private long usableSpace;

		public DiskInfo(String name, long totalSpace, long freeSpace, long usableSpace) {
			super();
			this.name = name;
			this.totalSpace = totalSpace;
			this.freeSpace = freeSpace;
			this.usableSpace = usableSpace;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getTotalSpace() {
			return totalSpace;
		}

		public void setTotalSpace(long totalSpace) {
			this.totalSpace = totalSpace;
		}

		public long getFreeSpace() {
			return freeSpace;
		}

		public void setFreeSpace(long freeSpace) {
			this.freeSpace = freeSpace;
		}

		public long getUsableSpace() {
			return usableSpace;
		}

		public void setUsableSpace(long usableSpace) {
			this.usableSpace = usableSpace;
		}

//		@Override
		public String toString(String head) {
			StringBuffer buf = new StringBuffer();
			buf.append(head + "Name         ").append(name).append("\n");
			buf.append(head + "Total Space  ").append(FormatUtils.getPrintSize(totalSpace, true)).append("\n");
			buf.append(head + "Free  Space  ").append(FormatUtils.getPrintSize(freeSpace, true)).append("\n");
			buf.append(head + "Usable Space ").append(FormatUtils.getPrintSize(usableSpace, true));
			return buf.toString();
		}

		public List<KeyValue> toKeyValue(String head) {
			List<KeyValue> kvlist = new ArrayList<KeyValue>();

			kvlist.add(new KeyValue(head + "Name         ", name));
			kvlist.add(new KeyValue(head + "Total Space  ", FormatUtils.getPrintSize(totalSpace, true)));
			kvlist.add(new KeyValue(head + "Free  Space  ", FormatUtils.getPrintSize(freeSpace, true)));
			kvlist.add(new KeyValue(head + "Usable Space ", FormatUtils.getPrintSize(usableSpace, true)));
			return kvlist;
		}
	}

	public OSInfo() {
	}

	public Map<String, String> getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(Map<String, String> customInfo) {
		this.customInfo = customInfo;
	}

	public boolean isLinux() {
		if (osName == null) {
			return false;
		}
		return (osName.toLowerCase().indexOf("linux") >= 0);
	}

	public boolean isWindows() {
		if (osName == null) {
			return false;
		}
		return (osName.toLowerCase().indexOf("windows") >= 0);
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getOsPatchLevel() {
		return osPatchLevel;
	}

	public void setOsPatchLevel(String osPatchLevel) {
		this.osPatchLevel = osPatchLevel;
	}

	public String getSysFileEncoding() {
		return sysFileEncoding;
	}

	public void setSysFileEncoding(String sysFileEncoding) {
		this.sysFileEncoding = sysFileEncoding;
	}

	public String getSysFileSeparator() {
		return sysFileSeparator;
	}

	public void setSysFileSeparator(String sysFileSeparator) {
		this.sysFileSeparator = sysFileSeparator;
	}

	public String getSysLineSeparator() {
		return sysLineSeparator;
	}

	public void setSysLineSeparator(String sysLineSeparator) {
		this.sysLineSeparator = sysLineSeparator;
	}

	public String getSysTimeZoneId() {
		return sysTimeZoneId;
	}

	public void setSysTimeZoneId(String sysTimeZoneId) {
		this.sysTimeZoneId = sysTimeZoneId;
	}

	public String getSysTimeZoneDisplayName() {
		return sysTimeZoneDisplayName;
	}

	public void setSysTimeZoneDisplayName(String sysTimeZoneDisplayName) {
		this.sysTimeZoneDisplayName = sysTimeZoneDisplayName;
	}

	public long getSysCurrentTime() {
		return sysCurrentTime;
	}

	public void setSysCurrentTime(long sysCurrentTime) {
		this.sysCurrentTime = sysCurrentTime;
	}

//	public long getSysStartTime() {
//		return sysStartTime;
//	}
//
//	public void setSysStartTime(long sysStartTime) {
//		this.sysStartTime = sysStartTime;
//	}
//
//	public long getSysUpTime() {
//		return sysUpTime;
//	}
//
//	public void setSysUpTime(long sysUpTime) {
//		this.sysUpTime = sysUpTime;
//	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public String getSysUser() {
		return sysUser;
	}

	public void setSysUser(String sysUser) {
		this.sysUser = sysUser;
	}

	public String getSysUserHome() {
		return sysUserHome;
	}

	public void setSysUserHome(String sysUserHome) {
		this.sysUserHome = sysUserHome;
	}

	public String getSysUserLanguage() {
		return sysUserLanguage;
	}

	public void setSysUserLanguage(String sysUserLanguage) {
		this.sysUserLanguage = sysUserLanguage;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String javaName) {
		this.vmName = javaName;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String javaVersion) {
		this.vmVersion = javaVersion;
	}

	public long getVmStartTime() {
		return vmStartTime;
	}

	public void setVmStartTime(long javaStartTime) {
		vmStartTime = javaStartTime;
	}

	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	public int getProcessors() {
		return processors;
	}

	public void setProcessors(int processors) {
		this.processors = processors;
	}

	public long getTotalPhysicalMemory() {
		return totalPhysicalMemory;
	}

	public void setTotalPhysicalMemory(long totalPhysicalMemory) {
		this.totalPhysicalMemory = totalPhysicalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getAvailableMemory() {
		return availableMemory;
	}

	public void setAvailableMemory(long availableMemory) {
		this.availableMemory = availableMemory;
	}

	public List<DiskInfo> getDiskInfos() {
		return diskInfos;
	}

	public void setDiskInfos(List<DiskInfo> disks) {
		this.diskInfos = disks;
	}

	public String getSysHostname() {
		return sysHostname;
	}

	public void setSysHostname(String sysHostname) {
		this.sysHostname = sysHostname;
	}

	public List<NetworkAdapterInfo> getNetworkAdapterInfos() {
		return networkAdapterInfos;
	}

	public void setNetworkAdapterInfos(List<NetworkAdapterInfo> networkAdapterInfos) {
		this.networkAdapterInfos = networkAdapterInfos;
	}

	public void addDiskInfo(DiskInfo diskInfo) {
		if (diskInfos == null) {
			diskInfos = new ArrayList<>();
		}

		diskInfos.add(diskInfo);
	}

	public void addNetworkAdapterInfo(NetworkAdapterInfo info) {
		if (networkAdapterInfos == null) {
			networkAdapterInfos = new ArrayList<>();
		}

		networkAdapterInfos.add(info);
	}

	public List<KeyValue> toKeyValueList() {
		List<KeyValue> kvlist = new ArrayList<KeyValue>();

		kvlist.add(new KeyValue("OS Name:", osName));
		kvlist.add(new KeyValue("OS Version:", osVersion));
		kvlist.add(new KeyValue("OS Arch:", osArch));
		kvlist.add(new KeyValue("OS Patch Level:", osPatchLevel));
		kvlist.add(new KeyValue("System File Encoding:", sysFileEncoding));
		kvlist.add(new KeyValue("System File Separator:", sysFileSeparator));
//		kvlist.add(new KeyValue("System Line Separator:", "\n".equals(sysLineSeparator)?"\\n":sysLineSeparator));
		kvlist.add(new KeyValue("System Time Zone Id:", sysTimeZoneId));
		kvlist.add(new KeyValue("System Time Zone Name:", sysTimeZoneDisplayName));
		kvlist.add(new KeyValue("System Current Time:", DatetimeFormat.YYYY_MM_DD_HHMMSS.format(sysCurrentTime)));
		kvlist.add(new KeyValue("System User:", sysUser));
		kvlist.add(new KeyValue("System User Home:", sysUserHome));
		kvlist.add(new KeyValue("System User Language:", sysUserLanguage));
		kvlist.add(new KeyValue("System Hostname:", sysHostname));
		kvlist.add(new KeyValue("VM Name:", vmName));
		kvlist.add(new KeyValue("VM Version:", vmVersion));
		kvlist.add(new KeyValue("VM Start Time:", DatetimeFormat.YYYY_MM_DD_HHMMSS.format(vmStartTime)));
		kvlist.add(new KeyValue("Hardware Cpu Load:", cpuLoad));
		kvlist.add(new KeyValue("Hardware Processors:", processors));
		kvlist.add(new KeyValue("Hardware Total Physical Memory:", FormatUtils.getPrintSize(totalPhysicalMemory, true)));
		kvlist.add(new KeyValue("Hardware Free  Physical Memory:", FormatUtils.getPrintSize(freeMemory, true)));
		kvlist.add(new KeyValue("Hardware Disks:"));
		if (diskInfos != null) {
			for (DiskInfo info : diskInfos) {
				kvlist.addAll(info.toKeyValue("     "));
			}
		}

		kvlist.add(new KeyValue("Hardware Network Adapters:"));
		if (networkAdapterInfos != null) {
			for (NetworkAdapterInfo info : networkAdapterInfos) {
				kvlist.addAll(info.toKeyValue("     "));
			}
		}

		kvlist.add(new KeyValue("Other Info:", customInfo));

		return kvlist;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		List<KeyValue> list = toKeyValueList();
		for (KeyValue keyValue : list) {
			buf.append(keyValue.getKey()).append(keyValue.getStringValue()).append("\n");
		}

//		buf.append("OS Name:").append(osName).append("\n");
//		buf.append("OS Version:").append(osVersion).append("\n");
//		buf.append("OS Arch:").append(osArch).append("\n");
//		buf.append("OS Patch Level:").append(osPatchLevel).append("\n");
//		buf.append("System File Encoding:").append(sysFileEncoding).append("\n");
//		buf.append("System File Separator:").append(sysFileSeparator).append("\n");
////		buf.append("System Line Separator:").append("\n".equals(sysLineSeparator)?"\\n":sysLineSeparator).append("\n");
//		buf.append("System Time Zone Id:").append(sysTimeZoneId).append("\n");
//		buf.append("System Time Zone Name:").append(sysTimeZoneDisplayName).append("\n");
//		buf.append("System Current Time:").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(sysCurrentTime)).append("\n");
//		buf.append("System User:").append(sysUser).append("\n");
//		buf.append("System User Home:").append(sysUserHome).append("\n");
//		buf.append("System User Language:").append(sysUserLanguage).append("\n");
//		buf.append("System Hostname:").append(sysHostname).append("\n");
//		buf.append("VM Name:").append(vmName).append("\n");
//		buf.append("VM Version:").append(vmVersion).append("\n");
//		buf.append("VM Start Time:").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(vmStartTime)).append("\n");
//		buf.append("Hardware Cpu Load:").append(cpuLoad).append("\n");
//		buf.append("Hardware Processors:").append(processors).append("\n");
//		buf.append("Hardware Total Physical Memory:").append(FormatUtils.getPrintSize(totalPhysicalMemory, true)).append("\n");
//		buf.append("Hardware Free  Physical Memory:").append(FormatUtils.getPrintSize(freeMemory, true)).append("\n");
//		buf.append("Hardware Disks:").append("\n");
//		if (diskInfos != null) {
//			for (DiskInfo info : diskInfos) {
//				buf.append(info.toString("    ")).append("\n");
//			}
//		}
//
//		buf.append("Hardware Network Adapters:").append("\n");
//		if (networkAdapterInfos != null) {
//			for (NetworkAdapterInfo info : networkAdapterInfos) {
//				buf.append(info.toString("    ")).append("\n");
//			}
//		}
//
//		buf.append("Other Info:").append(customInfo);

//		buf.append("OS Name							").append(osName).append("\n");
//		buf.append("OS Version						").append(osVersion).append("\n");
//		buf.append("OS Arch							").append(osArch).append("\n");
//		buf.append("OS Patch Level					").append(osPatchLevel).append("\n");
//		buf.append("System File Encoding			").append(sysFileEncoding).append("\n");
//		buf.append("System File Separator			").append(sysFileSeparator).append("\n");
////		buf.append("System Line Separator			").append("\n".equals(sysLineSeparator)?"\\n":sysLineSeparator).append("\n");
//		buf.append("System Time Zone Id				").append(sysTimeZoneId).append("\n");
//		buf.append("System Time Zone Name			").append(sysTimeZoneDisplayName).append("\n");
//		buf.append("System Current Time				").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(sysCurrentTime)).append("\n");
//		buf.append("System User						").append(sysUser).append("\n");
//		buf.append("System User Home				").append(sysUserHome).append("\n");
//		buf.append("System User Language			").append(sysUserLanguage).append("\n");
//		buf.append("System Hostname					").append(sysHostname).append("\n");
//		buf.append("VM Name							").append(vmName).append("\n");
//		buf.append("VM Version						").append(vmVersion).append("\n");
//		buf.append("VM Start Time					").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(vmStartTime)).append("\n");
//		buf.append("Hardware CpuLoad				").append(cpuLoad).append("\n");
//		buf.append("Hardware Processors				").append(processors).append("\n");
//		buf.append("Hardware Total Physical Memory	").append(FormatUtils.getPrintSize(totalPhysicalMemory, true)).append("\n");
//		buf.append("Hardware Free  Physical Memory	").append(FormatUtils.getPrintSize(freePhysicalMemory, true)).append("\n");
//		buf.append("Hardware Disks					").append("\n");
//		if (diskInfos != null) {
//			for (DiskInfo info : diskInfos) {
//				buf.append(info.toString("									")).append("\n");
//			}
//		}
//
//		buf.append("Hardware Network Adapters		").append("\n");
//		if (networkAdapterInfos != null) {
//			for (NetworkAdapterInfo info : networkAdapterInfos) {
//				buf.append(info.toString("									")).append("\n");
//			}
//		}
//
//		buf.append("Other Info						").append(customInfo);

//		buf.append("OS Name                         ").append(osName).append("\n");
//		buf.append("OS Version                      ").append(osVersion).append("\n");
//		buf.append("OS Arch                         ").append(osArch).append("\n");
//		buf.append("OS Patch Level                  ").append(osPatchLevel).append("\n");
//		buf.append("System File Encoding            ").append(sysFileEncoding).append("\n");
//		buf.append("System File Separator           ").append(sysFileSeparator).append("\n");
////		buf.append("System Line Separator           ").append("\n".equals(sysLineSeparator)?"\\n":sysLineSeparator).append("\n");
//		buf.append("System Time Zone Id             ").append(sysTimeZoneId).append("\n");
//		buf.append("System Time Zone Name           ").append(sysTimeZoneDisplayName).append("\n");
//		buf.append("System Current Time             ").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(sysCurrentTime)).append("\n");
//		buf.append("System User                     ").append(sysUser).append("\n");
//		buf.append("System User Home                ").append(sysUserHome).append("\n");
//		buf.append("System User Language            ").append(sysUserLanguage).append("\n");
//		buf.append("System Hostname                 ").append(sysHostname).append("\n");
//		buf.append("VM Name                         ").append(vmName).append("\n");
//		buf.append("VM Version                      ").append(vmVersion).append("\n");
//		buf.append("VM Start Time                   ").append(DatetimeFormat.YYYY_MM_DD_HHMMSS.format(vmStartTime)).append("\n");
//		buf.append("Hardware CpuLoad                ").append(cpuLoad).append("\n");
//		buf.append("Hardware Processors             ").append(processors).append("\n");
//		buf.append("Hardware Total Physical Memory  ").append(FormatUtils.getPrintSize(totalPhysicalMemory, true)).append("\n");
//		buf.append("Hardware Free  Physical Memory  ").append(FormatUtils.getPrintSize(freePhysicalMemory, true)).append("\n");
//		buf.append("Hardware Disks                  ").append("\n");
//		if (diskInfos != null) {
//			for (DiskInfo info : diskInfos) {
//				buf.append(info.toString("                                ")).append("\n");
//			}
//		}
//
//		buf.append("Hardware Network Adapters       ").append("\n");
//		if (networkAdapterInfos != null) {
//			for (NetworkAdapterInfo info : networkAdapterInfos) {
//				buf.append(info.toString("                                ")).append("\n");
//			}
//		}
//
//		buf.append("Other Info                      ").append(customInfo);

//		if (c != ' ') {
//			String str = buf.toString();
//			str = str.replace("  ", c + "" + c);
//			return str;
//		}

		return buf.toString();
	}

}
