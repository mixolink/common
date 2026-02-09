package com.amituofo.common.kit.checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum ChecksumName {
//	SelfChecksum, MD5, CRC32;
//	NONE((byte) 0), 
	CRC32((byte) 2), MD5((byte) 5);

	private byte algorithmID;

	ChecksumName(byte algorithmID) {
		this.algorithmID = algorithmID;
	}

	public byte getAlgorithmID() {
		return algorithmID;
	}

	public MessageDigest messageDigest() {
//		if (this == NONE) {
//			return null;
//		}

		try {
			return MessageDigest.getInstance(name());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean containsIn(ChecksumName[] vers) {
		return contains(vers, this);
	}

	public static ChecksumName of(byte algorithmId) {
		if (algorithmId == MD5.algorithmID) {
			return MD5;
		}
		if (algorithmId == CRC32.algorithmID) {
			return CRC32;
		}
		return null;
	}

	public static boolean contains(ChecksumName[] vers, ChecksumName ver) {
		if (vers == null) {
			return false;
		}

		for (ChecksumName verification : vers) {
			if (verification == ver) {
				return true;
			}
		}
		return false;
	}
}