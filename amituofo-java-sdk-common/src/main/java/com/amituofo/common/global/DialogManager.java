package com.amituofo.common.global;

import java.util.concurrent.atomic.AtomicInteger;

public class DialogManager {
	private static AtomicInteger showDialogCount = new AtomicInteger(0);

	private DialogManager() {
	}

	public static interface Interactive<T> {
		T show();
	}

//    public synchronized void showMessage(String message, Component parent) {
//        if (isBusy) {
//            // 队列化？或者忽略？或者合并消息？
//            System.out.println("弹窗忙碌，消息暂存: " + message);
//            return; 
//        }
//        
//        isBusy = true;
//        try {
//            // 统一在这里处理所有弹窗逻辑
//            JOptionPane.showMessageDialog(parent, message);
//            
//            // 如果需要紧接着弹另一个，在这里控制
//            // showNextDialogIfNeeded(parent); 
//        } finally {
//            isBusy = false;
//        }
//    }
//    
//    public synchronized void showCustomDialog(Component parent) {
//        if (isBusy) {
//            return; // 或者有别的策略
//        }
//        // ... 逻辑同上
//    }
	
	public static void increaseDialog() {
		showDialogCount.addAndGet(1);
	}
	
	public static void decreaseDialog() {
		showDialogCount.addAndGet(-1);
	}

	public static boolean hasInteractiveDialog() {
		return showDialogCount.get() != 0;
	}

	public static <T> T showInteractiveDialog(Interactive<T> run) {
		if (run == null) {
			return null;
		}
		synchronized (showDialogCount) {
			showDialogCount.addAndGet(1);
			try {
				return run.show();
//				if (SwingUtilities.isEventDispatchThread()) {
//					return run.show();
//				} else {
//					Value<T> value = new Value<>();
//					SwingUtilities.invokeLater(() -> {
//						value.setValue(run.show());
//					});
//					return value.getValue();
//				}
			} finally {
				showDialogCount.addAndGet(-1);
			}
		}
	}

	public static void showDialog(Interactive<Void> run) {
		if (run == null) {
			return;
		}

		synchronized (showDialogCount) {
			showDialogCount.addAndGet(1);
			try {
				run.show();
			} finally {
				showDialogCount.addAndGet(-1);
			}
		}
	}
}