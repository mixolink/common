package com.amituofo.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableModel;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.global.DialogManager;
import com.amituofo.common.kit.counter.Counter;
import com.amituofo.common.kit.kv.KeyValue;
import com.amituofo.common.kit.value.Value;
import com.amituofo.common.ui.action.InputValidator;
import com.amituofo.common.ui.swingexts.component.ArcTextPane;
import com.amituofo.common.ui.swingexts.component.JEPropertyDialogPanel;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialog;
import com.amituofo.common.ui.swingexts.dialog.SimpleDialogOption;
import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;
import com.formdev.flatlaf.util.SystemFileChooser;
import com.formdev.flatlaf.util.SystemFileChooser.FileFilter;

public class UIUtils {
	public static String DEFAULT_TITLE_OF_ERROR = "Error";
	public static String DEFAULT_TITLE_OF_ERROR_MORE = "More";
	public static String DEFAULT_TITLE_OF_INFO = "Notice";
	public static String DEFAULT_TITLE_OF_CONFIRM = "Confirm";
	public static String DEFAULT_TITLE_OF_WARNING = "Warning";
	public static String DEFAULT_TITLE_OF_ERROR_STACK_TRACE = "Stack Trace";

	protected static JFrame DEFAULT_MAIN_FRAME = null;

	private final static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	private static String[] monoFonts = null;

	public static JFrame getDefaultTopFrame() {
		return DEFAULT_MAIN_FRAME;
	}

	public static void setDefaultTopFrame(JFrame c) {
		DEFAULT_MAIN_FRAME = c;
	}

	public static void invokeLater(final Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}

	public static void invokeLater(final Runnable runnable, int delay) {
		if (delay <= 0) {
			SwingUtilities.invokeLater(runnable);
		} else {
			invokeDelay(runnable, delay);
		}
	}

	public static <T> void updateUI(UIUpdator<T> runnable) {
		UIWorker<T> worker = new UIWorker<T>(runnable);
		worker.execute();
	}

	public static void mergeDelayInvoke(final String runid, final Runnable runnable, int delay) {
		synchronized (timerMap) {
			Timer timer = timerMap.get(runid);
			if (timer != null) {
				timer.stop();
				timerMap.remove(runid);
			}

			Timer swingTimer = new Timer((int) delay, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean continueRun = false;
					synchronized (timerMap) {
						// Timer may canceled by another thread
						continueRun = timerMap.containsKey(runid);
						if (continueRun) {
							timerMap.remove(runid);
						}
					}

					if (continueRun) {
						runnable.run(); // 自动在 EDT 中执行，安全！
					}
				}
			});
			timerMap.put(runid, swingTimer);

			swingTimer.setRepeats(false);
			swingTimer.start();
		}
	}

	public static boolean cancelMergeDelayInvoke(String runid) {
		synchronized (timerMap) {
			Timer timer = timerMap.get(runid);
			if (timer != null) {
				timerMap.remove(runid);
				timer.stop();
				return true;
			}
		}
		return false;
	}

	public static Timer invokeDelay(final Runnable runnable, int delay) {
		// 创建 Swing Timer（注意：delay 单位是毫秒）
		Timer swingTimer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runnable.run(); // 自动在 EDT 中执行，安全！
			}
		});

		// 只执行一次（默认 repeat = true，需设为 false）
		swingTimer.setRepeats(false);

		// 启动定时器
		swingTimer.start();

		return swingTimer; // 返回以便外部可取消（调用 .stop()）
	}

	/**
	 * 获取组件的图像快照
	 */
	public static BufferedImage createSnapshot(JComponent component) {
		if (component == null || component.getWidth() <= 0 || component.getHeight() <= 0) {
			// 返回一个空白图片避免 NullPointer 或尺寸错误
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		component.paint(g); // 强制组件将自身绘制到Graphics对象上
		g.dispose();
		return image;
	}

	// public static void executeWaitingAction(final Runnable runnable) {
	// final WaitingDialog waitingDlg = new WaitingDialog(); // make sure
	// setUndecorated(true) is called, and it's modal
	// SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
	// @Override
	// protected Void doInBackground() {
	// runnable.run();
	// return null;
	// }
	//
	// @Override
	// protected void done() {
	// waitingDlg.dispose();
	// }
	// };
	// worker.execute();
	// waitingDlg.showWaitingDialog();
	// }

	public static void openError(Component parent, String message) {
		openError(parent, message, null);
	}

	public static void openError(Component parent, String message, Exception e) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		Runnable runnable = () -> {
			if (e == null) {
				DialogManager.increaseDialog();
				JOptionPane.showMessageDialog(parentComponent, message, DEFAULT_TITLE_OF_ERROR + " (￣▽￣;)", JOptionPane.ERROR_MESSAGE);
				DialogManager.decreaseDialog();
			} else {
				DialogManager.increaseDialog();
				Object[] options = { DEFAULT_TITLE_OF_ERROR_MORE, "OK" };
				int result = JOptionPane.showOptionDialog(parentComponent, message, DEFAULT_TITLE_OF_ERROR + " (￣▽￣;)", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null,
						options, options[1]);
				DialogManager.decreaseDialog();
				if (result == 0) {
					DialogManager.increaseDialog();
					JOptionPane.showMessageDialog(parentComponent, StringUtils.printStackTrace(e), DEFAULT_TITLE_OF_ERROR_STACK_TRACE, JOptionPane.ERROR_MESSAGE);
					DialogManager.decreaseDialog();
				}
			}
		};

		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}

	public static void openInformation(Component parent, String message) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			JOptionPane.showMessageDialog(parentComponent, message, DEFAULT_TITLE_OF_INFO, JOptionPane.INFORMATION_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			SwingUtilities.invokeLater(() -> {
				DialogManager.increaseDialog();
				JOptionPane.showMessageDialog(parentComponent, message, DEFAULT_TITLE_OF_INFO, JOptionPane.INFORMATION_MESSAGE);
				DialogManager.decreaseDialog();
			});
		}
	}

	public static boolean openInformationWithDontShowAgain(Component parent, String message, String dontShowText) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		// 1. 创建复选框
		JCheckBox dontShowAgain = new JCheckBox(dontShowText);
		dontShowAgain.setSelected(false);

		// 2. 创建消息面板（包含原始消息 + 复选框）
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// 消息标签（支持 HTML）
		JLabel messageLabel = new JLabel(message);
//		JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
		messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(messageLabel);
		panel.add(Box.createVerticalStrut(10)); // 垂直间距
		panel.add(dontShowAgain);

		// 3. 显示对话框
		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			JOptionPane.showMessageDialog(parentComponent, panel, DEFAULT_TITLE_OF_INFO, JOptionPane.INFORMATION_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					JOptionPane.showMessageDialog(parentComponent, panel, DEFAULT_TITLE_OF_INFO, JOptionPane.INFORMATION_MESSAGE);
					DialogManager.decreaseDialog();
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		// 4. 返回用户是否勾选了“不再显示”
		return dontShowAgain.isSelected();
	}

	public static InputConfirm openInfoConfirmWithDontShowAgain(Component parent, String message, String dontShowText) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		// 1. 创建复选框
		JCheckBox dontShowAgain = new JCheckBox(dontShowText);
		dontShowAgain.setSelected(false);

		// 2. 创建消息面板（包含原始消息 + 复选框）
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// 消息标签（支持 HTML）
		JLabel messageLabel = new JLabel(message);
//		JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
		messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(messageLabel);
		panel.add(Box.createVerticalStrut(10)); // 垂直间距
		panel.add(dontShowAgain);

		// 3. 显示对话框
		Counter yes = Counter.newCounter();
		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			yes.n = JOptionPane.showConfirmDialog(parentComponent, panel, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					yes.n = JOptionPane.showConfirmDialog(parentComponent, panel, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					DialogManager.decreaseDialog();
				});
			} catch (Exception e) {
				return new InputConfirm(yes.n, false);
			}
		}

		return new InputConfirm(yes.n, dontShowAgain.isSelected());
	}

	public static boolean openInfoConfirm(Component parent, String message) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		Counter yes = Counter.newCounter();
		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			yes.n = JOptionPane.showConfirmDialog(parentComponent, message, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					yes.n = JOptionPane.showConfirmDialog(parentComponent, message, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					DialogManager.decreaseDialog();
				});
			} catch (Exception e) {
				return false;
			}
		}

		return yes.n == 0;
	}

	public static boolean openWarnConfirm(Component parent, String message) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		Counter yes = Counter.newCounter();
		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			yes.n = JOptionPane.showConfirmDialog(parentComponent, message, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					yes.n = JOptionPane.showConfirmDialog(parentComponent, message, DEFAULT_TITLE_OF_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					DialogManager.decreaseDialog();
				});
			} catch (Exception e) {
				return false;
			}
		}

		return yes.n == 0;
	}

	public static void openWarning(Component parent, String message) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			JOptionPane.showMessageDialog(parentComponent, message, DEFAULT_TITLE_OF_WARNING, JOptionPane.WARNING_MESSAGE);
			DialogManager.decreaseDialog();
		} else {
			SwingUtilities.invokeLater(() -> {
				DialogManager.increaseDialog();
				JOptionPane.showMessageDialog(parentComponent, message, DEFAULT_TITLE_OF_WARNING, JOptionPane.WARNING_MESSAGE);
				DialogManager.decreaseDialog();
			});
		}
	}

	public static String openInput(Component parent, Object message, Object initValue, InputValidator<String> inputValidator) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		Value<String> value = new Value<>();

		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			value.setValue(JOptionPane.showInputDialog(parentComponent, message, initValue));
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					value.setValue(JOptionPane.showInputDialog(parentComponent, message, initValue));
					DialogManager.decreaseDialog();
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}

		if (value.hasValue() && inputValidator != null) {
			try {
				inputValidator.validate(value.getValue());
			} catch (InvalidParameterException e1) {
				UIUtils.openError(e1.getMessage());
				return openInput(parentComponent, message, value, inputValidator);
			}
		}

		return value.getValue();
	}

	public static InputUserPassword openInputUserPassword(Component parent, String title, String usermessage, String pwdmessage, String defaultUsername, boolean allowEmpty) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		do {
			JTextField user = new JTextField();
			if (StringUtils.isNotEmpty(defaultUsername)) {
				user.setText(defaultUsername);
			}
			JPasswordField password = new JPasswordField();
			Object[] ob = { new JLabel(usermessage), user, new JLabel(pwdmessage), password };

			Counter result = Counter.newCounter();

			if (SwingUtilities.isEventDispatchThread()) {
				DialogManager.increaseDialog();
				result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
				DialogManager.decreaseDialog();
			} else {
				try {
					SwingUtilities.invokeAndWait(() -> {
						DialogManager.increaseDialog();
						result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
						DialogManager.decreaseDialog();
					});
				} catch (InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
					return InputUserPassword.cancel();
				}
			}

			if (result.n == JOptionPane.OK_OPTION) {
				String username = user.getText();
				char[] pwd = password.getPassword();

				if (!allowEmpty && (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd))) {
					continue;
				}

				return InputUserPassword.ok(username, pwd);
			} else {
				return InputUserPassword.cancel();
			}
		} while (true);
	}

	public static InputPassword openInputPassword(Component parent, String title, String message, boolean allowEmpty) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		do {
			JPasswordField password = new JPasswordField();
			Object[] ob = { new JLabel(message), password };

			Counter result = Counter.newCounter();

			if (SwingUtilities.isEventDispatchThread()) {
				DialogManager.increaseDialog();
				result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
				DialogManager.decreaseDialog();
			} else {
				try {
					SwingUtilities.invokeAndWait(() -> {
						DialogManager.increaseDialog();
						result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
						DialogManager.decreaseDialog();
					});
				} catch (InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
					return InputPassword.cancel();
				}
			}

			if (result.n == JOptionPane.OK_OPTION) {
				char[] pwd = password.getPassword();

				if (!allowEmpty && StringUtils.isEmpty(pwd)) {
					continue;
				}

				return InputPassword.ok(pwd);
			} else {
				return InputPassword.cancel();
			}
		} while (true);
	}

	public static Object openInputCombox(Component parent, String title, String message, Object[] selections, Object defaultValue) {
		Component parentComponent = (parent != null) ? parent : DEFAULT_MAIN_FRAME;

		JComboBox<Object> combox = new JComboBox<Object>();

		combox.setModel(new DefaultComboBoxModel<Object>(selections));

		Object[] ob = { new JLabel(message), combox };

		Counter result = Counter.newCounter();

		if (SwingUtilities.isEventDispatchThread()) {
			DialogManager.increaseDialog();
			result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
			DialogManager.decreaseDialog();
		} else {
			try {
				SwingUtilities.invokeAndWait(() -> {
					DialogManager.increaseDialog();
					result.n = JOptionPane.showConfirmDialog(parentComponent, ob, title, JOptionPane.OK_OPTION);
					DialogManager.decreaseDialog();
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		if (result.n == JOptionPane.OK_OPTION) {
			return combox.getSelectedItem();
		} else {
			return defaultValue;
		}
	}

	public static void openError(String message) {
		openError(DEFAULT_MAIN_FRAME, message);
	}

	public static void openError(String message, Exception e) {
		openError(DEFAULT_MAIN_FRAME, message, e);
	}

	public static void openInformation(String message) {
		openInformation(DEFAULT_MAIN_FRAME, message);
	}

	public static boolean openInfoConfirm(String message) {
		return openInfoConfirm(DEFAULT_MAIN_FRAME, message);
	}

	public static boolean openWarnConfirm(String message) {
		return openWarnConfirm(DEFAULT_MAIN_FRAME, message);
	}

	public static void openWarning(String message) {
		openWarning(DEFAULT_MAIN_FRAME, message);
	}

	public static String openInput(Object message, Object initValue, InputValidator<String> inputValidator) {
		return openInput(DEFAULT_MAIN_FRAME, message, initValue, inputValidator);
	}

	public static InputUserPassword openInputUserPassword(String title, String usermessage, String pwdmessage, String defaultUsername, boolean allowEmpty) {
		return openInputUserPassword(DEFAULT_MAIN_FRAME, title, usermessage, pwdmessage, defaultUsername, allowEmpty);
	}

	public static InputPassword openInputPassword(String title, String message, boolean allowEmpty) {
		return openInputPassword(DEFAULT_MAIN_FRAME, title, message, allowEmpty);
	}

	public static Object openInputCombox(String title, String message, Object[] selections, Object defaultValue) {
		return openInputCombox(DEFAULT_MAIN_FRAME, title, message, selections, defaultValue);
	}

	public static void openMessageTip(String message) {
		openMessageTip(null, message, SystemColor.info, -1);
	}

	public static void openMessageTip(String message, int closeDelay) {
		openMessageTip(null, message, SystemColor.info, closeDelay);
	}

	public static void openMessageTip(JComponent parent, String message, int closeDelay) {
		openMessageTip(parent, message, SystemColor.info, closeDelay);
	}

	public static void openMessageTip(JComponent parent, String message, Color backgroudColor, int closeDelay) {
		JDialog dialog = new JDialog((Dialog) null, null, false);
//		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setUndecorated(true);
		// getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setType(Type.UTILITY);
		dialog.setResizable(false);

		dialog.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
				dialog.dispose();
			}

			public void windowGainedFocus(WindowEvent e) {
			}
		});

//		JLabel panel1 = new JLabel();
		JTextPane pane = new ArcTextPane();
		if (backgroudColor != null) {
			pane.setBackground(backgroudColor);
		}
//		pane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		pane.setText(message);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dialog.dispose();
			}
		});
		pane.setEditable(false);

		dialog.getContentPane().add(pane, BorderLayout.CENTER);

		dialog.setBounds(0, 0, (int) pane.getPreferredSize().getWidth(), (int) (pane.getPreferredSize().getHeight() * 1.0));

		Dimension screenSize;
		int xoffset = 0, yoffset = 0;
		if (parent == null) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			screenSize = kit.getScreenSize();
		} else {
			screenSize = parent.getSize();
			Point point = parent.getLocationOnScreen();
			xoffset = point.x;
			yoffset = point.y;

		}
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		int w = dialog.getWidth();
		int h = dialog.getHeight();
		dialog.setLocation(xoffset + ((width - w) / 2), yoffset + ((height - h) / 2));

		dialog.setVisible(true);

		if (closeDelay > 0) {
			invokeDelay(() -> {
				dialog.dispose();
			}, closeDelay);
		}
	}

	public static void openProperty(List<KeyValue> kvlist) {
		JEPropertyDialogPanel panel = new JEPropertyDialogPanel(kvlist);
		SimpleDialog.open(panel, SimpleDialogOption.New().withWidth(600).withHeight(1000).withCloseClickOutsite(true));
	}

	public static void removeAllComponents(JComponent panel) {
		if (panel == null || panel.getComponentCount() == 0) {
			return;
		}

		// 获取panel中所有的组件
		Component[] components = panel.getComponents();

		// 遍历并移除每个组件及其监听器
		for (Component component : components) {
			if (component instanceof AbstractButton) {
				// 移除按钮的ActionListener
				for (ActionListener listener : ((AbstractButton) component).getActionListeners()) {
					((AbstractButton) component).removeActionListener(listener);
				}
			}

			if (component instanceof JComponent) {
				removeAllComponents((JComponent) component);
			}

			if (component instanceof Destroyable) {
				((Destroyable) component).destroy();
			}
		}

		panel.removeAll();
	}

//	public static void cautionComponent(final JComponent c) {
//		final Border original = c.getBorder();
//
//		new Thread() {
//
//			@Override
//			public void run() {
//				try {
//					int i = 2;
//					int sleep = 200;
//					final Color CLR1 = new Color(220, 20, 60);
//					final Color CLR2 = new Color(102, 205, 170);
//					while ((i--) > 0) {
//						// c.setBorder(new LineBorder(CLR1, 2));
//						c.setBorder(new MatteBorder(2, 0, 0, 0, CLR2));
//						this.sleep(sleep);
//						// c.setBorder(new LineBorder(CLR2, 2));
//						c.setBorder(new MatteBorder(2, 0, 0, 0, CLR1));
//						c.repaint();
//						this.sleep(sleep);
//					}
//
//					c.setBorder(original);
//					c.repaint();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}

	public static void cautionComponent(final JComponent c, boolean around) {
		new Thread() {
			@Override
			public void run() {
				synchronized (c) {
					final Border original = c.getBorder();

					try {
						int i = 2;
						int sleep = 200;
						Color CLR1 = new Color(239, 83, 80);
						Color CLR2 = new Color(255, 241, 118);

						MatteBorder B2;
						MatteBorder B1;

						if (around) {
							B2 = new MatteBorder(2, 2, 2, 2, CLR2);
							B1 = new MatteBorder(2, 2, 2, 2, CLR1);
						} else {
							B2 = new MatteBorder(2, 0, 0, 0, CLR2);
							B1 = new MatteBorder(2, 0, 0, 0, CLR1);
						}

						while ((i--) > 0) {
							UIUtils.invokeLater(() -> {
								c.setBorder(B2);
							});

							this.sleep(sleep);
							UIUtils.invokeLater(() -> {
								c.setBorder(B1);
								c.repaint();
							});

							this.sleep(sleep);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						c.setBorder(original);
						c.repaint();
					}
				}
			}
		}.start();
	}

	public static void repaint(Container c) {
		int componentCount = c.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			Component comp = c.getComponent(i);

			if (comp instanceof JComponent) {
				comp.repaint();

			} else if (comp instanceof Container) {
				repaint((Container) comp);
			} else {
			}
		}

		c.repaint();
	}

	public static void setFont(Container c, String fontName) {
		int componentCount = c.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			Component comp = c.getComponent(i);

			if (comp instanceof JComponent) {
				// System.out.println(comp.getName());
				Font currentFont = comp.getFont();
				Font newfont = (new Font(fontName, currentFont.getStyle(), currentFont.getSize()));
				comp.setFont(newfont);
				setFont((Container) comp, newfont);

			} else if (comp instanceof Container) {
			} else {
			}
		}
	}

	public static void setFont(Container c, Font font) {
		int componentCount = c.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			Component comp = c.getComponent(i);

			if (comp instanceof JComponent) {
				// System.out.println(comp.getName());
				comp.setFont(font);
				setFont((Container) comp, font);

			} else if (comp instanceof Container) {
			} else {
			}
		}

		// c.repaint();
	}

	public static void setFont(Container c, Class[] components, Font font) {
		int componentCount = c.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			Component comp = c.getComponent(i);

			if (comp instanceof JComponent) {
				// System.out.println(comp.getName());
				for (Class class1 : components) {
					if (comp.getClass().getName().equalsIgnoreCase(class1.getName())) {
						comp.setFont(font);
						setFont((Container) comp, font);
						break;
					}
				}
			} else if (comp instanceof Container) {
			} else {
			}
		}

		// c.repaint();
	}

	public static void updateUIFont(Font font) {
		for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource oldf = (FontUIResource) value;

				FontUIResource fontRes = new FontUIResource(font);

				UIManager.put(key, fontRes);
			}
		}
	}

	public static String[] getSystemFontNames() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = e.getAvailableFontFamilyNames();
		// for (String fontName : fontNames) {
		// System.out.println(fontName);
		// }

		return fontNames;
	}

	public static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public static JMenuItem newMenuItem(ActionListener action) {
		JMenuItem item = new JMenuItem();
		item.addActionListener(action);
		return item;
	}

	public static JMenuItem newMenuItem(Icon icon, ActionListener action) {
		JMenuItem item = new JMenuItem();
		item.setIcon(icon);
		item.addActionListener(action);
		return item;
	}

	public static JMenuItem newMenuItem(String label, Icon icon, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.setIcon(icon);
		item.addActionListener(action);
		return item;
	}

	public static JMenuItem newMenuItem(String label, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(action);
		return item;
	}

	public static JMenuItem addMenuItem(JMenu menu, String label, Icon icon, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.setIcon(icon);
		item.addActionListener(action);
		menu.add(item);
		return item;
	}

	public static JMenuItem addMenuItem(JMenu menu, String label, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(action);
		menu.add(item);
		return item;
	}

	public static JMenuItem addMenuItem(JPopupMenu menu, String label, Icon icon, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.setIcon(icon);
		item.addActionListener(action);
		menu.add(item);
		return item;
	}

	public static JMenuItem addMenuItem(JPopupMenu menu, String label, ActionListener action) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(action);
		menu.add(item);
		return item;
	}

	public static void addSeparator(JMenu menu) {
		JSeparator separator = new JSeparator();
		menu.add(separator);
	}

	public static void addRadioMenuItem(JMenu menu, String[] labels, Icon[] icon, ActionListener[] actions) {
		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < actions.length; i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(labels[i]);
			item.setIcon(icon[i]);
			item.addActionListener(actions[i]);

			group.add(item);
			menu.add(item);
		}
		// return group;
	}

//	public static JRadioButtonMenuItem[] newRadioMenuItem(String[] labels, Icon[] icon, ActionListener[] actions) {
//		ButtonGroup group = new ButtonGroup();
//		JRadioButtonMenuItem[] items = new JRadioButtonMenuItem[labels.length];
//		for (int i = 0; i < actions.length; i++) {
//			JRadioButtonMenuItem item = new JRadioButtonMenuItem(labels[i]);
//			item.setIcon(icon[i]);
//			item.addActionListener(actions[i]);
//
//			group.add(item);
//			items[i] = item;
//		}
//		return items;
//	}

	public static void enableComponments(Container container, boolean enable) {
		Component[] cs = container.getComponents();
		for (Component component : cs) {
			if (component instanceof JPanel || component instanceof JToolBar) {
				enableComponments((Container) component, enable);
			} else if (component instanceof JComponent) {
				component.setEnabled(enable);
			} else if (component instanceof Container) {
				enableComponments((Container) component, enable);
			} else {
				component.setEnabled(enable);
			}
		}
	}

	public static void commitEdit(JPanel panel) {
		Component[] components = panel.getComponents();
		if (components != null) {
			for (Component component : components) {
				if (component instanceof JPanel) {
					commitEdit((JPanel) component);
				} else {
					if (component instanceof JSpinner) {
						JSpinner jp = ((JSpinner) component);
//						JComponent editor = jp.getEditor();
						jp.repaint();
//						if (jp.isFocusOwner() || jp.isCursorSet() || jp.hasFocus() || editor.isFocusOwner() || editor.isVisible() || editor.isShowing()
//								|| editor.isCursorSet()) {
						try {
							jp.commitEdit();
//								System.out.println("Commit edit " + component.getName() + " " + ((JSpinner) component).getValue());
						} catch (Exception e) {
//								e.printStackTrace();
						}
//						}
					}
				}
			}
		}
	}

	public static Font getDefaultFont() {
		Font defaultFont = UIManager.getFont("Label.font");
		return defaultFont;
	}

	public static Font deriveFont(int style, float size) {
		return getDefaultFont().deriveFont(style, size);
	}

	public static Font deriveFontSize(float size) {
		return getDefaultFont().deriveFont(size);
	}

	public static Font deriveFontSizePlus(float size) {
		return getDefaultFont().deriveFont(getDefaultFont().getSize() + size);
	}

	public static Font deriveFontSizePlus(Font font, float size) {
		return font.deriveFont(font.getSize() + size);
	}

	public static Font deriveFontStyle(int style) {
		return getDefaultFont().deriveFont(style);
	}

	public static boolean isFrameInValidPosition(int x, int y, int width, int height) {
		if (x < 0 || y < 0 || width < 50 || height < 50) { // 只检查最小尺寸，允许负x/y（多屏支持）
			return false;
		}
		Rectangle proposed = new Rectangle(x, y, width, height);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle virtualBounds = new Rectangle();
		for (GraphicsDevice gd : ge.getScreenDevices()) {
			virtualBounds = virtualBounds.union(gd.getDefaultConfiguration().getBounds());

			if (virtualBounds.intersects(proposed)) {
				return true;
			}
		}
		return false; // 无屏满足条件
	}

	/**
	 * 获取 JTable 当前主排序的列的模型索引。 * @param table 要检查的 JTable 实例。
	 * 
	 * @return 排序列的模型索引（从 0 开始）；如果表格未排序或没有设置 RowSorter，则返回 -1。
	 */
	public static int getSortedColumnModelIndex(JTable table) {
		// 1. 获取 RowSorter
		RowSorter<? extends TableModel> sorter = table.getRowSorter();

		// 检查 RowSorter 是否存在
		if (sorter == null) {
			// 表格未设置排序器
			return -1;
		}

		// 2. 获取 SortKey 列表
		List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();

		// 检查是否有 SortKey
		if (sortKeys.isEmpty()) {
			// 表格当前未进行排序
			return -1;
		}

		// 3. 获取第一个（主）SortKey
		RowSorter.SortKey primarySortKey = sortKeys.get(0);

		// 4. 检查排序状态是否为 UNSORTED (未排序)
		// 理论上如果列表不为空，它不应该是 UNSORTED，但为了严谨性可以检查
		if (primarySortKey.getSortOrder() == SortOrder.UNSORTED) {
			return -1;
		}

		// 5. 返回排序列的模型索引
		return primarySortKey.getColumn();
	}

//	public static class OverFlowedComponent {
//		public boolean isOverFlowed = false;
//	}

	public static boolean isToolBarOverflowed(JToolBar toolBar) {
		int max = toolBar.getVisibleRect().width;
		int total = 0;
		for (Component c : toolBar.getComponents()) {
			total += c.getPreferredSize().width;

			if (total > max) {
				return true;
			}
		}

		return false;
	}

	public static int getCharWidth(char c, Font font) {
		// 创建一个 1x1 的 BufferedImage（实际大小不重要，只要能创建 Graphics）
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		FontMetrics fm = g2d.getFontMetrics(font);
		int width = fm.charWidth(c);

		g2d.dispose(); // 释放资源
		return width;
	}

	public static void setDefaultSubFrameBounds(JFrame parentFrame, JFrame subFrame) {
		if (parentFrame == null || subFrame == null) {
			return;
		}
		if (parentFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			subFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			subFrame.setBounds(parentFrame.getX() + 25, parentFrame.getY() + 25, parentFrame.getWidth(), parentFrame.getHeight());
		}
	}

	public static void enableFixedBottomHeight(JSplitPane splitPane, int initialBottomHeight, Callback<Integer> buttomHeightChangedListener) {
		final int[] userBottomHeight = { initialBottomHeight };
		final long[] isAdjustingByCode = { 0 }; // 关键：标记是否是程序在调整

		// 监听 divider 变化（仅响应用户拖动）
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
			if ((System.currentTimeMillis() - isAdjustingByCode[0]) < 800) {
				return; // 忽略程序触发的变更
			}

			int total = splitPane.getHeight();
			int loc = splitPane.getDividerLocation();
			int dividerSize = splitPane.getDividerSize();
			if (total > 0 && loc >= 0) {
				int bottom = total - loc - dividerSize;
				if (bottom > 0) {
//					System.out.println("bottom "+bottom);
					userBottomHeight[0] = bottom; // 只记录用户手动调整的值

					if (buttomHeightChangedListener != null) {
						buttomHeightChangedListener.callback(bottom);
					}
				}
			}
		});

		// 监听窗口大小变化
		splitPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int total = splitPane.getHeight();
				int dividerSize = splitPane.getDividerSize();
				int desiredLoc = total - userBottomHeight[0] - dividerSize;

				if (desiredLoc >= 0 && total > 0) {
					// 关键：设置前打开标志，设置后关闭
					isAdjustingByCode[0] = System.currentTimeMillis();
					splitPane.setDividerLocation(desiredLoc);
				}
			}
		});

		splitPane.setDividerLocation(initialBottomHeight);

		// 初始设置
//	    SwingUtilities.invokeLater(() -> {
//	        if (splitPane.isShowing()) {
//	            isAdjustingByCode[0] = true;
//	            try {
//	                splitPane.setDividerLocation(
//	                    splitPane.getHeight() - initialBottomHeight - splitPane.getDividerSize()
//	                );
//	            } finally {
//	                isAdjustingByCode[0] = false;
//	            }
//	        }
//	    });
	}

	// public static void workerExec() {
	// new SwingWorker<Void, Void>() {
	//
	// @Override
	// protected Void doInBackground() throws Exception {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// }.execute();
	// }

	/**
	 * 计算 JLabel 中图标和文本的绘制区域
	 * 
	 * @return 包含 iconBounds 和 textBounds 的对象
	 */
	public static LabelLayoutInfo calculateLabelLayout(JLabel label, int cellWidth, int cellHeight) {
		Icon icon = label.getIcon();
		String text = label.getText();
		FontMetrics fm = label.getFontMetrics(label.getFont());

		int iconWidth = (icon != null) ? icon.getIconWidth() : 0;
		int iconHeight = (icon != null) ? icon.getIconHeight() : 0;
		int textWidth = (text != null && !text.isEmpty()) ? fm.stringWidth(text) : 0;
		int textHeight = fm.getHeight();

		// 总内容宽高（用于居中等对齐）
		int totalContentWidth = iconWidth + (icon != null && textWidth > 0 ? label.getIconTextGap() : 0) + textWidth;
		int totalContentHeight = Math.max(iconHeight, textHeight);

		// === 水平对齐（相对于单元格）===
		int contentX;
		switch (label.getHorizontalAlignment()) {
		case SwingConstants.LEFT:
		case SwingConstants.LEADING:
			contentX = 0;
			break;
		case SwingConstants.RIGHT:
		case SwingConstants.TRAILING:
			contentX = cellWidth - totalContentWidth;
			break;
		case SwingConstants.CENTER:
		default:
			contentX = (cellWidth - totalContentWidth) / 2;
			break;
		}

		// === 垂直对齐 ===
		int contentY;
		switch (label.getVerticalAlignment()) {
		case SwingConstants.TOP:
			contentY = 0;
			break;
		case SwingConstants.BOTTOM:
			contentY = cellHeight - totalContentHeight;
			break;
		case SwingConstants.CENTER:
		default:
			contentY = (cellHeight - totalContentHeight) / 2;
			break;
		}

		// === 图标和文本的相对位置（默认：图标在左，文本在右）===
		int iconX = contentX;
		int iconY = contentY + (totalContentHeight - iconHeight) / 2; // 垂直居中图标

		int textX = contentX + iconWidth + (icon != null && textWidth > 0 ? label.getIconTextGap() : 0);
		int textY = contentY + (totalContentHeight - textHeight) / 2 + fm.getAscent(); // 文本基线对齐

		Rectangle iconBounds = (icon != null) ? new Rectangle(iconX, iconY, iconWidth, iconHeight) : null;

		Rectangle textBounds = (text != null && !text.isEmpty()) ? new Rectangle(textX, textY - fm.getAscent(), textWidth, textHeight) : null;

		return new LabelLayoutInfo(iconBounds, textBounds);
	}

	public static class LabelLayoutInfo {
		public final Rectangle iconBounds;
		public final Rectangle textBounds;

		public LabelLayoutInfo(Rectangle iconBounds, Rectangle textBounds) {
			this.iconBounds = iconBounds;
			this.textBounds = textBounds;
		}
	}

	public static boolean detectModifierKeys(KeyEvent e) {
		return e.isShiftDown() || e.isAltDown() || e.isControlDown() || e.isMetaDown();
	}

	public static boolean detectNumberAlphabet(KeyEvent e) {
		// 1. 只关心按键按下事件 (KEY_PRESSED)，忽略释放 (KEY_RELEASED) 和 输入字符 (KEY_TYPED)
		if (e.getID() != KeyEvent.KEY_PRESSED) {
			return false;
		}

		int keyCode = e.getKeyCode();
		// 2. 检测数字 (0-9)
		// VK_0 到 VK_9 是连续的，或者直接用范围判断
		if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
			return true;
		}
		// 3. 检测字母 (A-Z)
		// VK_A 到 VK_Z 也是连续的
		else if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
			return true;
		}
		return false;
	}

	public static Rectangle getScreenBoundsContaining(int[] bounds) {
		Rectangle screenBounds = null;// getScreenBoundsContaining(savedBounds);
		if (bounds == null || bounds.length != 4 || bounds[2] < 500 || bounds[3] < 500) {
			return screenBounds;
		}

		Rectangle rect = new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (GraphicsDevice gd : ge.getScreenDevices()) {
			screenBounds = gd.getDefaultConfiguration().getBounds();
			if (screenBounds.intersects(rect)) {
				return screenBounds;
			}
		}
		return null;
	}

	public static String[] getMonospacedFonts() {
		if (monoFonts != null) {
			return monoFonts;
		}

		String[] testChars = { "i", "l", "m", "w", "M", "W" };
		List<String> monoFontList = new ArrayList<>();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] allFonts = ge.getAvailableFontFamilyNames();

		for (String fontName : allFonts) {
			Font font = new Font(fontName, Font.PLAIN, 12);
			if (isMonospaced(font, testChars)) {
				monoFontList.add(fontName);
			}
		}
		return monoFonts = monoFontList.toArray(new String[monoFontList.size()]);
	}

	private static boolean isMonospaced(Font font, String[] testChars) {
		// 用 Canvas 测量字符宽度
		java.awt.Canvas canvas = new java.awt.Canvas();
		java.awt.FontMetrics fm = canvas.getFontMetrics(font);

		int width = fm.stringWidth(testChars[0]);
		for (String ch : testChars) {
			if (fm.stringWidth(ch) != width) {
				return false;
			}
		}
		return true;
	}

	// 每种语言的代表性测试字符串
	private static final Map<String, String> LOCALE_TEST_STRINGS = new HashMap<>();
	static {
		LOCALE_TEST_STRINGS.put("de_DE", "äöüÄÖÜß");
		LOCALE_TEST_STRINGS.put("en_US", "ABCDEFGabcdefg");
		LOCALE_TEST_STRINGS.put("es_ES", "áéíóúüñÁÉÍÓÚÜÑ¿¡");
		LOCALE_TEST_STRINGS.put("fr_FR", "àâçéèêëîïôùûüÿœæ");
		LOCALE_TEST_STRINGS.put("ja_JP", "あいうえおアイウエオ日本語");
		LOCALE_TEST_STRINGS.put("ko_KR", "가나다라마바사아자차카타파하");
		LOCALE_TEST_STRINGS.put("pt_BR", "áâãàçéêíóôõú");
		LOCALE_TEST_STRINGS.put("ru_RU", "абвгдеёжзийклмнопрстуфхцчшщъыьэюя");
		LOCALE_TEST_STRINGS.put("zh_CN", "的一是在不了有和人这");
	}

	/**
	 * 获取支持指定语言的等宽字体列表
	 * 
	 * @param localeCode 语言代码，如 "zh_CN"，传 null 则返回全部等宽字体
	 */
	public static String[] getMonospacedFontsForLocale(String localeCode) {
		String[] monoFonts = getMonospacedFonts();

		if (localeCode == null) {
			return monoFonts;
		}

		String testString = LOCALE_TEST_STRINGS.get(localeCode);
		if (testString == null) {
			return monoFonts;
		}

		List<String> result = new ArrayList<>();
		for (String fontName : monoFonts) {
			Font font = new Font(fontName, Font.PLAIN, 12);
			// canDisplayUpTo 返回 -1 表示全部字符都能显示
			if (font.canDisplayUpTo(testString) == -1) {
				result.add(fontName);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * 获取同时支持多个语言的等宽字体列表
	 * 
	 * @param localeCodes 语言代码列表
	 */
	public static String[] getMonospacedFontsForLocales(List<String> localeCodes) {
		String[] monoFonts = getMonospacedFonts();

		// 合并所有语言的测试字符
		StringBuilder combined = new StringBuilder();
		for (String localeCode : localeCodes) {
			String testString = LOCALE_TEST_STRINGS.get(localeCode);
			if (testString != null) {
				combined.append(testString);
			}
		}

		if (combined.length() == 0) {
			return monoFonts;
		}

		String testString = combined.toString();
		List<String> result = new ArrayList<>();
		for (String fontName : monoFonts) {
			Font font = new Font(fontName, Font.PLAIN, 12);
			if (font.canDisplayUpTo(testString) == -1) {
				result.add(fontName);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * 选择可执行文件或程序包 适配 Windows (.exe/.bat/.cmd), macOS (.app 或 Unix Binary), Linux (Executable) * @param
	 * parent 父组件（用于定位弹窗，可传 null）
	 * 
	 * @return 选中的 File 对象；如果取消、关闭窗口或选择无效文件夹则返回 null
	 */
	public static File chooseExecutable(Component parent) {
	    // 1. 确定初始显示目录
	    File defaultDir;
	    if (SystemUtils.isWindows()) {
	        String pf = System.getenv("ProgramFiles");
	        defaultDir = (pf != null) ? new File(pf) : new File("C:\\");
	    } else if (SystemUtils.isMacOS()) {
	        defaultDir = new File("/Applications");
	    } else {
	        defaultDir = new File("/usr/bin");
	    }

	    // 容错：如果路径不存在则使用用户主目录
	    if (!defaultDir.exists()) {
	        defaultDir = FileSystemView.getFileSystemView().getHomeDirectory();
	    }

	    // 2. 创建 SystemFileChooser
	    SystemFileChooser fc = new SystemFileChooser();
	    fc.setCurrentDirectory(defaultDir);

	    // 3. 文件选择模式
	    // FILES_AND_DIRECTORIES 不被支持，macOS 下通过平台属性 + approveCallback 替代
	    fc.setFileSelectionMode(SystemFileChooser.FILES_ONLY);
	    fc.setAcceptAllFileFilterUsed(true);

	    // 4. macOS：允许进入 .app 包内部导航（反过来才能"看见"它作为可选文件）
	    //    注意：这里设为 false，让 .app 包作为整体文件而非目录出现
	    if (SystemUtils.isMacOS()) {
	        fc.putPlatformProperty(SystemFileChooser.MAC_TREATS_FILE_PACKAGES_AS_DIRECTORIES, false);
	    }

	    // 5. 文件过滤器（SystemFileChooser 只支持按扩展名过滤）
	    if (SystemUtils.isWindows()) {
	        fc.addChoosableFileFilter(
	            new SystemFileChooser.FileNameExtensionFilter(
	                "Executable Files (*.exe, *.bat, *.cmd)", "exe", "bat", "cmd"));
	    } else if (SystemUtils.isMacOS()) {
	        fc.addChoosableFileFilter(
	            new SystemFileChooser.FileNameExtensionFilter(
	                "Applications (*.app)", "app"));
	        // "All Files" 保留，让用户也能选无扩展名的可执行文件
	    }
	    // Linux：不添加扩展名过滤器，仅靠 approveCallback 校验执行权限

	    // 6. Approve 回调：做原代码中无法通过过滤器表达的后置校验
	    fc.setApproveCallback((selectedFiles, context) -> {
	        File selected = selectedFiles[0];

	        // macOS：.app 包本质是目录，FILES_ONLY 模式下系统已将其作为文件处理，直接放行
	        if (SystemUtils.isMacOS()) {
	            // .app 包或具有执行权限的文件均视为有效
	            if (selected.getName().toLowerCase().endsWith(".app")) {
	                return SystemFileChooser.APPROVE_OPTION;
	            }
	        }

	        // Linux/macOS：校验执行权限
	        if (!SystemUtils.isWindows()) {
	            if (!selected.canExecute()) {
	                context.showMessageDialog(
	                    JOptionPane.WARNING_MESSAGE,
	                    "The selected file is not executable.",
	                    null, 0);
	                return SystemFileChooser.CANCEL_OPTION;
	            }
	        }

	        return SystemFileChooser.APPROVE_OPTION;
	    });

	    // 7. 显示对话框
	    int returnVal = fc.showOpenDialog(parent);

	    if (returnVal == SystemFileChooser.APPROVE_OPTION) {
	        return fc.getSelectedFile();
	    }

	    return null; // 用户取消或点击 X 关闭
	}
}
