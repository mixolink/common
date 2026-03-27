package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.amituofo.common.ex.InvalidConfigException;
import com.amituofo.common.kit.config.Configuration;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialogContentPanel;
import com.amituofo.common.ui.util.UIUtils;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;

public class JEWizardPanelContainer extends SimpleDialogContentPanel {
	public static enum LabelOfButton {
		Next("Next >>"), Previous("<< Previous"), Finish("Finish");

		private String label;

		LabelOfButton(String label) {
			this.label = label;
		}

		String label() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}

	private JPanel cardcontainer;
	private JEWizardPanel[] wizardPanels;
	private JEWizardPanel topWizardPanel;
	private int topWizardIndex = 0;
	private JButton btnPrevious;
	private JButton btnNext;
	private boolean isLast = false;
//	private SimpleDialog wizardDialog;
	protected Configuration config = new Configuration();
	private Class<? extends JEWizardPanel>[] wizardPanelClasses;
	private JTextPane textPane;
	private JLabel stepLabel;
	private JLabel lblNewLabel;
	private JLabel[] wizardSteps;
	private int totalActiveWizardCount;
	private JPanel panel_2;
	private JPanel panelWizardStepWithAll;
	private JPanel panel_7;
//	private JPanel panelWizardStepWithActived;
	private int totalWizardStepCount;
	private Color currentwizardStepActiveBackground, currentwizardStepActiveForeground, currentwizardStepInactiveBackground, currentwizardStepInactiveForeground;

	public JEWizardPanelContainer() {
		setLayout(new BorderLayout(0, 0));

		currentwizardStepActiveBackground = UIManager.getColor("AUI.Wizard.StepTitle.activeBackground");
		if (currentwizardStepActiveBackground == null) {
			currentwizardStepActiveBackground = UIManager.getColor("ComboBox.selectionBackground");
		}
		currentwizardStepActiveForeground = UIManager.getColor("AUI.Wizard.StepTitle.activeForeground");
		if (currentwizardStepActiveForeground == null) {
			currentwizardStepActiveForeground = UIManager.getColor("ComboBox.selectionForeground");
		}
		currentwizardStepInactiveBackground = UIManager.getColor("AUI.Wizard.StepTitle.inactiveBackground");
		if (currentwizardStepInactiveBackground == null) {
			currentwizardStepInactiveBackground = UIManager.getColor("ComboBox.disabledBackground");
		}
		currentwizardStepInactiveForeground = UIManager.getColor("AUI.Wizard.StepTitle.inactiveForeground");
		if (currentwizardStepInactiveForeground == null) {
			currentwizardStepInactiveForeground = UIManager.getColor("ComboBox.disabledForeground");
		}

		JPanel panel = new JPanel();
//		panel.setBackground(new Color(210, 180, 140));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel toolBar = new JPanel();
		toolBar.setOpaque(false);
//		toolBar.setFloatable(false);
		panel.add(toolBar, BorderLayout.EAST);
		toolBar.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("120px"), FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("23px"), FormSpecs.RELATED_GAP_ROWSPEC, }));

		btnPrevious = new JButton(LabelOfButton.Previous.label());
		btnPrevious.setEnabled(false);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPreview();
			}
		});

//		toolBar.add(btnPreview, "2, 2, fill, fill");

		btnNext = new JButton(LabelOfButton.Next.label());
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isLast) {
					finish();
				} else {
					showNext();
				}
			}
		});
		toolBar.add(btnNext, "2, 2, fill, fill");

		JSeparator separator = new JSeparator();
		panel.add(separator, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("120px"), FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("23px"), FormSpecs.RELATED_GAP_ROWSPEC, }));

		panel_1.add(btnPrevious, "2, 2, fill, center");

		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));

		stepLabel = new JLabel("1 / 10");
//		stepLabel.setForeground(new Color(210, 180, 140));
		stepLabel.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panel_6.add(stepLabel);

		panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.PREF_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14dlu"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.PREF_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, }));

//		JLabel lblNewLabel_2 = new JLabel("New label");
//		panel_7.add(lblNewLabel_2);

		JPanel panel_5 = new JPanel();
//		panel_5.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(65, 105, 225)));
		panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_5.setBackground(UIManager.getColor("AUI.Frame.DecorateLine.background"));
//		panel_5.setBackground(new Color(72, 209, 204));
//		JPanel panel_5 = new JPanel();
//		panel_5.setBackground(new Color(65, 105, 225));
		panel_2.add(panel_5, "1, 1, 7, 1, fill, fill");

		panel_7 = new JPanel();
//		panel_7.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.add(panel_7, "1, 3, 7, 1, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		panelWizardStepWithAll = new JPanel();
		panel_7.add(panelWizardStepWithAll, BorderLayout.CENTER);

//		JLabel lblNewLabel_2 = new JLabel("New label");
//		lblNewLabel_2.setBackground(UIManager.getColor("Button.shadow"));
//		panelWizardStepWithAll.add(lblNewLabel_2);

//		panel_7.setLayout(new GridLayout(0, 2, 0, 0));

//		panelWizardStepWithAll = new JPanel();
//		panelWizardStepWithActived = new JPanel();
//		panel_7.setLayout(new CardLayout(0, 0));
//		panel_7.add(panelWizardStepWithAll, "panelWizardStepWithAll");
//		panel_7.add(panelWizardStepWithActived, "panelWizardStepWithActived");

//		JLabel lblNewLabel_2 = new JLabel("New label");
//		lblNewLabel_2.setBorder(new MatteBorder(0, 1, 0, 1, (Color) UIManager.getColor("Button.shadow")));
//		panel_7.add(lblNewLabel_2);

		JSeparator separator_2 = new JSeparator();
		panel_2.add(separator_2, "1, 5, 7, 1, fill, fill");
//		panel_7.setLayout(new GridLayout(0, 3, 0, 0));
//
//		JLabel label = new JLabel("1");
//		label.setHorizontalAlignment(SwingConstants.CENTER);
//		label.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
//		label.setOpaque(true);
//		label.setBackground(new Color(102, 205, 170));
//		panel_7.add(label);
//
//		JLabel lblNewLabel_2 = new JLabel("New label");
//		panel_7.add(lblNewLabel_2);
//
//		JLabel lblNewLabel_3 = new JLabel("New label");
//		panel_7.add(lblNewLabel_3);

		lblNewLabel = new JLabel();
//		lblNewLabel.setIcon(new ImageIcon(WizardPanelContainer.class.getResource("/javax/swing/plaf/basic/icons/image-delayed.png")));
		panel_2.add(lblNewLabel, "3, 7, fill, top");

		textPane = new JTextPane();
//		textPane.setFont(UIUtils.deriveFont(Font.BOLD, UIUtils.getDefaultFont().getSize() + 1));
		textPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textPane.setBackground(UIManager.getColor("AUI.Wizard.StepDesc.background"));
		textPane.setForeground(UIManager.getColor("AUI.Wizard.StepDesc.foreground"));
		textPane.setEnabled(false);
		textPane.setEditable(false);
		panel_2.add(textPane, "5, 7, fill, fill");

		JSeparator separator_1 = new JSeparator();
		panel_2.add(separator_1, "1, 9, 7, 1, fill, top");

		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.WEST);

		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.EAST);

		cardcontainer = new JPanel();
		cardcontainer.setLayout(new CardLayout(0, 0));
		add(cardcontainer, BorderLayout.CENTER);
	}

	private int getTotalStepCount() {
		return totalActiveWizardCount;
	}

	private int getCurrentStepNumber() {
		int currentstep = topWizardIndex + 1;
		if (currentstep > totalActiveWizardCount) {
			currentstep -= (totalWizardStepCount - totalActiveWizardCount);
		}

		return currentstep;
	}

	public void updateWizardStep() {
		totalActiveWizardCount = 0;
		boolean[] wizardEnableStatus = new boolean[wizardPanelClasses.length];
		JEWizardPanel[] wps = new JEWizardPanel[wizardPanelClasses.length];

		for (int i = 0; i < wizardPanelClasses.length; i++) {
			wps[i] = buildWizardPanel(i, true);
		}

		for (int i = 0; i < wps.length; i++) {
			JEWizardPanel wizardPanel = wps[i];

			wizardEnableStatus[i] = wizardPanel.isEnabled();
			if (wizardEnableStatus[i]) {
				totalActiveWizardCount++;
			}

			wizardSteps[i].setText((i + 1) + "-" + wizardPanel.getTitle());
//			wizardSteps[i].setText("[ " + (i + 1) + " ]");
			wizardSteps[i].setEnabled(wizardEnableStatus[i]);
			if (topWizardPanel != null && topWizardPanel.index == i) {
				continue;
			}
			wizardSteps[i].setBackground(wizardEnableStatus[i] ? null : UIManager.getColor("Button.shadow"));
		}

		if (topWizardPanel != null) {
			stepLabel.setText(getCurrentStepNumber() + " / " + getTotalStepCount());
		}

		if (panelWizardStepWithAll.getComponentCount() == 0) {
			panelWizardStepWithAll.setLayout(new GridLayout(1, wizardSteps.length, 0, 0));
			for (int i = 0; i < wizardEnableStatus.length; i++) {
				panelWizardStepWithAll.add(wizardSteps[i]);
			}
		}
		this.revalidate();
		this.repaint();
//		((CardLayout) panel_7.getLayout()).show(panel_7, name);
	}

	public Configuration getConfig() {
		return config;
	}

	private void finish() {
		try {
			if (!topWizardPanel.updateConfig(config)) {
				return;
			}
			if (topWizardPanel.finish(config)) {
				getParentDialog().dispose();
			}
		} catch (InvalidConfigException e) {
			UIUtils.openError(e.getMessage(), e);
			return;
		}

	}

	public void showFirst() {
		JEWizardPanel panel = getWizardPanel(0);
		if (panel == null) {
			return;
		}

		activeWizard(panel);
	}

	protected void showNext() {
		if (!btnNext.isEnabled()) {
			return;
		}

		try {
			if (topWizardPanel == null || !topWizardPanel.updateConfig(config)) {
				return;
			}
		} catch (InvalidConfigException e) {
			UIUtils.openError(e.getMessage(), e);
			return;
		}

		JEWizardPanel panel = null;
		do {
			topWizardIndex++;
			isLast = topWizardIndex == wizardPanels.length - 1;

			panel = getWizardPanel(topWizardIndex);
			if (panel == null) {
				UIUtils.openError("Failed to create next wizard.");
				return;
			}
			if (isLast || panel.isEnabled()) {
				activeWizard(panel);
			} else {
				panel = null;
			}
		} while (panel == null);
	}

	protected void showPreview() {
		if (!btnPrevious.isEnabled()) {
			return;
		}

		JEWizardPanel panel = null;
		do {
			topWizardIndex--;
			isLast = topWizardIndex == wizardPanels.length - 1;

			panel = getWizardPanel(topWizardIndex);
			if (topWizardIndex == 0 || panel.isEnabled()) {
				activeWizard(panel);
			} else {
				panel = null;
			}
		} while (panel == null);
	}

	private JEWizardPanel getWizardPanel(int index) {
		JEWizardPanel panel = wizardPanels[index];
		if (panel == null) {
			panel = buildWizardPanel(index, false);
			if (panel != null) {
				wizardPanels[index] = panel;
				cardcontainer.add(panel, panel.getID());
			}
		}

		return panel;
	}

	private JEWizardPanel buildWizardPanel(int index, boolean test) {
		JEWizardPanel panel;
		Class<? extends JEWizardPanel> clazz = wizardPanelClasses[index];
		Constructor<? extends JEWizardPanel> c;
		try {
			if (test) {
				panel = clazz.newInstance();
			} else {
				c = clazz.getConstructor(Integer.class, Configuration.class);
				panel = c.newInstance(index, config);
				panel.setWizardContainer(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return panel;
	}

	private boolean activeWizard(JEWizardPanel panel) {
		if (!panel.isEnabled()) {
			return false;
		}

		if (isLast) {
			btnNext.setText(LabelOfButton.Finish.label());
		} else {
			btnNext.setText(LabelOfButton.Next.label());
		}
		btnPrevious.setText(LabelOfButton.Previous.label());
		btnPrevious.setEnabled(topWizardIndex != 0);

		if (topWizardPanel != null) {
			wizardSteps[topWizardPanel.index].setBackground(currentwizardStepInactiveBackground);
			wizardSteps[topWizardPanel.index].setForeground(currentwizardStepInactiveForeground);
			wizardSteps[topWizardPanel.index].setBorder(new MatteBorder(0, 1, 0, 1, (Color) UIManager.getColor("Button.shadow")));
			topWizardPanel.deactiving();
		}
		((CardLayout) cardcontainer.getLayout()).show(cardcontainer, panel.getID());
		if (topWizardPanel != null) {
			wizardPanels[topWizardPanel.index] = null;
		}
		topWizardPanel = panel;
		topWizardPanel.activing();
		getParentDialog().setTitle(topWizardPanel.getTitle());
		textPane.setText(topWizardPanel.getDescription());
		lblNewLabel.setIcon(topWizardPanel.getDescriptionIcon());
		stepLabel.setText(getCurrentStepNumber() + " / " + getTotalStepCount());

		wizardSteps[topWizardPanel.index].setBackground(currentwizardStepActiveBackground);
		wizardSteps[topWizardPanel.index].setForeground(currentwizardStepActiveForeground);
		wizardSteps[topWizardPanel.index].setBorder(new LineBorder(UIManager.getColor("controlLtHighlight"), 1));

		cardcontainer.revalidate();
		cardcontainer.repaint();

		return true;
	}

	public void addWizardPanels(Class<? extends JEWizardPanel>... wizardPanelClasses) {
		this.topWizardIndex = 0;
		this.totalWizardStepCount = wizardPanelClasses.length;
		this.wizardPanelClasses = wizardPanelClasses;
		this.cardcontainer.removeAll();
		if (wizardPanelClasses == null || wizardPanelClasses.length == 0) {
			return;
		}

		this.wizardPanels = new JEWizardPanel[wizardPanelClasses.length];

		this.wizardSteps = new JLabel[wizardPanelClasses.length];
		for (int i = 0; i < wizardSteps.length; i++) {
			JLabel label = new JLabel(i + 1 + ".");
			label.setHorizontalAlignment(SwingConstants.CENTER);
//			label.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			label.setOpaque(true);
			label.setFont(UIUtils.deriveFont(Font.BOLD, 10));
			label.setBackground(currentwizardStepInactiveBackground);
			label.setForeground(currentwizardStepInactiveForeground);
			label.setBorder(new MatteBorder(0, 1, 0, 1, (Color) UIManager.getColor("Button.shadow")));

			this.wizardSteps[i] = label;
		}

		updateWizardStep();
	}


	@Override
	public boolean okPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}
}
