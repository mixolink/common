package com.amituofo.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MessageTip extends JDialog {

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					String msg = "\"在图形用户界面（GUI）环境中，大体上有两种类型的窗体： 框架窗体（frame window）和对话窗体（dialog window）。 <br/>在Java中，我们使用JFrame对象作为框架窗体，使用JDialog对象作为对话窗体。下面来熟悉JOptionPane类的几种常用方法：<br/>\\r\\n\\r\\n1.用于输出的JOptionPane类方法--showMesageDialog（）\"";
//					MessageTip dialog = new MessageTip(msg);
//					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//					dialog.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the dialog.
	 */
	public MessageTip(String message) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setUndecorated(true);
		// getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setType(Type.UTILITY);
		setResizable(false);

		JTextPane pane = new JTextPane();
		pane.setBackground(SystemColor.info);
		pane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		pane.setText(message);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MessageTip.this.dispose();
			}
		});
		pane.setEditable(false);

		getContentPane().add(pane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBackground(SystemColor.info);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);

		setBounds(0, 0, (int)pane.getPreferredSize().getWidth(), (int)(pane.getPreferredSize().getHeight()*1.5));

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		int w = this.getWidth();
		int h = this.getHeight();
		this.setLocation((width - w) / 2, (height - h) / 2);
	}

}
