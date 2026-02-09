package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;

import com.amituofo.common.kit.checksum.Checksum;
import com.amituofo.common.kit.checksum.ChecksumCalculator;
import com.amituofo.common.kit.checksum.ChecksumName;
import com.amituofo.common.kit.checksum.SyncChecksumCalculator;

public class ChecksumInputStream extends InputStream {
	private InputStream in;
	private ChecksumCalculator calculator;
	private long totalReadLength = 0;

	public ChecksumInputStream(InputStream in, ChecksumCalculator calculator) {
		this.calculator = calculator;
		this.in = in;
	}

	public ChecksumInputStream(InputStream in, ChecksumName checksumName) {
		this.calculator = new SyncChecksumCalculator(checksumName);
		this.in = in;
	}

//	public ChecksumInputStream(InputStream in) {
//		this.calculator = null;
//		this.in = in;
//	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int bytesRead = in.read(b, off, len);

		if (bytesRead > 0) {
			totalReadLength += bytesRead;

//			if (calculator != null) {
				calculator.update(b, off, bytesRead);
//			}
		}

		return bytesRead;
	}

	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read() throws IOException {
		int v = in.read();

		if (v != -1) {
			totalReadLength++;

//			if (calculator != null) {
				byte[] buf = new byte[1];
				buf[0] = (byte) v;
				calculator.update(buf, 0, 1);
//			}
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
		return in.available();
	}

	public void mark(int readlimit) {
		in.mark(readlimit);
	}

	public void close() throws IOException {
//		if (calculator != null) {
			calculator.close();
//		}
		in.close();
	}

	public boolean markSupported() {
		return in.markSupported();
	}

	public Checksum getMD5Checksum() {
//		if (calculator != null) {
			return new Checksum(ChecksumName.MD5, calculator.getMD5Checksum());
//		}
//		return null;
	}

	public Checksum getCRC32Checksum() {
//		if (calculator != null) {
			return new Checksum(calculator.getCRC32Checksum());
//		}
//		return null;
	}

	public long getTotalReadLength() {
		return totalReadLength;
	}

}