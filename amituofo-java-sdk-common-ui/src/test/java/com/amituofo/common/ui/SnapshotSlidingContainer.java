package com.amituofo.common.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.amituofo.common.ui.swingexts.component.CardSlideDirection;

public class SnapshotSlidingContainer extends JComponent {

	private final Map<String, JComponent> cards = new HashMap<>();
	private JComponent currentPanel = null;
	private final JLayeredPane layeredPane;
	private final Timer animator;

	// 动画常量
	private final int ANIMATION_DURATION = 350; // 动画总时长 (ms)
	private final int FRAME_INTERVAL = 1000 / 60; // 60 FPS

	private long startTime;
	private CardSlideDirection direction;
	private JPanel snapshotPanel; // 用于动画的临时面板

	public SnapshotSlidingContainer() {
		// 使用 JLayeredPane 作为核心容器
		layeredPane = new JLayeredPane();
		setLayout(new BorderLayout());
		add(layeredPane, BorderLayout.CENTER);

		// 初始化动画 Timer
		animator = new Timer(FRAME_INTERVAL, e -> animateStep());
		animator.setRepeats(true);
	}

	// --- 核心方法：添加面板 ---
	public void addPanel(JComponent panel, String name) {
		cards.put(name, panel);
	}

	// --- 核心方法：切换面板 ---
	public void showPanel(String name, CardSlideDirection direction) {
		if (animator.isRunning())
			return;

		JComponent nextPanel = cards.get(name);
		if (nextPanel == null || nextPanel == currentPanel)
			return;

		this.direction = direction;

		// 1. 获取当前面板快照 (Old Image)
		BufferedImage oldImage = createSnapshot(currentPanel);
		// 2. 将目标面板替换到内容层 (Default Layer)
		replaceContentPanel(nextPanel);
		// 3. 获取目标面板快照 (New Image)
		BufferedImage newImage = createSnapshot(currentPanel);

		// 4. 创建动画面板并添加到动画层
		createSlidingSnapshotPanel(oldImage, newImage);

		// 5. 启动动画
		startTime = System.currentTimeMillis();
		animator.start();
	}

	// --- 辅助方法 ---

	/**
	 * 将目标面板设置到 JLayeredPane 的内容层 (Default Layer)
	 */
	private void replaceContentPanel(JComponent nextPanel) {
		if (currentPanel != null) {
			layeredPane.remove(currentPanel);
		}

		// 确保目标面板的大小正确
		nextPanel.setSize(layeredPane.getSize());

		// 将真正的面板放到最底层 (Default Layer)，动画完成后它就露出来了
		layeredPane.add(nextPanel, JLayeredPane.DEFAULT_LAYER);
		currentPanel = nextPanel;

		// 强制布局和绘制，确保 currentPanel 已准备好作为内容显示
		layeredPane.revalidate();
		layeredPane.repaint();
	}

	/**
	 * 创建包含两个快照的动画组件，并将其添加到动画层 (Palette Layer)
	 */
	private void createSlidingSnapshotPanel(BufferedImage oldImage, BufferedImage newImage) {
		int W = layeredPane.getWidth();
		int H = layeredPane.getHeight();

		// 动画面板必须使用绝对布局
		snapshotPanel = new JPanel(null);
		snapshotPanel.setBounds(0, 0, W, H);

		// 创建 Old Panel 组件
		JComponent oldComp = new ImageComponent(oldImage);
		oldComp.setBounds(0, 0, W, H);
		snapshotPanel.add(oldComp);

		// 创建 New Panel 组件
		JComponent newComp = new ImageComponent(newImage);
		int initialX = 0, initialY = 0;

		// 根据方向，设置 New Panel 的初始位置 (屏幕外)
		switch (direction) {
		case LEFT:
			initialX = W;
			break;
		case RIGHT:
			initialX = -W;
			break;
		case UP:
			initialY = H;
			break;
		case DOWN:
			initialY = -H;
			break;
		}
		newComp.setBounds(initialX, initialY, W, H);
		snapshotPanel.add(newComp);

		// 将动画面板放到动画层 (Palette Layer)
		layeredPane.add(snapshotPanel, JLayeredPane.PALETTE_LAYER);
	}

	/**
	 * 获取组件的图像快照
	 */
	private BufferedImage createSnapshot(JComponent component) {
		if (component == null || component.getWidth() <= 0 || component.getHeight() <= 0) {
			// 返回一个空白图片避免 NullPointer 或尺寸错误
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		component.paint(g); // 强制组件将自身绘制到Graphics对象上
		g.dispose();
		return image;
	}

	// --- 动画循环 ---
	private void animateStep() {
		if (snapshotPanel == null || snapshotPanel.getComponentCount() < 2) {
			animator.stop(); // 停止计时器，防止后续调用
			return;
		}

		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - startTime;

		// 计算进度
		double progress = (double) elapsedTime / ANIMATION_DURATION;
		if (progress > 1.0) {
			progress = 1.0;
			animator.stop();
			animationFinished();
			return;
		}

		int W = layeredPane.getWidth();
		int H = layeredPane.getHeight();

		// 动画面板包含两个子组件：索引 0 是 OldPanel，索引 1 是 NewPanel
		JComponent oldComp = (JComponent) snapshotPanel.getComponent(0);
		JComponent newComp = (JComponent) snapshotPanel.getComponent(1);

		// 计算移动距离
		int currentX = (int) (W * progress);
		int currentY = (int) (H * progress);

		// 更新两个快照组件的位置
		switch (direction) {
		case LEFT: // Old Panel 左移, New Panel 从右侧滑入
			oldComp.setLocation(-currentX, 0);
			newComp.setLocation(W - currentX, 0);
			break;
		case RIGHT: // Old Panel 右移, New Panel 从左侧滑入
			oldComp.setLocation(currentX, 0);
			newComp.setLocation(-W + currentX, 0);
			break;
		case UP: // Old Panel 上移, New Panel 从底部滑入
			oldComp.setLocation(0, -currentY);
			newComp.setLocation(0, H - currentY);
			break;
		case DOWN: // Old Panel 下移, New Panel 从顶部滑入
			oldComp.setLocation(0, currentY);
			newComp.setLocation(0, -H + currentY);
			break;
		}

		snapshotPanel.repaint(); // 强制重绘动画层
	}

	/**
	 * 动画结束后的清理工作
	 */
	private void animationFinished() {
		// 移除动画层中的临时面板
		layeredPane.remove(snapshotPanel);
		snapshotPanel = null;

		// 确保真正的内容面板（currentPanel）被显示和验证
		layeredPane.revalidate();
		layeredPane.repaint();
	}

	// --- JComponent 子类，用于绘制 Image 快照 ---
	public class ImageComponent extends JComponent {
		private final BufferedImage image;

		public ImageComponent(BufferedImage image) {
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				// 将BufferedImage绘制到组件的当前边界内
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		}
	}
}