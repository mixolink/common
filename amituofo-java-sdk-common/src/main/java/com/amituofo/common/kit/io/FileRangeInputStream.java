package com.amituofo.common.kit.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileRangeInputStream extends FileInputStream {

//	private long limitedLen;
	private long remainReadableLen;

	public FileRangeInputStream(String name, long offset, long length) throws IOException {
		super(name);
		init(offset, length);
	}

	public FileRangeInputStream(File file, long offset, long length) throws IOException {
		super(file);
		init(offset, length);
	}

	private void init(long offset, long length) throws IOException {
		long avllen = super.available();
		if (length > avllen) {
			length = avllen;
		} else if (length < 0) {
			length = avllen;
		}
		long limitedLen = length;
		this.remainReadableLen = limitedLen;
		if (offset > 0) {
			this.skip(offset);
		}
	}

	@Override
	public int read() throws IOException {
		if (remainReadableLen > 0) {
			int value = super.read();
			remainReadableLen--;
			return value;
		}
		return -1;
	}

	@Override
	public int read(byte[] b) throws IOException {
		if (remainReadableLen > 0) {
			int rlen = -1;
			if (b.length <= remainReadableLen) {
				rlen = super.read(b);
			} else {
				rlen = super.read(b, 0, (int) remainReadableLen);
			}
			if (rlen < 0) {
				remainReadableLen = 0;
			} else {
				remainReadableLen -= rlen;
			}

			return rlen;
		}
		return -1;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (remainReadableLen > 0) {
			int rlen = -1;
			if (b.length <= remainReadableLen) {
				rlen = super.read(b, off, len);
			} else {
				rlen = super.read(b, off, (int) remainReadableLen);
			}
			if (rlen < 0) {
				remainReadableLen = 0;
			} else {
				remainReadableLen -= rlen;
			}

			return rlen;
		}
		return -1;
	}

}
