package com.amituofo.common.kit.checksum;

public class ByteArrayWriteResult {
	public static final ByteArrayWriteResult ZERO = new ByteArrayWriteResult(0);

	private long length;
	private Checksum checksum = null;

	public ByteArrayWriteResult(long length, Checksum checksum) {
		super();
		this.length = length;
		this.checksum = checksum;
	}

	public ByteArrayWriteResult(long length) {
		super();
		this.length = length;
	}

	public long length() {
		return length;
	}

	public Checksum checksum() {
		return checksum;
	}

	public boolean containsChecksum() {
		return checksum != null;
	}

}
