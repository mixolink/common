package com.amituofo.common.resource;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconResource {
	private static final String BASE_PATH = "/com/amituofo/common/resource/";
	private static final String ICON_BASE_PATH = BASE_PATH + "icon/";
	private static final String IMG_BASE_PATH = BASE_PATH + "image/";

	public static final ImageIcon ICON_XXXX_16x16 = getIcon("cloud-1-16x16.png");
//	public final static ImageIcon ICON_VIEW_FILE_16x16 = getIcon("book-1-16x16.png");

	public static final ImageIcon ICON_OK_16x16 = getIcon("ok-8-16x16.png");
	public static final ImageIcon ICON_CANCEL_16x16 = getIcon("cancel-8-16x16.png");
	public static final ImageIcon ICON_SAVE_16x16 = getIcon("save-1-16x16.png");

	public static final ImageIcon ICON_ADD_16x16 = getIcon("add-9-16x16.png");
	public static final ImageIcon ICON_REMOVE_16x16 = getIcon("delete-9-16x16.png");

	public static final ImageIcon ICON_SWITCH_ON_24x24 = getIcon("switch-on-24x24.png");
	public static final ImageIcon ICON_SWITCH_OFF_24x24 = getIcon("switch-off-24x24.png");
	public static final ImageIcon ICON_SWITCH_ON_32x32 = getIcon("switch-on-32x32.png");
	public static final ImageIcon ICON_SWITCH_OFF_32x32 = getIcon("switch-off-32x32.png");

	public static final Image IMAGE_LOADING = getImage("loading7.gif");

	public static final Icon ICON_EMPTY_16x16 = new EmptyIcon(16, 16);

//	public final static Icon ICON_FOLDER_16x16 = (Icon) FileSystemView.getFileSystemView().getSystemIcon(new File(System.getProperty("java.io.tmpdir")));// (Icon)

	private static ImageIcon getIcon(String id) {
		return new ImageIcon(IconResource.class.getResource(ICON_BASE_PATH + id));
	}

	private static Image getImage(String id) {
		return Toolkit.getDefaultToolkit().getImage(IconResource.class.getResource(IMG_BASE_PATH + id));
	}

	public static ImageIcon getIcon(Class clazz, String base, String id) {
		return new ImageIcon(clazz.getResource(base + "/icon/" + id));
	}
	

	
}
