package com.amituofo.common.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PulsingLabelDemo extends JFrame {

    private final JLabel label = new JLabel();
    private final Timer timer = new Timer(200, null); // 每 200ms 切换一次
    private int index = 0;                            // 当前高亮的字符位置
    private final String text = ">>>>>";              // 你想脉动的字符序列

    public PulsingLabelDemo() {
        setTitle("JLabel 脉动动画 >>>>>");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        label.setFont(new Font("SansSerif", Font.PLAIN, 72));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        // 启动动画
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLabel();
                index = (index + 1) % text.length(); // 循环
            }
        });
        timer.start();

        // 初始显示一次
        updateLabel();

        setSize(600, 200);
        setLocationRelativeTo(null);
    }

    private void updateLabel() {
        StringBuilder html = new StringBuilder("<html><center>");
        for (int i = 0; i < text.length(); i++) {
            if (i == index) {
                // 当前高亮的字符用粗体（也可以改颜色、字号等）
                html.append("<b>").append(text.charAt(i)).append("</b>");
            } else {
                html.append(text.charAt(i));
            }
        }
        html.append("</center></html>");
        label.setText(html.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PulsingLabelDemo().setVisible(true);
        });
    }
}