package com.amituofo.common.ui.swingexts.component;

import javax.swing.*;

import com.amituofo.common.ui.util.UIUtils;
import com.amituofo.common.util.SystemUtils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 可点击的超链接标签组件
 */
public class JEHyperlinkLabel extends JLabel {
	private String url;
	private Color normalColor = Color.BLUE;
	private Color hoverColor = new Color(0, 0, 238); // 深蓝色
	private Color clickedColor = new Color(128, 0, 128); // 紫色
	private boolean underline = true;

	/**
	 * 创建一个空的超链接标签
	 */
	public JEHyperlinkLabel() {
		this("", null);
	}

	/**
	 * 创建带有文本的超链接标签
	 * 
	 * @param text 显示文本
	 */
	public JEHyperlinkLabel(String text) {
		this(text, null);
	}

	/**
	 * 创建带有文本和URL的超链接标签
	 * 
	 * @param text 显示文本
	 * @param url  链接地址
	 */
	public JEHyperlinkLabel(String text, String url) {
		super(text);
		this.url = url;
		init();
	}
	
	public JEHyperlinkLabel(String text, Icon icon, String url) {
		super(text, icon, SwingConstants.LEADING);
		this.url = url;
		init();
	}

	private void init() {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		updateAppearance();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (url != null && !url.isEmpty()) {
					if (url.startsWith("http")) {
						try {
							Desktop.getDesktop().browse(new URI(url));
							setForeground(clickedColor);
						} catch (Exception ex) {
							SystemUtils.setClipboardString(url);
							UIUtils.openMessageTip(url + " copied to clipboard!");
						}
					} else {
						SystemUtils.setClipboardString(url);
						UIUtils.openMessageTip(url + " copied to clipboard!");
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setForeground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setForeground(normalColor);
			}
		});
	}

	private void updateAppearance() {
		String text = getText();
		if (underline) {
			setText("<html><u>" + (text != null ? text : "") + "</u></html>");
		}
		setForeground(normalColor);
	}

	// Getter and Setter 方法

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Color getNormalColor() {
		return normalColor;
	}

	public void setNormalColor(Color normalColor) {
		this.normalColor = normalColor;
		setForeground(normalColor);
	}

	public Color getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}

	public Color getClickedColor() {
		return clickedColor;
	}

	public void setClickedColor(Color clickedColor) {
		this.clickedColor = clickedColor;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
		updateAppearance();
	}

	@Override
	public void setText(String text) {
		if (underline) {
			super.setText("<html><u>" + text + "</u></html>");
		} else {
			super.setText(text);
		}
	}

	// 使用示例
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("JHyperlinkLabel 示例");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new FlowLayout());
//
//        // 创建超链接标签
//        JHyperlinkLabel link1 = new JHyperlinkLabel("点击访问FTP服务器", "ftp://1.1.1.1");
//        link1.setNormalColor(new Color(0, 102, 204)); // 设置自定义颜色
//        
//        JHyperlinkLabel link2 = new JHyperlinkLabel("点击访问示例网站", "https://www.example.com");
//        link2.setUnderline(false); // 取消下划线
//        
//        frame.add(link1);
//        frame.add(link2);
//        
//        frame.pack();
//        frame.setVisible(true);
//    }
}