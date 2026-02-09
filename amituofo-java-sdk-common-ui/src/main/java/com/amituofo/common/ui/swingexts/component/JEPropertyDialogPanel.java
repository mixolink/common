package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.amituofo.common.kit.kv.KeyValue;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialogContentPanel;

public class JEPropertyDialogPanel extends SimpleDialogContentPanel {

	public JEPropertyDialogPanel(List<KeyValue> kvlist) {
		setLayout(new BorderLayout(0, 0));
		
		// Define column names
		String[] columnNames = { "Name", "Value" };

		// Create a DefaultTableModel with column names
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

		// Create a JTable with the table model
		JTable table = new JTable(tableModel);

		// Add some example data
		if (kvlist != null) {
			for (KeyValue kv : kvlist) {
				tableModel.addRow(new Object[] { kv.getKey(), kv.getStringValue() });
			}
		}
		
//		tableModel.addRow(new Object[] { "Name", "Alice" });
//		tableModel.addRow(new Object[] { "Age", "30" });
//		tableModel.addRow(new Object[] { "Country", "Wonderland" });

		// Add table to a JScrollPane
		JScrollPane scrollPane = new JScrollPane(table);

		TableColumn keyColumn = table.getColumnModel().getColumn(0);
//		keyColumn.setMaxWidth(250);

		// Set dialog layout and add components
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);

		// Set dialog size and location
//		this.setSize(300, 200);
//		this.setLocationRelativeTo(null);
	}

	@Override
	public boolean okPressed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean cancelPressed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getTitle() {
		return "Properties";
	}
	
	@Override
	public ImageIcon getIcon() {
		return null;//GlobalIcon.UI16x16.of(GlobalIconNames.ICON_INFO_2_16x16;
	}
}
