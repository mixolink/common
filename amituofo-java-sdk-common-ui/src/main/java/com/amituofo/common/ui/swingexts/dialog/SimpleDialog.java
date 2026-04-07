package com.amituofo.common.ui.swingexts.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.amituofo.common.resource.IconResource;
import com.amituofo.common.ui.util.UIUtils;

public class SimpleDialog {
	private static Component DEFAULT_LOCATION_RELATIVE_TO = null;
//	private final Component originalDefaultUIParent = UIUtils.DEFAULT_MAIN_FRAME;

	private JDialog dialog;
	private JPanel contentPanel = new JPanel();
	private SimpleDialogContentPanel panel;

	public static void setDefaultLocationRelativeTo(Component DEFAULT_LOCATION_RELATIVE_TO) {
		SimpleDialog.DEFAULT_LOCATION_RELATIVE_TO = DEFAULT_LOCATION_RELATIVE_TO;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param titleIcon
	 */
	private SimpleDialog(final SimpleDialogContentPanel dialogContentPanel, SimpleDialogOption option) {
		dialog = new JDialog(option.getOwner());

		if (dialogContentPanel.getIcon() != null) {
			dialog.setIconImage(((ImageIcon) dialogContentPanel.getIcon()).getImage());
		} else if (option.getTitleIcon() != null) {
			dialog.setIconImage(option.getTitleIcon());
		}

		if (!option.isCloseClickOutsite()) {
			dialog.setModal(true);
			dialog.addWindowFocusListener(new WindowAdapter() {
				@Override
				public void windowLostFocus(WindowEvent e) {
					if (dialog != null)
						dialog.toFront();
				}
			});
		} else {
			dialog.addWindowFocusListener(new WindowFocusListener() {

				public void windowLostFocus(WindowEvent e) {
					dispose();
				}

				public void windowGainedFocus(WindowEvent e) {
				}
			});
		}
		
		dialog.setResizable(option.isResizeable());

		Container mainPanel = dialog.getContentPane();
//		((JComponent)mainPanel).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		dialog.setTitle(dialogContentPanel.getTitle());

		setContentPanel(dialogContentPanel);
		if (!option.isWithEmptyBorder()) {
			contentPanel.setBorder(null);
		} else {
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//			contentPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
		}

		// JPanel toolPane = new JPanel();
		// toolPane.setLayout(new BorderLayout());
		// mainPanel.add(toolPane, BorderLayout.SOUTH);
		//
		// JPanel additionToolPanelPane = new JPanel();
		// additionToolPanelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		// toolPane.add(additionToolPanelPane, BorderLayout.WEST);
		//
		JComponent[] cs = dialogContentPanel.getAdditionComponents();
		// if (cs != null) {
		// for (JComponent jComponent : cs) {
		// additionToolPanelPane.add(jComponent);
		// }
		// }

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// toolPane.add(buttonPane, BorderLayout.EAST);

		if (option.isWithOKButton()) {
			JButton okButton = new JButton("   OK   ");
			okButton.setIcon(IconResource.ICON_OK_16x16);
			okButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (dialogContentPanel.okPressed()) {
						dispose();
					}
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			if (dialog != null)
				dialog.getRootPane().setDefaultButton(okButton);
		}

		if (option.isWithCancelButton()) {
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setIcon(IconResource.ICON_CANCEL_16x16);
			cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (dialogContentPanel.cancelPressed()) {
						dispose();
					}
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}

		if (option.isWithCancelButton() || option.isWithOKButton() || cs != null && cs.length > 0) {
			JPanel toolPane = new JPanel();
			toolPane.setLayout(new BorderLayout());

			JSeparator separator = new JSeparator();
			toolPane.add(separator, BorderLayout.NORTH);

			JPanel additionToolPanelPane = new JPanel();
			additionToolPanelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			toolPane.add(additionToolPanelPane, BorderLayout.WEST);

			if (cs != null) {
				for (JComponent jComponent : cs) {
					if (jComponent != null)
						additionToolPanelPane.add(jComponent);
				}
			}

			toolPane.add(buttonPane, BorderLayout.EAST);
			mainPanel.add(toolPane, BorderLayout.SOUTH);
		}

		boolean autoDispose = (option.isCloseClickOutsite() || !option.isWithOKButton() && !option.isWithCancelButton());

		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (autoDispose) {
					dispose();
				} else {
					if (dialogContentPanel.cancelPressed()) {
						dispose();
					}
				}
			}

//			@Override
//			public void windowActivated(WindowEvent e) {
//				UIUtils.setDefaultTopFrame(dialog);
//			}
		});

		if (option.getWidth() > 0 && option.getHeight() > 0) {
			dialog.setBounds(100, 100, option.getWidth(), option.getHeight());
		} else {
			dialog.pack();
		}

		if (DEFAULT_LOCATION_RELATIVE_TO != null) {
			dialog.setLocationRelativeTo(DEFAULT_LOCATION_RELATIVE_TO);
		}

		Point p = dialog.getLocation();
		dialog.setLocation(p.x + option.getXOffset(), p.y + option.getYOffset());
	}

	private void show() {
		panel.showing();
		dialog.toFront();
		dialog.setVisible(true);
	}

	public synchronized void dispose() {
		if (panel != null) {
			panel.dispose();
			panel.destroy();
			panel = null;
		}
		if (dialog != null) {
			dialog.dispose();
			dialog = null;
		}
	}

	public void pack() {
		dialog.pack();
	}

	public void setTitle(String title) {
		dialog.setTitle(title);
	}

	public JDialog ofDialog() {
		return dialog;
	}

	public SimpleDialogContentPanel getContentPanel() {
		return panel;
	}

	private void setContentPanel(SimpleDialogContentPanel panel) {
		this.panel = panel;
		panel.setParentDialog(this);
		contentPanel.add(panel, BorderLayout.CENTER);
	}

	public static SimpleDialog open(SimpleDialogContentPanel panel) {
		return open(panel, new SimpleDialogOption().withWidth(800).withHeight(538));
	}

	public static SimpleDialog open(SimpleDialogContentPanel panel, int width, int height) {
		return open(panel, new SimpleDialogOption().withWidth(width).withHeight(height));
	}

	public static SimpleDialog open(SimpleDialogContentPanel dialogContentPanel, SimpleDialogOption option) {
		SimpleDialog dlg = new SimpleDialog(dialogContentPanel, option);
		UIUtils.invokeLater(() -> {
			dlg.show();
		});
		return dlg;
	}
	


}
