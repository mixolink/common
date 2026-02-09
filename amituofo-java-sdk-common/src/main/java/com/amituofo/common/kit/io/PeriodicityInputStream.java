/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;

import com.amituofo.common.define.Constants;
import com.amituofo.common.util.Base64Utils;
import com.amituofo.common.util.DigestUtils;
import com.amituofo.common.util.FormatUtils;
import com.amituofo.common.util.RandomUtils;
import com.amituofo.common.util.StreamUtils;

public class PeriodicityInputStream extends InputStream {
	private final long TOTALLEN;
	private final long FACTOR;

	private int currentValue;
	private long remainLen;

	public PeriodicityInputStream(long factor, long length) {
		this.TOTALLEN = length;
		this.FACTOR = Math.abs(factor);
//		this.factor = (byte) (factor % 256 - 128);
		reset();
	}

	@Override
	public void close() throws IOException {
		reset();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public int read() {
		if (remainLen <= 0) {
			return -1;
		}

		remainLen--;

		currentValue = (int) (FACTOR * remainLen + remainLen << 2) % 255;
		if (currentValue < 0) {
			return currentValue * -1;
		}

		return currentValue;
	}

	@Override
	public int read(byte[] b) {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) {
		if (remainLen <= 0) {
			return -1;
		}

		len = Math.min(b.length - off, len);

		int maxindex = b.length - 1;
		int index = 0;
		final int genlen = (len < remainLen ? len : (int) remainLen); // Math.min(len, length);
		int readlen = 0;
		for (int i = 0; i < genlen; i++) {
			int v = read();
			if (v == -1) {
				break;
			}
			readlen++;
			index = i + off;
			if (index > maxindex) {
				break;
			}

			b[index] = (byte) v;
		}

		return readlen;
	}

	@Override
	public synchronized void mark(int readlimit) {
		long limit = readlimit;
		if (limit > TOTALLEN) {
			limit = TOTALLEN;
		} else if (limit < 0) {
			limit = 0;
		}
		
		remainLen = limit;
	}

	@Override
	public long skip(long n) throws IOException {
		if (n > TOTALLEN) {
			n = TOTALLEN;
		} else if (n < 0) {
			n = 0;
		}

		remainLen = TOTALLEN - n;

		return n;
	}

	@Override
	public int available() {
		return (int) remainLen;
	}

	@Override
	public void reset() {
		this.remainLen = TOTALLEN;
	}

//	public static void main(String[] a) throws IOException {
//		for (int i = 0; i < 100; i++) {
//			String name = "filename-" + i;
//			System.out.println("vv" + name + "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
//
//			long factor = DigestUtils.ELFHash(name) * DigestUtils.BKDRHash(name) * DigestUtils.BPHash(name) * name.hashCode();
//			int size = (int) (Math.abs(factor) % (10000)) * Constants.SIZE_1KB * 2;
//			int skip = 1000;
//
//			// 输入因子与需要的数据长度
//			InputStream in = new PeriodicityInputStream(factor, size);
//			in.skip(skip);
//			// 读取内容及时md5
//			System.out.println(
//					name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
////			in.skip(2000);
////			System.out.println(name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
////			in.skip(5000);
////			System.out.println(name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
//
//			// 输入同样因子与需要的数据长度
////			in = new PeriodicityInputStream(factor, size);
////			in.skip(skip);
////			System.out.println(name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
////			in.skip(2000);
////			System.out.println(name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
////			in.skip(5000);
////			System.out.println(name + " size=" + FormatUtils.getPrintSize(size, true) + " md5=" + DigestUtils.format2HexLowerCase(DigestUtils.calcMD5(in)));
////			System.out.println(name + " content=" + StreamUtils.inputStreamToConsole(in, true));
//
//			{
//				int j = 0;
//				// 输入同样因子与需要的数据长度
//				in = new PeriodicityInputStream(factor, size);
//				byte[] buf4 = new byte[size];
//				int readlen = 0;
//				int v;
//				while ((v = in.read()) != -1) {
//					readlen++;
//					buf4[j++] = (byte) v;
//				}
//				if (size != readlen) {
//					System.err.println(name + " readlen=" + readlen);
//				}
//			}
//
//			{
//				in = new PeriodicityInputStream(factor, size);
//				int v;
//				int readlen = 0;
//				byte[] buf0 = new byte[size];
//				while ((v = in.read(buf0, 0, buf0.length)) != -1) {
//					readlen += v;
//				}
//				if (size != readlen) {
//					System.err.println(name + " readlen=" + readlen);
//				}
//			}
//
//			{
//				byte[] buf1 = new byte[4096];
//				byte[] buf2 = new byte[4096];
//				in = new PeriodicityInputStream(factor, size);
//				in.skip(skip);
//				in.read(buf1, 0, buf1.length);
//				in = new PeriodicityInputStream(factor, size);
//				in.skip(skip);
//				int n = RandomUtils.randomInt(0, size);
//				in.read(buf2, n, buf2.length);
//				for (int x = n; x < buf1.length; x++) {
//					if (buf1[x] != buf2[x]) {
//						System.err.println("not equal at " + x);
//					}
//				}
//			}
//
//			{
//				int n = RandomUtils.randomInt(0, size);
//				byte[] buf1 = new byte[n];
//				byte[] buf2 = new byte[n];
//				in = new PeriodicityInputStream(factor, size);
//				in.read(buf1);
//				in = new PeriodicityInputStream(factor, size);
//				in.read(buf2);
//				for (int x = 0; x < n; x++) {
//					if (buf1[x] != buf2[x]) {
//						System.err.println("not equal at " + x);
//					}
//				}
//			}
//
//			{
//				int n = RandomUtils.randomInt(0, size);
//				byte[] buf1 = new byte[n];
//				byte[] buf2 = new byte[n];
//				in = new PeriodicityInputStream(factor, size);
//				in.skip(n);
//				in.read(buf1);
//				in = new PeriodicityInputStream(factor, size);
//				in.skip(n);
//				in.read(buf2);
//				for (int x = 0; x < n; x++) {
//					if (buf1[x] != buf2[x]) {
//						System.err.println("not equal at " + x);
//					}
//				}
//			}
//
//			{
//				int n = 10240;// RandomUtils.randomInt(0, size);
//				byte[] buf1 = new byte[n];
//				byte[] buf2 = new byte[n];
//				InputStream in1 = new PeriodicityInputStream(factor, size);
////				in1.skip(n);
//				InputStream in2 = new PeriodicityInputStream(factor, size);
////				in2.skip(n);
//
//				int readlen1 = 0, readlen2 = 0;
//				do {
//					readlen1 += in1.read(buf1);
//					readlen2 += in2.read(buf2);
//					if (readlen1 > 0) {
//						for (int x = 0; x < buf1.length; x++) {
//							if (buf1[x] != buf2[x]) {
//								System.err.println("not equal at " + x);
//							}
//						}
//					}
//
//				} while (readlen1 < size);
//			}
//
//			System.out.println("^^" + name + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		}
//	}

}
