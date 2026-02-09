package com.amituofo.common.kit.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TheContent {

	private InputStream in;
	private long length;
//	private File file;

	public TheContent(InputStream in, long length) {
		super();
		this.in = in;
		this.length = length;
	}

	public TheContent(File file) throws FileNotFoundException {
		this.in = new FileInputStream(file);
		this.length = file.length();
	}

	public InputStream content() {
		return in;
	}

	public long length() {
		return length;
	}

	public static TheContent buildBlank() {
		return new TheContent(new ByteArrayInputStream(new byte[0]), 0);
	}

}
