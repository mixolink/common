package com.amituofo.common.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaveLoadingDemo extends JFrame {

    private final JLabel waveLabel = new JLabel();
    private final Timer timer = new Timer(100, null); // 100ms 一帧，丝滑！
    private int offset = 0;                           // 波浪偏移量

    public WaveLoadingDemo() {
        setTitle("波浪加载动画 by Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);

        waveLabel.setFont(new Font("SansSerif", Font.BOLD, 80));
        waveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(waveLabel);

        // 启动波浪动画
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                offset = (offset + 1) % 8;  // 8 个字符循环
                updateWave();
            }
        });
        timer.start();

        updateWave(); // 初始显示
    }

    private void updateWave() {
        String wave = "▁▂▃▄▅▆▇█▇▆▅▄▃▂▁";  // 经典波浪字符
        StringBuilder sb = new StringBuilder("<html><center>");

        for (int i = 0; i < wave.length(); i++) {
            int pos = (i + offset) % wave.length();
            char c = wave.charAt(pos);

            // 当前“波峰”用红色 + 大号字体高亮，其他灰色
            if (pos == 7 || pos == 8) {  // █ 和 ▇ 是最高点
                sb.append("<span style='color:#FF3366;font-size:100px;'>").append(c).append("</span>");
            } else if (pos == 6 || pos == 9) {
                sb.append("<span style='color:#FF6666;font-size:90px;'>").append(c).append("</span>");
            } else {
                sb.append("<span style='color:#888888;font-size:70px;'>").append(c).append("</span>");
            }
        }

        sb.append("<br><br><b>加载中...</b></center></html>");
        waveLabel.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WaveLoadingDemo().setVisible(true);
        });
    }
}