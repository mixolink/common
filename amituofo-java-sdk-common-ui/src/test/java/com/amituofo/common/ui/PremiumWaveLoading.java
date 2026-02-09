package com.amituofo.common.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PremiumWaveLoading extends JFrame {

    private final JLabel wave = new JLabel();
    private final Timer timer = new Timer(70, e -> animate()); // 70ms ≈ 90FPS 丝滑到炸
    private double time = 0;

    public PremiumWaveLoading() {
        setTitle("高端能量波加载动画");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        wave.setForeground(Color.WHITE);
        wave.setFont(new Font("Menlo", Font.PLAIN, 80));
        wave.setHorizontalAlignment(SwingConstants.CENTER);
        add(wave);

        timer.start();
    }

    private void animate() {
        time += 0.12;
        StringBuilder sb = new StringBuilder("<html><center>");

        int dots = 23;
        double baseSize = 60;
        double amplitude = 40;   // 波峰高度
        double frequency = 1.2;  // 波浪密度
        double speed = 0.15;     // 流动速度

        for (int i = 0; i < dots; i++) {
            double x = i * frequency;
            double offset = Math.sin(x + time) * amplitude;
            double size = baseSize + offset;

            // 颜色：从深蓝 → 青 → 白 → 青 → 深蓝（呼吸灯）
            double glow = Math.sin(x + time * 0.8) * 0.5 + 0.5;
            int r = (int)(20 + glow * 50);
            int g = (int)(150 + glow * 105);
            int b = 255;
            String hex = String.format("#%02x%02x%02xff", r, g, b);

            // 透明度呼吸
            double alpha = 0.7 + 0.3 * Math.sin(x - time * 1.5);

            sb.append("<span style='")
              .append("font-size:").append((int)size).append("px;")
              .append("color:").append(hex).append(";")
              .append("opacity:").append(String.format("%.2f", alpha)).append(";")
              .append("text-shadow: 0 0 20px ").append(hex).append(";")
              .append("font-weight:900'>")
              .append("●")
              .append("</span>");
        }

        sb.append("<br><br><span style='font-size:28px; color:#88AAFF; opacity:0.9'>")
          .append("Loading Neural Engine")
          .append("</span></center></html>");

        wave.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new PremiumWaveLoading();
            f.getContentPane().setBackground(Color.BLACK);
            f.setUndecorated(true);  // 可选：无边框更高级
            f.setVisible(true);
        });
    }
}