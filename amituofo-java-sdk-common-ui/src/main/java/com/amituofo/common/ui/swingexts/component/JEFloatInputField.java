package com.amituofo.common.ui.swingexts.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * 浮点数输入组件，基于JFormattedTextField
 * 支持输入浮点数，允许空白值
 */
public class JEFloatInputField extends JFormattedTextField {
    
    private Float minValue = null;
    private Float maxValue = null;
    
    /**
     * 构造函数 - 默认配置
     */
    public JEFloatInputField() {
        this(null);
    }
    
    /**
     * 构造函数 - 指定初始值
     * @param initialValue 初始浮点数值
     */
    public JEFloatInputField(Float initialValue) {
        super(new FloatFormatter());
        
        // 应用文档过滤器，只允许输入浮点数相关字符
        AbstractDocument doc = (AbstractDocument) getDocument();
        doc.setDocumentFilter(new FloatDocumentFilter());
        
        setValue(initialValue);
        setColumns(10);
        
        // 设置文本右对齐
        setHorizontalAlignment(JTextField.RIGHT);
        
        // 失去焦点时提交值
        setFocusLostBehavior(JFormattedTextField.COMMIT);
    }
    
    /**
     * 设置取值范围
     * @param min 最小值（null表示无限制）
     * @param max 最大值（null表示无限制）
     */
    public void setRange(Float min, Float max) {
        this.minValue = min;
        this.maxValue = max;
    }
    
    /**
     * 获取浮点数值
     * @return 浮点数值，如果为空则返回null
     */
    public Float getFloatValue() {
        Object value = getValue();
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        try {
            return Float.parseFloat(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 设置浮点数值
     * @param value 浮点数值
     */
    public void setFloatValue(Float value) {
        setValue(value);
    }
    
    /**
     * 验证输入值是否在范围内
     * @return true表示有效
     */
    public boolean isValueValid() {
        Float value = getFloatValue();
        if (value == null) {
            return true; // 允许空白
        }
        if (minValue != null && value < minValue) {
            return false;
        }
        if (maxValue != null && value > maxValue) {
            return false;
        }
        return true;
    }
    
    /**
     * 自定义格式化器 - 支持空白和浮点数
     */
    private static class FloatFormatter extends DefaultFormatter {
        
        public FloatFormatter() {
            setOverwriteMode(false);
            setAllowsInvalid(false);
            setCommitsOnValidEdit(false);
        }
        
        @Override
        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().isEmpty()) {
                return null;
            }
            
            try {
                return Float.parseFloat(text.trim());
            } catch (NumberFormatException e) {
                throw new ParseException("无效的浮点数格式", 0);
            }
        }
        
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value == null) {
                return "";
            }
            if (value instanceof Float) {
                return value.toString();
            }
            return value.toString();
        }
    }
    
    /**
     * 文档过滤器 - 只允许输入浮点数相关字符
     */
    private static class FloatDocumentFilter extends DocumentFilter {
        
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string == null) {
                return;
            }
            
            if (isValid(fb, offset, string, 0)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            if (text == null) {
                return;
            }
            
            if (isValid(fb, offset, text, length)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
        
        private boolean isValid(FilterBypass fb, int offset, String string, int length) 
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, offset));
            sb.append(string);
            if (offset + length < doc.getLength()) {
                sb.append(doc.getText(offset + length, doc.getLength() - offset - length));
            }
            
            String result = sb.toString();
            
            // 允许空字符串
            if (result.trim().isEmpty()) {
                return true;
            }
            
            // 只允许数字、小数点、负号、空格
            if (!result.matches("^[\\s\\-0-9.]*$")) {
                return false;
            }
            
            // 检查是否可能是有效的浮点数格式
            // 允许中间状态，如 "-", ".", "3.", "-.5" 等
            if (result.matches("^\\s*-?\\d*\\.?\\d*\\s*$")) {
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JFloatInputField 测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            
            // 示例1：普通浮点数输入
            gbc.gridx = 0; gbc.gridy = 0;
            frame.add(new JLabel("普通浮点数:"), gbc);
            
            gbc.gridx = 1;
            JEFloatInputField field1 = new JEFloatInputField();
            frame.add(field1, gbc);
            
            // 示例2：带初始值的输入
            gbc.gridx = 0; gbc.gridy = 1;
            frame.add(new JLabel("带初始值 (3.14):"), gbc);
            
            gbc.gridx = 1;
            JEFloatInputField field2 = new JEFloatInputField(3.14f);
            frame.add(field2, gbc);
            
            // 示例3：带范围限制的输入
            gbc.gridx = 0; gbc.gridy = 2;
            frame.add(new JLabel("范围限制 (0-100):"), gbc);
            
            gbc.gridx = 1;
            JEFloatInputField field3 = new JEFloatInputField();
            field3.setRange(0f, 100f);
            frame.add(field3, gbc);
            
            // 获取值按钮
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JButton btnGetValue = new JButton("获取所有值");
            frame.add(btnGetValue, gbc);
            
            // 结果显示区域
            gbc.gridy = 4;
            JTextArea resultArea = new JTextArea(6, 30);
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);
            frame.add(scrollPane, gbc);
            
            // 按钮事件
            btnGetValue.addActionListener(e -> {
                StringBuilder sb = new StringBuilder();
                sb.append("普通浮点数: ").append(field1.getFloatValue()).append("\n");
                sb.append("带初始值: ").append(field2.getFloatValue()).append("\n");
                sb.append("范围限制: ").append(field3.getFloatValue()).append("\n");
                sb.append("范围限制是否有效: ").append(field3.isValueValid()).append("\n");
                
                resultArea.setText(sb.toString());
            });
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}