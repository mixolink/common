package com.amituofo.common.ui.swingexts.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.amituofo.common.util.StringUtils;

public class JEShrinkButton extends JButton {
	private boolean isShrinkEnabled = false;
	private boolean isInited = false;
	private String originalText;
	private String shortText;

	public JEShrinkButton() {
		// TODO Auto-generated constructor stub
	}

	public JEShrinkButton(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public JEShrinkButton(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public JEShrinkButton(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public JEShrinkButton(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	public void setText(String text) {
		super.setText(text);
		this.originalText = text;
		this.shortText = originalText.substring(0, 1) + "~";
	}

	public synchronized void setShrinkEnabled(boolean enable) {
		if (this.isShrinkEnabled == enable || StringUtils.isEmpty(originalText) || originalText.length() <= 3) {
			return;
		}

		if (enable) {
			super.setText(shortText);
		} else {
			super.setText(originalText);
		}

		this.isShrinkEnabled = enable;

		if (!isInited) {
			init();
			isInited = true;
		}
	}

	public boolean isShrinkEnabled() {
		return isShrinkEnabled;
	}

	private void init() {
		this.addMouseListener(new MouseAdapter() { // 使用 Adapter 避免实现所有方法
			@Override
			public void mouseEntered(MouseEvent e) {
				// 鼠标进入按钮区域
//				System.out.println("鼠标进来了！");
//		        button.setBackground(Color.YELLOW);  // 示例：改变背景色
//		        button.setForeground(Color.RED);     // 改变文字颜色
//		        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 手型光标

				if (isShrinkEnabled) {
					JEShrinkButton.super.setText(originalText);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// 鼠标离开按钮区域
//				System.out.println("鼠标离开了！");
//		        button.setBackground(UIManager.getColor("Button.background"));  // 恢复默认背景
//		        button.setForeground(UIManager.getColor("Button.foreground"));  // 恢复默认文字颜色
//		        button.setCursor(Cursor.getDefaultCursor());  // 恢复默认光标

				if (isShrinkEnabled) {
					JEShrinkButton.super.setText(shortText);
				}
			}
		});
	}

}
