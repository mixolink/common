package com.amituofo.common.kit.checksum;

public class SyncChecksumCalculator extends ChecksumCalculator {

//	byte[] data;

	public SyncChecksumCalculator(ChecksumName... verifyon) {
		super(verifyon);
	}

//	public void update(byte[] data, int offset, int len) {
//		updateChecksum(data, offset, len);
////		this.data = data;
//	}

//	@Override
//	public void active() {
//	}

	@Override
	public void close() {
//		data = null;
	}

	@Override
	public boolean waitComplete() {
		return true;
	}

//	@Override
//	public byte[] reuseCache() {
//		return null;
////		return data;
//	}

//	public static void main(String[] args) throws Exception {
//		String sourceFilePath = "C:\\Temp\\denotifier.jar";
//		String destFilePath = "C:\\Temp\\xxxx.xxx";
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//		byte[] buffer = new byte[1024];
//		int len;
//
//		long t1 = System.currentTimeMillis();
//		CRC32 crc32 = new CRC32();
////		List<String> list = new ArrayList<>();
//		SyncOutputChecksumHelper h = new SyncOutputChecksumHelper(Verification.CalcOutputCRC32, Verification.CalcOutputMD5);
//		try {
//			fis = new FileInputStream(sourceFilePath);
//			fos = new FileOutputStream(destFilePath);
////	        CheckedOutputStream cos = new CheckedOutputStream(fos, crc);
//			while ((len = fis.read(buffer)) > 0) {
//				fos.write(buffer, 0, len);
//				crc32.update(buffer, 0, len);
//				h.update(buffer, 0, len);
//
////				MessageDigest md5x = MessageDigest.getInstance("MD5");
////				md5x.update(buffer, 0, len);
////
////				list.add(DigestUtils.bytesToHexString(md5x.digest()));
//			}
////	            System.out.println("CRC: " + crc.getValue());
//		} finally {
//			if (fis != null) {
//				fis.close();
//			}
//			if (fos != null) {
//				fos.close();
//			}
//		}
//
//		long t2 = System.currentTimeMillis();
//
//		long crc = FileChickUtils.calculateFileCrc(sourceFilePath);
//
//		long t3 = System.currentTimeMillis();
//
//		System.out.println(DigestUtils.calcMD5ToHex(new File(sourceFilePath)));
//		System.out.println(DigestUtils.bytesToHexString(h.getMD5Checksum()));
////		System.out.println(DigestUtils.bytesToHexString(mergeMD5(list)));
//		System.out.println(crc + " " + (t3 - t2));
//		System.out.println(h.getCRC32Checksum() + " " + (t3 - t2));
//		System.out.println(crc32.getValue() + " " + (t2 - t1));
//	}

//	@Override
//	public void readEnd() {
//		// TODO Auto-generated method stub
//		
//	}

}
