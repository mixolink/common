package com.amituofo.common.kit.checksum;

public class ByteArrayReadResult {
	public static final ByteArrayReadResult ZERO = new ByteArrayReadResult(0);

	private long length;
	private Checksum checksum = null;

	public ByteArrayReadResult(long length, Checksum checksum) {
		super();
		this.length = length;
		this.checksum = checksum;
	}

	public ByteArrayReadResult(long length) {
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
