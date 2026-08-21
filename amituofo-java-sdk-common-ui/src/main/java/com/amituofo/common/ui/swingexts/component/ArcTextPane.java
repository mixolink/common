package com.amituofo.common.ui.swingexts.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.StyledDocument;

import com.amituofo.common.ui.define.TextFieldMenu;

public class ArcTextPane extends JTextPane {
	private static final long serialVersionUID = 1L;

	public ArcTextPane() {
		init();
	}

	public ArcTextPane(StyledDocument doc) {
		super(doc);
		init();
	}

	private void init() {
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
		installTextMenu();
	}

	private void installTextMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem cutMenu = new JMenuItem(TextFieldMenu.Cut.getTitle());
		JMenuItem copyMenu = new JMenuItem(TextFieldMenu.Copy.getTitle());
		JMenuItem pasteMenu = new JMenuItem(TextFieldMenu.Paste.getTitle());
		JMenuItem selectAllMenu = new JMenuItem(TextFieldMenu.SelectAll.getTitle());

		cutMenu.addActionListener(e -> cut());
		copyMenu.addActionListener(e -> copy());
		pasteMenu.addActionListener(e -> paste());
		selectAllMenu.addActionListener(e -> selectAll());

		popupMenu.add(cutMenu);
		popupMenu.add(copyMenu);
		popupMenu.add(pasteMenu);
		popupMenu.add(new JSeparator());
		popupMenu.add(selectAllMenu);
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				boolean hasSelection = getSelectionStart() != getSelectionEnd();
				cutMenu.setEnabled(isEditable() && isEnabled() && hasSelection);
				copyMenu.setEnabled(hasSelection);
				pasteMenu.setEnabled(isEditable() && isEnabled() && hasTextOnClipboard());
				selectAllMenu.setEnabled(getDocument().getLength() > 0);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		setComponentPopupMenu(popupMenu);
	}

	private boolean hasTextOnClipboard() {
		try {
			Transferable content = getToolkit().getSystemClipboard().getContents(this);
			return content != null && content.isDataFlavorSupported(DataFlavor.stringFlavor);
		} catch (IllegalStateException | SecurityException e) {
			return false;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		int arc = UIManager.getInt("Component.arc");
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			// 圆角矩形填充,范围和 border 的 arc 保持一致,精确盖住整个组件区域
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		} finally {
			g2.dispose();
		}
		// setOpaque(false) 之后 super 不会再画方形背景,只会画文字内容
		super.paintComponent(g);
	}

}
