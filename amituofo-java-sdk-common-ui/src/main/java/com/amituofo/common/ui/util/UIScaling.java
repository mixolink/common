package com.amituofo.common.ui.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amituofo.common.util.StreamUtils;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;

public final class UIScaling {

	public static int scale(int i) {
		return i;
	}

	public static void setupLinuxScaling() {
		if (!SystemUtils.isLinux()) {
			return;
		}

		String scale = System.getProperty("mixolink.uiScale");

		if (scale == null) {
			scale = System.getenv("MIXOLINK_UI_SCALE");
		}
		if (scale == null) {
			scale = System.getenv("GDK_SCALE");
		}
		if (scale == null) {
			scale = readGnomeMonitorScale();
			System.out.println("readGnomeMonitorScale=" + scale);
		}
		if (scale == null) {
			scale = readXftDpiScale();
			System.out.println("readXftDpiScale=" + scale);
		}
		if (scale == null) {
			scale = readGnomeIntegerScale();
			System.out.println("readGnomeIntegerScale=" + scale);
		}
		if (StringUtils.isNotEmpty(scale)) {
			scale = fitScale(scale);
			System.out.println("sun.java2d.uiScale=" + scale);
			System.setProperty("sun.java2d.uiScale", scale);
		}
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
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			configureSecureXmlParser(factory);
			Document document = factory.newDocumentBuilder().parse(configFile.toFile());
			NodeList logicalMonitors = document.getElementsByTagName("logicalmonitor");

			String firstValidScale = null;

			for (int i = 0; i < logicalMonitors.getLength(); i++) {
				Node node = logicalMonitors.item(i);

				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				Element logicalMonitor = (Element) node;
				String scale = childText(logicalMonitor, "scale");
				if (scale == null) {
					continue;
				}

				if (firstValidScale == null) {
					firstValidScale = scale;
				}

				String primary = childText(logicalMonitor, "primary");
				if ("yes".equalsIgnoreCase(primary) || "true".equalsIgnoreCase(primary)) {
					return scale;
				}
			}

			return firstValidScale;
		} catch (Exception e) {
			return null;
		}
	}

	private static void configureSecureXmlParser(DocumentBuilderFactory factory) {
		try {
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		} catch (Exception ignored) {
		}

		try {
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		} catch (Exception ignored) {
		}

		try {
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		} catch (Exception ignored) {
		}

		try {
			factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		} catch (Exception ignored) {
		}

		try {
			factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		} catch (Exception ignored) {
		}

		factory.setXIncludeAware(false);
		factory.setExpandEntityReferences(false);
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

	private static String childText(Element parent, String tagName) {
		NodeList children = parent.getElementsByTagName(tagName);
		if (children.getLength() == 0) {
			return null;
		}

		Node child = children.item(0);
		if (child == null) {
			return null;
		}

		String value = child.getTextContent();
		return value == null ? null : value.trim();
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
		if (scale <= 1) {
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