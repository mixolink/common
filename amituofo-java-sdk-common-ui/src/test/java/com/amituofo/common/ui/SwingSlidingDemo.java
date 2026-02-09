package com.amituofo.common.ui;
// ... (之前的 createPanel, getRandomColor, SlideDirection 保持不变)

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.amituofo.common.ui.swingexts.component.CardSlideDirection;
public class SwingSlidingDemo {
    
    // *** 补齐的方法 1：创建带有切换按钮的面板 ***
    private static JPanel createPanel(String name, Color color, SnapshotSlidingContainer container, String nextPanelName, CardSlideDirection direction) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new GridBagLayout()); 
        
        JLabel label = new JLabel("这是 " + name);
        label.setFont(new Font("宋体", Font.BOLD, 24));
        label.setForeground(Color.BLACK); // 确保文字可见

        JButton nextButton = new JButton("切换到 " + nextPanelName + " (方向: " + direction.name() + ")");
        nextButton.setFont(new Font("宋体", Font.PLAIN, 16));

        // 绑定切换动作
        ActionListener action = e -> container.showPanel(nextPanelName, direction);
        nextButton.addActionListener(action);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridy = 0;
        panel.add(label, gbc);
        
        gbc.gridy = 1;
        panel.add(nextButton, gbc);
        
        return panel;
    }

    // *** 补齐的方法 2：随机颜色生成器 ***
    private static Color getRandomColor() {
        Random rand = new Random();
        // 生成鲜艳的颜色，但确保文字可读性
        int r = rand.nextInt(200) + 50; 
        int g = rand.nextInt(200) + 50;
        int b = rand.nextInt(200) + 50;
        return new Color(r, g, b);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("基于 Image 快照的滑动动画演示 (5个面板)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);

            SnapshotSlidingContainer container = new SnapshotSlidingContainer();
            
            // --- 创建 5 个面板并设置切换路径和方向 ---
            
            // Panel 1 -> Panel 2 (LEFT)
            JPanel panel1 = createPanel("面板 1", getRandomColor(), container, "Panel2", CardSlideDirection.LEFT);
            container.addPanel(panel1, "Panel1");

            // Panel 2 -> Panel 3 (UP)
            JPanel panel2 = createPanel("面板 2", getRandomColor(), container, "Panel3", CardSlideDirection.UP);
            container.addPanel(panel2, "Panel2");

            // Panel 3 -> Panel 4 (RIGHT)
            JPanel panel3 = createPanel("面板 3", getRandomColor(), container, "Panel4", CardSlideDirection.RIGHT);
            container.addPanel(panel3, "Panel3");
            
            // Panel 4 -> Panel 5 (DOWN)
            JPanel panel4 = createPanel("面板 4", getRandomColor(), container, "Panel5", CardSlideDirection.DOWN);
            container.addPanel(panel4, "Panel5");

            // Panel 5 -> Panel 1 (LEFT) (形成循环)
            JPanel panel5 = createPanel("面板 5", getRandomColor(), container, "Panel1", CardSlideDirection.LEFT);
            container.addPanel(panel5, "Panel5");
            
            // --- 补齐的组件：统一控制面板 (controlPanel) ---
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new GridLayout(1, 4, 10, 0));
            
            JButton leftBtn = new JButton("切回 P1 (LEFT)");
            leftBtn.addActionListener(e -> container.showPanel("Panel1", CardSlideDirection.LEFT));
            
            JButton rightBtn = new JButton("切回 P1 (RIGHT)");
            rightBtn.addActionListener(e -> container.showPanel("Panel1", CardSlideDirection.RIGHT));

            JButton upBtn = new JButton("切回 P1 (UP)");
            upBtn.addActionListener(e -> container.showPanel("Panel1", CardSlideDirection.UP));

            JButton downBtn = new JButton("切回 P1 (DOWN)");
            downBtn.addActionListener(e -> container.showPanel("Panel1", CardSlideDirection.DOWN));

            controlPanel.add(leftBtn);
            controlPanel.add(rightBtn);
            controlPanel.add(upBtn);
            controlPanel.add(downBtn);
            
            // --- 组合主窗口 ---
            frame.setLayout(new BorderLayout());
            frame.add(container, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH); 

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}