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
package com.amituofo.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CRC32;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.kit.interrupt.Interrupter;
import com.amituofo.common.kit.io.ProgressInputStream;

public class DigestUtils {
	/**
	 * 一个字节包含位的数量 8
	 */
	private static final int BITS_OF_BYTE = 8;

	/**
	 * 多项式
	 */
	private static final int POLYNOMIAL = 0xA001;

	/**
	 * 初始值
	 */
	private static final int INITIAL_VALUE = 0xFFFF;

	/**
	 * CRC16 编码
	 *
	 * @param  bytes 编码内容
	 * @return       编码结果
	 */
	public static int getCRC16(int[] bytes) {
		int res = INITIAL_VALUE;
		for (int data : bytes) {
			res = res ^ data;
			for (int i = 0; i < BITS_OF_BYTE; i++) {
				res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
			}
		}
		return res;
	}
	
	public static long getCRC32(byte[] data) {
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		long crc0 = crc32.getValue();
		return crc0;
	}

	public static final long[] crc32tab = { 0x00000000L, 0x77073096L, 0xee0e612cL, 0x990951baL, 0x076dc419L, 0x706af48fL, 0xe963a535L, 0x9e6495a3L, 0x0edb8832L,
			0x79dcb8a4L, 0xe0d5e91eL, 0x97d2d988L, 0x09b64c2bL, 0x7eb17cbdL, 0xe7b82d07L, 0x90bf1d91L, 0x1db71064L, 0x6ab020f2L, 0xf3b97148L, 0x84be41deL, 0x1adad47dL,
			0x6ddde4ebL, 0xf4d4b551L, 0x83d385c7L, 0x136c9856L, 0x646ba8c0L, 0xfd62f97aL, 0x8a65c9ecL, 0x14015c4fL, 0x63066cd9L, 0xfa0f3d63L, 0x8d080df5L, 0x3b6e20c8L,
			0x4c69105eL, 0xd56041e4L, 0xa2677172L, 0x3c03e4d1L, 0x4b04d447L, 0xd20d85fdL, 0xa50ab56bL, 0x35b5a8faL, 0x42b2986cL, 0xdbbbc9d6L, 0xacbcf940L, 0x32d86ce3L,
			0x45df5c75L, 0xdcd60dcfL, 0xabd13d59L, 0x26d930acL, 0x51de003aL, 0xc8d75180L, 0xbfd06116L, 0x21b4f4b5L, 0x56b3c423L, 0xcfba9599L, 0xb8bda50fL, 0x2802b89eL,
			0x5f058808L, 0xc60cd9b2L, 0xb10be924L, 0x2f6f7c87L, 0x58684c11L, 0xc1611dabL, 0xb6662d3dL, 0x76dc4190L, 0x01db7106L, 0x98d220bcL, 0xefd5102aL, 0x71b18589L,
			0x06b6b51fL, 0x9fbfe4a5L, 0xe8b8d433L, 0x7807c9a2L, 0x0f00f934L, 0x9609a88eL, 0xe10e9818L, 0x7f6a0dbbL, 0x086d3d2dL, 0x91646c97L, 0xe6635c01L, 0x6b6b51f4L,
			0x1c6c6162L, 0x856530d8L, 0xf262004eL, 0x6c0695edL, 0x1b01a57bL, 0x8208f4c1L, 0xf50fc457L, 0x65b0d9c6L, 0x12b7e950L, 0x8bbeb8eaL, 0xfcb9887cL, 0x62dd1ddfL,
			0x15da2d49L, 0x8cd37cf3L, 0xfbd44c65L, 0x4db26158L, 0x3ab551ceL, 0xa3bc0074L, 0xd4bb30e2L, 0x4adfa541L, 0x3dd895d7L, 0xa4d1c46dL, 0xd3d6f4fbL, 0x4369e96aL,
			0x346ed9fcL, 0xad678846L, 0xda60b8d0L, 0x44042d73L, 0x33031de5L, 0xaa0a4c5fL, 0xdd0d7cc9L, 0x5005713cL, 0x270241aaL, 0xbe0b1010L, 0xc90c2086L, 0x5768b525L,
			0x206f85b3L, 0xb966d409L, 0xce61e49fL, 0x5edef90eL, 0x29d9c998L, 0xb0d09822L, 0xc7d7a8b4L, 0x59b33d17L, 0x2eb40d81L, 0xb7bd5c3bL, 0xc0ba6cadL, 0xedb88320L,
			0x9abfb3b6L, 0x03b6e20cL, 0x74b1d29aL, 0xead54739L, 0x9dd277afL, 0x04db2615L, 0x73dc1683L, 0xe3630b12L, 0x94643b84L, 0x0d6d6a3eL, 0x7a6a5aa8L, 0xe40ecf0bL,
			0x9309ff9dL, 0x0a00ae27L, 0x7d079eb1L, 0xf00f9344L, 0x8708a3d2L, 0x1e01f268L, 0x6906c2feL, 0xf762575dL, 0x806567cbL, 0x196c3671L, 0x6e6b06e7L, 0xfed41b76L,
			0x89d32be0L, 0x10da7a5aL, 0x67dd4accL, 0xf9b9df6fL, 0x8ebeeff9L, 0x17b7be43L, 0x60b08ed5L, 0xd6d6a3e8L, 0xa1d1937eL, 0x38d8c2c4L, 0x4fdff252L, 0xd1bb67f1L,
			0xa6bc5767L, 0x3fb506ddL, 0x48b2364bL, 0xd80d2bdaL, 0xaf0a1b4cL, 0x36034af6L, 0x41047a60L, 0xdf60efc3L, 0xa867df55L, 0x316e8eefL, 0x4669be79L, 0xcb61b38cL,
			0xbc66831aL, 0x256fd2a0L, 0x5268e236L, 0xcc0c7795L, 0xbb0b4703L, 0x220216b9L, 0x5505262fL, 0xc5ba3bbeL, 0xb2bd0b28L, 0x2bb45a92L, 0x5cb36a04L, 0xc2d7ffa7L,
			0xb5d0cf31L, 0x2cd99e8bL, 0x5bdeae1dL, 0x9b64c2b0L, 0xec63f226L, 0x756aa39cL, 0x026d930aL, 0x9c0906a9L, 0xeb0e363fL, 0x72076785L, 0x05005713L, 0x95bf4a82L,
			0xe2b87a14L, 0x7bb12baeL, 0x0cb61b38L, 0x92d28e9bL, 0xe5d5be0dL, 0x7cdcefb7L, 0x0bdbdf21L, 0x86d3d2d4L, 0xf1d4e242L, 0x68ddb3f8L, 0x1fda836eL, 0x81be16cdL,
			0xf6b9265bL, 0x6fb077e1L, 0x18b74777L, 0x88085ae6L, 0xff0f6a70L, 0x66063bcaL, 0x11010b5cL, 0x8f659effL, 0xf862ae69L, 0x616bffd3L, 0x166ccf45L, 0xa00ae278L,
			0xd70dd2eeL, 0x4e048354L, 0x3903b3c2L, 0xa7672661L, 0xd06016f7L, 0x4969474dL, 0x3e6e77dbL, 0xaed16a4aL, 0xd9d65adcL, 0x40df0b66L, 0x37d83bf0L, 0xa9bcae53L,
			0xdebb9ec5L, 0x47b2cf7fL, 0x30b5ffe9L, 0xbdbdf21cL, 0xcabac28aL, 0x53b39330L, 0x24b4a3a6L, 0xbad03605L, 0xcdd70693L, 0x54de5729L, 0x23d967bfL, 0xb3667a2eL,
			0xc4614ab8L, 0x5d681b02L, 0x2a6f2b94L, 0xb40bbe37L, 0xc30c8ea1L, 0x5a05df1bL, 0x2d02ef8dL };
//    private static Logger logger = LoggerFactory.getLogger(FileChickUtils.class);

	public final static byte algorithmID = 77;

	private static long get_crc32_table(byte[] data, int offset, int length, long crc_reg) {
		length += offset;
		for (int j = offset; j < length; j++) {
			crc_reg = crc32tab[(int) ((crc_reg ^ data[j]) & 0xff)] ^ (crc_reg >> 8);
		}
		return crc_reg;
	}

	private static long get_crc32_table(ByteBuffer data, int offset, int length, long crc_reg) {
		length += offset;
		for (int j = offset; j < length; j++) {
			crc_reg = crc32tab[(int) ((crc_reg ^ data.get(j)) & 0xff)] ^ (crc_reg >> 8);
		}
		return crc_reg;
	}

	public static long calcCrc32(FileChannel fc) throws IOException {
		RandomAccessFile fileInputStream = null;
		try {
			int bytesRead = 0;
			long crcValue = 0xFFFFFFFFL;

			do {
				ByteBuffer buffer = ByteBuffer.allocate((int) 10 * 1024 * 1024); // 分配缓冲区
				bytesRead = fc.read(buffer); // 读取文件数据到缓冲区
				if (bytesRead > 0) {
					buffer.flip(); // 切换为读模式
					crcValue = get_crc32_table(buffer, 0, bytesRead, crcValue);
				}
			} while (bytesRead > 0);
			crcValue = crcValue ^ 0xFFFFFFFFL;

			return crcValue;
		} finally {
			if (fileInputStream != null)
				fileInputStream.close();
		}
	}

	public static long calcCrc32(File localFilePath) throws IOException {
		RandomAccessFile fileInputStream = null;
		try {
			fileInputStream = new RandomAccessFile(localFilePath, "r");
			long crcValue = 0xFFFFFFFFL;
			byte[] buffer = new byte[10 * 1024 * 1024];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				crcValue = get_crc32_table(buffer, 0, bytesRead, crcValue);
			}
			crcValue = crcValue ^ 0xFFFFFFFFL;
//            logger.info("CRC32 Value: " + crcValue);

			return crcValue;
		} finally {
			if (fileInputStream != null)
				fileInputStream.close();
		}
	}

	public static long calcCrc32(InputStream in, StepProgressListener progressListener, Interrupter interrupter) throws IOException {
		// Return the computed CRC32 value
		if (in == null || interrupter.isInterrupted()) {
			return -1;
		}

		CRC32 crc32 = new CRC32();
		byte[] buffer = new byte[1024];
		int bytesRead;

		if (progressListener != null) {
			in = new ProgressInputStream(in, -1, progressListener);
		}

		try {
			while ((bytesRead = in.read(buffer)) > 0) {
				if (interrupter.isInterrupted()) {
					return -1;
				}
				crc32.update(buffer, 0, bytesRead);
			}

			return crc32.getValue();
		} finally {
			in.close();
		}
	}

//	public static long calcCrc32(InputStream inputStream) throws IOException {
//        CRC32 crc32 = new CRC32();
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//
//        // Read the input stream and update CRC32
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            crc32.update(buffer, 0, bytesRead);
//        }
//
//        // Return the computed CRC32 value
//        return crc32.getValue();
//    }

	// private static MessageDigest md5 = null;
	// private static MessageDigest sha256 = null;
	// static {
	// try {
	// md5 = MessageDigest.getInstance("MD5");
	// sha256 = MessageDigest.getInstance("SHA-256");
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// };

	public static MessageDigest getMd5Digest() {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MessageDigest getSha256Digest() {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA-256");
			return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isMD5Hex(String checksum) {
		if (checksum == null) {
			return false;
		}

		int max = checksum.length();
		if (max != 32) {
			return false;
		}

		if (checksum.lastIndexOf('-') != -1) {
			return false;
		}

		for (int i = 0; i < max; i++) {
			char c = checksum.charAt(i);
			if (c >= 'a' && c <= 'z') {
				continue;
			}
			if (c >= 'A' && c <= 'Z') {
				continue;
			}
			if (c >= '0' && c <= '9') {
				continue;
			}

			return false;
		}

		return true;
	}

	// public static String encodeString(String input) {
	// Base64.Encoder encoder = Base64.getEncoder();
	// String encodedString = encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
	// return encodedString;
	// }
	//
	// public static String decodeString(String input) {
	// Base64.Decoder encoder = Base64.getDecoder();
	// String decodeString = new
	// String(encoder.decode(input.getBytes(StandardCharsets.UTF_8)));
	// return decodeString;
	// }

	public static String toBase64String(String input) {
		return Base64Utils.encode(input.getBytes());
	}

	public static String encodeBase64(String input) {
		return new String(Base64Utils.encode(input.getBytes()));
	}

//	public static String encodeBase64(String input, String charset) {
//		return Base64.encode(input.getBytes(charset), charset);
//	}

	public static String decodeBase64(String input) {
		return Base64Utils.decode(input.getBytes());
	}

	public static String decodeBase64(String input, Charset charset) {
		return Base64Utils.encode(input.getBytes(charset));
	}

//	public static int hash(String content) {
//		int h;
//		return (content == null) ? 0 : (h = content.hashCode()) ^ (h >>> 16);
//	}

	/**
	 * Returns a hash code for this string. The hash code for a {@code String} object is computed as <blockquote>
	 * 
	 * <pre>
	 * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
	 * </pre>
	 * 
	 * </blockquote> using {@code int} arithmetic, where {@code s[i]} is the <i>i</i>th character of the string, {@code n} is the length of the string,
	 * and {@code ^} indicates exponentiation. (The hash value of the empty string is zero.)
	 *
	 * @return a hash code value for this object.
	 */
//	public static int hashCode(String content) {
//		// assert key!=null && key.length()>0
//		int hash = 0;
//		byte[] value = content.getBytes(StandardCharsets.UTF_8);
//		if (value.length > 0) {
//			for (int i = 0; i < value.length; i++) {
//				hash = 31 * hash + value[i];
//			}
//		}
//		return hash;
//	}

	public static byte[] calcMD5(InputStream in) throws IOException {
		return calcHash(getMd5Digest(), in);
	}

	public static byte[] calcMD5(String content) {
		return calcHash(getMd5Digest(), content);
	}

	public static byte[] calcMD5(File file) throws IOException {
		return calcHash(getMd5Digest(), file);
	}

	public static byte[] calcMD5(byte[] data) {
		return calcHash(getMd5Digest(), data);
	}

	public static byte[] calcMD5(byte[] data, int offset, int length) {
		return calcHash(getMd5Digest(), data, offset, length);
	}

	public static byte[] calcMD5(RandomAccessFile accessFile) throws IOException {
		return calcHash(getMd5Digest(), accessFile);
	}

	public static String calcMD5ToHex(InputStream in) throws IOException {
		MessageDigest md5 = getMd5Digest();
		return format2HexLowerCase(calcHash(md5, in));
	}

	public static String calcMD5ToHex(String content) {
		MessageDigest md5 = getMd5Digest();
		return format2HexLowerCase(calcHash(md5, content));
	}

	public static String calcMD5ToHex(byte[] content) {
		MessageDigest md5 = getMd5Digest();
		return format2HexLowerCase(calcHash(md5, content));
	}

	public static String calcMD5ToHex(File file) throws IOException {
		MessageDigest md5 = getMd5Digest();
		return format2HexLowerCase(calcHash(md5, file));
	}

	public static boolean isMd5Equals(File file1, File file2) throws IOException {
		MessageDigest md5 = getMd5Digest();
		byte[] a1 = calcHash(md5, file1);
		byte[] a2 = calcHash(md5, file2);
		return Arrays.equals(a1, a2);
	}

	public static boolean isMd5Equals(InputStream in, File file) throws IOException {
		MessageDigest md5 = getMd5Digest();
		byte[] a1 = calcHash(md5, in);
		byte[] a2 = calcHash(md5, file);
		return Arrays.equals(a1, a2);
	}

	public static boolean isMd5Equals(InputStream in1, InputStream in2) throws IOException {
		MessageDigest md5 = getMd5Digest();
		byte[] a1 = calcHash(md5, in1);
		byte[] a2 = calcHash(md5, in2);
		return Arrays.equals(a1, a2);
	}

	public static boolean isMd5Equals(InputStream in1, String in2) throws IOException {
		MessageDigest md5 = getMd5Digest();
		byte[] a1 = calcHash(md5, in1);
		byte[] a2 = calcHash(md5, in2);
		return Arrays.equals(a1, a2);
	}

	public static boolean isMd5Equals(String in1, String in2) throws IOException {
		MessageDigest md5 = getMd5Digest();
		byte[] a1 = calcHash(md5, in1);
		byte[] a2 = calcHash(md5, in2);
		return Arrays.equals(a1, a2);
	}

	public static byte[] calcSHA256(InputStream in) throws IOException {
		return calcHash(getSha256Digest(), in);
	}

	public static byte[] calcSHA256(String content) {
		return calcHash(getSha256Digest(), content);
	}

	public static byte[] calcSHA256(File file) throws IOException {
		return calcHash(getSha256Digest(), file);
	}

	public static byte[] calcHash(MessageDigest md, String content) {
		byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
		return digest;
	}

	public static String format2Hex(byte[] digest) {
		return format2HexUpperCase(digest);
	}

	public static String format2Hex(long crc) {
		return String.format("%08X", crc);
	}

	public static String format2HexUpperCase(byte[] digest) {
		if (digest == null || digest.length == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			sb.append(String.format("%02X", digest[i] & 0xff));
		}
		return sb.toString();
	}

	public static String format2HexLowerCase(byte[] digest) {
		if (digest == null || digest.length == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			sb.append(String.format("%02x", digest[i] & 0xff));
		}
		return sb.toString();
	}

	public static byte[] formatHex2Byte(String hexstr) {
		int len = hexstr.length();
//		if(len %2!=0) {
//			
//		}
		byte[] bc = new byte[len / 2];
		int j = 0;
		for (int i = 0; i < len; i += 2) {
			String onebyte = hexstr.substring(i, i + 2);
			BigInteger intx = new BigInteger(onebyte, 16);

			byte b = intx.byteValue();
			bc[j] = b;
			j++;
		}
		return bc;
	}

//	public static void main(String[] arg) {
//		MessageDigest md5 = getMd5Digest();
//		byte[] hex = calcHash(md5, "xx");
//		String hexstr = format2Hex(hex);
//		System.out.println(hexstr);
//
//		int len = hexstr.length();
//		byte[] bc = new byte[len / 2];
//		int j = 0;
//		for (int i = 0; i < len; i += 2) {
//			String onebyte = hexstr.substring(i, i + 2);
//			BigInteger intx = new BigInteger(onebyte, 16);
//
//			byte b = intx.byteValue();
//			bc[j] = b;
//			j++;
//		}
//		System.out.println(format2Hex(bc));
//	}

	public static byte[] calcHash(MessageDigest md, RandomAccessFile accessFile) throws IOException {
		final int SIZE = 1024 * 1024 * 5;
		byte[] buffer = new byte[SIZE];
		byte[] digest = null;

		long read = 0;

		// calculate the hash of the hole file for the test
		long offset = accessFile.length();
		int unitsize;
		while (read < offset) {
			unitsize = (int) (((offset - read) >= SIZE) ? SIZE : (offset - read));
			accessFile.read(buffer, 0, unitsize);

			md.update(buffer, 0, unitsize);

			read += unitsize;
		}

		accessFile.close();
		digest = md.digest();

		return digest;
	}

	public static byte[] calcHash(MessageDigest md, File file) throws IOException {
		RandomAccessFile accessFile = new RandomAccessFile(file, "r");

		return calcHash(md, accessFile);
//		final int SIZE = 1024 * 1024 * 5;
//		byte[] buffer = new byte[SIZE];
//		byte[] digest = null;
//
//		long read = 0;
//
//		// calculate the hash of the hole file for the test
//		long offset = file.length();
//		int unitsize;
//		while (read < offset) {
//			unitsize = (int) (((offset - read) >= SIZE) ? SIZE : (offset - read));
//			accessFile.read(buffer, 0, unitsize);
//
//			md.update(buffer, 0, unitsize);
//
//			read += unitsize;
//		}
//
//		accessFile.close();
//		digest = md.digest();
//
//		return digest;
	}

	public static byte[] calcHash(MessageDigest md, InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		}

		byte[] buffer = new byte[1024 * 1024 * 4];
		int read = -1;

		try {
			while ((read = in.read(buffer)) > 0) {
				md.update(buffer, 0, read);
			}

			byte[] digest = md.digest();

			return digest;
		} finally {
			in.close();
		}
	}

	public static byte[] calcHash(MessageDigest md, InputStream in, StepProgressListener progressListener, Interrupter interrupter) throws IOException {
		if (in == null || interrupter.isInterrupted()) {
			return new byte[0];
		}

		if (progressListener != null) {
			in = new ProgressInputStream(in, -1, progressListener);
		}

		byte[] buffer = new byte[1024 * 1024 * 4];
		int read = -1;

		try {
			while ((read = in.read(buffer)) > 0) {
				if (interrupter.isInterrupted()) {
					return new byte[0];
				}
				md.update(buffer, 0, read);
			}

			byte[] digest = md.digest();

			return digest;
		} finally {
			in.close();
		}
	}

	public static byte[] calcHash(MessageDigest md, byte[] data, int offset, int length) {
		md.update(data, offset, length);

		byte[] digest = md.digest();

		return digest;
	}

	public static byte[] calcHash(MessageDigest md, byte[] data) {
		md.update(data, 0, data.length);

		byte[] digest = md.digest();

		return digest;
	}

//	public static long ELFHash(String key) {
//        long hash = 0, x = 0;
//
//        for (int i = 0; i < key.length(); i++) {
//            hash = (hash << 4) + key.charAt(i);
//
//            if ((x = hash & 0xF0000000L) != 0) {
//                hash ^= (x >> 24);
//            }
//            hash &= ~x;
//        }
//
//        return hash;
//    }

	public static String bytesToHexString(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param  hexString the hex string
	 * @return           byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param  c char
	 * @return   byte
	 */
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static long RSHash(String str) {
		int b = 378551;
		int a = 63689;
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}

		return hash;
	}
	/* End Of RS Hash Function */

	public static long JSHash(String str) {
		long hash = 1315423911;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}

		return hash;
	}
	/* End Of JS Hash Function */

	public static long PJWHash(String str) {
		long BitsInUnsignedInt = (long) (4 * 8);
		long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
		long OneEighth = (long) (BitsInUnsignedInt / 8);
		long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
		long hash = 0;
		long test = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << OneEighth) + str.charAt(i);

			if ((test = hash & HighBits) != 0) {
				hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
			}
		}

		return hash;
	}
	/* End Of P. J. Weinberger Hash Function */

	public static long ELFHash(String str) {
		long hash = 0;
		long x = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << 4) + str.charAt(i);

			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
			}
			hash &= ~x;
		}

		return hash;
	}
	/* End Of ELF Hash Function */

	public static long BKDRHash(String str) {
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash * seed) + str.charAt(i);
		}

		return hash;
	}
	/* End Of BKDR Hash Function */

	public static long SDBMHash(String str) {
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}

		return hash;
	}
	/* End Of SDBM Hash Function */

	public static long DJBHash(String str) {
		long hash = 5381;

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + str.charAt(i);
		}

		return hash;
	}
	/* End Of DJB Hash Function */

	public static long DEKHash(String str) {
		long hash = str.length();

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}

		return hash;
	}
	/* End Of DEK Hash Function */

	public static long BPHash(String str) {
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = hash << 7 ^ str.charAt(i);
		}

		return hash;
	}
	/* End Of BP Hash Function */

	public static long FNVHash(String str) {
		long fnv_prime = 0x811C9DC5;
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash *= fnv_prime;
			hash ^= str.charAt(i);
		}

		return hash;
	}
	/* End Of FNV Hash Function */

	public static long APHash(String str) {
		long hash = 0xAAAAAAAA;

		for (int i = 0; i < str.length(); i++) {
			if ((i & 1) == 0) {
				hash ^= ((hash << 7) ^ str.charAt(i) * (hash >> 3));
			} else {
				hash ^= (~((hash << 11) + str.charAt(i) ^ (hash >> 5)));
			}
		}

		return hash;
	}

	public static long calcHash(int hashAlgorithmId, String value) {
		String newValue = value.toLowerCase();
		long valueHash;

		switch (hashAlgorithmId) {
		case 1:
			valueHash = newValue.hashCode();
			break;
		case 2:
			valueHash = DigestUtils.APHash(newValue);
			break;
		case 3:
			valueHash = DigestUtils.SDBMHash(newValue);
			break;
		case 4:
			valueHash = DigestUtils.BKDRHash(newValue);
			break;
		case 5:
			valueHash = DigestUtils.JSHash(newValue);
			break;
		case 6:
			valueHash = DigestUtils.RSHash(newValue);
			break;
		case 7:
			valueHash = DigestUtils.DJBHash(newValue);
			break;
		case 8:
			valueHash = DigestUtils.DEKHash(newValue);
			break;
		case 9:
			valueHash = DigestUtils.BPHash(newValue);
			break;
		case 10:
			valueHash = DigestUtils.FNVHash(newValue);
			break;
		case 11:
			valueHash = DigestUtils.ELFHash(newValue);
			break;
		case 12:
			valueHash = DigestUtils.PJWHash(newValue);
			break;
		default:
			valueHash = newValue.hashCode() + newValue.length();
		}

		return valueHash;
	}
	
//	public static int xxHash32(String input) {
//	    byte[] data = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
//	    int seed = 0;
//	    final int PRIME1 = 0x9E3779B1;
//	    final int PRIME2 = 0x85EBCA77;
//	    final int PRIME3 = 0xC2B2AE3D;
//	    final int PRIME4 = 0x27D4EB2F;
//	    final int PRIME5 = 0x165667B1;
//
//	    int length = data.length;
//	    int index = 0;
//	    int hash;
//
//	    if (length >= 16) {
//	        int v1 = seed + PRIME1 + PRIME2;
//	        int v2 = seed + PRIME2;
//	        int v3 = seed;
//	        int v4 = seed - PRIME1;
//
//	        int limit = length - 16;
//	        while (index <= limit) {
//	            int k1 = (data[index] & 0xFF)
//	                   | ((data[index + 1] & 0xFF) << 8)
//	                   | ((data[index + 2] & 0xFF) << 16)
//	                   | ((data[index + 3] & 0xFF) << 24);
//	            v1 += k1 * PRIME2;
//	            v1 = Integer.rotateLeft(v1, 13);
//	            v1 *= PRIME1;
//	            index += 4;
//
//	            int k2 = (data[index] & 0xFF)
//	                   | ((data[index + 1] & 0xFF) << 8)
//	                   | ((data[index + 2] & 0xFF) << 16)
//	                   | ((data[index + 3] & 0xFF) << 24);
//	            v2 += k2 * PRIME2;
//	            v2 = Integer.rotateLeft(v2, 13);
//	            v2 *= PRIME1;
//	            index += 4;
//
//	            int k3 = (data[index] & 0xFF)
//	                   | ((data[index + 1] & 0xFF) << 8)
//	                   | ((data[index + 2] & 0xFF) << 16)
//	                   | ((data[index + 3] & 0xFF) << 24);
//	            v3 += k3 * PRIME2;
//	            v3 = Integer.rotateLeft(v3, 13);
//	            v3 *= PRIME1;
//	            index += 4;
//
//	            int k4 = (data[index] & 0xFF)
//	                   | ((data[index + 1] & 0xFF) << 8)
//	                   | ((data[index + 2] & 0xFF) << 16)
//	                   | ((data[index + 3] & 0xFF) << 24);
//	            v4 += k4 * PRIME2;
//	            v4 = Integer.rotateLeft(v4, 13);
//	            v4 *= PRIME1;
//	            index += 4;
//	        }
//
//	        hash = Integer.rotateLeft(v1, 1)
//	             + Integer.rotateLeft(v2, 7)
//	             + Integer.rotateLeft(v3, 12)
//	             + Integer.rotateLeft(v4, 18);
//	    } else {
//	        hash = seed + PRIME5;
//	    }
//
//	    hash += length;
//
//	    // Process remaining bytes
//	    while (index + 4 <= length) {
//	        int k1 = (data[index] & 0xFF)
//	               | ((data[index + 1] & 0xFF) << 8)
//	               | ((data[index + 2] & 0xFF) << 16)
//	               | ((data[index + 3] & 0xFF) << 24);
//	        hash += k1 * PRIME3;
//	        hash = Integer.rotateLeft(hash, 17) * PRIME4;
//	        index += 4;
//	    }
//
//	    while (index < length) {
//	        hash += (data[index++] & 0xFF) * PRIME5;
//	        hash = Integer.rotateLeft(hash, 11) * PRIME1;
//	    }
//
//	    // final mix
//	    hash ^= hash >>> 15;
//	    hash *= PRIME2;
//	    hash ^= hash >>> 13;
//	    hash *= PRIME3;
//	    hash ^= hash >>> 16;
//
//	    return hash;
//	}


}
