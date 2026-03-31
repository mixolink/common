package com.amituofo.common.ui.swingexts.component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.amituofo.common.api.Callback;
import com.amituofo.common.ui.define.TextFieldMenu;
import com.amituofo.common.ui.util.UIUtils;

public class JEPopupTextField extends JTextField implements MouseListener {

	private static final long serialVersionUID = -406608462064697359L;
	private boolean popupVisable = false;
	private JPopupMenu popupMenu = null;
	private JMenuItem cutMenu = null, copyMenu = null, pasteMenu = null, replacePasteGoMenu = null, selectAllMenu = null;

	private List<Callback<String>> entryListener = new ArrayList<>();

	public JEPopupTextField() {

		super();
		popupMenu = new JPopupMenu();
		popupMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				popupVisable = true;
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				popupVisable = false;
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				popupVisable = false;
			}
		});

		cutMenu = new JMenuItem(TextFieldMenu.Cut.getTitle());
		copyMenu = new JMenuItem(TextFieldMenu.Copy.getTitle());
		pasteMenu = new JMenuItem(TextFieldMenu.Paste.getTitle());
		replacePasteGoMenu = new JMenuItem(TextFieldMenu.ClearPasteGo.getTitle());
		selectAllMenu = new JMenuItem(TextFieldMenu.SelectAll.getTitle());

		cutMenu.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		copyMenu.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		pasteMenu.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		selectAllMenu.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));

		cutMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		});
		copyMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		pasteMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});
		replacePasteGoMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JEPopupTextField.this.setText("");
				paste();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
				}
				UIUtils.invokeLater(() -> {
					fireEnter();
				});
			}
		});
		selectAllMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});

		popupMenu.add(cutMenu);
		popupMenu.add(copyMenu);
		popupMenu.add(pasteMenu);
//		popupMenu.add(appendPasteGoMenu);
		popupMenu.add(replacePasteGoMenu);
		popupMenu.add(new JSeparator());
		popupMenu.add(selectAllMenu);

//		this.add(popupMenu);
		this.addMouseListener(this);

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent evt) {
			}

			@Override
			public void keyReleased(KeyEvent evt) {
			}

			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					fireEnter();
				}
			}
		});
	}

	private void fireEnter() {
		String text = this.getText();
		for (Callback<String> callback : entryListener) {
			callback.callback(text);
		}
	}

	public void addEnterListener(Callback<String> listener) {
		entryListener.add(listener);
	}

	public boolean isPopupMenuVisable() {
		return popupVisable;
	}

	public void mousePressed(MouseEvent e) {
		popupMenuTrigger(e);
	}

	public void mouseReleased(MouseEvent e) {
		popupMenuTrigger(e);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	private void popupMenuTrigger(MouseEvent e) {

		if (e.isPopupTrigger()) {

			this.requestFocusInWindow();

			cutMenu.setEnabled(isAbleToCopyAndCut());
			copyMenu.setEnabled(isAbleToCopyAndCut());
			pasteMenu.setEnabled(isAbleToPaste());
			selectAllMenu.setEnabled(isAbleToSelectAll());

			popupMenu.show(this, e.getX() + 3, e.getY() + 3);
		}
	}

	private boolean isAbleToSelectAll() {
		return !("".equalsIgnoreCase(this.getText()) || (null == this.getText()));
	}

	private boolean isAbleToCopyAndCut() {

		return (this.getSelectionStart() != this.getSelectionEnd());
	}

	private boolean isAbleToPaste() {

		Transferable content = this.getToolkit().getSystemClipboard().getContents(this);
		try {
			return (content.getTransferData(DataFlavor.stringFlavor) instanceof String);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}