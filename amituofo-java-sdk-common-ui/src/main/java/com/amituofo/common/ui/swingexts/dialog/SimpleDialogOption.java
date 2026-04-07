package com.amituofo.common.ui.swingexts.dialog;

import java.awt.Frame;
import java.awt.Image;

import com.amituofo.common.ui.util.UIUtils;

public class SimpleDialogOption {
	private Frame owner = UIUtils.getDefaultTopFrame();
	private Image titleIcon;
	private int xOffset;
	private int yOffset;
	private int width = 800;
	private int height = 538;
	private boolean withEmptyBorder = true;
	private boolean withOKButton = true;
	private boolean withCancelButton = true;
	private boolean closeClickOutsite = false;
	private boolean resizeable = true;

	public SimpleDialogOption() {
	}
	
	public static SimpleDialogOption New() {
		return new SimpleDialogOption();
	}

	// --- With 方法设置 ---

	public SimpleDialogOption withOwner(Frame owner) {
		this.owner = owner;
		return this;
	}

	public SimpleDialogOption withTitleIcon(Image titleIcon) {
		this.titleIcon = titleIcon;
		return this;
	}

	public SimpleDialogOption withXOffset(int xOffset) {
		this.xOffset = xOffset;
		return this;
	}

	public SimpleDialogOption withYOffset(int yOffset) {
		this.yOffset = yOffset;
		return this;
	}

	public SimpleDialogOption withWidth(int width) {
		this.width = width;
		return this;
	}

	public SimpleDialogOption withHeight(int height) {
		this.height = height;
		return this;
	}

	public SimpleDialogOption withEmptyBorder(boolean withEmptyBorder) {
		this.withEmptyBorder = withEmptyBorder;
		return this;
	}

	public SimpleDialogOption withOKButton(boolean withOKButton) {
		this.withOKButton = withOKButton;
		return this;
	}

	public SimpleDialogOption withCancelButton(boolean withCancelButton) {
		this.withCancelButton = withCancelButton;
		return this;
	}

	public SimpleDialogOption withCloseClickOutsite(boolean closeClickOutsite) {
		this.closeClickOutsite = closeClickOutsite;
		return this;
	}

	public SimpleDialogOption withResizeable(boolean resizeable) {
		this.resizeable = resizeable;
		return this;
	}

	public Frame getOwner() {
		return owner;
	}

	public Image getTitleIcon() {
		return titleIcon;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isWithEmptyBorder() {
		return withEmptyBorder;
	}

	public boolean isWithOKButton() {
		return withOKButton;
	}

	public boolean isWithCancelButton() {
		return withCancelButton;
	}

	public boolean isCloseClickOutsite() {
		return closeClickOutsite;
	}

	public boolean isResizeable() {
		return resizeable;
	}

}