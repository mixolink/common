package com.amituofo.common.util;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.define.Constants;
import com.amituofo.common.kit.info.OSInfo;
import com.amituofo.common.kit.info.OSInfo.DiskInfo;
import com.amituofo.common.kit.info.OSInfo.NetworkAdapterInfo;

public class SystemUtils {
	private static final char[] LINUX_INVALID_FILENAME_CHARS = "/\\?%*:|\"<>\n\r\t".toCharArray();

	private static String OS = System.getProperty("os.name").toLowerCase();
	private static boolean isLinux = (OS.indexOf("linux") >= 0 || OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	private static boolean isMacOS = (OS.indexOf("mac") >= 0);
//	private static boolean isMacOSX = (OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0);
	private static boolean isWindows = (OS.indexOf("windows") >= 0);

	public static boolean isLinux() {
		return isLinux;
	}

	public static boolean isMacOS() {
		return isMacOS;
	}

//	public static boolean isMacOSX() {
//		return isMacOSX;
//	}

	public static boolean isWindows() {
		return isWindows;
	}

	// public static boolean isOS2() {
	// return OS.indexOf("os/2") >= 0;
	// }
	//
	// public static boolean isSolaris() {
	// return OS.indexOf("solaris") >= 0;
	// }
	//
	// public static boolean isSunOS() {
	// return OS.indexOf("sunos") >= 0;
	// }
	//
	// public static boolean isMPEiX() {
	// return OS.indexOf("mpe/ix") >= 0;
	// }
	//
	// public static boolean isHPUX() {
	// return OS.indexOf("hp-ux") >= 0;
	// }
	//
	// public static boolean isAix() {
	// return OS.indexOf("aix") >= 0;
	// }
	//
	// public static boolean isOS390() {
	// return OS.indexOf("os/390") >= 0;
	// }
	//
	// public static boolean isFreeBSD() {
	// return OS.indexOf("freebsd") >= 0;
	// }
	//
	// public static boolean isIrix() {
	// return OS.indexOf("irix") >= 0;
	// }
	//
	// public static boolean isDigitalUnix() {
	// return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
	// }
	//
	// public static boolean isNetWare() {
	// return OS.indexOf("netware") >= 0;
	// }
	//
	// public static boolean isOSF1() {
	// return OS.indexOf("osf1") >= 0;
	// }
	//
	// public static boolean isOpenVMS() {
	// return OS.indexOf("openvms") >= 0;
	// }
	
	public static boolean isMacInDarkMode() {
	    if (!isMacOS) {
	        return false;
	    }

	    try {
	        Process process = new ProcessBuilder(
	            "defaults", "read", "-g", "AppleInterfaceStyle"
	        ).start();

	        process.waitFor();
	        return process.exitValue() == 0;
	    } catch (Exception e) {
	        return false;
	    }
	}

	public static boolean containsFileNameUnsupportChar(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return true;
		}

		for (char c : LINUX_INVALID_FILENAME_CHARS) {
			if (filename.indexOf(c) != -1) {
				return true;
			}
		}
		return false;
	}

	public static String getOSDriver() {
		if (isWindows()) { // 判断是否为Windows操作系统
			return System.getenv("SystemDrive");
		} else { // 其他操作系统
			return "/";
		}
	}

	public static String getOSHostsPath() {
		if (SystemUtils.isWindows()) {
			return SystemUtils.getOSDriver() + "\\Windows\\System32\\drivers\\etc\\hosts";
		} else {
			return "/etc/hosts";
		}
	}

	private static FileLock lock;

	public static boolean isRunning(String key) {
		key = DigestUtils.calcMD5ToHex(key).toLowerCase();
		FileOutputStream fo = null;
		try {
			File instanceLock = null;
			String folder = System.getProperty("java.io.tmpdir");
			if (folder == null || folder.length() == 0) {
				File tmp = File.createTempFile("TMP", "");
				folder = tmp.getParent();
				tmp.delete();
			}

			if (folder != null && folder.length() > 1) {
				if (folder.charAt(folder.length() - 1) != File.separatorChar) {
					folder += File.separatorChar;
				}
			}

			instanceLock = new File(folder + key);
			instanceLock.deleteOnExit();
			if (!instanceLock.exists())
				instanceLock.createNewFile();
			fo = new FileOutputStream(instanceLock);
			lock = fo.getChannel().tryLock();
			// if lock=null means the file has been locked,one instance is running
			return (lock == null);
		} catch (Throwable ex) {
			ex.printStackTrace();
			try {
				fo.close();
			} catch (IOException e) {
			}
			return false;
		}
	}

	public static boolean isRunning(Class<?> dgc) {
		FileOutputStream fo = null;
		String key = "INSTANCE-" + dgc.getSimpleName();
		return isRunning(key);
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean openWebpage(URL url) {
		try {
			return openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean openWebpage(String url) {
		try {
			openWebpage(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Dimension getScreenSize() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
//		int width = (int)screensize.getWidth();
//		int height = (int)screensize.getHeight();
//		//屏幕的物理大小还需要知道屏幕的dpi 意思是说一英寸多少个象素
//		int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		return screensize;
	}

	/**
	 * 把文本设置到剪贴板（复制）
	 */
	public static void setClipboardString(String text) {
//		if (StringUtils.isEmpty(text)) {
//			return;
//		}

		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 封装文本内容
		Transferable trans = new StringSelection(text);
		// 把文本内容设置到系统剪贴板
		clipboard.setContents(trans, null);
	}

	public static void setClipboardString(List<?> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		StringBuffer buf = new StringBuffer();
		int max = list.size();
		if (max > 0) {

			int last = max - 1;
			for (int i = 0; i < last; i++) {
				Object item = list.get(i);
				if (item != null) {
					buf.append(item.toString());
				}
				buf.append("\n");
			}

			Object item = list.get(last);
			if (item != null) {
				buf.append(item.toString());
			}

			SystemUtils.setClipboardString(buf.toString());
		}

		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 封装文本内容
		Transferable trans = new StringSelection(buf.toString());
		// 把文本内容设置到系统剪贴板
		clipboard.setContents(trans, null);
	}

	/**
	 * 从剪贴板中获取文本（粘贴）
	 */
	public static String getClipboardString() {
		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// 获取剪贴板中的内容
		Transferable trans = clipboard.getContents(null);

		if (trans != null) {
			// 判断剪贴板中的内容是否支持文本
			if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					// 获取剪贴板中的文本内容
					String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
					return text;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static boolean isPortInUsed(int port) {
		try {
			return isPortInUsed("127.0.0.1", port);
		} catch (UnknownHostException e) {
		}
		return false;
	}

	public static boolean isPortInUsed(String host, int port) throws UnknownHostException {
		boolean flag = false;
		Socket socket = null;
		InetAddress Address = InetAddress.getByName(host);
		try {
			socket = new Socket(Address, port); // 建立一个Socket连接
			flag = true;
		} catch (IOException e) {

		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static List<String> getLocalIPAddress() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipList;
	}

	public static String getJavaPath() {
		String home = System.getProperty("java.home");
		if (FileUtils.isFileExist(home)) {
			String javapath = URLUtils.catFilePath(System.getProperty("java.home"), "bin" + File.separator + "java");

			if (SystemUtils.isWindows()) {
				javapath += ".exe";
			}
			return javapath;
		}

		return null;
	}

	public static void openFile(String filepath) {
		if (FileUtils.isFileExist(filepath)) {
			try {
				Desktop.getDesktop().open(new File(filepath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean exited = false;

	public static void exit(int code) {
		if (!exited) {
			exited = true;

			System.out.println("SYSTEM EXIT " + code);
			System.exit(code);
		}
	}

	public static boolean shutdown(int sec) throws IOException, InterruptedException {
		if (isLinux()) {
			Process p = Runtime.getRuntime().exec("sudo shutdown -h +" + Math.abs(sec));
			return p.waitFor() == 0;
		}

		if (isWindows()) {
			Process p = Runtime.getRuntime().exec("shutdown -s -t " + Math.abs(sec));
			return p.waitFor() == 0;
		}

		return false;
	}

	public static boolean reboot(int sec) throws IOException, InterruptedException {
		if (isLinux()) {
			Process p = Runtime.getRuntime().exec("sudo shutdown -r +" + Math.abs(sec));
			return p.waitFor() == 0;
		}

		if (isWindows()) {
			Process p = Runtime.getRuntime().exec("shutdown -r -t " + Math.abs(sec));
			return p.waitFor() == 0;
		}

		return false;
	}

	private final static List<Runnable> shutdownHookList1 = new ArrayList<>();
	private final static Stack<Runnable> shutdownHookList2 = new Stack<>();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (Runnable runnable : shutdownHookList1) {
					try {
						runnable.run();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				while (!shutdownHookList2.isEmpty()) {
					Runnable runnable = shutdownHookList2.pop();
					try {
						runnable.run();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void addShutdownHook(Runnable run) {
		if (run != null) {
			shutdownHookList1.add(run);
		}
	}

	public static void addLastShutdownHook(Runnable run) {
		if (run != null) {
			shutdownHookList2.add(run);
		}
	}

//	public static class Mem {
//	    public long total;
//	    public long used;
//	    public long free;
//	    public long shared;
//	    public long buffers;
//	    public long cached;
//
//	    public MEMINFO(long total, long used, long free, long shared, long buffers, long cached) {
//	        this.total = total;
//	        this.used = used;
//	        this.free = free;
//	        this.shared = shared;
//	        this.buffers = buffers;
//	        this.cached = cached;
//	    }
//
//	public static getMem() {
//	     String command = "free";
//         Process process = Runtime.getRuntime().exec(command);
//         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//         
//         // 读取第一行 mem 的信息
//         String line = reader.readLine();
//         String[] arr = line.split("\\s+");
//         long total = Long.parseLong(arr[1]);
//         long used = Long.parseLong(arr[2]);
//         long free = Long.parseLong(arr[3]);
//         long shared = Long.parseLong(arr[4]);
//         long buffers = Long.parseLong(arr[5]);
//         long cached = Long.parseLong(arr[6]);
//
//         reader.close();
//	}

//	public static MemoryInfo readMemoryInfo() {
//	        MemoryInfo memoryInfo = new MemoryInfo();
//
//	        try {
//	            FileReader fileReader = new FileReader("/proc/meminfo");
//	            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//	            String line;
//	            while ((line = bufferedReader.readLine()) != null) {
//	                if (line.startsWith("MemTotal:")) {
//	                    memoryInfo.setMemTotal(Long.parseLong(line.split("\\s+")[1]));
//	                } else if (line.startsWith("MemFree:")) {
//	                    memoryInfo.setMemFree(Long.parseLong(line.split("\\s+")[1]));
//	                } else if (line.startsWith("MemAvailable:")) {
//	                    memoryInfo.setMemAvailable(Long.parseLong(line.split("\\s+")[1]));
//	                }
//	            }
//
//	            bufferedReader.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//
//	        return memoryInfo;
//	    }

	public static OSInfo gatherOSInfo() {
		OSInfo info = new OSInfo();
		// ----------------------------------------------------------------------------------------------------------
		info.setOsName(System.getProperty("os.name"));
		info.setOsVersion(System.getProperty("os.version"));
		info.setOsArch(System.getProperty("os.arch"));
		info.setOsPatchLevel(System.getProperty("sun.os.patch.level"));
		// ----------------------------------------------------------------------------------------------------------
		info.setSysFileEncoding(System.getProperty("file.encoding"));
		info.setSysFileSeparator(System.getProperty("file.separator"));
		info.setSysLineSeparator(System.getProperty("line.separator"));
		// ----------------------------------------------------------------------------------------------------------
		RuntimeMXBean vmbean = ManagementFactory.getRuntimeMXBean();
		info.setVmName(vmbean.getVmName());
		info.setVmVersion(vmbean.getVmVersion());
		info.setVmStartTime(vmbean.getStartTime());
		// ----------------------------------------------------------------------------------------------------------
		info.setSysUser(System.getProperty("user.name"));
		info.setSysUserHome(System.getProperty("user.home"));
		info.setSysUserLanguage(System.getProperty("user.language"));
		// ----------------------------------------------------------------------------------------------------------
		TimeZone systemTimeZone = TimeZone.getDefault();
		String systemTimeZoneId = systemTimeZone.getID();
		String systemTimeZoneDisplayName = systemTimeZone.getDisplayName();
		// ----------------------------------------------------------------------------------------------------------
		info.setSysCurrentTime(System.currentTimeMillis());
		info.setSysTimeZoneId(systemTimeZoneId);
		info.setSysTimeZoneDisplayName(systemTimeZoneDisplayName);
		// ----------------------------------------------------------------------------------------------------------
		File[] roots = File.listRoots();
		for (File root : roots) {
			info.addDiskInfo(new DiskInfo(root.getPath(), root.getTotalSpace(), root.getFreeSpace(), root.getUsableSpace()));
		}
		// ----------------------------------------------------------------------------------------------------------

		if (isLinux()) {
			try {
				FileReader fileReader = new FileReader("/proc/meminfo");
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				int n = 3;
				String line;
				while (n > 0 && (line = bufferedReader.readLine()) != null) {
					line = line.toLowerCase();
					long unit = 1;
					if (line.lastIndexOf("kb") != -1) {
						unit = Constants.SIZE_1KB;
					} else if (line.lastIndexOf("mb") != -1) {
						unit = Constants.SIZE_1MB;
					}

					if (line.indexOf("memtotal:") != -1) {
						n--;
						info.setTotalPhysicalMemory(unit * Long.parseLong(line.split("\\s+")[1]));
					} else if (line.indexOf("memfree:") != -1) {
						n--;
						info.setFreeMemory(unit * Long.parseLong(line.split("\\s+")[1]));
					} else if (line.indexOf("memavailable:") != -1) {
						n--;
						info.setAvailableMemory(unit * Long.parseLong(line.split("\\s+")[1]));
					}
				}

				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			OperatingSystemMXBean osbean = ManagementFactory.getOperatingSystemMXBean();
			if (osbean instanceof com.sun.management.OperatingSystemMXBean) {
				com.sun.management.OperatingSystemMXBean sumosbean = ((com.sun.management.OperatingSystemMXBean) osbean);

				long totalvirtualMemory = sumosbean.getTotalPhysicalMemorySize();
				long freePhysicalMemorySize = sumosbean.getFreePhysicalMemorySize();
				info.setTotalPhysicalMemory(totalvirtualMemory);
				info.setFreeMemory(freePhysicalMemorySize);
				info.setAvailableMemory(totalvirtualMemory - freePhysicalMemorySize);
			}
		}

		// ----------------------------------------------------------------------------------------------------------
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		double cpuLoad = osBean.getSystemLoadAverage();
		int processors = osBean.getAvailableProcessors();
		info.setCpuLoad(cpuLoad);
		info.setProcessors(processors);
		// ----------------------------------------------------------------------------------------------------------
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkAdapterInfo adapterinfo = new NetworkAdapterInfo();

				List<String> iplist = new ArrayList<>();
				NetworkInterface ni = netInterfaces.nextElement();
				if (ni.isLoopback() || ni.isPointToPoint()) {
					continue;
				}

				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (!ip.getHostAddress().contains(":") && !ip.isLoopbackAddress()) {
//						System.out.println("ip=" + ip.getHostAddress() + " isSiteLocalAddress=" + ip.isSiteLocalAddress());
						iplist.add(ip.getHostAddress());
					}
				}

				if (iplist.size() > 0) {
					adapterinfo.setName(ni.getName());
					adapterinfo.setIps(StringUtils.toStringArray(iplist));
					adapterinfo.setUp(ni.isUp());
					adapterinfo.setVirtual(ni.isVirtual());
					info.addNetworkAdapterInfo(adapterinfo);
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ----------------------------------------------------------------------------------------------------------

		return info;

//		((com.sun.management.OperatingSystemMXBean)osBean).getProcessor();
	}

	public static int getProcessID(Process process) {
		if (process == null) {
			return -1;
		}

		int pid = -1;

		if (isWindows()) {
			return pid;
		}

		Class<? extends Process> clazz = process.getClass();
		String className = clazz.getName();
		try {
			// Linux and Mac OS X
			if (className.equals("java.lang.UNIXProcess")) {
				Field field = clazz.getDeclaredField("pid");
				field.setAccessible(true);
				pid = (Integer) field.get(process);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pid;
	}

//	public static int getProcessID(Process process) {
//		if (process == null) {
//			return -1;
//		}
//
//		int pid = -1;
//
//		Class<? extends Process> clazz = process.getClass();
//		String className = clazz.getName();
//		try {
//			// Windows
//			if (isWindows()) {
//				Field field = process.getClass().getDeclaredField("handle");
//				field.setAccessible(true);
//				pid = Kernel32.INSTANCE.GetProcessId(new WinNT.HANDLE(new Pointer((Long) field.get(process))));
//			} else {
//				// Linux and Mac OS X
//				if (className.equals("java.lang.UNIXProcess")) {
//					Field field = clazz.getDeclaredField("pid");
//					field.setAccessible(true);
//					pid = (Integer) field.get(process);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return pid;
//	}

	public static boolean moveToRecycleBin(File file) throws IOException, InterruptedException {
		if (!file.exists())
			return false;
		String os = System.getProperty("os.name").toLowerCase();

		if (isWindows()) {
			return move2WindowsRecycleBin(file);
		} else if (isLinux()) {
			return move2LinuxRecycleBin(file);
		} else if (isMacOS()) {
			return move2MacRecycleBin(file);
		} else {
			System.err.println("Unsupported OS: " + os);
			return false;
		}
	}

	// Windows: 使用 PowerShell
	private static boolean move2WindowsRecycleBin(File file) throws IOException, InterruptedException {
		String path = file.getAbsolutePath().replace("\\", "\\\\");
		String cmd;
		if (file.isDirectory()) {
			cmd = String.format("powershell -Command \"$ErrorActionPreference='Stop'; " + "Add-Type -AssemblyName Microsoft.VisualBasic; "
					+ "[Microsoft.VisualBasic.FileIO.FileSystem]::DeleteDirectory('%s','OnlyErrorDialogs','SendToRecycleBin')\"", path);
		} else {
			cmd = String.format("powershell -Command \"$ErrorActionPreference='Stop'; " + "Add-Type -AssemblyName Microsoft.VisualBasic; "
					+ "[Microsoft.VisualBasic.FileIO.FileSystem]::DeleteFile('%s','OnlyErrorDialogs','SendToRecycleBin')\"", path);
		}

		Process process = Runtime.getRuntime().exec(cmd);
		int exitCode = process.waitFor();
		String error = StreamUtils.inputStreamToString(process.getErrorStream(), true);
		return exitCode == 0 && error.isEmpty();
	}

	// macOS: 使用 AppleScript
	private static boolean move2MacRecycleBin(File file) throws IOException, InterruptedException {
		String path = file.getAbsolutePath();
		String[] cmd = { "osascript", "-e", "tell application \"Finder\" to delete POSIX file \"" + path + "\"" };
		Process process = Runtime.getRuntime().exec(cmd);
		int exitCode = process.waitFor();
		String error = StreamUtils.inputStreamToString(process.getErrorStream(), true);
		return exitCode == 0 && error.isEmpty();
	}

	// Linux: FreeDesktop Trash
	private static boolean move2LinuxRecycleBin(File file) {
		try {
			Path trashFiles = Paths.get(System.getProperty("user.home"), ".local/share/Trash/files");
			Path trashInfo = Paths.get(System.getProperty("user.home"), ".local/share/Trash/info");
			Files.createDirectories(trashFiles);
			Files.createDirectories(trashInfo);

			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = file.getName();
			Path targetFile = trashFiles.resolve(fileName + "_" + timestamp);
			Files.move(file.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

			Path infoFile = trashInfo.resolve(fileName + "_" + timestamp + ".trashinfo");
			String content = "[Trash Info]\n" + "Path=" + file.getAbsolutePath() + "\n" + "DeletionDate=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()) + "\n";
			Files.write(infoFile, content.getBytes());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 静态方法：比较 version1 是否大于 version2
	public static boolean isVersion1Newer(String version1, String version2) {
		// 校验输入
		if (version1 == null || version2 == null || version1.isEmpty() || version2.isEmpty()) {
			throw new IllegalArgumentException("Version strings cannot be null or empty");
		}

		// 按点号分割版本号
		String[] parts1 = StringUtils.split(version1, '.');
		String[] parts2 = StringUtils.split(version2, '.');

		// 转换为整数数组
		int[] nums1 = new int[parts1.length];
		int[] nums2 = new int[parts2.length];

		try {
			for (int i = 0; i < parts1.length; i++) {
				nums1[i] = Integer.parseInt(parts1[i]);
			}
			for (int i = 0; i < parts2.length; i++) {
				nums2[i] = Integer.parseInt(parts2[i]);
			}
		} catch (Exception e) {
			return false;
//			throw new IllegalArgumentException("Invalid version format: " + e.getMessage());
		}

		// 比较版本号，短的补零
		int maxLength = Math.max(nums1.length, nums2.length);
		for (int i = 0; i < maxLength; i++) {
			int part1 = i < nums1.length ? nums1[i] : 0;
			int part2 = i < nums2.length ? nums2[i] : 0;
			if (part1 > part2) {
				return true;
			} else if (part1 < part2) {
				return false;
			}
		}
		return false; // 相等时返回 false（version1 不大于 version2）
	}
	
	public static void openSystemFileBrowser(String path) throws Exception {
	    // 如果 path 为 null、空，或者无效，则使用用户主目录或根目录
	    if (path == null || path.trim().isEmpty()) {
	        if (isWindows) {
	            path = System.getProperty("user.home");
	        } else if (isMacOS) {
	            path = System.getProperty("user.home");
	        } else {
	            path = System.getProperty("user.home"); // Linux fallback
	        }
	    }

	    // 规范化路径（处理跨平台分隔符问题）
	    File file = new File(path);
	    if (!file.exists()) {
	        throw new IllegalArgumentException("Path does not exist: " + path);
	    }
	    path = file.getAbsolutePath();

	    if (isWindows) {
	        openWindowsFileBrowser(path);
	    } else if (isMacOS) {
	        openMacFileBrowser(path);
	    } else {
	        openLinuxFileBrowser(path);
	    }
	}

	private static void openWindowsFileBrowser(String path) throws Exception {
	    // Windows Explorer 接受原生路径（如 C:\Users\...）
	    new ProcessBuilder("explorer", path).start();
	}

	private static void openMacFileBrowser(String path) throws Exception {
	    // macOS: 使用 "open -R" 可以 reveal 文件/文件夹（聚焦到该位置）
	    // 但更常见的是直接 open 文件夹（会用 Finder 打开）
	    // 注意：open 命令要求路径是 POSIX 格式（/Users/...），Java 的 getAbsolutePath() 已满足
	    new ProcessBuilder("open", path).start();
	}

	private static void openLinuxFileBrowser(String path) throws Exception {
	    // 尝试使用 xdg-open（标准桌面环境通用方式）
	    try {
	        new ProcessBuilder("xdg-open", path).start();
	        return;
	    } catch (Exception ignored) {}

	    // 备用方案：尝试常见文件管理器
	    String[] fileManagers = {
	        "nautilus",
	        "dolphin",
	        "thunar",
	        "pcmanfm",
	        "nemo"
	    };

	    for (String fm : fileManagers) {
	        try {
	            new ProcessBuilder(fm, path).start();
	            return;
	        } catch (Exception ignored) {}
	    }

	    throw new RuntimeException("No available file manager found!");
	}
	
	public static void openSystemTerminal(String path) throws Exception {
	    // 如果 path 为 null、空，或者无效，则忽略
	    if (path == null || path.trim().isEmpty()) {
	        path = null;
	    }

	    if (isWindows) {
	        openWindowsTerminal(path);
	        return;
	    }

	    if (isMacOS) {
	        openMacTerminal(path);
	        return;
	    }

	    openLinuxTerminal(path);
	}
	
	private static void openMacTerminal(String path) throws Exception {
	    if (path == null || path.trim().isEmpty()) {
	        // 打开默认 Terminal（不指定路径）
	        new ProcessBuilder("open", "-a", "Terminal").start();
	        return;
	    }

	    // 安全转义路径中的特殊字符（特别是单引号）
	    String escapedPath = path.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'");

	    // 构造 AppleScript：确保即使 Terminal 未运行也能新建窗口并 cd
	    String script =
	        "tell application \"Terminal\"\n" +
	        "    activate\n" +
	        "    do script \"cd \\\"" + escapedPath + "\\\" && clear\"\n" +
	        "end tell";

	    new ProcessBuilder("osascript", "-e", script).start();
	}

	private static void openLinuxTerminal(String path) throws Exception {
	    String[] terms = {
	        "x-terminal-emulator",
	        "gnome-terminal",
	        "konsole",
	        "xfce4-terminal",
	        "xterm",
	        "lxterminal"
	    };

	    for (String t : terms) {
	        try {
	            if (path == null) {
	                new ProcessBuilder(t).start();
	            } else {
	                new ProcessBuilder(t, "-e", "sh", "-c", "cd \"" + path + "\"; exec bash").start();
	            }
	            return;
	        } catch (Exception ignored) {}
	    }

	    throw new RuntimeException("No available terminal found!");
	}
	
	private static void openWindowsTerminal(String path) throws Exception {
	    if (path == null) {
	        new ProcessBuilder("cmd", "/c", "start", "cmd").start();
	    } else {
	        new ProcessBuilder("cmd", "/c", "start", "cmd", "/k", "cd /d \"" + path + "\"").start();
	    }
	}
	
	public static void openSshTerminal(String user, String host, int port, String remotePath) throws IOException {
        if (user == null || host == null) {
            throw new IllegalArgumentException("User and host must not be null");
        }
        if (remotePath == null) remotePath = "～";

        // 构造远程 cd 命令（安全转义路径）
        String escapedPath = remotePath.replace("'", "'\"'\"'"); // 转义单引号
        String sshCommand = String.format(
            "ssh -t -p %d %s@%s \"cd '%s' && exec \\$SHELL\"",
            port, user, host, escapedPath
        );

        ProcessBuilder pb;

        if (isWindows) {
            // Windows: 使用 Windows Terminal (推荐) 或 cmd
            try {
                // 尝试 Windows Terminal (现代默认)
                pb = new ProcessBuilder("wt", "new-tab", "--title", "SSH to " + host, "powershell", "-Command", sshCommand);
            } catch (Exception e) {
                // 回退到 cmd
                pb = new ProcessBuilder("cmd", "/c", "start", "cmd", "/k", sshCommand);
            }
        } else if (isMacOS) {
            // macOS: 用 Terminal.app 执行 ssh 命令
            String script = String.format(
                "tell application \"Terminal\"\n" +
                "    activate\n" +
                "    do script \"%s\"\n" +
                "end tell",
                sshCommand.replace("\"", "\\\"")
            );
            pb = new ProcessBuilder("osascript", "-e", script);
        } else {
            // Linux: 尝试 GNOME Terminal, Konsole, xterm...
            String[] terminals = {
                "gnome-terminal", "--", "bash", "-c", sshCommand,
                "konsole", "-e", "bash", "-c", sshCommand,
                "xfce4-terminal", "-e", "bash -c '" + sshCommand + "'",
                "xterm", "-e", "bash", "-c", sshCommand
            };

            IOException lastEx = null;
            for (int i = 0; i < terminals.length; i += 2) {
                try {
                    String[] cmd;
                    if ("xfce4-terminal".equals(terminals[i])) {
                        cmd = new String[]{terminals[i], terminals[i+1]};
                    } else {
                        cmd = new String[]{terminals[i], terminals[i+1], terminals[i+2]};
                    }
                    new ProcessBuilder(cmd).start();
                    return;
                } catch (IOException e) {
                    lastEx = e;
                }
            }
            throw new IOException("No supported terminal found on Linux", lastEx);
        }

        pb.start();
    }
	
	 /**
     * 获取当前操作系统的默认应用程序安装目录（用于 JFileChooser 初始目录）
     *
     * @return 推荐的初始目录 File 对象；如果无法确定，则返回用户主目录
     */
    public static File getDefaultApplicationDirectory() {

        if (isWindows()) {
            // Windows: 优先 64位 Program Files
            String programFiles = System.getenv("ProgramFiles");
            if (programFiles != null) {
                File pf = new File(programFiles);
                if (pf.exists() && pf.isDirectory()) {
                    return pf;
                }
            }
            // 回退到 C:\Program Files
            File fallback = new File("C:\\Program Files");
            return fallback.exists() ? fallback : getUserHome();

        } else if (isMacOS()) {
            // macOS: /Applications
            File apps = new File("/Applications");
            return apps.exists() ? apps : getUserHome();

        } else {
            // Linux / Unix-like
            // 由于 Linux 没有统一程序目录，且用户常将 AppImage 放在 ~/Applications 或 ~/bin，
            // 最安全的做法是默认打开用户主目录（比 /usr/bin 更友好）
            return getUserHome();
        }
    }

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

	public static void main(String[] arg) throws IOException, InterruptedException { // 测试用例，基于你提供的版本号顺序
		String[][] testCases = { { "2.3", "2.0.1" }, { "2.0.1", "1.9" }, { "1.9", "1.1.2" }, { "1.1.2", "1.1.0" }, { "1.1.0", "1.0" }, { "1.0", "1.0" } // 测试相等情况
		};

		System.out.println("Version comparison results:");
		for (String[] test : testCases) {
			try {
				boolean result = isVersion1Newer(test[0], test[1]);
				System.out.printf("%s %s %s: %b%n", test[0], result ? ">" : "<=", test[1], result);
			} catch (IllegalArgumentException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
//		System.out.println(gatherOSInfo());
//		System.out.println(getOSDriver());
//		System.out.println(System.getenv());

		System.out.println(JSON.toJSONString(gatherOSInfo(), true));

		OperatingSystemMXBean osbean = ManagementFactory.getOperatingSystemMXBean();
//		if (osbean instanceof com.sun.management.OperatingSystemMXBean) {
//			com.sun.management.OperatingSystemMXBean sumosbean = ((com.sun.management.OperatingSystemMXBean) osbean);

		String j = JSON.toJSONString(osbean, true);
		System.out.println(j);
//		}

//		Process ps = Runtime.getRuntime().exec("C:\\windows\\system32\\notepad.exe");
//		System.out.println(getProcessID(ps));
//		System.out.println(JSON.toJSONString(gatherOSInfo(), true));
//		Thread.sleep(10000);

//		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
//		while (netInterfaces.hasMoreElements()) {
//
//			NetworkInterface ni = netInterfaces.nextElement();
//			if (ni.isLoopback() || ni.isPointToPoint()) {
//				continue;
//			}
//
//			Enumeration<InetAddress> address = ni.getInetAddresses();
//			while (address.hasMoreElements()) {
//				InetAddress ip = address.nextElement();
//				if (!ip.getHostAddress().contains(":") && !ip.isLoopbackAddress()) {
////					System.out.println("ip=" + ip.getHostAddress() + " isSiteLocalAddress=" + ip.isSiteLocalAddress());
//					System.out.println(JSON.toJSONString(
//							ip.isAnyLocalAddress() + " " + ip.isLinkLocalAddress() + " " + ip.isLoopbackAddress() +" "+ip.isSiteLocalAddress()+ ip.getHostAddress(), true));
//				}
//			}
//
//		}
	}

}