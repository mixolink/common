package com.amituofo.common.ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.amituofo.common.ui.swingexts.component.JECircularProgressBar;

public class CircularProgressBarDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("圆形进度条示例");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JECircularProgressBar progressBar = new JECircularProgressBar();
            progressBar.setPreferredSize(new Dimension(120, 120));
//            progressBar.setForegroundColor(new Color(52, 152, 219)); // 蓝色
//            progressBar.setBackgroundColor(new Color(230, 230, 230));

            // 添加百分比标签（可选）
            JLabel label = new JLabel("0%", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(progressBar, BorderLayout.CENTER);
            panel.add(label, BorderLayout.SOUTH);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            frame.add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // 模拟进度更新（每100ms增加1%）
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            final int[] value = {0};
            scheduler.scheduleAtFixedRate(() -> {
                if (value[0] <= 100) {
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(value[0]);
//                        label.setText(value[0] + "%");
                    });
                    value[0]++;
                } else {
                    scheduler.shutdown();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        });
    }
}