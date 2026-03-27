package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

//import org.apache.batik.util.gui.xmleditor.LineNumberBorder;
import org.apache.batik.util.gui.xmleditor.XMLTextEditor;

import com.amituofo.common.resource.IconResource;
import com.amituofo.common.ui.listener.DataSavingListener;
import com.amituofo.common.ui.util.UIUtils;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class JEXMLEditPanel extends JEPanel {
	protected JTextField nameTextField;
	protected JEditorPane contentEditor;
	protected JComboBox<String> encodeCombox;

	private DataSavingListener<String, byte[]> dataSavingListener;
	private JButton btnSave;

	public JEXMLEditPanel(boolean showNameField, boolean showXMLEditorSaveButton) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setBounds(0, 0, 445, 106);
		panel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("450px:grow"), FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default:grow"), FormSpecs.RELATED_GAP_ROWSPEC, }));
		contentEditor = new XMLTextEditor();
//		org.apache.batik.util.gui.xmleditor.XMLTextEditor.
//		Font font = new Font(contentEditor.getFont().getFontName(),contentEditor.getFont().getStyle(),18);
//		contentEditor.setFont(font);
//		{
//	        LineNumberBorder lineNumberBorder = new LineNumberBorder();
////	        lineNumberBorder.setPadding(5);  // 可选：调整行号边框与文本之间的间距
//	        // 将行号边框设置为 JTextArea 的边框
////	        setBorder(lineNumberBorder);
//	    };
		contentEditor.setBackground(UIManager.getColor("TextField.background"));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "2, 3, fill, fill");

		scrollPane.setViewportView(contentEditor);
		contentEditor.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, "2, 1, fill, fill");
		panel_2.setLayout(new BorderLayout(0, 0));

//		JLabel lblContent = new JLabel("Content:");
//		panel_2.add(lblContent, BorderLayout.WEST);

		JToolBar toolBar = new JToolBar();
		panel_2.add(toolBar, BorderLayout.EAST);
		toolBar.setFloatable(false);

		encodeCombox = new JComboBox<String>();
		encodeCombox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String metaContent = contentEditor.getText();
				if (metaContent != null && metaContent.length() > 0) {
					try {
						metaContent = new String(metaContent.getBytes(), Charset.forName((String) encodeCombox.getSelectedItem()));

						contentEditor.setText(metaContent);
						contentEditor.setCaretPosition(0);
					} catch (Exception e1) {
						// e1.printStackTrace();
						UIUtils.openError("Unsupported character encode.", e1);
						// GlobalContext.getLogger().error("The character encoding is not supported.", e1);
					}
				}
			}
		});

//		JButton btnNewButton = new JButton("Font");
//		btnNewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				FontChooser fc = new FontChooser(contentEditor.getFont());
////				fc.setVisible(true);
//				fc.showDialog(null);
////				fc.showDialog(FrameworkUIElements.MAIN_FRAME.getInstance());
//				Font font = fc.getSelectedFont();
//				if (font != null) {// && color != null) {
//					contentEditor.setFont(font);
//				}
//			}
//		});
//		toolBar.add(btnNewButton);
//
//		JLabel lblNewLabel_1 = new JLabel("  ");
//		toolBar.add(lblNewLabel_1);

//		JLabel lblEncoding = new JLabel("Encoding: ");
//		toolBar.add(lblEncoding);
		// encodeCombox.setModel(new DefaultComboBoxModel<String>(Charset.availableCharsets().keySet().toArray(new
		// String[Charset.availableCharsets().size()])));
		encodeCombox.setModel(
				new DefaultComboBoxModel<String>(new String[] { "US-ASCII", "UTF-8", "UTF-16", "GBK", "GB2312", "BIG5", "Shift_JIS", "EUC-KR", "ISO-8859-1", }));
		encodeCombox.setSelectedItem("UTF-8");
		toolBar.add(encodeCombox);

		if (showXMLEditorSaveButton) {
			JToolBar toolBar_1 = new JToolBar();
			toolBar_1.setFloatable(false);
			add(toolBar_1, BorderLayout.SOUTH);

			btnSave = new JButton("Save");
			btnSave.setIcon(IconResource.ICON_SAVE_16x16);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					save();
				}
			});
			toolBar_1.add(btnSave);
		}

		nameTextField = new JTextField();
		if (showNameField) {
			JPanel panel_1 = new JPanel();
			add(panel_1, BorderLayout.NORTH);
			panel_1.setLayout(new BorderLayout(0, 0));

			JLabel lblMetadataName = new JLabel("Name:");
			panel_1.add(lblMetadataName, BorderLayout.WEST);

			panel_1.add(nameTextField);
			nameTextField.setColumns(10);
		}
	}

	public String getName() {
		return nameTextField.getText();
	}

	public byte[] getContentInByte() {
		byte[] content = contentEditor.getText().getBytes();
		return content;
	}

	public String getContentInString() {
		return contentEditor.getText();
	}

	public void setName(String name) {
		nameTextField.setText(name);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		contentEditor.setEnabled(enabled);
		nameTextField.setEnabled(enabled);
		encodeCombox.setEnabled(enabled);

		if (btnSave != null)
			btnSave.setEnabled(enabled);
	}

	public void setXMLContent(Object value) {
		if (value == null) {
			contentEditor.setText("");
		} else if (value instanceof String) {
			contentEditor.setText(value.toString());
		} else if (value instanceof byte[]) {
			contentEditor.setText(new String((byte[]) value, Charset.forName((String) encodeCombox.getSelectedItem())));
		}

		contentEditor.setCaretPosition(0);
	}

	public void setDataSavingListener(DataSavingListener<String, byte[]> dataSavingListener) {
		this.dataSavingListener = dataSavingListener;
	}

	public boolean save() {
		if (dataSavingListener != null) {
			return dataSavingListener.savingData(getName(), getContentInByte());
		}

		return false;
	}

	public void clear() {
		contentEditor.setText("");
		nameTextField.setText("");
		encodeCombox.setSelectedIndex(1);
	}
}
