package com.amituofo.common.ui.lang;

import java.util.Comparator;

import javax.swing.Icon;

public class DataCell {
	protected String title;
	protected Icon icon;
	protected Object data;
	protected int style;
	protected String newName;

	public DataCell(int rowNumber, Icon icon) {
		this(String.valueOf(rowNumber), icon, 0, null);
	}

	// public DataCell(String title, Icon icon, int style) {
	// this(title, icon, style, null);
	// }

	public DataCell(Object data) {
		this(null, null, 0, data);
	}

	public DataCell(String title, Object data) {
		this(title, null, 0, data);
	}

	public DataCell(String title, Icon icon) {
		this(title, icon, 0, null);
	}

	public DataCell(String title, Icon icon, Object data) {
		this(title, icon, 0, data);
	}

	public DataCell(String title, Icon icon, int style, Object data) {
		this.title = title;
		this.data = data;
		this.icon = icon;
		this.style = style;
	}

	public Object getData() {
		return data;
	}

	public Icon getIcon() {
		return icon;
	}

	public int getStyle() {
		return style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String toString() {
		if (title != null) {
			return title;
		} else if (data != null) {
			return data.toString();
		} else {
			return "";
		}
	}

	public final static Comparator COMPARATOR = new Comparator() {

		@Override
		public int compare(Object o1, Object o2) {
			DataCell a = (DataCell) o1;
			DataCell b = (DataCell) o2;

			if (a.title == null && b.title == null) {
				return 0; // 都为 null 视为相等
			}
			if (a.title == null) {
				return 1; // s1 为 null，排在后面
			}
			if (b.title == null) {
				return -1; // s2 为 null，排在后面
			}

			return a.title.compareToIgnoreCase(b.title);
		}
	};
}
