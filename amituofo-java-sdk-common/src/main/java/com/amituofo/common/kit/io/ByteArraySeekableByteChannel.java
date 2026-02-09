package com.amituofo.common.kit.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class ByteArraySeekableByteChannel implements SeekableByteChannel {
	private byte[] data;
	private ByteArrayInputStream in = null;
	private long position = 0;

	public ByteArraySeekableByteChannel(byte[] data) {
		this.data = data;
	}

	public ByteArraySeekableByteChannel(ByteArrayInputStream in) {
		this.in = in;
		Field bufField;
		try {
			bufField = ByteArrayInputStream.class.getDeclaredField("buf");

			bufField.setAccessible(true); // 允许访问私有字段

			// 获取内部的 byte[] 数组
			this.data = (byte[]) bufField.get(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		if (position >= data.length) {
			return -1; // End of stream
		}

		int bytesToRead = Math.min(dst.remaining(), data.length - (int) position);
		dst.put(data, (int) position, bytesToRead);
		position += bytesToRead;
		return bytesToRead;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		throw new UnsupportedOperationException("Write operation is not supported");
	}

	@Override
	public long position() throws IOException {
		return position;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {
		if (newPosition < 0 || newPosition > data.length) {
			throw new IllegalArgumentException("Position out of bounds");
		}
		position = newPosition;
		return this;
	}

	@Override
	public long size() throws IOException {
		return data.length;
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		throw new UnsupportedOperationException("Truncate operation is not supported");
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public void close() throws IOException {
		data = null;
		if (in != null) {
			in.close();
		}
	}

	public static void main(String[] args) throws IOException {
		byte[] data = "This is a byte array".getBytes();
		SeekableByteChannel channel = new ByteArraySeekableByteChannel(data);

		// 读取数据
		ByteBuffer buffer = ByteBuffer.allocate(10);
		int bytesRead;
		while ((bytesRead = channel.read(buffer)) != -1) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.print((char) buffer.get());
			}
			buffer.clear();
		}

		// 重设并重新读取
		channel.position(0); // 重置为起始位置
		System.out.println("\nRe-reading from the beginning:");

		buffer.clear();
		while ((bytesRead = channel.read(buffer)) != -1) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.print((char) buffer.get());
			}
			buffer.clear();
		}
	}
}
