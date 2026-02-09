package com.amituofo.common.ui.swingexts.component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.amituofo.common.ui.util.UIUtils;

/**
 * 模拟 macOS 偏好设置风格的 Tabbed 面板组件。
 * 核心区别：内容区使用 BorderLayout 动态移除和添加面板，不使用 CardLayout。
 */
public class JEMacStyleTabbedPanel extends JPanel {

    // 自定义监听器接口
    public interface TabSelectionListener {
        void tabChanged(int oldIndex, int newIndex, String title);
    }
    
    // 内容面板容器，使用 BorderLayout
    private JPanel contentContainer = new JPanel(new BorderLayout()); 
    
    private JPanel toolbarPanel = new JPanel();
    private ButtonGroup buttonGroup = new ButtonGroup();
    
    // 存储所有 Tab 按钮和对应的 JComponent
    private List<TabInfo> tabList = new ArrayList<>();
    
    private List<TabSelectionListener> listeners = new ArrayList<>();
    
    private int selectedIndex = -1; // 记录当前选中的索引
    
    // 按钮外部间隙
    private static final int BUTTON_EXTERNAL_GAP = 5; 

    // 按钮内部边距
    private static final int BUTTON_MARGIN_V = 10;
    private static final int BUTTON_MARGIN_H = 12;

    // TabInfo 内部类，用于存储 Tab 标题、按钮和对应的组件
    private static class TabInfo {
        String title;
        MacStyleToggleButton button;
        JComponent component;

        TabInfo(String title, MacStyleToggleButton button, JComponent component) {
            this.title = title;
            this.button = button;
            this.component = component;
        }
    }

    public JEMacStyleTabbedPanel() {
        super(new BorderLayout());

        // 设置工具栏底色为 JTextField 背景色
        Color toolbarBg = UIManager.getColor("TextField.background");
        if (toolbarBg == null) {
            toolbarBg = UIManager.getColor("Panel.background"); 
        }
        if (toolbarBg == null) {
            toolbarBg = new Color(245, 245, 245);
        }
        toolbarPanel.setBackground(toolbarBg);

        // 初始化工具栏布局和间隙
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_EXTERNAL_GAP, BUTTON_EXTERNAL_GAP)); 
        
        // 分隔线
        Color separatorColor = UIManager.getColor("ToolBar.separatorColor");
        if (separatorColor == null) {
            separatorColor = new Color(200, 200, 200);
        }
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, separatorColor));
        
        // 内容容器的背景色
        Color contentBg = UIManager.getColor("Panel.background");
        if (contentBg == null) {
            contentBg = new Color(255, 255, 255);
        }
        contentContainer.setBackground(contentBg);
        
        this.add(toolbarPanel, BorderLayout.NORTH);
        this.add(contentContainer, BorderLayout.CENTER);
    }
    
    // ... (Listener 注册和触发方法保持不变) ...
    public void addTabSelectionListener(TabSelectionListener listener) {
        listeners.add(listener);
    }
    
    private void fireTabChanged(int oldIndex, int newIndex, String title) {
        if (oldIndex != newIndex) {
            for (TabSelectionListener listener : listeners) {
            	UIUtils.invokeLater(() -> {
            		listener.tabChanged(oldIndex, newIndex, title);
            	});
            }
        }
    }
    
    /**
     * 核心切换逻辑，手动移除和添加组件。
     */
    private void switchContentPanel(int oldIndex, int newIndex) {
        if (oldIndex != newIndex) {
            // 1. 移除旧的组件 (如果存在)
            contentContainer.removeAll(); 

            // 2. 添加新的组件
            TabInfo newTab = tabList.get(newIndex);
            contentContainer.add(newTab.component, BorderLayout.CENTER);
            
            // 3. 刷新容器
            contentContainer.revalidate();
            contentContainer.repaint();

            // 4. 触发事件
            fireTabChanged(oldIndex, newIndex, newTab.title);
        }
    }
    
    /**
     * 添加一个新的 Tab 和内容面板。
     */
    public void addTab(String title, Icon icon, JComponent component) {
        MacStyleToggleButton button = new MacStyleToggleButton(title, icon);
        int newIndex = tabList.size();
        
        TabInfo info = new TabInfo(title, button, component);
        
        // 绑定切换逻辑
        button.addActionListener(e -> {
            int oldIndex = this.selectedIndex;
            // 只有当点击的不是当前选中的 Tab 时才执行切换
            if (oldIndex != newIndex) {
                this.selectedIndex = newIndex;
                switchContentPanel(oldIndex, newIndex);
            }
        });
        
        // 存储 Tab 信息
        tabList.add(info);
        buttonGroup.add(button);
        toolbarPanel.add(button);
        
        // 如果是第一个添加的面板，则默认选中
        if (newIndex == 0) {
            button.setSelected(true);
            this.selectedIndex = 0;
            
            // 首次添加组件
            contentContainer.add(component, BorderLayout.CENTER);
            contentContainer.revalidate();

            // 初始选中时，oldIndex 为 -1
            fireTabChanged(-1, 0, title); 
        }
    }
    
    /**
     * 根据索引切换到指定的 Tab。
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < tabList.size() && index != selectedIndex) {
            int oldIndex = this.selectedIndex;
            this.selectedIndex = index;
            
            TabInfo info = tabList.get(index);
            info.button.setSelected(true); // 选中按钮
            
            switchContentPanel(oldIndex, index);
        } else if (index < 0 || index >= tabList.size()) {
//            System.err.println("错误: Tab 索引 " + index + " 超出范围。");
        }
    }
    
    /**
     * 根据索引切换到指定的 Tab。
     */
    public void setSelectedTab(int index) {
        setSelectedIndex(index);
    }

    /**
     * 根据标题切换到指定的 Tab。
     */
    public void setSelectedTab(String title) {
        int index = -1;
        for (int i = 0; i < tabList.size(); i++) {
            if (tabList.get(i).title.equals(title)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            setSelectedIndex(index);
        } else {
//            System.err.println("错误: 未找到标题为 [" + title + "] 的 Tab。");
        }
    }
    
    // =======================================================================
    // 核心定制类：MacStyleToggleButton (保持不变)
    // =======================================================================
    private static class MacStyleToggleButton extends JToggleButton {
        // ... (省略 MacStyleToggleButton 的实现，因为它与上个版本完全相同) ...
        private static final Color SELECTED_BG_COLOR;
        private static final Color HOVER_BG_COLOR;
        private static final int ARC = 8; 

        static {
            Color highlight = UIManager.getColor("Button.select");
            if (highlight == null) { highlight = new Color(0, 122, 255); }
            SELECTED_BG_COLOR = new Color(highlight.getRed(), highlight.getGreen(), highlight.getBlue(), 30);
            
            Color hover = UIManager.getColor("Button.focus");
            if (hover == null) { hover = new Color(150, 150, 150); }
            HOVER_BG_COLOR = new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), 30);
        }

        public MacStyleToggleButton(String text, Icon icon) {
            super(text, icon);
            
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            
            setBorder(BorderFactory.createEmptyBorder(BUTTON_MARGIN_V, BUTTON_MARGIN_H, BUTTON_MARGIN_V, BUTTON_MARGIN_H)); 
            setFocusPainted(false); 
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) { repaint(); }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) { repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (isSelected() || getModel().isPressed()) {
                g2.setColor(SELECTED_BG_COLOR);
                g2.fill(new RoundRectangle2D.Double(2, 2, w - 4, h - 4, ARC, ARC));
            } else if (getModel().isRollover()) {
                g2.setColor(HOVER_BG_COLOR);
                g2.fill(new RoundRectangle2D.Double(2, 2, w - 4, h - 4, ARC, ARC));
            }

            g2.dispose();
            super.paintComponent(g);
        }
        
        @Override
        public Color getBackground() { return new Color(0, 0, 0, 0); }
    }

    // =======================================================================
    // 演示用途的容器类 (保持不变)
    // =======================================================================
//    public static void main(String[] args) {
//        try {
//            FlatLaf.setup(new FlatMacLightLaf());
//            UIManager.put("FormattedTextField.arc", 10);
//            UIManager.put("Spinner.roundRect", true); 
//            UIManager.put("Spinner.arc", 10);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("MacStyleTabbedPanel 演示 (BorderLayout 动态切换)");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            
//            MacStyleTabbedPanel tabbedPanel = new MacStyleTabbedPanel();
//            
//            tabbedPanel.addTabSelectionListener((oldIndex, newIndex, title) -> {
//                System.out.println("Tab 切换事件触发：原索引: " + oldIndex + ", 新索引: " + newIndex + ", 标题: " + title);
//            });
//            
//            tabbedPanel.addTab("通用", getPlaceholderIcon(new Color(255, 100, 100)), createContentPanel("通用设置"));
//            tabbedPanel.addTab("用户与群组", getPlaceholderIcon(new Color(100, 255, 100)), createContentPanel("用户与群组"));
//            tabbedPanel.addTab("网络", getPlaceholderIcon(new Color(255, 255, 100)), createContentPanel("网络设置"));
//            
//            JButton switchButton = new JButton("切换到 Tab 1 (索引 0)");
//            switchButton.addActionListener(e -> tabbedPanel.setSelectedIndex(0)); 
//            
//            JPanel controlPanel = new JPanel(new FlowLayout());
//            controlPanel.add(switchButton);
//            
//            frame.add(controlPanel, BorderLayout.SOUTH);
//            frame.add(tabbedPanel, BorderLayout.CENTER);
//            
//            frame.setPreferredSize(new Dimension(750, 550));
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//
//    private static JPanel createContentPanel(String title) {
//        JPanel panel = new JPanel(new BorderLayout(20, 20));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        panel.setBackground(UIManager.getColor("Panel.background"));
//        
//        JLabel label = new JLabel("这是 " + title + " 的配置界面", SwingConstants.CENTER);
//        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
//        panel.add(label, BorderLayout.CENTER);
//        
//        JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
//        spinner.setPreferredSize(new Dimension(100, 30));
//        
//        JPanel bottomPanel = new JPanel(new FlowLayout());
//        bottomPanel.setOpaque(false);
//        bottomPanel.add(new JLabel("示例 JSpinner:"));
//        bottomPanel.add(spinner);
//        
//        panel.add(bottomPanel, BorderLayout.SOUTH);
//        
//        return panel;
//    }
//
//    private static Icon getPlaceholderIcon(Color color) {
//        return new ImageIcon(color.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
//    }
}