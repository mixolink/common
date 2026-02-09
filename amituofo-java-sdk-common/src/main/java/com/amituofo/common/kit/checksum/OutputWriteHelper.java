package com.amituofo.common.kit.checksum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OutputWriteHelper {
	private long writeLen = 0;
	private OutputStream out;

	private IOException e = null;

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
	Queue<Buf> writtenQueue = null;
	private boolean enableCacheReuse;

	public OutputWriteHelper(OutputStream out, boolean enableCacheReuse) {
		this.out = out;
		this.enableCacheReuse = enableCacheReuse;

		active();
	}

	public void write(byte[] data, int offset, int len) throws IOException {
		if (continueFlag) {
			Buf buf = new Buf(data, offset, len);
			pendingQueue.add(buf);
		} else {
			if (e != null) {
				throw e;
			}
		}
	}

	public long getOutputLength() {
		return writeLen;
	}

	public void readEnd() {
		enableCacheReuse = false;
		if (writtenQueue != null) {
			Queue<Buf> q = writtenQueue;
			writtenQueue = null;
			q.clear();
		}
	}

	private void active() {
		close();
		continueFlag = true;
		pendingQueue = new LinkedList<>();
		if (enableCacheReuse) {
			writtenQueue = new LinkedList<>();
		}
		latch = new CountDownLatch(1);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (continueFlag || (pendingQueue != null && pendingQueue.size() > 0)) {
					Buf buf = pendingQueue.poll();
					if (buf != null) {
						try {
							out.write(buf.data, buf.offset, buf.len);
							writeLen += buf.len;
						} catch (IOException e) {
							OutputWriteHelper.this.e = e;
							break;
						}
						if (enableCacheReuse) {
							writtenQueue.add(buf);
//							System.out.println("WC=" + writtenQueue.size());
						}
					} else {
						try {
							Thread.sleep(15);
						} catch (InterruptedException e) {
						}
					}
				}

				if (OutputWriteHelper.this.e == null) {
					try {
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				close();

				latch.countDown();
			}
		});
		t.start();
	}

	public byte[] reuseCache() {
		if (writtenQueue != null) {
			Buf buf = writtenQueue.poll();
			return buf != null ? buf.data : null;
		}

		return null;
	}

	public void close() {
		continueFlag = false;
		if (writtenQueue != null) {
			try {
				writtenQueue.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
			writtenQueue = null;
		}
		if (pendingQueue != null) {
			try {
				pendingQueue.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
			pendingQueue = null;
		}
	}

	public boolean waitComplete() {
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
//		OutputChecksumHelper h = new AsyncOutputWriter(1024, Verification.CalcOutputCRC32);
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
