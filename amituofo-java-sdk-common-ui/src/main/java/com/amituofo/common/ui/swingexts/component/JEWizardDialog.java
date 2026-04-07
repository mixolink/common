package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;
import java.awt.Image;

import com.amituofo.common.ui.swingexts.dialog.SimpleDialog;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialogOption;

public class JEWizardDialog {

	public static void setDefaultLocationRelativeTo(Component DEFAULT_LOCATION_RELATIVE_TO) {
		SimpleDialog.setDefaultLocationRelativeTo(DEFAULT_LOCATION_RELATIVE_TO);
	}

	public static SimpleDialog open(JEWizardPanelContainer panel) {
		return open(panel, 800, 538);
	}

	public static SimpleDialog open(JEWizardPanelContainer panel, int width, int height) {
		return open(panel, null, width, height, true, false);
	}

	public static SimpleDialog open(JEWizardPanelContainer panel, Image titleIcon, int width, int height) {
		return open(panel, titleIcon, width, height, true, false);
	}

	private static SimpleDialog open(JEWizardPanelContainer dialogContentPanel, Image titleIcon, int width, int height, boolean withContentBorder, boolean closeClickOutsite) {
//		WizardDialog dlg = new WizardDialog(dialogContentPanel, titleIcon, width, height, withContentBorder, closeClickOutsite);
//		UIUtils.invokeLater(() -> {
//			dlg.show();
//		});

		SimpleDialog dlg = SimpleDialog.open(dialogContentPanel,
				SimpleDialogOption.New().withWidth(width).withHeight(height).withTitleIcon(titleIcon).withEmptyBorder(withContentBorder).withCloseClickOutsite(closeClickOutsite));
		dialogContentPanel.showFirst();
		return dlg;
	}

	/**
	 * Create the dialog.
	 */
//	private WizardDialog(final WizardContainerPanel dialogContentPanel, Image titleIcon, int width, int height, boolean withContentBorder, boolean closeClickOutsite) {
//		dialog = new JDialog();
//		dialog.setIconImage(titleIcon);
//
//		dialogContentPanel.setWizardDialog(this);
//
//		if (!closeClickOutsite) {
//			dialog.setModal(true);
//		} else {
//			dialog.addWindowFocusListener(new WindowFocusListener() {
//				public void windowLostFocus(WindowEvent e) {
//					dialog.dispose();
//				}
//
//				public void windowGainedFocus(WindowEvent e) {
//				}
//			});
//		}
//		dialog.setBounds(100, 100, width, height);
//		if (DEFAULT_LOCATION_RELATIVE_TO != null) {
//			dialog.setLocationRelativeTo(DEFAULT_LOCATION_RELATIVE_TO);
//		}
//		Container mainPanel = dialog.getContentPane();
//		mainPanel.setLayout(new BorderLayout());
//		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//		mainPanel.add(contentPanel, BorderLayout.CENTER);
//		contentPanel.setLayout(new BorderLayout(0, 0));
////		setTitle(dialogContentPanel.getTitle());
//		setContentPanel(dialogContentPanel);
//		if (!withContentBorder) {
//			contentPanel.setBorder(null);
//		}
//
//		// JPanel toolPane = new JPanel();
//		// toolPane.setLayout(new BorderLayout());
//		// mainPanel.add(toolPane, BorderLayout.SOUTH);
//		//
//		// JPanel additionToolPanelPane = new JPanel();
//		// additionToolPanelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
//		// toolPane.add(additionToolPanelPane, BorderLayout.WEST);
//		//
////		JComponent[] cs = dialogContentPanel.getAdditionComponents();
//		// if (cs != null) {
//		// for (JComponent jComponent : cs) {
//		// additionToolPanelPane.add(jComponent);
//		// }
//		// }
//
//		JPanel buttonPane = new JPanel();
//		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		// toolPane.add(buttonPane, BorderLayout.EAST);
//
//		dialog.addWindowListener(new WindowAdapter() {
//
//			@Override
//			public void windowClosing(WindowEvent e) {
//				// TODO Auto-generated method stub
////				if (dialogContentPanel.cancelPressed()) {
////					WizardDialog.this.dispose();
////				}
//			}
//
//		});
//		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	}
//
//	public WizardContainerPanel getContentPanel() {
//		return panel;
//	}
//
//	private void setContentPanel(WizardContainerPanel panel) {
//		this.panel = panel;
////		panel.setParentDialog(this);
//		contentPanel.add(panel, BorderLayout.CENTER);
//
//		panel.showFirst();
//	}
//
//	private void show() {
//		dialog.toFront();
//		dialog.setVisible(true);
//	}
//
//	public synchronized void dispose() {
//		if (dialog != null) {
//			dialog.dispose();
//			dialog = null;
//		}
//	}
//
//	public void setTitle(String title) {
//		dialog.setTitle(title);
//	}

}
