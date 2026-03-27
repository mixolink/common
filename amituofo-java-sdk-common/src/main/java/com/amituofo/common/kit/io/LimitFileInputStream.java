package com.amituofo.common.kit.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class LimitFileInputStream extends InputStream {
	private final long OFFSET; // 读取的偏移量
	private final long MAX_LENGTH; // 最大读取长度
	private long readLen;

	private FileInputStream in;
	private File file;

	public LimitFileInputStream(File file, long offset, long maxLength) throws IOException {
		this.file = file;
		this.OFFSET = offset;
		this.MAX_LENGTH = maxLength;

		long len = file.length();
		if ((len - offset) < maxLength) {
			throw new IOException("Max length exceed.");
		}

		this.reset();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		long remaining = MAX_LENGTH - readLen; // 计算剩余可读取字节数
		if (remaining <= 0) {
			// 如果已经读取了最大长度，则返回-1表示结束
			return -1;
		}
		if (len > remaining) { // 如果请求的长度超过剩余可读取字节数，则只读取剩余部分
			len = (int) remaining;
		}
		int bytesRead = in.read(b, off, len); // 调用父类的read方法读取指定长度的字节
		if (bytesRead != -1) {
			readLen += bytesRead; // 更新已读取的偏移量
		}
		return bytesRead;
	}

	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read() throws IOException {
		if (readLen >= MAX_LENGTH) { // 如果已经读取了最大长度，则返回-1表示结束
			return -1;
		}
		int v = in.read();
		if (v != -1) {
			readLen++;
		}
		return v;
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(OFFSET + n);
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
		return (int) (MAX_LENGTH - readLen);
	}

	public void mark(int readlimit) {
		in.mark(readlimit);
	}

	public void close() throws IOException {
		in.close();
	}

	public void reset() throws IOException {
		if (this.in != null) {
			this.in.close();
		}

		this.in = new FileInputStream(file);
		if (OFFSET > 0) {
			in.skip(OFFSET); // 跳过指定长度的字节，从offset开始读取
		}
		readLen = 0;
	}

	public FileChannel getChannel() {
		return in.getChannel();
	}

	public boolean markSupported() {
		return in.markSupported();
	}

}