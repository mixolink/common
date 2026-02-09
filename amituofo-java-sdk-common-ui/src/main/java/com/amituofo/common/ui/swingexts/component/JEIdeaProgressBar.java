package com.amituofo.common.ui.swingexts.component;
import javax.swing.*;
import java.awt.*;

public class JEIdeaProgressBar extends JComponent implements Runnable {
    private int offset = 0;
    private boolean indeterminate = true; // 是否播放动画
    private Color idleColor = UIManager.getColor("ProgressBar.foreground");

    // ======== 颜色常量 ========
    // 动画模式下的背景渐变
    private static final Color BG_COLOR_LEFT  = new Color(25, 40, 90);
    private static final Color BG_COLOR_RIGHT = new Color(45, 80, 150);

    // 光带渐变：透明 -> 亮蓝 -> 透明
    private static final Color HIGHLIGHT_START = new Color(120, 180, 255, 0);
    private static final Color HIGHLIGHT_MID   = new Color(120, 180, 255, 255);
    private static final Color HIGHLIGHT_END   = new Color(120, 180, 255, 0);

    private static final int SPEED = 4;        // 光带流动速度
    private static final int FRAME_DELAY = 20; // 帧间隔 (ms)

    public JEIdeaProgressBar() {
        setPreferredSize(new Dimension(300, 6));
        new Thread(this).start();
    }

    /**
     * 控制是否为不确定模式（动画开关）
     */
    public void setIndeterminate(boolean value) {
        this.indeterminate = value;
        repaint();
    }

    /**
     * 设置关闭动画时的静态颜色
     */
    public void setIdleColor(Color c) {
        this.idleColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (!indeterminate) {
            // -------- 静态模式（与JProgressBar一致） --------
            g2d.setColor(idleColor);
            g2d.fillRoundRect(0, 0, width, height, height, height);
        } else {
            // -------- 动画模式（渐变背景） --------
            GradientPaint backgroundPaint = new GradientPaint(
                0, 0, BG_COLOR_LEFT,
                width, 0, BG_COLOR_RIGHT
            );
            g2d.setPaint(backgroundPaint);
            g2d.fillRoundRect(0, 0, width, height, height, height);

            // -------- 光带位置 --------
            int barWidth = width / 3;
            int x = offset % (width + barWidth) - barWidth;

            // 光带渐变（左半部分）
            GradientPaint gp1 = new GradientPaint(
                x, 0, HIGHLIGHT_START,
                x + barWidth / 2, 0, HIGHLIGHT_MID,
                true
            );
            g2d.setPaint(gp1);
            g2d.fillRoundRect(x, 0, barWidth / 2, height, height, height);

            // 光带渐变（右半部分）
            GradientPaint gp2 = new GradientPaint(
                x + barWidth / 2, 0, HIGHLIGHT_MID,
                x + barWidth, 0, HIGHLIGHT_END,
                true
            );
            g2d.setPaint(gp2);
            g2d.fillRoundRect(x + barWidth / 2, 0, barWidth / 2, height, height, height);
        }
        g2d.dispose();
    }

    @Override
    public void run() {
        while (true) {
            if (indeterminate) {
                offset += SPEED;
                repaint();
            }
            try {
                Thread.sleep(FRAME_DELAY);
            } catch (InterruptedException ignored) {}
        }
    }
}

//public class IdeaGradientProgressBar {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("IDEA 风格渐变进度条");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(400, 120);
//            frame.setLayout(new GridLayout(2, 1, 10, 10));
//
//            IdeaGradientProgressBar bar1 = new IdeaGradientProgressBar();
//            IdeaGradientProgressBar bar2 = new IdeaGradientProgressBar();
//            bar2.setIndeterminate(false); // 第二个关闭动画
//
//            frame.add(bar1);
//            frame.add(bar2);
//            frame.setVisible(true);
//        });
//    }
//}
