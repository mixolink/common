package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RowRenderer extends DefaultTableCellRenderer {
       public RowRenderer() {
            super();
        }
    public Component getTableCellRendererComponent(JTable t, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // 设置奇偶行的背景色，可在此根据需要进行修改
        if (row % 2 == 0)
            setBackground(Color.WHITE);
        else
            setBackground(new Color(238, 238, 238));
        return super.getTableCellRendererComponent(t, value, isSelected,
                hasFocus, row, column);
    }
     
}