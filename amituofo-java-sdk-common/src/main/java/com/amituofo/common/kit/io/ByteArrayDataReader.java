package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.amituofo.common.api.DataReader;

public class ByteArrayDataReader implements DataReader {

	private byte[] data;
	private long position;

	/**
	 * Uses the supplied array directly as the data source.
	 */
	public ByteArrayDataReader(byte[] data) {
		this.data = Objects.requireNonNull(data, "data");
	}

	@Override
	public byte[] read(long offset, int length) throws IOException {
		byte[] buffer = new byte[length];
		int bytesRead = read(offset, length, buffer, 0);
		if (bytesRead != length) {
			throw new IOException("Read length different. read=" + bytesRead + " expect=" + length);
		}
		return buffer;
	}

	@Override
	public int read(long offset, int length, byte[] targetBuffer, int targetOffset) throws IOException {
		seek(offset);
		return read(targetBuffer, targetOffset, length);
	}

	@Override
	public InputStream content(long offset, long length) throws IOException {
		if (offset < 0 || length < 0 || length > data.length - offset) {
			throw new IOException("Invalid content range. offset=" + offset + " length=" + length);
		}
		return new LimitByteArrayInputStream(data, (int) offset, (int) length);
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		return read(buffer, 0, buffer.length);
	}

	private int read(byte[] buffer, int offset, int length) {
		Objects.requireNonNull(buffer, "buffer");
		if (offset < 0 || length < 0 || length > buffer.length - offset) {
			throw new IndexOutOfBoundsException();
		}
		if (length == 0) {
			return 0;
		}
		if (position >= data.length) {
			return -1;
		}

		int bytesRead = Math.min(length, data.length - (int) position);
		System.arraycopy(data, (int) position, buffer, offset, bytesRead);
		position += bytesRead;
		return bytesRead;
	}

	@Override
	public void seek(long offset) throws IOException {
		if (offset < 0) {
			throw new IOException("Negative seek offset");
		}
		position = offset;
	}

	@Override
	public long length() {
		return data.length;
	}

	@Override
	public void close() {
		data = new byte[0];
		position = 0;
	}
}
