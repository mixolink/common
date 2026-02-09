package com.amituofo.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.amituofo.common.ui.swingexts.JComponents;

public class FontChooser2 extends JPanel {
	public static final Integer[] FONT_SIZES = new Integer[] { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72 };

	public static enum Style {
		Plain("Plain", Font.PLAIN), Italic("Italic", Font.ITALIC), Bold("Bold", Font.BOLD), Bold_Italic("Bold Italic", Font.BOLD | Font.ITALIC);

		private String name;
		private int value;

		Style(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String toString() {
			return name;
		}

		public int getValue() {
			return value;
		}

		public static Style valueOf(Font font) {
			if (font.getStyle() == Plain.value) {
				return Plain;
			} else if (font.getStyle() == Italic.value) {
				return Italic;
			} else if (font.getStyle() == Bold.value) {
				return Bold;
			} else if (font.getStyle() == Bold_Italic.value) {
				return Bold_Italic;
			} else {
				return Plain;
			}
		}
	}

	private Color current_color = Color.BLACK;// 当前字色,默认黑色.
	private JDialog dialog; // 用于显示模态的窗体
	private JTextField txtFont; // 显示选择字体的TEXT
	private JTextField txtStyle; // 显示选择字型的TEXT
	private JTextField txtSize; // 显示选择字大小的TEXT
	private JTextField example; // 展示框（输入框）
	private JList<String> lstFont; // 选择字体的列表.
	private JList<Style> lstStyle; // 选择字型的列表.
	private JList<Integer> lstSize; // 选择字体大小的列表.

	private Color selectedcolor; // 用户选择的颜色
	private Font chooseFont;

	// 无参初始化
	public FontChooser2() {
		// this.selectedfont = null;
		this.selectedcolor = null;
		/* 初始化界面 */
		init(JComponents.getDefaultSystemFont(), null);
	}

	// 重载构造，有参的初始化 用于初始化字体界面
	public FontChooser2(Font font, Color color) {
		if (font != null) {
			init(font, color);
		} else {
			init(JComponents.getDefaultSystemFont(), null);
		}
	}

	// 可供外部调用的方法
	public Font getSelectedFont() {
		return chooseFont;
	}

	// public void setSelectedfont(Font selectedfont) {
	// this.selectedfont = selectedfont;
	// }

	public Color getSelectedcolor() {
		return selectedcolor;
	}

	public void setSelectedcolor(Color selectedcolor) {
		this.selectedcolor = selectedcolor;
	}

	/* 初始化界面 */
	private void init(final Font font, final Color color) {
		this.chooseFont = font;
		// 实例化变量
		JLabel lblFont = new JLabel("Font:");
		JLabel lblStyle = new JLabel("Style:");
		JLabel lblSize = new JLabel("Size:");
		txtFont = new JTextField();
		txtStyle = new JTextField();
		txtSize = new JTextField();

		// 取得当前环境可用字体.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

//		lstFont = new JList<String>(fontNames);
//		lstStyle = new JList<Style>(Style.values());
//		lstSize = new JList<Integer>(FONT_SIZES);
		lstFont = new JList<String>();
		lstStyle = new JList<Style>();
		lstSize = new JList<Integer>();
		
		JScrollPane spFont = new JScrollPane(lstFont);
		JScrollPane spSize = new JScrollPane(lstSize);

		JPanel showPan = new JPanel();
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");

		// 布局控件
		// 字体框
		this.setLayout(null); // 不用布局管理器
		add(lblFont);
		lblFont.setBounds(12, 10, 88, 20);
		txtFont.setEditable(false);
		add(txtFont);
		txtFont.setBounds(10, 30, 155, 20);

		add(spFont);
		spFont.setBounds(10, 50, 155, 206);

		// 样式
		add(lblStyle);
		lblStyle.setBounds(175, 10, 94, 20);
		txtStyle.setEditable(false);
		add(txtStyle);
		txtStyle.setBounds(175, 30, 130, 20);
		lstStyle.setBorder(javax.swing.BorderFactory.createLineBorder(Color.gray));
		add(lstStyle);
		lstStyle.setBounds(175, 50, 130, 100);

		// 大小
		add(lblSize);
		lblSize.setBounds(315, 10, 60, 20);
		txtSize.setEditable(false);
		add(txtSize);
		txtSize.setBounds(315, 30, 60, 20);
		add(spSize);
		spSize.setBounds(315, 50, 60, 100);

		// 展示框
		example = new JTextField();
		example.setBounds(10, 10, 300, 50);
		example.setHorizontalAlignment(JTextField.CENTER);
		example.setText("亚洲字体\r\nAaBb,CcDd.");
//		example.setBackground(Color.white);
		example.setEditable(false);
		showPan.setBorder(javax.swing.BorderFactory.createTitledBorder("Example"));
		add(showPan);
		showPan.setBounds(174, 160, 210, 100);
		showPan.setLayout(new BorderLayout());
		showPan.add(example);
		example.setForeground(color);

		// 确定和取消按钮
		add(ok);
		ok.setBounds(205, 268, 88, 20);
		add(cancel);
		cancel.setBounds(296, 268, 88, 20);
		
//		updateSelectFont();

		JButton btnColor = new JButton("Color");
		btnColor.setEnabled(false);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color col_temp = new JColorChooser().showDialog(null, null, Color.pink);
				if (col_temp != null) {
					current_color = col_temp;
					example.setForeground(current_color);
				}
			}
		});
		btnColor.setBounds(10, 267, 155, 20);
		add(btnColor);
		
		lstFont.setSelectedValue(font.getFamily(), true);
		lstStyle.setSelectedValue(Style.valueOf(font), true); // 初始化样式list
		lstSize.setSelectedValue(font.getSize(), true);
		updateSelectFont();

		/* 用户选择字体 */
		lstFont.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateSelectFont();
			}
		});

		/* 用户选择字型 */
		lstStyle.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateSelectFont();
			}
		});

		/* 用户选择字体大小 */
		lstSize.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateSelectFont();
			}
		});

		/* 用户确定 */
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* 用户用户选择的颜色设置 */
				setSelectedcolor(current_color);
				dialog.dispose();
				dialog = null;
			}
		});

		/* 用户取消 */
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FontChooser2.this.chooseFont = null;
				dialog.dispose();
				dialog = null;
			}
		});

//		UIUtils.executeAction(new Runnable() {
//			
//			@Override
//			public void run() {
//				lstFont.setSelectedValue(font.getFamily(), true);
//				lstStyle.setSelectedValue(Style.valueOf(font), true); // 初始化样式list
//				lstSize.setSelectedValue(font.getSize(), true);
//
//				updateSelectFont();
//			}
//		});
		
	}

	private void updateSelectFont() {
		String fontFamily = lstFont.getSelectedValue();
		Style style = lstStyle.getSelectedValue();
		int size = lstSize.getSelectedValue();

		this.chooseFont = new Font(fontFamily, style.value, size);
		txtFont.setText(fontFamily);
		txtStyle.setText(style.name());
		txtSize.setText(String.valueOf(size));
		UIUtils.invokeLater(new Runnable() {
			@Override
			public void run() {
				example.setFont(chooseFont);
			}
		});
	}

	/* 显示字体选择器对话框(x,y表示窗体的启动位置) */
	public void showDialog(Frame parent) {
		dialog = new JDialog(parent, "Font", true);
		dialog.getContentPane().add(this);
		dialog.setResizable(false);
		dialog.setSize(396, 325);
		// 设置接界面的启动位置
		// dialog.setLocation(x, y);
		dialog.addWindowListener(new WindowAdapter() {

			/* 窗体关闭时调用 */
			public void windowClosing(WindowEvent e) {
				// dialog.removeAll();
				dialog.dispose();
				dialog = null;
			}
		});
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}

	public static void main(String[] a) {
		FontChooser2 one = new FontChooser2(); // 无参
		one.showDialog(null);
		// 获取选择的字体
		Font font = one.getSelectedFont();
		// 获取选择的颜色
		Color color = one.getSelectedcolor();
		if (font != null && color != null) {
			/* 打印用户选择的字体和颜色 */
			System.out.println(font);
			System.out.println(color);
		}
	}
}
