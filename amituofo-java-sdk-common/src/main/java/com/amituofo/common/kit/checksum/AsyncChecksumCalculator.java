package com.amituofo.common.kit.checksum;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncChecksumCalculator extends ChecksumCalculator {
	private class Buf {
		byte[] data;
		int offset;
		int len;

		public Buf(byte[] data, int offset, int len) {
			super();
			this.data = data;
			this.offset = offset;
			this.len = len;
		}
	}

	CountDownLatch latch;
	boolean continueFlag = true;
	Queue<Buf> pendingQueue = null;
//	Queue<Buf> updatedQueue = null;
//	private int bufferSize;

	public AsyncChecksumCalculator(ChecksumName... verifyon) {
		super(verifyon);
//		this.bufferSize = bufferSize;
		
		active();
	}

//	public AsyncOutputChecksumHelper(int bufferSize, Verification... verifyon) {
//		super(verifyon);
//		this.bufferSize = bufferSize;
//	}

	@Override
	public void update(byte[] data, int offset, int len) {
		if (continueFlag) {
			Buf buf = new Buf(data, offset, len);
			pendingQueue.add(buf);
		}
	}

//	@Override
//	public void readEnd() {
//		if (updatedQueue != null) {
//			Queue<Buf> q = updatedQueue;
//			updatedQueue = null;
//			try {
//				q.clear();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	private void active() {
		close();
		continueFlag = true;
		pendingQueue = new ArrayDeque<>();
//		updatedQueue = new ArrayDeque<>();
		latch = new CountDownLatch(1);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (continueFlag || (pendingQueue != null && pendingQueue.size() > 0)) {
					Buf buf = pendingQueue.poll();
					if (buf != null) {
						AsyncChecksumCalculator.super.update(buf.data, buf.offset, buf.len);

//						if (updatedQueue != null) {
//							updatedQueue.add(buf);
////							System.out.println("CC=" + updatedQueue.size());
//						}
					} else {
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
						}
					}
				}

				close();

				latch.countDown();
			}
		});
		t.start();
	}

//	@Override
//	public byte[] reuseCache() {
//		if (updatedQueue != null) {
//			Buf buf = updatedQueue.poll();
////			return buf != null ? buf.data : new byte[bufferSize];
//			return buf != null ? buf.data : null;
//		}
//
//		return null;
//	}

	@Override
	public void close() {
		continueFlag = false;
//		if (updatedQueue != null) {
//			try {
//				updatedQueue.clear();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			updatedQueue = null;
//		}
		
		if (pendingQueue != null) {
			try {
				pendingQueue.clear();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pendingQueue = null;
		}
	}

	@Override
	public boolean waitComplete() {
//		if (updatedQueue != null) {
//			Queue<Buf> q = updatedQueue;
//			updatedQueue = null;
//			try {
//				q.clear();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		if (latch != null) {
			try {
				latch.await(5, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				continueFlag = false;
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

//	public static void main(String[] args) throws Exception {
//		String sourceFilePath = "C:\\Temp\\TEST-COPY\\TEST-COPY.zip";
//		String destFilePath = "C:\\Temp\\xxxx.xxx";
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//		byte[] buffer = new byte[1024];
//		int len;
//
//		long t1 = System.currentTimeMillis();
//		CRC32 crc32 = new CRC32();
//
//		OutputChecksumHelper h = new AsyncOutputChecksumHelper(Verification.CalcOutputCRC32);
//		try {
//			fis = new FileInputStream(sourceFilePath);
//			fos = new FileOutputStream(destFilePath);
////	            CheckedOutputStream cos = new CheckedOutputStream(fos, crc);
//			while ((len = fis.read(buffer)) > 0) {
//				fos.write(buffer, 0, len);
//				crc32.update(buffer, 0, len);
//				h.update(buffer, 0, len);
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
//		System.out.println(crc + " " + (t3 - t2));
//		System.out.println(h.getCRC32Checksum() + " " + (t3 - t2));
//		System.out.println(crc32.getValue() + " " + (t2 - t1));
//	}

}
