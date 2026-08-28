package com.amituofo.common.ui.swingexts.component;

import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class JEPasswordField extends JPasswordField {

    private boolean isModified = false;

    /**
     * 用于区分"程序调用 setText 等方法" 与 "用户手动输入"，
     * 为 true 时，DocumentListener 不会把变化记为用户修改。
     */
    private boolean suppressChangeTracking = false;

    private final DocumentListener modificationListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            markModifiedIfNeeded();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            markModifiedIfNeeded();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // Plain text documents generally do not fire this event.
        }
    };

    public JEPasswordField() {
        registerModificationListener();
    }

    public JEPasswordField(String text) {
        super(text);
        registerModificationListener();
    }

    public JEPasswordField(int columns) {
        super(columns);
        registerModificationListener();
    }

    public JEPasswordField(String text, int columns) {
        super(text, columns);
        registerModificationListener();
    }

    public JEPasswordField(Document doc, String text, int columns) {
        super(doc, text, columns);
        registerModificationListener();
    }

    private void registerModificationListener() {
        Document document = getDocument();
        if (document != null) {
            document.addDocumentListener(modificationListener);
        }
    }

    private void markModifiedIfNeeded() {
        if (!suppressChangeTracking) {
            isModified = true;
        }
    }

    @Override
    public void setDocument(Document doc) {
        Document oldDocument = getDocument();
        if (oldDocument != null && modificationListener != null) {
            oldDocument.removeDocumentListener(modificationListener);
        }

        super.setDocument(doc);

        // During the superclass constructor, subclass fields have not been
        // initialized yet. The constructor registers the listener afterwards.
        if (doc != null && modificationListener != null) {
            doc.addDocumentListener(modificationListener);
        }
    }

    /**
     * 重写 setText，使程序主动设置内容时不会被误判为"用户修改"。
     * 例如表单回显、设置默认值等场景。
     */
    @Override
    public void setText(String text) {
        suppressChangeTracking = true;
        try {
            super.setText(text);
        } finally {
            suppressChangeTracking = false;
        }
    }

    public boolean isPasswordModified() {
        return isModified;
    }

    public void reset() {
        setText("");
        isModified = false;
    }
}
