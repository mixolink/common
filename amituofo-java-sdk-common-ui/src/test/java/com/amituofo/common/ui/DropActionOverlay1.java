package com.amituofo.common.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * 增强型拖放覆盖层组件 支持文件拖放和Table间数据拖放
 *
 * @author  Your Name
 * @version 2.0
 */
public class DropActionOverlay1 {

	// 标准 DataFlavor
	private static final DataFlavor WINDOWS_FILE_FLAVOR = DataFlavor.javaFileListFlavor;
	private static final DataFlavor LINUX_FILE_FLAVOR;

	static {
		DataFlavor temp = null;
		try {
			temp = new DataFlavor("text/uri-list;class=java.lang.String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		LINUX_FILE_FLAVOR = temp;
	}

	private JComponent targetComponent;
	private JLayeredPane layeredPane;
	private DropOverlayPanel overlayPanel;
	private List<DropAction> dropActions;
	private BiConsumer<TransferData, DropAction> dropHandler;
	private List<DataFlavor> acceptedFlavors;
	private boolean enableOverlay = true;

	/**
	 * 构造函数
	 *
	 * @param targetComponent 目标组件（如 JScrollPane 或 ItemTable）
	 * @param layeredPane     父级 JLayeredPane
	 */
	public DropActionOverlay1(JComponent targetComponent, JLayeredPane layeredPane) {
		this.targetComponent = targetComponent;
		this.layeredPane = layeredPane;
		this.dropActions = new ArrayList<>();
		this.acceptedFlavors = new ArrayList<>();

		// 默认接受文件拖放
		acceptedFlavors.add(WINDOWS_FILE_FLAVOR);
		if (LINUX_FILE_FLAVOR != null) {
			acceptedFlavors.add(LINUX_FILE_FLAVOR);
		}

		initOverlayPanel();
	}

	/**
	 * 初始化覆盖面板
	 */
	private void initOverlayPanel() {
		overlayPanel = new DropOverlayPanel();
		overlayPanel.setVisible(false);
		layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);
	}

	/**
	 * 添加接受的 DataFlavor
	 *
	 * @param flavor 数据类型
	 */
	public void addAcceptedFlavor(DataFlavor flavor) {
		if (!acceptedFlavors.contains(flavor)) {
			acceptedFlavors.add(flavor);
		}
	}

	/**
	 * 移除接受的 DataFlavor
	 *
	 * @param flavor 数据类型
	 */
	public void removeAcceptedFlavor(DataFlavor flavor) {
		acceptedFlavors.remove(flavor);
	}

	/**
	 * 设置是否启用覆盖层
	 *
	 * @param enable true=显示选项面板，false=不显示（直接使用默认操作）
	 */
	public void setEnableOverlay(boolean enable) {
		this.enableOverlay = enable;
	}

	/**
	 * 添加拖放操作
	 *
	 * @param label     操作名称
	 * @param color     操作颜色
	 * @param isDefault 是否为默认操作
	 */
	public void addAction(String label, Color color, boolean isDefault) {
		if (isDefault) {
			// 将其他操作的默认状态改为 false
			for (DropAction action : dropActions) {
				action.isDefault = false;
			}
		}
		dropActions.add(new DropAction(label, color, isDefault));
	}

	/**
	 * 移除指定名称的操作
	 *
	 * @param  label 操作名称
	 * @return       是否成功移除
	 */
	public boolean removeAction(String label) {
		return dropActions.removeIf(action -> action.label.equals(label));
	}

	/**
	 * 清空所有操作
	 */
	public void clearActions() {
		dropActions.clear();
	}

	/**
	 * 获取所有操作
	 *
	 * @return 操作列表的副本
	 */
	public List<DropAction> getActions() {
		return new ArrayList<>(dropActions);
	}

	/**
	 * 获取默认操作
	 *
	 * @return 默认操作，如果没有则返回第一个操作
	 */
	public DropAction getDefaultAction() {
		for (DropAction action : dropActions) {
			if (action.isDefault) {
				return action;
			}
		}
		return dropActions.isEmpty() ? null : dropActions.get(0);
	}

	/**
	 * 设置拖放处理器
	 *
	 * @param handler 处理器，接收 TransferData 和选中的操作
	 */
	public void setDropHandler(BiConsumer<TransferData, DropAction> handler) {
		this.dropHandler = handler;
	}

	/**
	 * 更新覆盖面板的大小和位置
	 *
	 * @param bounds 新的边界
	 */
	public void updateBounds(Rectangle bounds) {
		overlayPanel.setBounds(bounds);
	}

	/**
	 * 检查拖拽数据是否可接受
	 *
	 * @param transferable 拖拽数据
	 * @return 是否可接受
	 */
	public boolean isDragAcceptable(Transferable transferable) {
		for (DataFlavor flavor : acceptedFlavors) {
			if (transferable.isDataFlavorSupported(flavor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取可接受的 DataFlavor
	 *
	 * @param transferable 拖拽数据
	 * @return 可接受的 DataFlavor，如果没有则返回 null
	 */
	public DataFlavor getAcceptedFlavor(Transferable transferable) {
		for (DataFlavor flavor : acceptedFlavors) {
			if (transferable.isDataFlavorSupported(flavor)) {
				return flavor;
			}
		}
		return null;
	}

	/**
	 * 设置覆盖层可见性
	 *
	 * @param visible 是否可见
	 */
	public void setVisible(boolean visible) {
		if (enableOverlay && !dropActions.isEmpty()) {
			overlayPanel.setVisible(visible);
		} else if (!visible) {
			overlayPanel.setVisible(false);
		}
	}

	/**
	 * 设置拖拽位置
	 *
	 * @param position 拖拽位置
	 */
	public void setDragPosition(Point position) {
		overlayPanel.setDragPosition(position);
		overlayPanel.repaint();
	}

	/**
	 * 获取选中的操作
	 *
	 * @param position 鼠标位置
	 * @return 选中的操作
	 */
	public DropAction getSelectedAction(Point position) {
		return overlayPanel.getSelectedAction(position);
	}

	/**
	 * 处理拖放数据
	 *
	 * @param transferable 拖放数据
	 * @param location     拖放位置
	 */
	public void handleDrop(Transferable transferable, Point location) {
		try {
			DataFlavor usedFlavor = getAcceptedFlavor(transferable);

			if (usedFlavor == null) {
				return;
			}

			// 确定使用的操作
			DropAction action;
			if (enableOverlay && overlayPanel.isVisible()) {
				action = overlayPanel.getSelectedAction(location);
				if (action == null) {
					action = getDefaultAction();
				}
			} else {
				action = getDefaultAction();
			}

			if (action != null && dropHandler != null) {
				TransferData transferData = new TransferData(transferable, usedFlavor);
				dropHandler.accept(transferData, action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			setVisible(false);
		}
	}

	/**
	 * 拖放操作类
	 */
	public static class DropAction {
		public String label;
		public Color color;
		public boolean isDefault;

		public DropAction(String label, Color color, boolean isDefault) {
			this.label = label;
			this.color = color;
			this.isDefault = isDefault;
		}

		@Override
		public String toString() {
			return label + (isDefault ? " (默认)" : "");
		}
	}

	/**
	 * 传输数据封装类
	 */
	public static class TransferData {
		private Transferable transferable;
		private List<File> files;
		private DataFlavor usedFlavor;

		public TransferData(Transferable transferable, DataFlavor usedFlavor) {
			this.transferable = transferable;
			this.usedFlavor = usedFlavor;
			this.files = extractFiles(transferable, usedFlavor);
		}

		/**
		 * 获取原始 Transferable 对象
		 */
		public Transferable getTransferable() {
			return transferable;
		}

		/**
		 * 获取使用的 DataFlavor
		 */
		public DataFlavor getUsedFlavor() {
			return usedFlavor;
		}

		/**
		 * 获取文件列表（如果是文件拖放）
		 */
		public List<File> getFiles() {
			return files;
		}

		/**
		 * 是否为文件拖放
		 */
		public boolean isFileDrop() {
			return files != null && !files.isEmpty();
		}

		/**
		 * 是否为自定义数据拖放（如 Table 间拖放）
		 */
		public boolean isCustomDrop() {
			return !isFileDrop();
		}

		/**
		 * 提取文件列表
		 */
		private List<File> extractFiles(Transferable t, DataFlavor flavor) {
			try {
				if (flavor.equals(WINDOWS_FILE_FLAVOR)) {
					// Windows 文件列表
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) t.getTransferData(WINDOWS_FILE_FLAVOR);
					return files;
				} else if (LINUX_FILE_FLAVOR != null && flavor.equals(LINUX_FILE_FLAVOR)) {
					// Linux URI 列表
					String data = (String) t.getTransferData(LINUX_FILE_FLAVOR);
					List<File> files = new ArrayList<>();
					for (StringTokenizer st = new StringTokenizer(data, "\r\n"); st.hasMoreTokens();) {
						String token = st.nextToken().trim();
						if (token.startsWith("#") || token.isEmpty()) {
							continue;
						}
						files.add(new File(new URI(token)));
					}
					return files;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 覆盖面板
	 */
	public static class DropOverlayPanel extends JPanel {
		private Point dragPosition;
		private List<Rectangle> actionBoxes;
		private int hoveredBox = -1;
		private List<DropAction> dropActions = new ArrayList<>();

		// 可配置的样式参数
		private Color backgroundColor = new Color(0, 0, 0, 50);
		private Color boxBackgroundColor = new Color(80, 80, 80, 180);
		private Color normalBorderColor = new Color(255, 255, 255, 150);
		private Color hoverBorderColor = Color.WHITE;
		private Color normalTextColor = new Color(255, 255, 255, 200);
		private Color hoverTextColor = Color.WHITE;
		private float[] dashPattern = { 5f, 3f };

		public DropOverlayPanel() {
			setOpaque(false);
			actionBoxes = new ArrayList<>();
		}

		public void setDragPosition(Point position) {
			this.dragPosition = position;
			updateHoveredBox();
		}

		private void updateHoveredBox() {
			hoveredBox = -1;
			if (dragPosition != null && actionBoxes != null) {
				for (int i = 0; i < actionBoxes.size(); i++) {
					if (actionBoxes.get(i) != null && actionBoxes.get(i).contains(dragPosition)) {
						hoveredBox = i;
						break;
					}
				}
			}
		}

		public DropAction getSelectedAction(Point position) {
			for (int i = 0; i < actionBoxes.size() && i < dropActions.size(); i++) {
				if (actionBoxes.get(i) != null && actionBoxes.get(i).contains(position)) {
					return dropActions.get(i);
				}
			}
			return null;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// 绘制半透明背景
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());

			// 绘制提示文本
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("微软雅黑", Font.BOLD, 20));
			String hint = "拖放到下方选项";
			FontMetrics fm = g2d.getFontMetrics();
			int hintWidth = fm.stringWidth(hint);
			g2d.drawString(hint, (getWidth() - hintWidth) / 2, 80);

			// 布局参数
			int panelWidth = getWidth();
			int panelHeight = getHeight();

			int selectionAreaHeight = panelHeight / 3;
			int selectionAreaTop = (panelHeight - selectionAreaHeight) / 2;
			int totalWidth = (int) (panelWidth * 0.7);
			int startX = (panelWidth - totalWidth) / 2;

			actionBoxes.clear();

			// 分离默认操作和其他操作
			DropAction defaultAction = null;
			List<DropAction> otherActions = new ArrayList<>();

			for (DropAction action : dropActions) {
				if (action.isDefault) {
					defaultAction = action;
				} else {
					otherActions.add(action);
				}
			}

			int defaultBoxHeight = (int) (selectionAreaHeight * 0.6);
			int spacing = 15;
			int startY = selectionAreaTop;

			// 绘制默认操作
			if (defaultAction != null) {
				Rectangle box = new Rectangle(startX, startY, totalWidth, defaultBoxHeight);
				actionBoxes.add(box);
				int actionIndex = dropActions.indexOf(defaultAction);
				boolean isHovered = (actionIndex == hoveredBox);

				drawActionBox(g2d, box, defaultAction, isHovered, true);
			}

			// 绘制其他操作
			if (!otherActions.isEmpty()) {
				int otherBoxHeight = selectionAreaHeight - defaultBoxHeight - spacing;
				int otherBoxWidth = (totalWidth - spacing * (otherActions.size() - 1)) / otherActions.size();
				int secondRowY = startY + defaultBoxHeight + spacing;

				for (int i = 0; i < otherActions.size(); i++) {
					DropAction action = otherActions.get(i);
					int x = startX + i * (otherBoxWidth + spacing);
					Rectangle box = new Rectangle(x, secondRowY, otherBoxWidth, otherBoxHeight);
					actionBoxes.add(box);

					int actionIndex = dropActions.indexOf(action);
					boolean isHovered = (actionIndex == hoveredBox);

					drawActionBox(g2d, box, action, isHovered, false);
				}
			}

			g2d.dispose();
		}

		private void drawActionBox(Graphics2D g2d, Rectangle box, DropAction action, boolean isHovered, boolean isDefault) {
			// 绘制底色
			g2d.setColor(boxBackgroundColor);
			g2d.fillRoundRect(box.x, box.y, box.width, box.height, 15, 15);

			// 绘制虚线边框
			if (isHovered) {
				g2d.setColor(hoverBorderColor);
				g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
			} else {
				g2d.setColor(normalBorderColor);
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
			}
			g2d.drawRoundRect(box.x, box.y, box.width, box.height, 15, 15);

			// 绘制文本
			int fontSize = isDefault ? Math.max(18, Math.min(28, box.height / 4)) : Math.max(14, Math.min(18, box.height / 4));
			if (isHovered)
				fontSize += 2;
			g2d.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
			g2d.setColor(isHovered ? hoverTextColor : normalTextColor);

			FontMetrics fm = g2d.getFontMetrics();
			int textWidth = fm.stringWidth(action.label);
			int textX = box.x + (box.width - textWidth) / 2;
			int textY = box.y + (box.height + fm.getAscent()) / 2 - 5;
			g2d.drawString(action.label, textX, textY);

			// 默认标签
			if (isDefault && box.width > 200) {
				g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
				String defaultLabel = "默认";
				int labelWidth = g2d.getFontMetrics().stringWidth(defaultLabel);
				int labelX = box.x + box.width - labelWidth - 15;
				int labelY = box.y + 20;
				g2d.setColor(new Color(255, 255, 255, 150));
				g2d.drawString(defaultLabel, labelX, labelY);
			}
		}

		// Getter 和 Setter 方法用于自定义样式
		public void setBackgroundColor(Color color) {
			this.backgroundColor = color;
		}

		public void setBoxBackgroundColor(Color color) {
			this.boxBackgroundColor = color;
		}

		public void setNormalBorderColor(Color color) {
			this.normalBorderColor = color;
		}

		public void setHoverBorderColor(Color color) {
			this.hoverBorderColor = color;
		}

		public void setNormalTextColor(Color color) {
			this.normalTextColor = color;
		}

		public void setHoverTextColor(Color color) {
			this.hoverTextColor = color;
		}

		public void setDropActions(List<DropAction> actions) {
			this.dropActions = new ArrayList<>(actions);
		}
	}

	/**
	 * 获取覆盖面板，用于自定义样式
	 *
	 * @return 覆盖面板实例
	 */
	public DropOverlayPanel getOverlayPanel() {
		return overlayPanel;
	}

	/**
	 * 设置操作列表
	 *
	 * @param actions 操作列表
	 */
	public void setActions(List<DropAction> actions) {
		this.dropActions = new ArrayList<>(actions);
		if (overlayPanel != null) {
			overlayPanel.setDropActions(actions);
		}
	}
}
