package demo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * 可拖拽 Tab 的 JTabbedPane 示例
 * 将 Tab 拖出 TabbedPane 区域后，自动创建独立浮动窗口
 */
public class DetachableTabbedPane extends JTabbedPane {

    private int draggedTabIndex = -1;
    private boolean isDragging = false;
    private Point dragStartPoint;
    private static final int DRAG_THRESHOLD = 20; // 触发拖拽的最小像素距离
    private JWindow ghostWindow;  // 拖拽时的半透明预览窗口

    public DetachableTabbedPane() {
        initDragListeners();
    }

    private void initDragListeners() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggedTabIndex = indexAtLocation(e.getX(), e.getY());
                dragStartPoint = e.getPoint();
                isDragging = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedTabIndex < 0) return;

                Point current = e.getPoint();
                double dist = current.distance(dragStartPoint);

                if (!isDragging && dist > DRAG_THRESHOLD) {
                    isDragging = true;
                    showGhostWindow(e);
                }

                if (isDragging && ghostWindow != null) {
                    // 让 ghost 跟随鼠标（转换为屏幕坐标）
                    Point screenPt = e.getLocationOnScreen();
                    ghostWindow.setLocation(
                        screenPt.x - ghostWindow.getWidth() / 2,
                        screenPt.y - 10
                    );
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDragging || draggedTabIndex < 0) {
                    reset();
                    return;
                }

                hideGhostWindow();

                // 判断鼠标是否在 TabbedPane 范围外
                Point screenPt = e.getLocationOnScreen();
                Rectangle tabbedBounds = getBoundsOnScreen();

                if (!tabbedBounds.contains(screenPt) && getTabCount() > 1) {
                    detachTab(draggedTabIndex, screenPt);
                }

                reset();
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    /** 显示拖拽时的半透明 ghost 窗口 */
    private void showGhostWindow(MouseEvent e) {
        if (draggedTabIndex < 0) return;

        Component comp = getComponentAt(draggedTabIndex);
        String title = getTitleAt(draggedTabIndex);

        // 截取 Tab 内容为图片
        BufferedImage img = new BufferedImage(
            Math.max(comp.getWidth(), 200),
            Math.max(comp.getHeight(), 100),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = img.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        comp.paint(g2);
        g2.dispose();

        JLabel label = new JLabel(new ImageIcon(img));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        ghostWindow = new JWindow();
        ghostWindow.setOpacity(0.75f);
        ghostWindow.getContentPane().add(label);
        ghostWindow.pack();

        Point screenPt = e.getLocationOnScreen();
        ghostWindow.setLocation(screenPt.x - ghostWindow.getWidth() / 2, screenPt.y - 10);
        ghostWindow.setVisible(true);
    }

    private void hideGhostWindow() {
        if (ghostWindow != null) {
            ghostWindow.dispose();
            ghostWindow = null;
        }
    }

    /** 将指定 Tab 弹出为独立 JFrame */
    private void detachTab(int index, Point screenLocation) {
        String title = getTitleAt(index);
        Icon icon = getIconAt(index);
        Component comp = getComponentAt(index);
        String tooltip = getToolTipTextAt(index);

        removeTabAt(index);

        JFrame frame = new JFrame(title);
        if (icon instanceof ImageIcon) {
            frame.setIconImage(((ImageIcon) icon).getImage());
        }

        // 在顶部加一个"Dock back"按钮，支持拖回
        JButton dockBtn = new JButton("⬅ Dock back to main window");
        dockBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dockBtn.setBackground(new Color(230, 240, 255));
        dockBtn.setFocusPainted(false);
        dockBtn.addActionListener(ev -> {
            frame.dispose();
            addTab(title, icon, comp, tooltip);
            setSelectedComponent(comp);
        });

        frame.setLayout(new BorderLayout());
        frame.add(dockBtn, BorderLayout.NORTH);
        frame.add(comp, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocation(
            screenLocation.x - 50,
            screenLocation.y - 20
        );
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 窗口关闭时也把 Tab 还回去（可选行为）
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // 如果还没有手动 dock back，关闭时丢弃即可
                // 如需自动归还，取消下面的注释：
                // addTab(title, icon, comp, tooltip);
            }
        });

        frame.setVisible(true);
    }

    private void reset() {
        draggedTabIndex = -1;
        isDragging = false;
        dragStartPoint = null;
        hideGhostWindow();
    }

    private Rectangle getBoundsOnScreen() {
        try {
            Point loc = getLocationOnScreen();
            return new Rectangle(loc.x, loc.y, getWidth(), getHeight());
        } catch (Exception e) {
            return new Rectangle();
        }
    }

    // ─── Demo Main ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DetachableTabbedPane Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 450);
            frame.setLocationRelativeTo(null);

            DetachableTabbedPane tabbedPane = new DetachableTabbedPane();

            // Tab 1
            JPanel p1 = new JPanel(new BorderLayout());
            p1.setBackground(new Color(240, 248, 255));
            p1.add(new JLabel("  📊 Dashboard Content", JLabel.LEFT), BorderLayout.NORTH);
            p1.add(new JTextArea("Some dashboard widgets here...\n\nDrag this tab out of the window!"), BorderLayout.CENTER);
            tabbedPane.addTab("Dashboard", p1);

            // Tab 2
            JPanel p2 = new JPanel(new BorderLayout());
            p2.setBackground(new Color(255, 248, 240));
            JTextArea editor = new JTextArea("// Code Editor\npublic class Hello {\n    public static void main(String[] args) {\n        System.out.println(\"Hello!\");\n    }\n}");
            editor.setFont(new Font("Monospaced", Font.PLAIN, 14));
            p2.add(new JScrollPane(editor));
            tabbedPane.addTab("Editor", p2);

            // Tab 3
            JPanel p3 = new JPanel(new GridLayout(3, 2, 10, 10));
            p3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            p3.setBackground(new Color(245, 255, 245));
            for (String s : new String[]{"Name:", "Email:", "Phone:", "Company:", "Role:", "Notes:"}) {
                if (s.endsWith(":")) p3.add(new JLabel(s));
                else p3.add(new JTextField(s));
            }
            // 补足字段
            String[] labels = {"Name:", "Email:", "Phone:"};
            String[] fields = {"Alice", "alice@example.com", "555-1234"};
            tabbedPane.addTab("Profile", p3);

            frame.add(tabbedPane);

            JLabel hint = new JLabel("  💡 Tip: Drag a tab OUT of the window to detach it into a floating window", JLabel.LEFT);
            hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
            hint.setForeground(Color.GRAY);
            hint.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            frame.add(hint, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }
}