package com.amituofo.common.ui.util;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

public class EDTViolationDetector extends RepaintManager {

    @Override
    public synchronized void addInvalidComponent(JComponent component) {
        checkEDT(component);
        super.addInvalidComponent(component);
    }

    @Override
    public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
        checkEDT(component);
        super.addDirtyRegion(component, x, y, w, h);
    }

    private void checkEDT(JComponent component) {
        if (!SwingUtilities.isEventDispatchThread()) {
            // 打印完整调用栈，直接定位到违规代码行
            System.err.println("=== EDT Violation: " + component.getClass().getName() + " ===");
            Thread.currentThread().dumpStack();
            System.err.println();
        }
    }
}