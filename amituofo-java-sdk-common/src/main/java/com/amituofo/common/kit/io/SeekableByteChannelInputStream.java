package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.concurrent.locks.ReentrantLock;

public class SeekableByteChannelInputStream extends InputStream {
	private SeekableByteChannel channel;
	private long remaining;
	private final ByteBuffer singleByteBuffer = ByteBuffer.allocate(1);
	private boolean autoCloseChannel;
	private final ReentrantLock lockChannel;

	public SeekableByteChannelInputStream(SeekableByteChannel channel, long offset, long length) throws IOException {
		this(channel, null, false, offset, length);
	}

	public SeekableByteChannelInputStream(SeekableByteChannel channel, ReentrantLock lockChannel, long offset, long length) throws IOException {
		this(channel, lockChannel, false, offset, length);
	}

	public SeekableByteChannelInputStream(SeekableByteChannel channel, ReentrantLock lockChannel, boolean autoCloseChannel, long offset, long length) throws IOException {
		this.channel = channel;
		this.remaining = length;
		this.autoCloseChannel = autoCloseChannel;

		this.lockChannel = lockChannel;
		if (this.lockChannel != null) {
			this.lockChannel.lock();
		}

		this.channel.position(offset);
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
		if (this.lockChannel != null) {
			this.lockChannel.unlock();
		}

		if (autoCloseChannel && channel != null) {
			channel.close();
			channel = null;
		}
	}
}
