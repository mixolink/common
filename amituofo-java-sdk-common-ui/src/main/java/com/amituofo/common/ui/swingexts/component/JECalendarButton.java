package com.amituofo.common.ui.swingexts.component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import raven.datetime.DatePicker;

/**
 * 日历图标按钮，点击弹出日期选择框。
 *
 * <pre>
 * 基本用法：
 *   JCalendarButton btn = new JCalendarButton();
 *   btn.onDateSelected(date -> System.out.println("选中: " + date));
 *
 * 搭配输入框：
 *   JCalendarButton btn = new JCalendarButton(textField);
 *   btn.onDateSelected(date -> System.out.println("选中: " + date));
 * </pre>
 *
 * 依赖：
 *   - io.github.dj-raven:swing-datetime-picker:2.1.3
 *   - com.formdev:flatlaf
 */
public class JECalendarButton extends JButton {

    private final DatePicker datePicker = new DatePicker();

    // ---------------------------------------------------------------- 构造方法

    /** 纯按钮模式：选好日期后通过 onDateSelected 回调获取 */
//    public JECalendarButton() {
//        this(null);
//    }

	public JECalendarButton(Icon icon) {
		this(icon, null);
	}

	/**
	 * 输入框绑定模式：选好日期后自动填入 editor，同时触发回调。
	 *
	 * @param editor 需要显示日期的文本框，传 null 则不绑定
	 */
    public JECalendarButton(Icon icon, JFormattedTextField editor) {
        super(icon);
        initPicker(editor);
        initButton();
    }

    // ---------------------------------------------------------------- 初始化

    private void initPicker(JFormattedTextField editor) {
        datePicker.setCloseAfterSelected(true);
        if (editor != null) {
            datePicker.setEditor(editor);
        }
    }

    private void initButton() {
//        setToolTipText("选择日期");
        // 尝试使用 FlatLaf 图标，失败则用 emoji 文字
//        try {
//            setIcon(new FlatSVGIcon("com/formdev/flatlaf/extras/icons/calendar.svg", 16, 16));
//            setText(null);
//        } catch (Exception ignored) {
//            // 保留 emoji 文字作为备用
//        }
        addActionListener(e -> datePicker.showPopup(this));
    }

    // ---------------------------------------------------------------- 公开 API

    /**
     * 设置日期选中回调，每次用户选好日期后触发。
     *
     * @param callback 回调，参数为 {@link Date}（已转为系统默认时区）
     */
    public void onDateSelected(Consumer<Date> callback) {
        datePicker.addDateSelectionListener(evt -> {
            LocalDate ld = datePicker.getSelectedDate();
            if (ld != null) {
                Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                callback.accept(date);
            }
        });
    }

    /**
     * 获取当前选中的日期，未选则返回 null。
     *
     * @return {@link Date} 或 null
     */
    public Date getSelectedDate() {
        LocalDate ld = datePicker.getSelectedDate();
        if (ld == null) return null;
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 以编程方式设置选中日期。
     *
     * @param date 要设置的日期，传 null 清空选择
     */
    public void setSelectedDate(Date date) {
        if (date == null) {
            datePicker.clearSelectedDate();
        } else {
            LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            datePicker.setSelectedDate(ld);
        }
    }

    /** 清空当前选中的日期 */
    public void clearSelectedDate() {
        datePicker.clearSelectedDate();
    }

    /** 是否已选中日期 */
    public boolean isDateSelected() {
        return datePicker.isDateSelected();
    }

    /**
     * 直接访问底层 DatePicker，用于进行高级配置
     * （如范围选择模式、禁用特定日期等）。
     */
    public DatePicker getDatePicker() {
        return datePicker;
    }

    // ---------------------------------------------------------------- 用法示例

//    public static void main(String[] args) {
//        com.formdev.flatlaf.FlatLightLaf.setup();
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("JCalendarButton 示例");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 20));
//
//            JLabel label = new JLabel("未选择");
//
//            // ── 示例 1：纯按钮 ──────────────────────────────
//            JECalendarButton btn1 = new JECalendarButton(IconLib.UI_SMALL.of(IconNames.ICON_CALENDAR));
//            btn1.onDateSelected(date -> label.setText("纯按钮选中: " + date));
//
//            // ── 示例 2：输入框 + 按钮 ─────────────────────────
//            JFormattedTextField textField = new JFormattedTextField();
//            textField.setEditable(false);
//            textField.setColumns(12);
//            textField.setFont(textField.getFont().deriveFont(14f));
//
//            JECalendarButton btn2 = new JECalendarButton(textField);
//            btn2.onDateSelected(date -> label.setText("带输入框选中: " + date));
//
//            // 输入框 + 按钮放在同一个面板里
//            JPanel fieldPanel = new JPanel(new BorderLayout(2, 0));
//            fieldPanel.add(textField, BorderLayout.CENTER);
//            fieldPanel.add(btn2, BorderLayout.EAST);
//
//            frame.add(new JLabel("纯按钮:"));
//            frame.add(btn1);
//            frame.add(new JSeparator(SwingConstants.VERTICAL));
//            frame.add(new JLabel("输入框+按钮:"));
//            frame.add(fieldPanel);
//            frame.add(label);
//
//            frame.setSize(560, 120);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}