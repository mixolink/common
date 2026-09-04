package com.amituofo.common.ui.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.amituofo.common.util.StreamUtils;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;

public final class UIScaling {

	public static String setupLinuxScaling(String scale) {
		if (!SystemUtils.isLinux()) {
			return null;
		}

		if (StringUtils.isNotEmpty(scale)) {
			System.setProperty("sun.java2d.uiScale", scale);
			return scale;
		}

		scale = System.getenv("GDK_SCALE");
		if (scale == null) {
			scale = readGnomeMonitorScale();
//			System.out.println("readGnomeMonitorScale=" + scale);
		}
		if (scale == null) {
			scale = readXftDpiScale();
//			System.out.println("readXftDpiScale=" + scale);
		}
		if (scale == null) {
			scale = readGnomeIntegerScale();
//			System.out.println("readGnomeIntegerScale=" + scale);
		}
		if (StringUtils.isNotEmpty(scale)) {
//			System.out.println("sun.java2d.uiScale=" + scale);
			scale = fitScale(scale);
//			System.out.println("sun.java2d.uiScale=" + scale);
			System.setProperty("sun.java2d.uiScale", scale);
		}
		
		return scale;
	}

	private static String readGnomeIntegerScale() {
		// 方法3：读 gsettings（GNOME）
		try {
			Process p = Runtime.getRuntime().exec("gsettings get org.gnome.desktop.interface scaling-factor");
			String output = StreamUtils.inputStreamToString(p.getInputStream(), true).trim();
			// 输出类似 "uint32 2"
			String[] parts = output.split(" ");
			String value = parts[parts.length - 1];
			return value;
		} catch (Exception ignored) {
		}
		return null;
	}

	private static String readXftDpiScale() {
		// 方法2：读 Xft.dpi（X11）
		try {
			Process p = Runtime.getRuntime().exec("xrdb -query");
			String output = StreamUtils.inputStreamToString(p.getInputStream(), true).trim();
			for (String line : output.split("\n")) {
				if (line.startsWith("Xft.dpi")) {
					int dpi = Integer.parseInt(line.split(":")[1].trim());
					float scale0 = dpi / 96.0f;
					return String.valueOf(scale0);
				}
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * 读取 GNOME 主显示器缩放比例。
	 *
	 * @return 例如 1.25、1.5、2.0；无法读取时返回 null
	 */
	public static String readGnomeMonitorScale() {
		Path configFile = resolveGnomeMonitorsFile();

		if (configFile == null || !Files.isRegularFile(configFile)) {
			return null;
		}

		try {
			String content = new String(Files.readAllBytes(configFile), StandardCharsets.UTF_8);
			String firstValidScale = null;
			int offset = 0;
			while ((offset = content.indexOf("<logicalmonitor>", offset)) >= 0) {
				int end = content.indexOf("</logicalmonitor>", offset);
				if (end < 0) {
					break;
				}

				String monitor = content.substring(offset, end);
				String scale = tagValue(monitor, "scale");
				if (scale == null) {
					offset = end + 1;
					continue;
				}

				if (firstValidScale == null) {
					firstValidScale = scale;
				}

				String primary = tagValue(monitor, "primary");
				if ("yes".equalsIgnoreCase(primary) || "true".equalsIgnoreCase(primary)) {
					return scale;
				}

				offset = end + 1;
			}

			return firstValidScale;
		} catch (Exception e) {
			return null;
		}
	}

	private static Path resolveGnomeMonitorsFile() {
		String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");

		if (xdgConfigHome != null && !xdgConfigHome.trim().isEmpty()) {
			return Paths.get(xdgConfigHome, "monitors.xml");
		}

		String userHome = System.getProperty("user.home");

		if (userHome == null || userHome.trim().isEmpty()) {
			return null;
		}

		return Paths.get(userHome, ".config", "monitors.xml");
	}

	private static String tagValue(String content, String tagName) {
		String openingTag = "<" + tagName + ">";
		String closingTag = "</" + tagName + ">";
		int start = content.indexOf(openingTag);
		if (start < 0) {
			return null;
		}

		start += openingTag.length();
		int end = content.indexOf(closingTag, start);
		if (end < 0) {
			return null;
		}

		return content.substring(start, end).trim();
	}

	private static String fitScale(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		try {
			float scale = Float.parseFloat(value.trim());

			return fitScale(scale);
		} catch (NumberFormatException ignored) {
		}

		return null;
	}

	private static String fitScale(float scale) {
		if (scale <= 1.0) {
			return "1";
		} else {
			return "2";
		}

//		if (scale >= 2) {
//			return "2";
//		}
//
//		return Integer.toString((int) Math.ceil(scale));
	}
}
