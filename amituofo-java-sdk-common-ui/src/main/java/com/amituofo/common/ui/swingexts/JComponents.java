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
import com.amituofo.common.util.StringUtils;
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

	// 获取系统字体
	private static Set<String> InstalledFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
	private static final String MULTILINGUAL_TEST_TEXT = "Aa09äöüßéñçРусский简体中文繁體中文空日本語한국어";

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

	public static String getPreferFontKeyworld(Locale localeCode) {
		String country = localeCode.getCountry().toUpperCase();
		switch (country) {
		case "CN":
			return "SC";
		case "TW":
			return "TC";
		case "JP":
			return "JP";
		case "KR":
			return "KR";
		}
		return null;
	}

	public static Font getDefaultTerminalFont(Locale localeCode) {
		Locale locale = localeCode != null ? localeCode : Locale.getDefault();
		String[] fonts = UIUtils.getMonospacedFontsForLocale(localeCode);
		if (fonts != null && fonts.length > 0) {
			String testText = getTestText(locale);
			Font preferredFont;
			String preferFontKeyworld = getPreferFontKeyworld(locale);
			if (preferFontKeyworld != null) {
				String[] matchFonts = StringUtils.filterInclude(fonts, preferFontKeyworld);
				if (matchFonts != null && matchFonts.length > 0) {
					preferredFont = getFirstSupportedFont(matchFonts, matchFonts, testText);
					if (preferredFont != null) {
						return preferredFont;
					}
				}
			}

			preferredFont = getFirstSupportedFont(fonts, new String[] { "DialogInput", "Monospaced", "Courier New" }, testText);
			if (preferredFont != null) {
				return preferredFont;
			}

			preferredFont = getFirstSupportedFont(fonts, fonts, testText);
			if (preferredFont != null) {
				return preferredFont;
			}
		}

		return new Font(Font.MONOSPACED, Font.PLAIN, getDefaultSystemFont().getSize());
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

	public static Font getPreferFont(Locale locale) {
		Font defaultFont = Label.getFont();
		int size = defaultFont.getSize();
		Locale targetLocale = locale != null ? locale : Locale.getDefault();
		String[] candidates = getCandidates(targetLocale);
//		String testText = getTestText(targetLocale);

		for (String name : candidates) {
			if (InstalledFonts.contains(name)) {
				Font font;
				if (SystemUtils.isPosixSystem() && name.contains("Mono CJK")) {
					font = new Font(name, Font.BOLD, size);
				} else {
					font = new Font(name, Font.PLAIN, size);
				}
				
//				if (canDisplay(font, testText) && isComfortableUIFont(font)) {
//					return font;
//				}

				return font;
			}
		}

		// 最终 fallback（JVM 自己处理）
		return defaultFont;
	}

	private static String[] getCandidates(Locale locale) {
		String lang = locale.getLanguage().toLowerCase();
		boolean traditionalChinese = "TW".equalsIgnoreCase(locale.getCountry()) || "HK".equalsIgnoreCase(locale.getCountry());

		// ===== Windows =====
		if (SystemUtils.isWindows()) {
			switch (lang) {
			case "zh":
				return traditionalChinese
						? new String[] { "Microsoft JhengHei UI", "Microsoft JhengHei", "Segoe UI", "Dialog" }
						: new String[] { "Microsoft YaHei UI", "Microsoft YaHei", "Segoe UI", "Dialog" };
			case "ja":
				return new String[] { "Yu Gothic UI", "Meiryo UI", "Meiryo", "Segoe UI", "Dialog" };
			case "ko":
				return new String[] { "Malgun Gothic", "Segoe UI", "Dialog" };
			case "ar":
				return new String[] { "Segoe UI", "Tahoma", "Dialog" };
			case "hi":
				return new String[] { "Nirmala UI", "Segoe UI", "Dialog" };
			case "en":
				return new String[] { "Microsoft YaHei UI", "Microsoft JhengHei UI", "Arial Unicode MS", "Dialog" };
			default:
				return new String[] { "Segoe UI", "Arial", "Dialog" };
			}
		}

		// ===== macOS =====
		if (SystemUtils.isMacOS()) {
			switch (lang) {
			case "zh":
				return traditionalChinese
						? new String[] { "PingFang TC", "PingFang HK", "Heiti TC", "Dialog" }
						: new String[] { "PingFang SC", "Hiragino Sans GB", "Heiti SC", "Dialog" };
			case "ja":
				return new String[] { "PingFang TC", "Hiragino Sans", "Hiragino Kaku Gothic ProN", "YuGothic", "Dialog" };
			case "ko":
				return new String[] { "Apple SD Gothic Neo", "AppleGothic", "PingFang TC", "Dialog" };
			case "en":
				return new String[] { "PingFang SC", "Arial Unicode MS", "Dialog" };
			default:
				// ❗不要再用 Helvetica / Zapf / Geeza
				return new String[] { "PingFang SC", "Helvetica Neue", "Arial", "Dialog" };
			}
		}
		
		// ===== Linux =====
		if (SystemUtils.isPosixSystem()) {
			switch (lang) {
			case "zh":
				return traditionalChinese
						? new String[] { "Noto Sans CJK TC", "Noto Sans TC", "Noto Sans Mono CJK TC", "Noto Sans", "DejaVu Sans", "Dialog" }
						: new String[] { "Noto Sans CJK SC", "Noto Sans SC", "Noto Sans Mono CJK SC", "Noto Sans", "DejaVu Sans", "Dialog" };
			case "ja":
				return new String[] { "Noto Sans CJK JP", "Noto Sans JP", "Noto Sans Mono CJK JP", "Noto Sans", "DejaVu Sans", "Dialog" };
			case "ko":
				return new String[] { "Noto Sans CJK KR", "Noto Sans KR", "Noto Sans Mono CJK KR", "Noto Sans", "DejaVu Sans", "Dialog" };
			case "ar":
				return new String[] { "Noto Sans Arabic", "DejaVu Sans", "Dialog" };
			case "hi":
				return new String[] { "Noto Sans Devanagari", "DejaVu Sans", "Dialog" };
			case "en":
				return new String[] { "Noto Sans CJK SC", "Noto Sans", "DejaVu Sans", "Dialog" };
			default:
				return new String[] { "Noto Sans", "DejaVu Sans", "Dialog" };
			}
		}

		// fallback（极少情况）
		return new String[] { "Dialog" };
	}

	private static Font getFirstSupportedFont(String[] availableFonts, String[] candidates, String testText) {
		int size = getDefaultSystemFont().getSize();
		for (String candidate : candidates) {
			if (StringUtils.contains(availableFonts, candidate)) {
				Font font = new Font(candidate, Font.PLAIN, size);
//				if (canDisplay(font, testText)) {
					return font;
//				}
			}
		}
		return null;
	}

	private static String getTestText(Locale locale) {
		String lang = locale.getLanguage().toLowerCase();
		if ("en".equals(lang)) {
			return MULTILINGUAL_TEST_TEXT;
		}
		if ("zh".equals(lang)) {
			return "TW".equalsIgnoreCase(locale.getCountry()) || "HK".equalsIgnoreCase(locale.getCountry())
					? "Aa09繁體中文檔案資料夾空"
					: "Aa09简体中文文件夹空";
		}
		switch (lang) {
		case "ja":
			return "Aa09日本語ファイル空";
		case "ko":
			return "Aa09한국어파일폴더";
		case "ru":
			return "Aa09Русскийфайл";
		case "pl":
			return "Aa09Zażółćgęśląjaźń";
		case "de":
			return "Aa09ÄÖÜäöüß";
		case "es":
			return "Aa09ÁÉÍÓÚÜÑ¿¡";
		case "fr":
			return "Aa09ÀÂÇÉÈÊËÎÏÔÙÛÜŸŒÆ";
		case "pt":
			return "Aa09ÁÂÃÀÇÉÊÍÓÔÕÚ";
		case "it":
			return "Aa09ÀÈÉÌÍÎÒÓÙÚ";
		default:
			return "Aa09File folder";
		}
	}

	private static boolean isComfortableUIFont(Font font) {
		FontMetrics metrics = new Canvas().getFontMetrics(font);
		if (metrics.getHeight() <= 0) {
			return false;
		}
		double averageWidthRatio = metrics.stringWidth("AaMmWw09") / 8.0 / metrics.getHeight();
		double ascentRatio = (double) metrics.getAscent() / metrics.getHeight();
		return averageWidthRatio >= 0.30 && averageWidthRatio <= 0.70 && ascentRatio >= 0.68 && ascentRatio <= 0.90;
	}

	/**
	 * 检测字体是否能正确显示字符串
	 */
//	private static boolean canDisplay(Font font, String text) {
//		for (int i = 0; i < text.length(); i++) {
//			if (!font.canDisplay(text.charAt(i))) {
//				return false;
//			}
//		}
//		return true;
//	}

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
