package com.amituofo.common.ui.swingexts.component;

import javax.swing.JPasswordField;
import javax.swing.text.Document;
import javax.swing.JPasswordField;
import javax.swing.text.Document;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JEPasswordField extends JPasswordField {

    private boolean isModified = false;

    /**
     * 用于区分"程序调用 setText 等方法" 与 "用户手动输入"，
     * 为 true 时，DocumentListener 不会把变化记为用户修改。
     */
    private boolean suppressChangeTracking = false;

    public JEPasswordField() {
        registEvent();
    }

    public JEPasswordField(String text) {
        super(text);
        registEvent();
    }

    public JEPasswordField(int columns) {
        super(columns);
        registEvent();
    }

    public JEPasswordField(String text, int columns) {
        super(text, columns);
        registEvent();
    }

    public JEPasswordField(Document doc, String txt, int columns) {
        super(doc, txt, columns);
        registEvent();
    }

    private void registEvent() {
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                markModifiedIfNeeded();
                // 可以在这里做其他实时检查，比如长度、强度等
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markModifiedIfNeeded();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // 一般纯文本组件不会触发这个，基本可以忽略
            }

            private void markModifiedIfNeeded() {
                if (!suppressChangeTracking) {
                    isModified = true;
                }
            }
        });
    }

    /**
     * 重写 setText，使程序主动设置内容时不会被误判为"用户修改"。
     * 例如表单回显、设置默认值等场景。
     */
    @Override
    public void setText(String t) {
        suppressChangeTracking = true;
        try {
            super.setText(t);
        } finally {
            suppressChangeTracking = false;
        }
    }

    /**
     * 如果外部替换了底层 Document（调用 setDocument），
     * 需要在新文档上重新注册监听器，否则修改追踪会失效。
     */
    @Override
    public void setDocument(Document doc) {
        super.setDocument(doc);
        // 注意：构造函数中调用 super(doc, txt, columns) 时，
        // 该方法可能在 registEvent() 首次调用前被间接触发，
        // 此时 getDocument() 已经是新文档，直接重新注册即可。
        registEvent();
    }

    public boolean isPasswordModified() {
        return isModified;
    }

    public void reset() {
        this.setText("");
        isModified = false;
    }
}