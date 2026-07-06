package com.amituofo.common.ui.swingexts;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.amituofo.common.ui.util.UIUtils;
import com.amituofo.common.util.SystemUtils;

public enum JComponents {
	Button("Button", "font"), CheckBox("CheckBox", "font"), CheckBoxMenuItem("CheckBoxMenuItem", "font", "acceleratorFont"), ColorChooser("ColorChooser", "font"),
	ComboBox("ComboBox", "font"), EditorPane("EditorPane", "font"), FileChooser("FileChooser", "listFont"), FormattedTextField("FormattedTextField", "font"),
	InternalFrame("InternalFrame", "titleFont"), Label("Label", "font"), List("List", "font"), Menu("Menu", "font", "acceleratorFont"), MenuBar("MenuBar", "font"),
	MenuItem("MenuItem", "font", "acceleratorFont"), OptionPane("OptionPane", "font", "messageFont", "buttonFont"), Panel("Panel", "font"), PasswordField("PasswordField", "font"),
	PopupMenu("PopupMenu", "font"), ProgressBar("ProgressBar", "font"), RadioButton("RadioButton", "font"), RadioButtonMenuItem("RadioButtonMenuItem", "font", "acceleratorFont"),
	ScrollPane("ScrollPane", "font"), Slider("Slider", "font"), Spinner("Spinner", "font"), TabbedPane("TabbedPane", "font"), Table("Table", "font"),
	TableHeader("TableHeader", "font"), TextArea("TextArea", "font"), TextField("TextField", "font"), TextPane("TextPane", "font"), TitledBorder("TitledBorder", "font"),
	ToggleButton("ToggleButton", "font"), ToolBar("ToolBar", "font"), ToolTip("ToolTip", "font"), Tree("Tree", "font"), Viewport("Viewport", "font");

	private final String componentName;
	private String[] fontKeyNames;

	JComponents(String componentName, String... fontKeyNames) {
		this.componentName = componentName;
		this.fontKeyNames = new String[fontKeyNames.length];
		for (int i = 0; i < fontKeyNames.length; i++) {
			this.fontKeyNames[i] = componentName + "." + fontKeyNames[i];
		}
	}

	private static Font SYSTEM_DEFAULT_FONT;

	static {
		SYSTEM_DEFAULT_FONT = Label.getFont();
	}

	public static void resetDefaultSystemFont() {
		SYSTEM_DEFAULT_FONT = Label.getFont();
	}

	public static Font getDefaultSystemFont() {
		return SYSTEM_DEFAULT_FONT;
	}

	public static Font getDefaultTerminalFont(String localeCode) {
		String[] fonts = UIUtils.getMonospacedFontsForLocale(localeCode);

		return fonts != null && fonts.length > 0 ? new Font(fonts[0], Font.PLAIN, getDefaultSystemFont().getSize())
				: new Font("Courier New", Font.PLAIN, getDefaultSystemFont().getSize());
	}

	public static Font newPlainFont(String name) {
		return new Font(name, Font.PLAIN, JComponents.getDefaultFont().getSize());
	}

	public static Font newBoldFont(String name) {
		return new Font(name, Font.BOLD, JComponents.getDefaultFont().getSize());
	}

	public static boolean isFixedWidth(Font font) {
		Canvas canvas = new Canvas();
		FontMetrics fm = canvas.getFontMetrics(font);
		int refWidth = fm.charWidth('M');
		if (refWidth == 0)
			return false;

		// ASCII 可打印字符必须等宽
		for (char c = '!'; c <= '~'; c++) {
			if (fm.charWidth(c) != refWidth)
				return false;
		}

		// 宽高比 0.3~0.75（过滤间距异常字体）
		double ratio = (double) refWidth / fm.getHeight();
		return ratio >= 0.3 && ratio <= 0.75;
	}

//	public static Font getPreferFont(Locale locale) {
//		Font preferredFont = Label.getFont();
//		String[] preferredFontFamily = null;
//
//		// 当前系统字体列表
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		String[] installedFonts = ge.getAvailableFontFamilyNames();
//
//		String lang = locale.getLanguage();
//
//		if (SystemUtils.isWindows()) {
//			// Windows 各语种默认字体
//			switch (lang) {
//			case "zh": // 中文（简体/繁体略有不同，这里 unified）
//				preferredFontFamily = new String[] { "Microsoft YaHei UI", "Microsoft YaHei", "SimHei", "SimSun", "Segoe UI", "Dialog" };
//				break;
//			case "ja": // 日语
//				preferredFontFamily = new String[] { "Yu Gothic UI", "Yu Gothic", "Meiryo UI", "Meiryo", "MS Gothic", "Segoe UI", "Dialog" };
//				break;
//			case "ko": // 韩语
//				preferredFontFamily = new String[] { "Malgun Gothic", "Malgun Gothic Semilight", "Segoe UI", "Dialog" };
//				break;
//			case "ar": // 阿拉伯语
//				preferredFontFamily = new String[] { "Segoe UI", "Tahoma", "Dialog" };
//				break;
//			case "ru": // 俄语
//				preferredFontFamily = new String[] { "Segoe UI", "Calibri", "Dialog" };
//				break;
//			case "el": // 希腊语
//				preferredFontFamily = new String[] { "Segoe UI", "Calibri", "Dialog" };
//				break;
//			case "th": // 泰语
//				preferredFontFamily = new String[] { "Leelawadee UI", "Leelawadee", "Tahoma", "Segoe UI", "Dialog" };
//				break;
//			case "hi": // 印地语（天城文）
//				preferredFontFamily = new String[] { "Nirmala UI", "Mangal", "Segoe UI", "Dialog" };
//				break;
//			default: // 其他语言一律 English fallback
//				preferredFontFamily = new String[] { "Microsoft YaHei UI", "Segoe UI", "Calibri", "Dialog" };
//				break;
//			}
//		} else if (SystemUtils.isMacOS()) {
//			// macOS 在 Big Sur 之后主字体为 SF Pro / Hiragino / AppleGothic 等
//			switch (lang) {
//			case "zh":
//				preferredFontFamily = new String[] { "PingFang SC", "Heiti SC", "Hiragino Sans GB", "Arial Unicode MS", "Dialog" };
//				break;
//			case "ja":
//				preferredFontFamily = new String[] { "Apple Braille", "Hiragino Kaku Gothic ProN", "Dialog" };
//				break;
//			case "ko":
//				preferredFontFamily = new String[] { "Apple SD Gothic Neo", "AppleGothic", "Dialog" };
//				break;
//			default:
//				preferredFontFamily = new String[] { "Helvetica Neue", "Geeza Pro", "Zapf Dingbats" };
//				break;
//			}
//		} else if (SystemUtils.isLinux()) {
//			// Linux 字体分布较乱，所以给多 fallback
//			switch (lang) {
//			case "zh":
//				preferredFontFamily = new String[] { "WenQuanYi Micro Hei", "Noto Sans CJK SC", "DejaVu Sans", "Dialog" };
//				break;
//			case "ja":
//				preferredFontFamily = new String[] { "Noto Sans CJK JP", "VL Gothic", "DejaVu Sans", "Dialog" };
//				break;
//			case "ko":
//				preferredFontFamily = new String[] { "Noto Sans CJK KR", "UnDotum", "DejaVu Sans", "Dialog" };
//				break;
//			case "ar":
//				preferredFontFamily = new String[] { "Noto Sans Arabic", "DejaVu Sans", "Dialog" };
//				break;
//			case "hi":
//				preferredFontFamily = new String[] { "Noto Sans Devanagari", "Lohit Devanagari", "Dialog" };
//				break;
//			default:
//				preferredFontFamily = new String[] { "Dialog", "DejaVu Sans", "Noto Sans", "Ubuntu" };
//				break;
//			}
//		}
//
//		// 遍历优先列表，第一个匹配的即为结果
//		if (preferredFontFamily != null) {
//			for (String font : preferredFontFamily) {
//				for (String installed : installedFonts) {
//					if (installed.equalsIgnoreCase(font)) {
//						return new Font(font, Font.PLAIN, getDefaultSystemFont().getSize());
//					}
//				}
//			}
//		}
//
//		// fallback
//		return preferredFont;
//	}
	
	   public static Font getPreferFont(Locale locale) {
	        Font defaultFont = Label.getFont();
	        int size = defaultFont.getSize();

	        // 获取系统字体
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        Set<String> installedFonts = new HashSet<>(Arrays.asList(ge.getAvailableFontFamilyNames()));

	        String lang = locale.getLanguage().toLowerCase();

	        String[] candidates = getCandidates(lang);

	        // 测试字符串（覆盖多语言，防止 glyph 缺失）
	        String testText = "abcABC123中文あいう한글";

	        for (String name : candidates) {
	            if (installedFonts.contains(name)) {
	                Font f = new Font(name, Font.PLAIN, size);

	                // 核心：确保字体真的能显示（避免你遇到的“a a a”问题）
	                if (canDisplay(f, testText)) {
	                    return f;
	                }
	            }
	        }

	        // 最终 fallback（JVM 自己处理）
	        return defaultFont;
	    }

	    private static String[] getCandidates(String lang) {

	        // ===== Windows =====
	        if (SystemUtils.isWindows()) {
	            switch (lang) {
	                case "zh":
	                    return new String[]{
	                            "Microsoft YaHei UI",
	                            "Microsoft YaHei",
	                            "Segoe UI",
	                            "SimHei",
	                            "Dialog"
	                    };
	                case "ja":
	                    return new String[]{
	                            "Yu Gothic UI",
	                            "Meiryo",
	                            "Segoe UI",
	                            "Dialog"
	                    };
	                case "ko":
	                    return new String[]{
	                            "Malgun Gothic",
	                            "Segoe UI",
	                            "Dialog"
	                    };
	                case "ar":
	                    return new String[]{
	                            "Segoe UI",
	                            "Tahoma",
	                            "Dialog"
	                    };
	                case "hi":
	                    return new String[]{
	                            "Nirmala UI",
	                            "Segoe UI",
	                            "Dialog"
	                    };
	                default:
	                    return new String[]{
	                            "Segoe UI",
	                            "Calibri",
	                            "Dialog"
	                    };
	            }
	        }

	        // ===== macOS =====
	        if (SystemUtils.isMacOS()) {
	            switch (lang) {
	                case "zh":
	                    return new String[]{
	                            "PingFang SC",
	                            "Hiragino Sans GB",
	                            "Heiti SC",
	                            "Dialog"
	                    };
	                case "ja":
	                    return new String[]{
	                            "Hiragino Kaku Gothic ProN",
	                            "PingFang SC",
	                            "Dialog"
	                    };
	                case "ko":
	                    return new String[]{
	                            "Apple SD Gothic Neo",
	                            "PingFang SC",
	                            "Dialog"
	                    };
	                default:
	                    // ❗不要再用 Helvetica / Zapf / Geeza
	                    return new String[]{
	                            "PingFang SC",
	                            "Dialog"
	                    };
	            }
	        }

	        // ===== Linux =====
	        if (SystemUtils.isPosixSystem()) {
	            switch (lang) {
	                case "zh":
	                    return new String[]{
	                            "Noto Sans CJK SC",
	                            "Noto Sans",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	                case "ja":
	                    return new String[]{
	                            "Noto Sans CJK JP",
	                            "Noto Sans",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	                case "ko":
	                    return new String[]{
	                            "Noto Sans CJK KR",
	                            "Noto Sans",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	                case "ar":
	                    return new String[]{
	                            "Noto Sans Arabic",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	                case "hi":
	                    return new String[]{
	                            "Noto Sans Devanagari",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	                default:
	                    return new String[]{
	                            "Noto Sans",
	                            "DejaVu Sans",
	                            "Dialog"
	                    };
	            }
	        }

	        // fallback（极少情况）
	        return new String[]{"Dialog"};
	    }

	    /**
	     * 检测字体是否能正确显示字符串
	     */
	    private static boolean canDisplay(Font font, String text) {
	        for (int i = 0; i < text.length(); i++) {
	            if (!font.canDisplay(text.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }

	public String[] getFontKeys() {
		return fontKeyNames;
	}

	public String getForegroundKey() {
		return componentName + ".foreground";
	}

	public void setFont(String fontFamily) {
		for (String fontKeyName : fontKeyNames) {
			Object value = UIManager.get(fontKeyName);
			// if (value instanceof FontUIResource) {
			FontUIResource oldf = (FontUIResource) value;
			Font newfont = new Font(fontFamily, oldf.getStyle(), oldf.getSize());
			FontUIResource fontRes = new FontUIResource(newfont);
			UIManager.put(fontKeyName, fontRes);
			// }
		}
	}

	public Font getFont() {
		Object value = UIManager.get(fontKeyNames[0]);
		// if (value instanceof FontUIResource) {
//		Font oldf = (Font) value;
		return (Font) value;
		// }
	}

	public void setFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (String fontKeyName : fontKeyNames) {
			UIManager.put(fontKeyName, fontRes);
		}
	}

	public static void setAllFont(Font font) {
		JComponents[] vs = JComponents.values();
		for (JComponents fontComponmentKey : vs) {
			fontComponmentKey.setFont(font);
		}
	}

	public static void setAllForeground(Color clr) {
		if (clr != null) {
			JComponents[] vs = JComponents.values();
			for (JComponents fontComponmentKey : vs) {
				UIManager.put(fontComponmentKey.getForegroundKey(), clr);
			}
		}
	}

	public static void setAllFont(String fontFamily) {
		JComponents[] vs = JComponents.values();
		for (JComponents fontComponmentKey : vs) {
			fontComponmentKey.setFont(fontFamily);
		}
	}

	public static Font getDefaultFont() {
		Font defaultFont = UIManager.getFont("Label.font");
		return defaultFont;
	}

	public static Font deriveFont(int style, float size) {
		return getDefaultFont().deriveFont(style, size);
	}

	public static Font deriveFontSize(float size) {
		return getDefaultFont().deriveFont(size);
	}

	public static Font deriveFontStyle(int style) {
		return getDefaultFont().deriveFont(style);
	}

}
