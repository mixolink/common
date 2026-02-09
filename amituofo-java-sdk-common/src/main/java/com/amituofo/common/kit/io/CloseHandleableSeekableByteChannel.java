package com.amituofo.common.kit.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import com.amituofo.common.api.IOCloseListener;

public class CloseHandleableSeekableByteChannel implements SeekableByteChannel {
	private SeekableByteChannel channel;
	private IOCloseListener ioCloseListener;

	public CloseHandleableSeekableByteChannel(SeekableByteChannel channel, IOCloseListener ioCloseListener) {
		super();
		this.channel = channel;
		this.ioCloseListener = ioCloseListener;
	}

	public boolean isOpen() {
		return channel.isOpen();
	}

	public void close() throws IOException {
		if (channel == null) {
			return;
		}

		try {
			channel.close();
			channel = null;
		} catch (IOException e) {
			throw e;
		} finally {
			if (ioCloseListener != null) {
				ioCloseListener.closed();
			}
		}
	}

	public int read(ByteBuffer dst) throws IOException {
		return channel.read(dst);
	}

	public int write(ByteBuffer src) throws IOException {
		return channel.write(src);
	}

	public long position() throws IOException {
		return channel.position();
	}

	public SeekableByteChannel position(long newPosition) throws IOException {
		return channel.position(newPosition);
	}

	public long size() throws IOException {
		return channel.size();
	}

	public SeekableByteChannel truncate(long size) throws IOException {
		return channel.truncate(size);
	}

}
