package com.amituofo.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.amituofo.common.ui.action.SelectionAction;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialog;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialogContentPanel;

public class ColorChooser extends SimpleDialogContentPanel implements ChangeListener {

	protected JColorChooser tcc;
	// private Color newColor;
	// protected JLabel banner;
	// private static Color newColor;
	private SelectionAction<Color> selection;
	private Color newColor;

	public ColorChooser(Color initialColor, SelectionAction<Color> selection) {
		super();
		setLayout(new BorderLayout(0, 0));
		this.selection = selection;

		// Set up the banner at the top of the window
		// banner = new JLabel("", JLabel.CENTER);
		// banner.setForeground(Color.yellow);
		// banner.setBackground(Color.blue);
		// banner.setOpaque(true);
		// banner.setFont(new Font("SansSerif", Font.BOLD, 24));
		// banner.setPreferredSize(new Dimension(100, 65));

		// JPanel bannerPanel = new JPanel(new BorderLayout());
		// bannerPanel.add(banner, BorderLayout.CENTER);
		// bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));

		// Set up color chooser for setting text color
		tcc = new JColorChooser(initialColor == null ? Color.BLUE : initialColor);
		tcc.getSelectionModel().addChangeListener(this);
		tcc.setBorder(BorderFactory.createTitledBorder("Choose Color"));

		// add(tcc, BorderLayout.CENTER);
		add(tcc);
	}

	public void stateChanged(ChangeEvent e) {
		newColor = tcc.getColor();
		// banner.setBackground(newColor);
	}

	public static void showDialog(Color initialColor, SelectionAction<Color> selection) {
		SimpleDialog.open(new ColorChooser(initialColor, selection), 450, 380);
	}

	// private static void createAndShowGUI(Color initialColor) {
	// // Create and set up the window.
	// JFrame frame = new JFrame("ColorChooserDemo");
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// // Create and set up the content pane.
	// JComponent newContentPane = new ColorChooserDemo(initialColor);
	// newContentPane.setOpaque(true); // content panes must be opaque
	// frame.setContentPane(newContentPane);
	//
	// // Display the window.
	// frame.pack();
	// frame.setVisible(true);
	// }

//	public static void main(String[] args) {
//		// Schedule a job for the event-dispatching thread:
//		// creating and showing this application's GUI.
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				// createAndShowGUI(Color.blue);
//				showDialog(Color.blue, new SelectionAction<Color>() {
//
//					@Override
//					public boolean selected(Color item) {
//						return true;
//					}
//				});
//			}
//		});
//	}

	@Override
	public boolean okPressed() {
		if (selection != null)
			selection.selected(newColor);

		return true;
	}

	@Override
	public boolean cancelPressed() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Color";
	}

	@Override
	public ImageIcon getIcon() {
		return null;
	}
}