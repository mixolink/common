package com.amituofo.common.ui;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

/**
 * DropActionOverlay 集成示例 展示如何与现有的 TransferHandler 配合使用
 */
public class DropActionOverlayIntegrationDemo extends JFrame {

	private JTable leftTable;
	private JTable rightTable;
	private DefaultTableModel leftModel;
	private DefaultTableModel rightModel;
	private String leftDirectory = "/左侧目录";
	private String rightDirectory = "/右侧目录";

	// 自定义 DataFlavor（模拟 ItemTransferable.ITEM_FLAVORS）
	private static DataFlavor CUSTOM_ITEM_FLAVOR;

	static {
		try {
			CUSTOM_ITEM_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.String");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public DropActionOverlayIntegrationDemo() {
		setTitle("Table 间拖放集成示例");
		setSize(1200, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		initComponents();
	}

	private void initComponents() {
		setLayout(new GridLayout(1, 2, 10, 0));

		// 左侧表格
		add(createTablePanel("左侧表格", true));

		// 右侧表格
		add(createTablePanel("右侧表格", false));
	}

	private JPanel createTablePanel(String title, boolean isLeft) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(title));

		// 创建 JLayeredPane
		JLayeredPane layeredPane = new JLayeredPane();

		// 创建表格
		String[] columns = { "文件名", "路径", "大小" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		JTable table = new JTable(model);

		// 设置表格的 TransferHandler（允许拖出）
		table.setDragEnabled(true);
		table.setDropMode(DropMode.ON_OR_INSERT_ROWS);
		SourceTransferHandler transferHandler = new SourceTransferHandler(table);
		table.setTransferHandler(transferHandler);
		if (isLeft) {
			leftTable = table;
			leftModel = model;
			// 添加示例数据
			leftModel.addRow(new Object[] { "doc1.txt", leftDirectory + "/doc1.txt", "1.2 KB" });
			leftModel.addRow(new Object[] { "doc2.pdf", leftDirectory + "/doc2.pdf", "520 KB" });
			leftModel.addRow(new Object[] { "image.png", leftDirectory + "/image.png", "2.1 MB" });
		} else {
			rightTable = table;
			rightModel = model;
			// 右侧表格初始为空
		}

		JScrollPane scrollPane = new JScrollPane(table);
		layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);

		panel.add(layeredPane, BorderLayout.CENTER);

		// 为两个表格都设置 DropActionOverlay（实现双向拖拽）
		setupDropOverlay(table, scrollPane, layeredPane, transferHandler);

		// 监听大小变化
		panel.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int w = layeredPane.getWidth();
				int h = layeredPane.getHeight();
				scrollPane.setBounds(0, 0, w, h);

				// 更新 overlay 边界
				Component[] components = layeredPane.getComponentsInLayer(JLayeredPane.PALETTE_LAYER);
				if (components.length > 0) {
					components[0].setBounds(0, 0, w, h);
				}
			}
		});

		return panel;
	}

	private void setupDropOverlay(JTable table, JScrollPane scrollPane, JLayeredPane layeredPane, SourceTransferHandler transferHandler) {
		// 创建 DropActionOverlay
		DropActionOverlay1 dropOverlay = new DropActionOverlay1(scrollPane, layeredPane);

		// 添加自定义 DataFlavor（支持 Table 间拖放）
		if (CUSTOM_ITEM_FLAVOR != null) {
			dropOverlay.addAcceptedFlavor(CUSTOM_ITEM_FLAVOR);
		}

		// 添加操作选项
		dropOverlay.addAction("覆盖", new Color(100, 149, 237), true);
		dropOverlay.addAction("跳过已存在", new Color(144, 238, 144), false);
		dropOverlay.addAction("全部添加", new Color(255, 218, 185), false);
		dropOverlay.addAction("取消", new Color(216, 191, 216), false);

		// 设置拖放处理器，传递目标表格信息
		dropOverlay.setDropHandler((transferData, action) -> {
			handleDrop(transferData, action, table);
		});

		// 将 DropActionOverlay 设置到 TransferHandler 中
		transferHandler.setDropOverlay(dropOverlay);

		// 初始化边界
		SwingUtilities.invokeLater(() -> {
			int w = layeredPane.getWidth();
			int h = layeredPane.getHeight();
			scrollPane.setBounds(0, 0, w, h);
			dropOverlay.updateBounds(new Rectangle(0, 0, w, h));
		});
	}

	/**
	 * 处理拖放数据 模拟您的 ItemSelectionTransferHandler.transmit() 逻辑
	 */
	private void handleDrop(DropActionOverlay1.TransferData transferData, DropActionOverlay1.DropAction action, JTable targetTable) {
		try {
			if (transferData.isFileDrop()) {
				// 文件拖放（从文件浏览器）
				handleFileDrop(transferData.getFiles(), action, targetTable);
			} else if (transferData.getUsedFlavor().equals(CUSTOM_ITEM_FLAVOR)) {
				// Table 间拖放（模拟 ItemTransferable.ITEM_FLAVORS）
				handleTableDrop(transferData, action, targetTable);
			} else {
				// 其他类型的拖放
				JOptionPane.showMessageDialog(this, "不支持的拖放类型", "错误", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "拖放处理失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 处理文件拖放
	 */
	private void handleFileDrop(java.util.List<File> files, DropActionOverlay1.DropAction action, JTable targetTable) {
		if (files == null || files.isEmpty()) {
			return;
		}

		// 根据目标表格选择对应的数据模型和目录
		DefaultTableModel model = (targetTable == leftTable) ? leftModel : rightModel;
		String targetDirectory = (targetTable == leftTable) ? leftDirectory : rightDirectory;

		for (File file : files) {
			String fileName = file.getName();
			String filePath = targetDirectory + "/" + fileName;
			String fileSize = formatFileSize(file.length());

			switch (action.label) {
			case "覆盖":
				int existingRow = findFileRow(fileName, model);
				if (existingRow >= 0) {
					model.setValueAt(filePath, existingRow, 1);
					model.setValueAt(fileSize, existingRow, 2);
				} else {
					model.addRow(new Object[] { fileName, filePath, fileSize });
				}
				break;

			case "跳过已存在":
				if (findFileRow(fileName, model) < 0) {
					model.addRow(new Object[] { fileName, filePath, fileSize });
				}
				break;

			case "全部添加":
				model.addRow(new Object[] { fileName, filePath, fileSize });
				break;

			case "取消":
				return;
			}
		}

		showMessage("文件拖放", String.format("使用【%s】模式处理了 %d 个文件，目标目录：%s", action.label, files.size(), targetDirectory));
	}

	/**
	 * 处理 Table 间拖放 模拟您的 ItemTransferable.ITEM_FLAVORS 处理逻辑
	 */
	private void handleTableDrop(DropActionOverlay1.TransferData transferData, DropActionOverlay1.DropAction action, JTable targetTable) {
		try {
			// 获取拖放的数据
			String data = (String) transferData.getTransferable().getTransferData(transferData.getUsedFlavor());

			// 解析数据（这里简化处理，实际应该解析您的 ItemPackage）
			String[] rows = data.split("\n");

			// 根据目标表格选择对应的数据模型和目录
			DefaultTableModel model = (targetTable == leftTable) ? leftModel : rightModel;
			String targetDirectory = (targetTable == leftTable) ? leftDirectory : rightDirectory;

			for (String row : rows) {
				if (row.trim().isEmpty())
					continue;

				String[] parts = row.split("\t");
				if (parts.length >= 3) {
					String fileName = parts[0];
					String filePath = targetDirectory + "/" + fileName;
					String fileSize = parts[2]; // 保持原始大小

					switch (action.label) {
					case "覆盖":
						int existingRow = findFileRow(fileName, model);
						if (existingRow >= 0) {
							model.setValueAt(filePath, existingRow, 1);
							model.setValueAt(fileSize, existingRow, 2);
						} else {
							model.addRow(new Object[] { fileName, filePath, fileSize });
						}
						break;

					case "跳过已存在":
						if (findFileRow(fileName, model) < 0) {
							model.addRow(new Object[] { fileName, filePath, fileSize });
						}
						break;

					case "全部添加":
						model.addRow(new Object[] { fileName, filePath, fileSize });
						break;

					case "取消":
						return;
					}
				}
			}

			showMessage("Table 拖放", String.format("使用【%s】模式处理了 %d 行数据，目标目录：%s", action.label, rows.length, targetDirectory));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int findFileRow(String fileName, DefaultTableModel model) {
		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getValueAt(i, 0).equals(fileName)) {
				return i;
			}
		}
		return -1;
	}

	private int findFileRow(String fileName) {
		return findFileRow(fileName, rightModel);
	}

	private String formatFileSize(long size) {
		if (size < 1024)
			return size + " B";
		if (size < 1024 * 1024)
			return String.format("%.1f KB", size / 1024.0);
		return String.format("%.1f MB", size / (1024.0 * 1024));
	}

	private void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 源表格的 TransferHandler（允许拖出数据）
	 */
	class SourceTransferHandler extends TransferHandler {
		private DropActionOverlay1 dropOverlay;
		private JTable table;

		public SourceTransferHandler(JTable table) {
			// TODO Auto-generated constructor stub
			this.table = table;
		}

		public void setDropOverlay(DropActionOverlay1 dropOverlay) {
			this.dropOverlay = dropOverlay;
		}

		@Override
		public int getSourceActions(JComponent c) {
			return COPY;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			if (!(c instanceof JTable)) {
				return null;
			}

			JTable table = (JTable) c;
			int[] selectedRows = table.getSelectedRows();

			if (selectedRows.length == 0) {
				return null;
			}

			// 构建传输数据
			StringBuilder sb = new StringBuilder();
			for (int row : selectedRows) {
				for (int col = 0; col < table.getColumnCount(); col++) {
					sb.append(table.getValueAt(row, col));
					if (col < table.getColumnCount() - 1) {
						sb.append("\t");
					}
				}
				sb.append("\n");
			}

			return new StringTransferable(sb.toString());
		}

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if(comp==table) {
				return false;
			}
			return true;
			// TODO Auto-generated method stub
		}

//		@Override
//        public boolean canImport(TransferHandler.TransferSupport support) {
//            if (!support.isDrop() || dropOverlay == null) {
//                if (dropOverlay != null) {
//                    dropOverlay.setVisible(false);
//                }
//                return false;
//            }
//
//            // 检查数据类型是否可接受
//            if (!dropOverlay.isDragAcceptable(support.getTransferable())) {
//                if (dropOverlay != null) {
//                    dropOverlay.setVisible(false);
//                }
//                return false;
//            }
//
//            // 显示并更新 DropOverlayPanel
//            Point location = support.getDropLocation().getDropPoint();
//            dropOverlay.setDragPosition(location);
//            dropOverlay.setVisible(true);
//            
//            return true;
//        }
//
//        @Override
//        public boolean importData(TransferHandler.TransferSupport support) {
//            if (!canImport(support) || dropOverlay == null) {
//                if (dropOverlay != null) {
//                    dropOverlay.setVisible(false);
//                }
//                return false;
//            }
//
//            try {
//                Point location = support.getDropLocation().getDropPoint();
//                dropOverlay.handleDrop(support.getTransferable(), location);
//                return true;
//            } finally {
//                if (dropOverlay != null) {
//                    dropOverlay.setVisible(false);
//                }
//            }
//        }

		@Override
		protected void exportDone(JComponent source, Transferable data, int action) {
			if (dropOverlay != null) {
				dropOverlay.setVisible(false);
			}
			super.exportDone(source, data, action);
		}
	}

	/**
	 * 字符串 Transferable（模拟您的 ItemTransferable）
	 */
	class StringTransferable implements java.awt.datatransfer.Transferable {
		private String data;

		public StringTransferable(String data) {
			this.data = data;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { CUSTOM_ITEM_FLAVOR, DataFlavor.stringFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return CUSTOM_ITEM_FLAVOR.equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) {
			return data;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			DropActionOverlayIntegrationDemo frame = new DropActionOverlayIntegrationDemo();
			frame.setVisible(true);
		});
	}
}
