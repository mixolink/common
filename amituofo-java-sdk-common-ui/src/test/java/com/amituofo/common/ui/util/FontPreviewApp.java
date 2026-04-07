package com.amituofo.common.ui.util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FontPreviewApp extends JFrame {

	private static final int MAX_COLUMNS = 3; // 最多三列
	private static final int CELL_HEIGHT = 120; // 统一格子高度
	private String previewText = "Hello 世界 123. Abc 字体效果 The quick brown fox";
	private int fontSize = 22;

	private JPanel gridPanel;

	public FontPreviewApp() {
		setTitle("系统字体预览（最多3列・高度统一）");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// ==================== 顶部控制区 ====================
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		// 预览文字
		JPanel row1 = new JPanel(new BorderLayout(10, 0));
		row1.add(new JLabel("预览文字："), BorderLayout.WEST);
		JTextField textField = new JTextField(previewText, 40);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateText();
			}

			public void removeUpdate(DocumentEvent e) {
				updateText();
			}

			public void changedUpdate(DocumentEvent e) {
				updateText();
			}

			private void updateText() {
				String t = textField.getText().trim();
				if (!t.isEmpty()) {
					previewText = t;
					refreshAll();
				}
			}
		});
		row1.add(textField, BorderLayout.CENTER);
		top.add(row1);

		// 字号滑块
		JPanel row2 = new JPanel(new BorderLayout(10, 0));
		row2.add(new JLabel("字号："), BorderLayout.WEST);
		JSlider slider = new JSlider(12, 60, fontSize);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(e -> {
			fontSize = slider.getValue();
			refreshAll();
		});
		row2.add(slider, BorderLayout.CENTER);
		top.add(Box.createVerticalStrut(8));
		top.add(row2);

		add(top, BorderLayout.NORTH);

		// ==================== 网格区 ====================
		gridPanel = new JPanel(new GridLayout(0, MAX_COLUMNS, 12, 12));
		gridPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		gridPanel.setBackground(new Color(245, 245, 245));

		JScrollPane scroll = new JScrollPane(gridPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		add(scroll, BorderLayout.CENTER);

		// 填充字体
		populateFonts();

		// ==================== 窗口大小 ====================
		pack();
		// pack 后把高度压低，视觉更紧凑
		setSize(getWidth(), Math.min(getHeight(), Toolkit.getDefaultToolkit().getScreenSize().height * 7 / 10));
		setMinimumSize(new Dimension(800, 500));
//        setLocationRelativeToNiceSize();
		setLocationRelativeTo(null);
	}

	private void setSizeToNiceSize() {
		// 让宽度刚好能放3列且不出现横向滚动条
		int cellWidth = 320;
		int totalWidth = cellWidth * MAX_COLUMNS + 100; // 边距+滚动条预留
		setPreferredSize(new Dimension(totalWidth, 700));
		pack();
	}

	private String[] getAllFontNames() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return ge.getAvailableFontFamilyNames();
	}

	private void populateFonts() {
		gridPanel.removeAll();

		for (String fontName : getAllFontNames()) {
			JPanel cell = new JPanel(new BorderLayout());
			cell.setPreferredSize(new Dimension(300, CELL_HEIGHT));
			cell.setMinimumSize(new Dimension(250, CELL_HEIGHT));
			cell.setMaximumSize(new Dimension(2000, CELL_HEIGHT));
			cell.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
					BorderFactory.createEmptyBorder(12, 12, 12, 12)));
			cell.setBackground(Color.WHITE);

			JLabel nameLabel = new JLabel(fontName);
			nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

			JLabel previewLabel = new JLabel("<html>" + previewText.replace("\n", "<br>") + "</html>");
			previewLabel.setFont(new Font(fontName, Font.PLAIN, fontSize));

			JPanel center = new JPanel(new BorderLayout());
			center.add(previewLabel, BorderLayout.CENTER);

			cell.add(nameLabel, BorderLayout.NORTH);
			cell.add(center, BorderLayout.CENTER);

			gridPanel.add(cell);
		}

		revalidate();
		repaint();
	}

	private void refreshAll() {
		for (Component comp : gridPanel.getComponents()) {
			if (comp instanceof JPanel) {
				JPanel cell = (JPanel) comp;
				for (Component c : cell.getComponents()) {
					if (c instanceof JLabel) {
						JLabel lbl = (JLabel) c;
						String text = lbl.getText();
						if (text.contains("html") || text.contains(previewText.substring(0, Math.min(10, previewText.length())))) {
							lbl.setText("<html>" + previewText.replace("\n", "<br>") + "</html>");
							lbl.setFont(lbl.getFont().deriveFont((float) fontSize));
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			FontPreviewApp app = new FontPreviewApp();
			app.setVisible(true);
		});
	}
}