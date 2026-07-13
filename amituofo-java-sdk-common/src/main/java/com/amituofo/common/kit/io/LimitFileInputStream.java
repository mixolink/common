package com.amituofo.common.kit.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class LimitFileInputStream extends FileInputStream {
	private final long OFFSET; // 读取的偏移量
	private final long MAX_LENGTH; // 最大读取长度
	private long readLen;

	public LimitFileInputStream(File file, long offset, long maxLength) throws IOException {
		super(file);
		this.OFFSET = offset;
		this.MAX_LENGTH = maxLength;

		long len = file.length();
		if ((len - offset) < maxLength) {
			throw new IOException("Max length exceed.");
		}

		if (OFFSET > 0) {
			getChannel().position(OFFSET);
			readLen = 0;
		}
	}

	public LimitFileInputStream(Path path, long offset, long maxLength) throws IOException {
		super(path.toFile());
		this.OFFSET = offset;
		this.MAX_LENGTH = maxLength;

		long len = Files.size(path);
		if ((len - offset) < maxLength) {
			throw new IOException("Max length exceed.");
		}

		if (OFFSET > 0) {
			getChannel().position(OFFSET);
			readLen = 0;
		}
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
		int bytesRead = super.read(b, off, len); // 调用父类的read方法读取指定长度的字节
		if (bytesRead != -1) {
			readLen += bytesRead; // 更新已读取的偏移量
		}
		return bytesRead;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read() throws IOException {
		if (readLen >= MAX_LENGTH) { // 如果已经读取了最大长度，则返回-1表示结束
			return -1;
		}
		int v = super.read();
		if (v != -1) {
			readLen++;
		}
		return v;
	}

	@Override
	public int available() throws IOException {
		long remaining = MAX_LENGTH - readLen;
		return remaining > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) remaining;
	}

	@Override
	public void reset() throws IOException {
		if (OFFSET > 0) {
			super.getChannel().position(OFFSET);
		} else {
			super.getChannel().position(0);
		}
		readLen = 0;
	}

}