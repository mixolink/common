package com.amituofo.common.kit.checksum;

import com.amituofo.common.util.DigestUtils;

public class Checksum {
	private final ChecksumName name;

	private byte[] checksum = null;
	private long checksumCrc;

	private String checksumHex = null;

	public Checksum(ChecksumName type, String checksumHex) {
		super();
		this.name = type;
		this.checksumHex = checksumHex;
	}

	public Checksum(ChecksumName type, byte[] checksum) {
		super();
		this.name = type;
		this.checksum = checksum;
//		this.checksumHex = DigestUtils.format2Hex(checksum);
	}

	public Checksum(long crc) {
		super();
		this.name = ChecksumName.CRC32;
		this.checksumCrc = crc;
	}

	public long getCrc() {
		return checksumCrc;
	}

	public byte[] getChecksum() {
		if (checksum == null) {
			if (checksumHex != null) {
				checksum = DigestUtils.hexStringToBytes(checksumHex);
			}
		}
		return checksum;
	}

	public String getChecksumHex() {
		if (checksumHex == null) {
			if (name == ChecksumName.CRC32) {
				checksumHex = Long.toHexString(checksumCrc);
			} else if (checksum != null) {
				checksumHex = DigestUtils.format2Hex(checksum);
			}
		}
		return checksumHex;
	}

	public ChecksumName getChecksumName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChecksumName=");
		builder.append(name);
		builder.append(" Hex=");
		builder.append(getChecksumHex());
		return builder.toString();
	}

	public static boolean equals(Checksum o1, Checksum o2) {
		if (o1 != null && o2 == null) {
			return false;
		}

		if (o1 == null && o2 != null) {
			return false;
		}

		if (o1 == null && o2 == null) {
			return true;
		}

		if (o1.getChecksumName() != o2.getChecksumName()) {
			return false;
		}

		String hex1 = o1.getChecksumHex();
		String hex2 = o2.getChecksumHex();

		if (hex1 != null && hex2 == null) {
			return false;
		}

		if (hex1 == null && hex2 != null) {
			return false;
		}

		if (hex1 == null && hex2 == null) {
			return false;
		}

		return hex1.equalsIgnoreCase(hex2);
	}
}
