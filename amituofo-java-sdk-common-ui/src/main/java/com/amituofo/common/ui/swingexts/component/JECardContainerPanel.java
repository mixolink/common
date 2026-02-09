package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.ui.action.CardAction;
import com.amituofo.common.ui.action.DestroyAction;
import com.amituofo.common.ui.lang.CardComponent;
import com.amituofo.common.ui.listener.ActiveListener;
import com.amituofo.common.ui.util.UIUtils;

public class JECardContainerPanel<C extends JComponent, D> extends JEPanel {
//	public static boolean DISABLE_SWITCH_ANIMATION = false;

	private CardComponent<C, D> topDataComponent = null;
	private Map<String, CardComponent<C, D>> components = new HashMap<String, CardComponent<C, D>>();

	private final JLayeredPane layeredPane;
	private final Timer animator;

	// 动画常量
	private final int ANIMATION_DURATION = 250; // 动画总时长 (ms)
	private final int FRAME_INTERVAL = 1000 / 60; // 60 FPS

	private long startTime;
	private CardSlideDirection defaultSlideDirection = null;
	private CardSlideDirection direction = null;
	private JPanel snapshotPanel; // 用于动画的临时面板

	public JECardContainerPanel(boolean disableAnimation) {
		super.setLayout(new BorderLayout());
		if (disableAnimation) {
			layeredPane = null;
			animator = null;
		} else {
			layeredPane = new JLayeredPane();
			add(layeredPane, BorderLayout.CENTER);

			// 初始化动画 Timer
			animator = new Timer(FRAME_INTERVAL, e -> animateStep());
			animator.setRepeats(true);
		}
	}

	public JECardContainerPanel() {
		this(true);
	}

	public void setLayout(LayoutManager mgr) {
	}

	public void addCard(String name, C comp, D data) {
		CardComponent<C, D> cd = new CardComponent<C, D>(name, comp, data);
		synchronized (components) {
			components.put(name, cd);

//			if (components.size() == 1) {
//				switchTo(name);
//			}
		}
	}

//	public void add(Component comp, String constraints) {
//		addComponent(constraints, (C) comp, null);
//	}

	@Override
	public void destroy() {
		synchronized (components) {
			topDataComponent = null;
			if (!components.isEmpty()) {
				for (CardComponent<C, D> dc : components.values()) {
					if (dc.getComponent() instanceof Destroyable) {
						((Destroyable) dc.getComponent()).destroy();
					}
				}

				super.removeAll();
				components.clear();
			}
		}
		super.destroy();
	}

	@Override
	public void refresh() {
		if (topDataComponent != null) {
			Component c = topDataComponent.getComponent();
			if (c instanceof CardAction) {
				((CardAction) c).refresh();
			}
		}
	}

	public void switchTo(String name) {
		switchTo(name, null);
	}

	public void switchTo(String name, Callback<CardComponent<C, D>> callback) {
		if (layeredPane != null) {
			slideTo(name, defaultSlideDirection, callback);
		} else {
			normalSwitchTo(name, callback);
		}
	}

	private void normalSwitchTo(String name, Callback<CardComponent<C, D>> callback) {
		synchronized (components) {
			if (!components.containsKey(name)) {
				return;
			}

			final CardComponent<C, D> nextComponent = components.get(name);

			if (nextComponent == topDataComponent || nextComponent == null) {
				if (callback != null) {
					callback.callback(nextComponent);
				}
				return;
			}

			if (topDataComponent != null) {
				Component c = topDataComponent.getComponent();
				if (c instanceof CardAction) {
					((CardAction) c).deactiving();
				}
			}

			JComponent nextPanel = nextComponent.getComponent();
			super.removeAll();
//			System.out.println("0Is EDT: " + SwingUtilities.isEventDispatchThread() + " " + name + " " + System.identityHashCode(CardContainerPanel.this));
			super.add(nextPanel, BorderLayout.CENTER);
			super.revalidate();
			super.repaint();

			topDataComponent = nextComponent;

			if (nextPanel instanceof CardAction) {
				((CardAction) nextPanel).activing();
			} else {
//				nextPanel.revalidate();
//				nextPanel.repaint();
			}

			if (callback != null) {
				callback.callback(nextComponent);
			}
		}
	}

//	@Override
//	public void removeAll() {
//		synchronized (components) {
//			topDataComponent = null;
//			if (!components.isEmpty()) {
//				super.removeAll();
//				components.clear();
//			}
//		}
//	}

	public Collection<CardComponent<C, D>> getCards() {
		return components.values();
	}

	public boolean hasCard(String name) {
		return components.containsKey(name);
	}

	public CardComponent<C, D> getCard(String name) {
		return components.get(name);
	}

	public CardComponent<C, D> getTopCard() {
		return topDataComponent;
	}

	public CardComponent<C, D> removeAndDestroyCard(String name) {
		synchronized (components) {
			CardComponent<C, D> cd = components.remove(name);
			if (cd != null) {
				if (topDataComponent == cd) {
					topDataComponent = null;
				}

				remove(cd.getComponent());

				if (cd.getComponent() instanceof DestroyAction) {
					((DestroyAction) cd.getComponent()).destroy();
				}
			}

			return cd;
		}
	}

	// ---

	public void setSwitchAnimation(CardSlideDirection direction) {
		this.defaultSlideDirection = direction;
	}

	/**
	 * 切换到指定的面板并播放动画
	 * 
	 * @param name      目标面板的名称
	 * @param direction 新面板滑入的方向
	 */
	public void slideTo(String name, CardSlideDirection direction) {
		slideTo(name, direction, null);
	}

	public void slideTo(String name, CardSlideDirection direction, Callback<CardComponent<C, D>> callback) {
		if (layeredPane == null) {
			normalSwitchTo(name, callback);
			return;
		}

		if (animator.isRunning()) {
			return; // 避免在动画进行时再次切换
		}

		synchronized (components) {
			if (!components.containsKey(name)) {
				return;
			}

			final CardComponent<C, D> nextComponent = components.get(name);

			if (nextComponent == topDataComponent || nextComponent == null) {
				if (callback != null) {
					callback.callback(nextComponent);
				}
				return;
			}

			JComponent nextPanel = nextComponent.getComponent();
			if (direction == null || topDataComponent == null) {
				replaceContentPanel(nextPanel);
			} else {
				this.direction = direction;
				C currentPanel = topDataComponent.getComponent();

				// 1. 获取当前面板快照 (Old Image)
				BufferedImage oldImage = UIUtils.createSnapshot(currentPanel);

				if (currentPanel instanceof CardAction) {
					((CardAction) currentPanel).deactiving();
				}

				// 2. 将目标面板替换到内容层 (Default Layer)
				replaceContentPanel(nextPanel);
				// 3. 获取目标面板快照 (New Image)
				BufferedImage newImage = UIUtils.createSnapshot(nextPanel);
				// 4. 创建动画面板并添加到动画层
				createSlidingSnapshotPanel(oldImage, newImage);
				// 5. 启动动画
				this.startTime = System.currentTimeMillis();
				this.animator.start();
			}

			topDataComponent = nextComponent;

			if (nextPanel instanceof CardAction) {
				((CardAction) nextPanel).activing();
			} else {
//				nextPanel.revalidate();
//				nextPanel.repaint();
			}

			if (callback != null) {
				callback.callback(nextComponent);
			}
		}
	}

	@Override
	public void doLayout() {
		super.doLayout();

		if (layeredPane == null) {
			return;
		}

		// 1. 确保 layeredPane 充满整个容器
		// 这行代码是关键！它使 layeredPane 的大小与 SnapshotSlidingContainer 保持一致。
		layeredPane.setBounds(0, 0, getWidth(), getHeight());
		// 2. 确保内容面板也充满 layeredPane
		if (topDataComponent != null) {
			// currentPanel 放在 DEFAULT_LAYER，它的尺寸必须与 layeredPane 一致
			topDataComponent.getComponent().setSize(layeredPane.getSize());
		}

		// 3. 如果动画正在进行，确保 snapshotPanel 的尺寸正确（可选，通常setBounds已经处理）
//			if (snapshotPanel != null) {
//				snapshotPanel.setSize(layeredPane.getSize());
//			}
	}

	/**
	 * 将目标面板设置到 JLayeredPane 的内容层 (Default Layer)
	 */
	private void replaceContentPanel(JComponent nextPanel) {
//		if (layeredPane == null) {
//			return;
//		}

		if (topDataComponent != null) {
//			layeredPane.removeAll();
			layeredPane.remove(topDataComponent.getComponent());
		}

		// 确保目标面板的大小正确
//		layeredPane.setBounds(0, 0, 1794, 1289);
//		nextPanel.setBounds(0, 0, 1794, 1289);
//		System.out.println(this.getSize());
//		System.out.println(layeredPane.getSize());
//		nextPanel.setSize(layeredPane.getSize());
		// 将真正的面板放到最底层 (Default Layer)，动画完成后它就露出来了
		layeredPane.add(nextPanel, JLayeredPane.DEFAULT_LAYER);
		super.revalidate();
		super.repaint();
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
		JComponent oldComp = new JEImageComponent(oldImage);
		oldComp.setBounds(0, 0, W, H);
		snapshotPanel.add(oldComp);

		// 创建 New Panel 组件
		JComponent newComp = new JEImageComponent(newImage);
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

	private void animateStep() {
		if (snapshotPanel == null) {
			animator.stop(); // 停止计时器，防止后续调用
			return;
		}

		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - startTime;

		// 1. 计算线性进度 (0.0 到 1.0)
		double linearProgress = (double) elapsedTime / ANIMATION_DURATION;

		// 2. 判断动画是否结束，并计算最终的缓动进度 (progress)
		final double progress;
		if (linearProgress >= 1.0) {
			// 动画结束
			animator.stop();
			animationFinished();
			// 强制进度为 1.0，确保精确到达终点
			progress = 1.0;
			return;
		} else {
			// 3. 应用 Ease Out Quadratic (二次方) 缓动函数
			// f(t) = 1 - (1 - t)^2
			// 这使得速度先快后慢，在切换即将完成时平滑减速
			progress = 1.0 - Math.pow(1.0 - linearProgress, 2);
		}

		int W = layeredPane.getWidth();
		int H = layeredPane.getHeight();

		// 动画面板包含两个子组件：索引 0 是 OldPanel，索引 1 是 NewPanel
		JComponent oldComp = (JComponent) snapshotPanel.getComponent(0);
		JComponent newComp = (JComponent) snapshotPanel.getComponent(1);

		// 计算移动距离
		// currentX/currentY 的变化现在是先快后慢的（非线性）
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
		super.revalidate();
		super.repaint();
	}
}
