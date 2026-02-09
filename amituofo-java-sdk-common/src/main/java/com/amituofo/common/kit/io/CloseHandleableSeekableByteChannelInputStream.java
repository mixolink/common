package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import com.amituofo.common.api.IOCloseListener;

public class CloseHandleableSeekableByteChannelInputStream extends InputStream {
	private SeekableByteChannel channel;
	private long remaining;
	private final ByteBuffer singleByteBuffer = ByteBuffer.allocate(1);
	private boolean autoCloseChannel;
	private IOCloseListener ioCloseListener;

	public CloseHandleableSeekableByteChannelInputStream(SeekableByteChannel channel, boolean autoCloseChannel, long offset, long length, IOCloseListener ioCloseListener)
			throws IOException {
		this.channel = channel;
		this.remaining = length;
		this.autoCloseChannel = autoCloseChannel;
		this.channel.position(offset);
		this.ioCloseListener = ioCloseListener;
	}

	@Override
	public int read() throws IOException {
		if (remaining <= 0)
			return -1;
		singleByteBuffer.clear();
		int read = channel.read(singleByteBuffer);
		if (read <= 0)
			return -1;
		remaining--;
		singleByteBuffer.flip();
		return singleByteBuffer.get() & 0xFF;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (remaining <= 0)
			return -1;
		len = (int) Math.min(len, remaining);
		ByteBuffer buffer = ByteBuffer.wrap(b, off, len);
		int read = channel.read(buffer);
		if (read <= 0)
			return -1;
		remaining -= read;
		return read;
	}

	@Override
	public long skip(long n) throws IOException {
		long toSkip = Math.min(n, remaining);
		channel.position(channel.position() + toSkip);
		remaining -= toSkip;
		return toSkip;
	}

	@Override
	public int available() throws IOException {
		return (int) Math.min(remaining, Integer.MAX_VALUE);
	}

	@Override
	public void close() throws IOException {
		if (autoCloseChannel && channel != null) {
			try {
				channel.close();
				channel = null;
			} catch (IOException e) {
				throw e;
			}
		}

		if (ioCloseListener != null) {
			ioCloseListener.closed();
			ioCloseListener = null;
		}
	}
}
