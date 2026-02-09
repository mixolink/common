package com.amituofo.common.ui.swingexts.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.amituofo.common.type.RunStatus;
import com.amituofo.common.ui.util.UIUtils;

import javax.swing.UIManager;

public abstract class DefaultStatusRenderer extends TableLabelCellRenderer {
	public final static Color Unknown = new Color(120, 144, 156);
	public final static Color Pending = new Color(66, 165, 245);
	public final static Color Initializing = new Color(100, 181, 246);
	public final static Color Starting = new Color(25, 118, 210);
	public final static Color Running = new Color(0, 172, 117);// 0, 1, 217 0, 172, 117
	public final static Color Pause = new Color(120, 144, 156);
	public final static Color Idle = new Color(120, 144, 156);
	public final static Color Restarting = new Color(142, 36, 170);
	public final static Color Stoping = new Color(142, 36, 170);
	public final static Color Stoped = new Color(117, 117, 117);
	public final static Color Interrupting = new Color(142, 36, 170);
//	public final static Color Interrupted = new Color(215, 204, 200);
//	public final static Color Finishing = new Color(186, 104, 200);
//	public final static Color Finished = new Color(215, 204, 200);
	public final static Color Error = new Color(211, 47, 47);
	public final static Color Alert = new Color(255, 160, 0);

	public final static Color Text = Color.WHITE;// new Color(251, 251, 251);

//	private final static Color CLR_GREEN1 = new Color(0, 204, 255);
//	private final static Color CLR_GREEN2 = new Color(51, 153, 204);
//	private final static Color CLR_GREEN3 = new Color(0, 204, 153);
//	private final static Color CLR_SPEED_SLOW = new Color(13, 71, 161);
//	private final static Color CLR_SPEED_NORMAL = new Color(0, 209, 189);
//	private final static Color CLR_SPEED_FAST = new Color(0, 223, 89);
//
//	private final static Color CLR_STOP = new Color(215, 204, 200);
//
//	private final static Color CLR_WORKING = new Color(0, 137, 123);
//
//
//	private final static Color WN_CLR = new Color(255, 215, 0);
//	private final static Color NG_CLR = new Color(255, 99, 71);// new Color(255, 69, 0);
//	private final static Color OK_CLR = new Color(102, 205, 170);// new Color(60, 179, 113);
//	private final static Color WK_CLR = new Color(222, 184, 135);// new Color(65, 105, 225);

	public DefaultStatusRenderer() {
		super();
		setBorder(new LineBorder(UIManager.getColor("controlLtHighlight"), 1));
		setForeground(Text);
//		setFont(UIUtils.deriveFontStyle(Font.BOLD));
	}

//	public void setText(String text) {
//		setForeground(Text);
//		super.setText(text);
//	}

//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> {
//			createAndShowGUI();
//		});
//	}
//
//	private static void createAndShowGUI() {
//		JFrame frame = new JFrame("状态颜色示例");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new GridLayout(0, 2, 10, 5));
//		frame.getContentPane().setBackground(Color.WHITE);
//
//		// 用 LinkedHashMap 保持插入顺序
//		Map<String, Color> colorMap = new LinkedHashMap<>();
//		colorMap.put("Unknown", Unknown);
//		colorMap.put("Pending", Pending);
//		colorMap.put("Starting", Starting);
//		colorMap.put("Initializing", Initializing);
//		colorMap.put("Running", Running);
//		colorMap.put("Pause", Pause);
//		colorMap.put("Idle", Idle);
//		colorMap.put("Restarting", Restarting);
//		colorMap.put("Stoping", Stoping);
//		colorMap.put("Stoped", Stoped);
//		colorMap.put("Interrupting", Interrupting);
//		colorMap.put("Error", Error);
//		colorMap.put("Alert", Alert);
//
//		colorMap.put("WN_CLR", WN_CLR);
//		colorMap.put("NG_CLR", NG_CLR);
//		colorMap.put("OK_CLR", OK_CLR);
//		colorMap.put("WK_CLR", WK_CLR);
//
//		colorMap.put("CLR_GREEN1", CLR_GREEN1);
//		colorMap.put("CLR_GREEN2", CLR_GREEN2);
//		colorMap.put("CLR_GREEN3", CLR_GREEN3);
//		colorMap.put("CLR_SPEED_SLOW", CLR_SPEED_SLOW);
//		colorMap.put("CLR_SPEED_NORMAL", CLR_SPEED_NORMAL);
//		colorMap.put("CLR_SPEED_FAST", CLR_SPEED_FAST);
//		colorMap.put("CLR_STOP", CLR_STOP);
//		colorMap.put("CLR_WORKING", CLR_WORKING);
//
//		for (Map.Entry<String, Color> entry : colorMap.entrySet()) {
//			String name = entry.getKey();
//			Color color = entry.getValue();
//
//			JPanel panel = new JPanel(new BorderLayout());
//			panel.setBackground(color);
//			panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
//
//			JLabel label = new JLabel(name + "  RGB: (" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
//			label.setHorizontalAlignment(SwingConstants.CENTER);
//
//			// 自动选择字体颜色：亮色背景用黑字，暗色背景用白字
//			double brightness = (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
//			label.setForeground(brightness > 150 ? Color.BLACK : Color.WHITE);
//
//			panel.add(label, BorderLayout.CENTER);
//			frame.getContentPane().add(panel);
//		}
//
//		frame.pack();
//		frame.setSize(500, 500);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}

}