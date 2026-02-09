package com.amituofo.common.ui.swingexts.component;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.amituofo.common.util.StringUtils;

public class JEFilterComboBox extends JComboBox<String> {
	private static final long serialVersionUID = -5189048710098724983L;

	public JEFilterComboBox() {
		create();
	}

//	public FilterComboBox(ComboBoxModel<String> aModel) {
//		super(aModel);
//		create();
//	}

	public JEFilterComboBox(String[] items) {
		super(items);
		create();
	}

	public JEFilterComboBox(Vector<String> items) {
		super(items);
		create();
	}

	private void create() {
		this.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
//			long lastKeyPressTime=0;
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !JEFilterComboBox.this.isPopupVisible()) {
					String filterPatten = JEFilterComboBox.this.getEditor().getItem().toString();

					if (isValidInput(filterPatten)) {
						addString(filterPatten);
						JEFilterComboBox.this.setSelectedItem(filterPatten);
					} else {
						JEFilterComboBox.this.getEditor().setItem("");
					}
				}

//				if(lastKeyPressTime!=0) {
//					long interval = System.currentTimeMillis()-lastKeyPressTime;
//					if(interval>1000) {
//						
//					}
//				}

//				System.out.println(e.getKeyChar());
			}
		});
		
		
		this.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				onFocusLosted(e);
			}
		});
		this.setEditable(true);
	}

	protected void onFocusLosted(FocusEvent e) {
		
	}

	protected void newItemAdded(String item) {

	}

	protected boolean isValidInput(String item) {
		return true;
	}

	@Override
	public String getSelectedItem() {
		String v = (String) super.getSelectedItem();
//		addString(v);

		return v;
	}

	private void addString(String input) {
		if (StringUtils.isEmpty(input)) {
			return;
		}

		ComboBoxModel<String> model = this.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			String val = model.getElementAt(i);
			if (val != null) {
				if (input.equalsIgnoreCase(val)) {
					return;
				}
			}
		}

		JEFilterComboBox.this.addItem(input);
		newItemAdded(input);
	}

}
