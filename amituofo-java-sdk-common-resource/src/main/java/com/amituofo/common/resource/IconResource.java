package com.amituofo.common.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.SVGLoader;

public class IconResource {
	private static final String BASE_PATH = "/com/amituofo/common/resource/";
	private static final String ICON_BASE_PATH = BASE_PATH + "icon/";
	private static final String ICON_SVG_PATH = ICON_BASE_PATH + "svg/";
	private static final String IMG_BASE_PATH = BASE_PATH + "image/";

//	public static final ImageIcon ICON_XXXX_16x16 = getIcon("cloud-1-16x16.png");

//	public static final ImageIcon ICON_OK_16x16 = getIcon("ok-8-16x16.png");
//	public static final ImageIcon ICON_CANCEL_16x16 = getIcon("cancel-8-16x16.png");
//	public static final ImageIcon ICON_SAVE_16x16 = getIcon("save-1-16x16.png");
//
//	public static final ImageIcon ICON_ADD_16x16 = getIcon("add-9-16x16.png");
//	public static final ImageIcon ICON_REMOVE_16x16 = getIcon("delete-9-16x16.png");
//
//	public static final ImageIcon ICON_SWITCH_ON_24x24 = getIcon("switch-on-24x24.png");
//	public static final ImageIcon ICON_SWITCH_OFF_24x24 = getIcon("switch-off-24x24.png");
//	public static final ImageIcon ICON_SWITCH_ON_32x32 = getIcon("switch-on-32x32.png");
//	public static final ImageIcon ICON_SWITCH_OFF_32x32 = getIcon("switch-off-32x32.png");

	public final static JSVGIcon CLOSING_ICON = getSvgIcon("close-1.svg", 18);
	public final static JSVGIcon CLOSING_ICON_SELECTED = getSvgIcon("close-focus-1.svg", 18);

	public static final JSVGIcon ICON_OK_16x16 = getSvgIcon("ok-1.svg", 18);
	public static final JSVGIcon ICON_CANCEL_16x16 = getSvgIcon("cancel-1.svg", 18);
	public static final JSVGIcon ICON_SAVE_16x16 = getSvgIcon("save-1.svg", 18);

	public static final JSVGIcon ICON_ADD_16x16 = getSvgIcon("add-1.svg", 18);
	public static final JSVGIcon ICON_REMOVE_16x16 = getSvgIcon("remove-1.svg", 18);

	public static final JSVGIcon ICON_SWITCH_ON_24x24 = getSvgIcon("switch-on-1.svg", 24);
	public static final JSVGIcon ICON_SWITCH_OFF_24x24 = getSvgIcon("switch-off-1.svg", 24);

	public static final Image IMAGE_LOADING = getImage("loading7.gif");

	public static final Icon ICON_EMPTY_16x16 = new EmptyIcon(16, 16);

//	public final static Icon ICON_FOLDER_16x16 = (Icon) FileSystemView.getFileSystemView().getSystemIcon(new File(System.getProperty("java.io.tmpdir")));// (Icon)

	private static Map<String, Icon> CACHE = new HashMap<String, Icon>();
	public final static Icon ARROW_ICON = createArrowIcon();

	private static Icon createArrowIcon() {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int arrowSize = 3;
				// 修改这两行：让图标在按钮中居中显示
				int centerX = x + getIconWidth() / 2;
				int centerY = y + getIconHeight() / 2;

				int[] xPoints = { centerX - arrowSize, centerX + arrowSize, centerX };
				int[] yPoints = { centerY - arrowSize / 2, centerY - arrowSize / 2, centerY + arrowSize / 2 + 1 };

				g2.setColor(c.isEnabled() ? Color.BLACK : Color.GRAY);
				g2.fillPolygon(xPoints, yPoints, 3);

				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				// 可以适当减小图标宽度以适应更窄的按钮
				return 8;
			}

			@Override
			public int getIconHeight() {
				return 12;
			}
		};
	}

	private static ImageIcon getIcon(String id) {
		return new ImageIcon(IconResource.class.getResource(ICON_BASE_PATH + id));
	}

	private static JSVGIcon getSvgIcon(String id, int size) {
		SVGLoader loader = new SVGLoader();
		URL url = IconResource.class.getResource(ICON_SVG_PATH + id);
//		String content = new String(toByteArray(url.openStream()), StandardCharsets.UTF_8);
//		String fixedContent = content.replace("currentColor", CLR);
		SVGDocument document = loader.load(url);

		if (document == null) {
			return null; // 加载失败
		}

		// 2. 将其转换为 Swing Icon
		// 注意：JSVG 会根据你指定的 width/height 自动处理坐标偏移
		return new JSVGIcon(document, size, size);
	}

	private static Image getImage(String id) {
		return Toolkit.getDefaultToolkit().getImage(IconResource.class.getResource(IMG_BASE_PATH + id));
	}

	public static ImageIcon getIcon(Class clazz, String base, String id) {
		return new ImageIcon(clazz.getResource(base + "/icon/" + id));
	}

	public static ImageIcon getImageIcon(Class clazz, String base, String id) {
		return new ImageIcon(clazz.getResource(base + "/icon/" + id));
	}

	public static JSVGIcon getSvgIcon(Class clazz, String base, String id, int size) {
		SVGLoader loader = new SVGLoader();
		URL url = clazz.getResource(base + "/icon/" + id + ".svg");

		if (url == null) {
			System.err.println("Icon " + base + "/icon/" + id + ".svg not found!");
			return null;
		}

//		String content = new String(toByteArray(url.openStream()), StandardCharsets.UTF_8);
//		String fixedContent = content.replace("currentColor", CLR);
		SVGDocument document = loader.load(url);

		if (document == null) {
			return null; // 加载失败
		}

		// 2. 将其转换为 Swing Icon
		// 注意：JSVG 会根据你指定的 width/height 自动处理坐标偏移
		return new JSVGIcon(document, size, size);
	}

	public static byte[] toByteArray(InputStream inputStream) throws IOException {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[4096];
			int nRead;

			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			return buffer.toByteArray();
		} finally {
			inputStream.close();
		}
	}

	public static Icon buildColorCircleIcon(Color color, int size) {
		String ID = color.getRed() + "." + color.getGreen() + "." + color.getBlue() + "/" + size;
		Icon icon = CACHE.get(ID);

		if (icon == null) {
//			icon = new Icon() {
//				public int getIconWidth() {
//					return size;
//				}
//
//				public int getIconHeight() {
//					return size;
//				}
//
//				public void paintIcon(Component c, Graphics g, int x, int y) {
//					Graphics2D g2 = (Graphics2D) g.create();
//					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//					g2.setColor(color);
//					g2.fillOval(x, y, size, size);
//					g2.dispose();
//				}
//			};

			// 定义图标的大小，通常 16x16 或 12x12 适合放在按钮或列表里
			icon = new Icon() {
				@Override
				public void paintIcon(Component c, Graphics g, int x, int y) {
					Graphics2D g2d = (Graphics2D) g.create();

					// 开启抗锯齿，保证圆形的边缘平滑，不出现锯齿
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2d.setColor(color);

					// 稍微留一点边距（比如 1 像素），让图标看起来不那么拥挤
					int margin = 2;
					int circleSize = size - (margin * 2);

					// 绘制实心圆
					g2d.fill(new Ellipse2D.Double(x + margin, y + margin, circleSize, circleSize));

					// 如果颜色太淡（接近白色），可以加一个极细的灰色边框，防止图标“消失”在白色背景中
					if (color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240) {
						g2d.setColor(Color.LIGHT_GRAY);
						g2d.draw(new Ellipse2D.Double(x + margin, y + margin, circleSize, circleSize));
					}

					g2d.dispose();
				}

				@Override
				public int getIconWidth() {
					return size;
				}

				@Override
				public int getIconHeight() {
					return size;
				}
			};

			CACHE.put(ID, icon);
		}

		return icon;
	}

}
