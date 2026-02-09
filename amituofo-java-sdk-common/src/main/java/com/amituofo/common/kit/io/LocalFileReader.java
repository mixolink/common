package com.amituofo.common.kit.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.amituofo.common.api.DataReader;

public class LocalFileReader implements DataReader {
	protected RandomAccessFile r;
	private File file;

	public LocalFileReader(File file) throws IOException {
		this.r = new RandomAccessFile(file, "r");
		this.file = file;
	}

	@Override
	public byte[] read(long offset, int length) throws IOException {
		byte[] b = new byte[length];
		r.seek(offset);
		int readlen = r.read(b);

		if (readlen != length) {
			throw new IOException("Read length different! read=" + readlen + " expect=" + length);
		}

		return b;
	}

	@Override
	public int read(long offset, int length, byte[] targetBuffer, int targetOffset) throws IOException {
		r.seek(offset);
		return r.read(targetBuffer, targetOffset, length);
	}

	@Override
	public InputStream content(long offset, long length) throws IOException {
		InputStream in = new LimitFileInputStream(file, offset, length);
		return in;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return r.read(b);
	}

	@Override
	public void seek(long pos) throws IOException {
		r.seek(pos);
	}
	
	@Override
	public long length() {
		return file.length();
	}

	@Override
	public void close() {
		try {
			r.close();
			r = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
