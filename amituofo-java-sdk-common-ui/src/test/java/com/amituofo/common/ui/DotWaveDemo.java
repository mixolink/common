package com.amituofo.common.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DotWaveDemo extends JFrame {

    private final JLabel label = new JLabel();
    private final Timer timer = new Timer(120, e -> updateUltimateDotWave());
    private int offset = 0;

    public DotWaveDemo() {
        setTitle("小圆点波浪动画 •••••");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 300);
        setLocationRelativeTo(null);

        label.setFont(new Font("SansSerif", Font.BOLD, 60));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        timer.start();
        updateUltimateDotWave(); // 初始显示
    }

    private void updateWave() {
        StringBuilder sb = new StringBuilder("<html><center>");
        int[] sizes = {30, 50, 80, 100, 80, 50, 30}; // 7 个点的高度变化
        for (int i = 0; i < 7; i++) {
            int pos = (i + offset) % sizes.length;
            int size = sizes[pos];
            sb.append("<span style='font-size:").append(size)
              .append("px; color:#3388FF'>•</span>");
        }
        offset = (offset + 1) % sizes.length;
        label.setText(sb.append("</center></html>").toString());
    }
    
    private void updateRainbowDotWave() {
        StringBuilder sb = new StringBuilder("<html><center>");
        String[] colors = {"#FF3366", "#FF6633", "#FFAA33", "#FFFF33", "#66FF66", "#33FFFF", "#3366FF", "#AA33FF"};
        int pointCount = 15;
        double speed = 0.3;

        for (int i = 0; i < pointCount; i++) {
            // 波浪数学公式：sin(x + time)
            double phase = i * 0.8 + (System.currentTimeMillis() * speed / 100);
            int size = (int)(50 + 40 * Math.sin(phase)); // 30~90px 浮动
            String color = colors[(i + (int)(System.currentTimeMillis()/200)) % colors.length];

            sb.append("<span style='font-size:").append(size)
              .append("px; color:").append(color).append("'>●</span>");
        }
        label.setText(sb.append("</center></html>").toString());
    }
    
    private void updateUltimateDotWave() {
        StringBuilder sb = new StringBuilder("<html><center>");
        int count = 20;
        double time = System.currentTimeMillis() / 300.0;

        for (int i = 0; i < count; i++) {
            double x = i * 0.6;
            double y = Math.sin(x + time) * 0.6;           // 波浪高度
            double opacity = 0.5 + 0.5 * Math.sin(x - time); // 呼吸透明度

            int size = (int)(40 + y * 40); // 20~80px
            int alpha = (int)(255 * opacity);
            String hexColor = String.format("#%06X", (int)(Math.abs(y) * 0xFF0000 + 0x0088FF));

            sb.append("<span style='font-size:").append(size)
              .append("px; color:").append(hexColor)
              .append("; opacity:").append(String.format("%.2f", opacity))
              .append("'>●</span>");
        }
        label.setText(sb.append("</center></html>").toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DotWaveDemo().setVisible(true));
    }
}