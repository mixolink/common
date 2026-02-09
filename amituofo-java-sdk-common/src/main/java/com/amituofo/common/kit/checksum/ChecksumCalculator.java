package com.amituofo.common.kit.checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public abstract class ChecksumCalculator {
//	List<String> md5ChecksumList = null;

	MessageDigest md5 = null;
	Checksum crc32 = null;

	public ChecksumCalculator(ChecksumName... verifyon) {
		if (ChecksumName.MD5.containsIn(verifyon)) {
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (ChecksumName.CRC32.containsIn(verifyon)) {
			crc32 = new CRC32();
		}
	}

//	public boolean onlyMD5() {
//		return md5 != null && crc32 == null;
//	}

//	public abstract void update(byte[] data, int offset, int len);

//	public abstract void readEnd();

//	public abstract void active();

	public abstract void close();

	public abstract boolean waitComplete();

//	public abstract byte[] reuseCache();

//	public String getMergedETag() {
//		if (md5ChecksumList != null && md5ChecksumList.size() > 0) {
//			return ETagUtils.mergeETag(md5ChecksumList);
//		}
//		return null;
//	}

	public byte[] getMD5Checksum() {
		if (md5 != null) {
			return md5.digest();
		}
		return null;
	}

	public long getCRC32Checksum() {
		if (crc32 != null) {
			return crc32.getValue();
		}
		return 0;
	}

	public void update(byte[] data, int offset, int len) {
		if (md5 != null) {
			md5.update(data, offset, len);
		}

		if (crc32 != null) {
			crc32.update(data, offset, len);
		}
	}

//	protected void mergeMD5(String checksum) {
//		if (md5ChecksumList != null) {
//			md5ChecksumList.add(checksum);
//		}
//	}

//	protected static byte[] mergeMD5(List<String> md5ChecksumList) {
//		try {
//			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//			StringBuilder stringBuilder = new StringBuilder();
//
//			for (String md5 : md5ChecksumList) {
//				stringBuilder.append(md5);
//			}
//
//			byte[] mergedBytes = messageDigest.digest(stringBuilder.toString().getBytes());
//
//			StringBuilder mergedMD5 = new StringBuilder();
//			for (byte b : mergedBytes) {
//				String hex = Integer.toHexString(0xff & b);
//				if (hex.length() == 1) {
//					mergedMD5.append('0');
//				}
//				mergedMD5.append(hex);
//			}
//
//			return DigestUtils.hexStringToBytes(mergedMD5.toString());
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

}