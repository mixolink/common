package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;

public class LimitInputStream extends InputStream {
	private long remaining;

	private InputStream in;

	public LimitInputStream(InputStream in, long maxLength) {
		this.remaining = (maxLength < 0 ? Long.MAX_VALUE : maxLength);
		this.in = in;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (remaining <= 0) {
			// 如果已经读取了最大长度，则返回-1表示结束
			return -1;
		}
		if (len > remaining) { // 如果请求的长度超过剩余可读取字节数，则只读取剩余部分
			len = (int) remaining;
		}
		int bytesRead = in.read(b, off, len); // 调用父类的read方法读取指定长度的字节
		if (bytesRead != -1) {
			remaining -= bytesRead; // 更新已读取的偏移量
		} else {
			remaining = 0;
		}
		return bytesRead;
	}

	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read() throws IOException {
		if (remaining <= 0) {
			// 如果已经读取了最大长度，则返回-1表示结束
			return -1;
		}
		int v = in.read();
		if (v != -1) {
			remaining--;
		} else {
			remaining = 0;
		}
		return v;
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	public int hashCode() {
		return in.hashCode();
	}

	public boolean equals(Object obj) {
		return in.equals(obj);
	}

	public String toString() {
		return in.toString();
	}

	public int available() throws IOException {
		return (int) (Math.min(Integer.MAX_VALUE, remaining));
	}

	public void mark(int readlimit) {
		in.mark(readlimit);
	}

	public void close() throws IOException {
		remaining = 0;
		in.close();
	}

	public boolean markSupported() {
		return in.markSupported();
	}

}