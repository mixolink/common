package com.amituofo.common.ui.swingexts.component;

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.peer.LightweightPeer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import com.amituofo.common.ui.resource.Icons;

public class JESplitButton extends JToolBar {
	private final AbstractButton mainButton;
	private final JButton arrowButton;
	private final JPopupMenu popupMenu = new JPopupMenu();
	private final List<BeforePopupMenuListener> listeners = new ArrayList<>();

	public interface BeforePopupMenuListener {
		void beforePopupMenuShown(JPopupMenu popupMenu);
	}

	public JESplitButton(Icon icon) {
		this(null, icon);
	}

	public JESplitButton(String text, Icon icon) {
		this(new JButton(text, icon));
	}

	public JESplitButton(AbstractButton mainbtn) {
		setFloatable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setLayout(new BorderLayout(0, 0));

		mainButton = mainbtn;
		arrowButton = new JButton(Icons.ARROW_ICON); // 默认外观

		mainButton.setFocusable(false);
		arrowButton.setFocusable(false);

		mainButton.setMargin(new Insets(2, 6, 2, 6));
		arrowButton.setMargin(new Insets(2, 4, 2, 4));

		// 边界分割线
//        arrowButton.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(180, 180, 180)));

		add(mainButton, BorderLayout.CENTER);
		add(arrowButton, BorderLayout.EAST);

		// 弹出菜单动作
		arrowButton.addActionListener(e -> {
			for (BeforePopupMenuListener l : listeners)
				l.beforePopupMenuShown(popupMenu);
			popupMenu.show(arrowButton, 0, arrowButton.getHeight());
		});
	}

	public AbstractButton getMainButton() {
		return mainButton;
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setIcon(Icon defaultIcon) {
		mainButton.setIcon(defaultIcon);
	}

	public void setToolTipText(String text) {
		mainButton.setToolTipText(text);
	}

	public void setText(String text) {
		mainButton.setText(text);
	}

	public void addBeforePopupMenuListener(BeforePopupMenuListener listener) {
		listeners.add(listener);
	}

	public void addMainButtonActionListener(ActionListener actionListener) {
		mainButton.addActionListener(actionListener);
	}

	public void addMainButtonMouseListener(MouseListener l) {
		if (l == null) {
			return;
		}
		mainButton.addMouseListener(l);
	}

	public JMenuItem addMenuItem(String text, ActionListener listener) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(listener);
		popupMenu.add(item);
		return item;
	}

	public JMenuItem addMenuItem(JMenuItem item) {
		popupMenu.add(item);
		return item;
	}

	public void addSeparator() {
		popupMenu.addSeparator();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension main = mainButton.getPreferredSize();
		Dimension arrow = arrowButton.getPreferredSize();
		// 限制箭头宽度更窄，但保持相同高度
		arrow.width = 14;
		arrow.height = main.height;
		arrowButton.setPreferredSize(arrow);

		return new Dimension(main.width + arrow.width, main.height);
	}

	// Demo
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> {
//			JFrame f = new JFrame("SplitButton Demo");
//			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			JToolBar tb = new JToolBar();
//			tb.setFloatable(false);
//
//			SplitButton sb = new SplitButton("Save", null);
//			sb.addActionListener(e -> JOptionPane.showMessageDialog(f, "Save."));
//			sb.addMenuItem("Save As...", e -> JOptionPane.showMessageDialog(f, "Save As..."));
//			sb.addMenuItem("Save Copy", e -> JOptionPane.showMessageDialog(f, "Save Copy"));
//			sb.addSeparator();
//			sb.addMenuItem("Save Template", e -> JOptionPane.showMessageDialog(f, "Save Template"));
//
//			tb.add(sb);
//			f.add(tb, BorderLayout.NORTH);
//			f.setSize(300, 150);
//			f.setVisible(true);
//		});
//	}

}
