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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.DataHandler;
import com.amituofo.common.api.HandleBytes;
import com.amituofo.common.api.IOAbortable;
import com.amituofo.common.api.RecordHandler;
import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.kit.checksum.ChecksumCalculator;
import com.amituofo.common.kit.interrupt.Interrupter;
import com.amituofo.common.type.HandleFeedback;
import com.amituofo.common.type.ReadProgressEvent;

public class StreamUtils {

	public static int DEFAULT_BUFFER_SIZE = 1024;

	public static InputStream bytesToInputStream(byte[] bytes) throws IOException {
		return new ByteArrayInputStream(bytes);
	}

	public static long inputStreamToFile(InputStream ins, String filePath, boolean autoCloseInputStream) throws IOException {
		File dir = new File(filePath).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream os = new FileOutputStream(filePath);
		return inputStream2OutputStream(ins, autoCloseInputStream, os, true);
	}

	public static long inputStreamToFile(InputStream ins, File file, boolean autoCloseInputStream) throws IOException {
		OutputStream os = new FileOutputStream(file);
		return inputStream2OutputStream(ins, autoCloseInputStream, os, true);
	}

	public static byte[] inputStreamToBytes(InputStream ins, boolean autoCloseInputStream) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputStream2OutputStream(ins, autoCloseInputStream, os, true);
		return os.toByteArray();
	}

	public static void skip(InputStream ins, int length) throws IOException {
		inputStream2ByteArray(ins, length, true);
	}

	public static byte[] uploadStream2ByteArray(InputStream ins, int maxLength) throws IOException {
		return inputStream2ByteArray(ins, maxLength, true);
	}

//	public static byte[] inputStream2ByteArray(int buffersize, InputStream ins, int maxLength, boolean autoCloseInputStream, boolean skip) throws IOException {
//		ByteArrayOutputStream out = null;
//		int bytesRead = 0;
//		int totalBytesRead = 0;
//
//		if (!skip) {
//			out = new ByteArrayOutputStream(maxLength);
//		}
//
//		byte[] buffer = new byte[Math.min(buffersize, maxLength)];
//		int nextTobeReadLength = buffer.length;
//		try {
//			while ((bytesRead = ins.read(buffer, 0, nextTobeReadLength)) != -1) {
//				if (out != null) {
//					out.write(buffer, 0, bytesRead);
//				}
//				totalBytesRead += bytesRead;
//				nextTobeReadLength = Math.min(nextTobeReadLength, (maxLength - totalBytesRead));
//
//				if (totalBytesRead == maxLength) {
//					break;
//				}
//			}
//		} finally {
//			if (autoCloseInputStream) {
//				ins.close();
//			}
//
//			buffer = null;
//			if (out != null) {
//				out.flush();
//				byte[] bs = out.toByteArray();
//				out.close();
//				return bs;
//			}
//		}
//
//		return null;
//	}

	public static byte[] inputStream2ByteArray(int buffersize, InputStream ins, int maxLength, boolean autoCloseInputStream) throws IOException {
		byte[] data;
		if (maxLength >= 0) {
			data = new byte[maxLength];
			int totalBytesRead = inputStream2ByteArray(buffersize, ins, data, 0, maxLength, autoCloseInputStream);
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[buffersize];
			try {
				int bytesRead = 0;
				while ((bytesRead = ins.read(buffer, 0, buffersize)) != -1) {
					if (bytesRead <= 0) {
						break;
					}
					out.write(buffer, 0, bytesRead);
				}
				data = out.toByteArray();
			} finally {
				if (autoCloseInputStream) {
					ins.close();
				}

				buffer = null;
			}
		}

		return data;
	}

	public static int inputStream2ByteArray(int buffersize, InputStream ins, byte[] buffer, int offset, int maxLength, boolean autoCloseInputStream) throws IOException {
		int totalBytesRead = 0;

		int nextTobeReadLength = Math.min(maxLength, buffer.length);
		try {
			int bytesRead = 0;
			while ((bytesRead = ins.read(buffer, totalBytesRead + offset, nextTobeReadLength)) != -1) {
				totalBytesRead += bytesRead;

				if (maxLength > 0) {
					nextTobeReadLength = Math.min(nextTobeReadLength, (maxLength - totalBytesRead));

					if (totalBytesRead >= maxLength) {
						break;
					}
				} else {
					if (bytesRead <= 0) {
						break;
					}
				}
			}
		} finally {
			if (autoCloseInputStream) {
				ins.close();
			}

			buffer = null;
		}

		return totalBytesRead;
	}

	public static int inputStream2ByteBuffer(InputStream srcin, ByteBuffer dstbuffer, int offset, int length, boolean autoCloseInputStream) throws IOException {
		// 确保 offset 和 length 合理
		if (offset < 0 || length < 0 || offset + length > dstbuffer.capacity()) {
			throw new IllegalArgumentException("Invalid offset or length");
		}

		// 设置 ByteBuffer 的写入位置
		if (offset > 0) {
			dstbuffer.position(offset);
		}

		int totalBytesRead = 0;
		int bytesRead;

		try {
			// 循环读取数据
			while (totalBytesRead < length) {
				// 计算可写入的字节数
				int bytesToWrite = Math.min(length - totalBytesRead, dstbuffer.remaining());
				int writeOffset = dstbuffer.position();

				// 直接读取到 ByteBuffer
				bytesRead = srcin.read(dstbuffer.array(), writeOffset, bytesToWrite);

				if (bytesRead == -1) {
					break; // 到达输入流末尾
				}

				// 更新 ByteBuffer 的写入位置
				dstbuffer.position(writeOffset + bytesRead);
				totalBytesRead += bytesRead;
			}
		} finally {
			// 选择性地关闭输入流
			if (autoCloseInputStream) {
				srcin.close();
			}
		}

		return totalBytesRead; // 返回实际读取的字节数
	}

	public static byte[] inputStream2ByteArray(InputStream ins, int maxLength, boolean autoCloseInputStream) throws IOException {
		return inputStream2ByteArray(DEFAULT_BUFFER_SIZE, ins, maxLength, autoCloseInputStream);
	}

	public static byte[] inputStream2ByteArray(InputStream ins, boolean autoCloseInputStream) throws IOException {
		return inputStream2ByteArray(DEFAULT_BUFFER_SIZE, ins, -1, autoCloseInputStream);
	}

	public static ByteArrayInputStream inputStream2ByteArrayInputStream(InputStream ins, boolean autoCloseInputStream) throws IOException {
		byte[] buf = inputStreamToBytes(ins, autoCloseInputStream);
		return new ByteArrayInputStream(buf);
	}

	public static String inputStreamToString(InputStream ins, String charsetName, boolean autoCloseInputStream) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputStream2OutputStream(ins, autoCloseInputStream, os, true);
		// return new String(os.toByteArray());
		return os.toString(charsetName);
	}

	public static String inputStreamToString(InputStream ins, String charsetName, boolean autoCloseInputStream, int readLength) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputStream2OutputStream(ins, autoCloseInputStream, os, true, readLength, null);
		// return new String(os.toByteArray());
		return os.toString(charsetName);
	}

	public static String inputStreamToString(InputStream ins, boolean autoCloseInputStream) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputStream2OutputStream(ins, autoCloseInputStream, os, true);
		return os.toString();
	}

	public static String inputStreamToString(InputStream ins, int len, boolean autoCloseInputStream) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(len);
		inputStream2OutputStream(ins, autoCloseInputStream, os, true);
		return os.toString();
	}

	public static long inputStreamToConsole(InputStream ins, boolean autoCloseInputStream) throws IOException {
		OutputStream os = System.out;
		return inputStream2OutputStream(ins, autoCloseInputStream, os, false);
	}

	public static long inputStream2OutputStream(InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream) throws IOException {
		return inputStream2OutputStream(ins, autoCloseInputStream, os, autoCloseOutputStream, null);
	}

	public static long inputStream2OutputStream(int buffersize, InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream)
			throws IOException {
		return inputStream2OutputStream(buffersize, ins, autoCloseInputStream, os, autoCloseOutputStream, null);
	}

	public static long inputStream2OutputStream(int buffersize, InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream,
			int readLength, Interrupter interrupter) throws IOException {
		long outLength = 0;

		int bytesRead = 0;
		final byte[] buffer = new byte[buffersize];

		try {
			while ((bytesRead = ins.read(buffer)) != -1) {
//			while ((bytesRead = ins.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);

				outLength += bytesRead;
				// System.out.println(String.format("%.1f",((double)(read/total))*100));
				if (outLength >= readLength || interrupter != null && interrupter.isInterrupted()) {
					return outLength;
				}
			}

			os.flush();
		} finally {
			if (autoCloseOutputStream) {
				consume(os);
			}

			if (autoCloseInputStream) {
				consume(ins);
			}
		}

		return outLength;
	}

	public static long inputStream2OutputStream(InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream, int readLength,
			Interrupter interrupter) throws IOException {
		return inputStream2OutputStream(DEFAULT_BUFFER_SIZE, ins, autoCloseInputStream, os, autoCloseOutputStream, readLength, interrupter);
	}

	public static long inputStream2OutputStream(InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream, Interrupter interrupter)
			throws IOException {
		return inputStream2OutputStream(DEFAULT_BUFFER_SIZE, ins, autoCloseInputStream, os, autoCloseOutputStream, interrupter);
	}

	public static long inputStream2OutputStream(int buffersize, InputStream ins, boolean autoCloseInputStream, OutputStream os, boolean autoCloseOutputStream,
			Interrupter interrupter) throws IOException {
		long outLength = 0;

		int bytesRead = 0;
		final byte[] buffer = new byte[buffersize];

		try {
			while ((bytesRead = ins.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);

				outLength += bytesRead;
				// System.out.println(String.format("%.1f",((double)(read/total))*100));
				if (interrupter != null && interrupter.isInterrupted()) {
					return outLength;
				}
			}

			os.flush();
		} finally {
			if (autoCloseOutputStream) {
				consume(os);
			}

			if (autoCloseInputStream) {
				consume(ins);
			}
		}

		return outLength;
	}

	public static long inputStream2OutputStream(InputStream ins, OutputStream os, Interrupter interrupter, ChecksumCalculator checksumhelper) throws IOException {
		long outLength = 0;
		int bytesRead = 0;
		byte[] buffer = new byte[8192];

		while ((bytesRead = ins.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);

			if (checksumhelper != null) {
				checksumhelper.update(buffer, 0, bytesRead);
			}

			outLength += bytesRead;

			if (interrupter != null && interrupter.isInterrupted()) {
				break;
			}
		}

		return outLength;
	}

	public static void inputStreamPrintConsole(InputStream in, boolean autoCloseInputStream) throws IOException {
		try {
			int data = in.read();
			while (data != -1) {
//				System.out.print((char) data);
				data = in.read();
			}
		} finally {
			if (autoCloseInputStream) {
				consume(in);
			}
		}
	}

	public static long inputStream2None(InputStream in, boolean autoCloseInputStream) throws IOException {
		long outLength = 0;

		int bytesRead = 0;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while ((bytesRead = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
				outLength += bytesRead;
			}
		} finally {
			if (autoCloseInputStream) {
				consume(in);
			}
		}
		return outLength;
	}

	public static long inputStream2None(InputStream in, boolean autoCloseInputStream, Interrupter interrupter) throws IOException {
		long outLength = 0;

		int bytesRead = 0;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while ((bytesRead = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
				outLength += bytesRead;

				if (interrupter != null && interrupter.isInterrupted()) {
					break;
				}
			}
		} finally {
			if (autoCloseInputStream) {
				consume(in);
			}
		}
		return outLength;
	}

	public static long inputStream2None(InputStream in, StepProgressListener progressListener, Interrupter interrupter) {
		if (progressListener == null) {
			try {
				return inputStream2None(in, true, interrupter);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long outLength = 0;

		int bytesRead = 0;
		byte[] buffer = new byte[StreamUtils.DEFAULT_BUFFER_SIZE];
		try {
			try {
				while ((bytesRead = in.read(buffer, 0, StreamUtils.DEFAULT_BUFFER_SIZE)) != -1) {
					outLength += bytesRead;
					progressListener.progressChanged(ReadProgressEvent.BYTE_READING_EVENT, bytesRead);

					if (interrupter != null && interrupter.isInterrupted()) {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			progressListener.progressChanged(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
			StreamUtils.consume(in);
		}
		return outLength;
	}

	public static void consume(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	public static void close(Closeable in) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static void close(InputStream... in) {
		if (in != null) {
			for (InputStream inputStream : in) {
				if (inputStream != null)
					try {
						inputStream.close();
					} catch (Exception e) {
						// e.printStackTrace();
					}
			}
		}
	}

	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream... out) {
		if (out != null) {
			for (OutputStream outputStream : out) {
				if (outputStream != null)
					try {
						outputStream.close();
					} catch (Exception e) {
						// e.printStackTrace();
					}
			}
		}
	}

	public static void consume(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static long readInputStream(InputStream ins, boolean autoCloseInputStream, DataHandler<byte[], Integer, Void> handler, Interrupter interrupter)
			throws IOException, HandleException {
		long outLength = 0;

		int bytesRead = 0;
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

		int index = 0;
		try {
			while ((bytesRead = ins.read(buffer)) != -1) {
				handler.handle(index++, bytesRead, buffer);

				outLength += bytesRead;
				// System.out.println(String.format("%.1f",((double)(read/total))*100));
				if (interrupter != null && interrupter.isInterrupted()) {
					return outLength;
				}
			}

		} finally {
			if (autoCloseInputStream) {
				consume(ins);
			}
		}

		return outLength;
	}

	public static void readLines(InputStream in, final RecordHandler<Long, String> recordHandler) throws IOException {
		InputStreamReader inputReader = null;
		BufferedReader bf = null;
		try {
			inputReader = new InputStreamReader(in);
			bf = new BufferedReader(inputReader);

			long row = 1;
			// 按行读取字符串
			String line;
			while ((line = bf.readLine()) != null) {
				HandleFeedback result = recordHandler.handle(row, line);
				if (result == HandleFeedback.interrupted) {
					break;
				}
				row++;
			}
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
				}
			}
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e) {
				}
			}

			recordHandler.finished();
		}
	}

	public static void readLines(InputStream in, int ignoreRowCount, final int cacheRowCount, final RecordHandler<Long, List<String>> recordHandler) throws IOException {
		InputStreamReader inputReader = null;
		BufferedReader bf = null;
		try {
			inputReader = new InputStreamReader(in);
			bf = new BufferedReader(inputReader);

			long row = 0;
			// 按行读取字符串
			if (ignoreRowCount > 0) {
				while (bf.readLine() != null) {
					row++;
					if (ignoreRowCount == row) {
						break;
					}
				}
			}

			String line;
			List<String> lines = new ArrayList<String>(cacheRowCount);
			while ((line = bf.readLine()) != null) {
				row++;

				if (lines.size() > cacheRowCount) {
					long startRow = row - lines.size() + 1;
					HandleFeedback result = recordHandler.handle(startRow, lines);
					lines = new ArrayList<String>(cacheRowCount);
					if (result == HandleFeedback.interrupted) {
						break;
					}
				}

				lines.add(line);
			}

			if (lines.size() > 0) {
				long startRow = row - lines.size() + 1;
				recordHandler.handle(startRow, lines);
			}
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
				}
			}
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e) {
				}
			}

			recordHandler.finished();
		}
	}

	public static void readContent(InputStream ins, long totalLen, HandleBytes handler) throws HandleException, FileNotFoundException, IOException {
		long remainlen = totalLen;
		int bytesRead = 0;
		final byte[] buffer = new byte[1024 * 1024];

		try {
			while ((bytesRead = ins.read(buffer)) != -1) {
				remainlen -= bytesRead;
				HandleFeedback beedback = handler.handle(remainlen, bytesRead, buffer);

				// System.out.println(String.format("%.1f",((double)(read/total))*100));
				if (beedback == HandleFeedback.failed || beedback == HandleFeedback.interrupted) {
					return;
				}
			}

		} finally {
			StreamUtils.consume(ins);
		}
	}

	public static void abort(InputStream ins) {
		if (ins != null && ins instanceof IOAbortable) {
			try {
				((IOAbortable) ins).abort();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
