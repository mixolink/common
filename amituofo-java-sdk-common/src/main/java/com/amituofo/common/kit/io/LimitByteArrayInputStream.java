/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.amituofo.common.kit.io;

import java.io.InputStream;

public class LimitByteArrayInputStream extends InputStream {
	protected byte buf[];
	protected int pos = 0;
	protected int mark = 0;
	protected int size;

	public LimitByteArrayInputStream(byte buf[]) {
		this.buf = buf;
		this.size = buf.length;
	}

	public LimitByteArrayInputStream(byte buf[], int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.size = Math.min(offset + length, buf.length);
		this.mark = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int read() {
//		synchronized (buf) {
			if (pos < size) {
				return (buf[pos++] & 0xff);
			}
//		}
		return -1;
	}

	public int read(byte b[], int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
//		synchronized (buf) {
			if (pos >= size) {
				return -1;
			}

			int avail = size - pos;
			if (len > avail) {
				len = avail;
			}
			if (len <= 0) {
				return 0;
			}
			System.arraycopy(buf, pos, b, off, len);
			pos += len;
//		}
		return len;
	}

	public long skip(long n) {
//		synchronized (buf) {
			long k = size - pos;
			if (n < k) {
				k = n < 0 ? 0 : n;
			}

			pos += k;
			return k;
//		}
	}

	public int available() {
		return size - pos;
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int readAheadLimit) {
		mark = pos;
	}

	public void reset() {
		pos = mark;
	}

	public void close() {
		buf = null;
		pos = 0;
		size = 0;
	}

}
