package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListInputStream extends InputStream {

	private final List<byte[]> list;
	private int currentIndex;
	private int currentOffset;

	public ListInputStream(List<byte[]> list) {
		this.list = Objects.requireNonNull(list);
		this.currentIndex = 0;
		this.currentOffset = 0;
	}

	@Override
	public int read() throws IOException {
		if (currentIndex >= list.size()) {
			return -1;
		}

		byte[] currentArray = list.get(currentIndex);
		int value = currentArray[currentOffset] & 0xFF;
		if (++currentOffset >= currentArray.length) {
			currentIndex++;
			currentOffset = 0;
		}

		return value;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (currentIndex >= list.size()) {
			return -1;
		}

		int totalBytesRead = 0;
		while (len > 0 && currentIndex < list.size()) {
			byte[] currentArray = list.get(currentIndex);
			int bytesToRead = Math.min(len, currentArray.length - currentOffset);
			System.arraycopy(currentArray, currentOffset, b, off, bytesToRead);

			off += bytesToRead;
			len -= bytesToRead;
			totalBytesRead += bytesToRead;

			if (bytesToRead >= currentArray.length - currentOffset) {
				currentIndex++;
				currentOffset = 0;
			} else {
				currentOffset += bytesToRead;
			}
		}

		return totalBytesRead;
	}

	@Override
	public synchronized void reset() throws IOException {
		this.currentIndex = 0;
		this.currentOffset = 0;
	}

	@Override
	public void close() throws IOException {
		list.clear();
		this.currentIndex = 0;
		this.currentOffset = 0;
	}

//	public static void main(String[] args) throws IOException {
//		// 测试数据：多个byte[]作为List中的元素
//		List<byte[]> dataList = new ArrayList<>();
//		dataList.add(new byte[] { 1, 2, 3 });
//		dataList.add(new byte[] { 4, 5 });
//		dataList.add(new byte[] { 6 });
//		dataList.add(new byte[] { 7, 8, 9, 10 });
//
//		// 测试ListInputStream读取整体数据流
//		System.out.println("Testing reading the entire data stream:");
//
//		InputStream inputStream = new ListInputStream(dataList);
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//		byte[] buffer = new byte[1024];
//		int bytesRead;
//		while ((bytesRead = inputStream.read(buffer)) != -1) {
//			outputStream.write(buffer, 0, bytesRead);
//		}
//
//		byte[] data = outputStream.toByteArray();
//		System.out.println("Data read: " + Arrays.toString(data));
//
//		// 测试ListInputStream使用read(byte[] b, int off, int len)方法读取数据流
//		System.out.println("Testing reading data into a buffer:");
//
//		inputStream = new ListInputStream(dataList);
//		buffer = new byte[4];
//		bytesRead = inputStream.read(buffer);
//
//		System.out.println("Bytes read: " + bytesRead);
//		System.out.println("Data read: " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
//
//		buffer = new byte[6];
//		bytesRead = inputStream.read(buffer, 0, 6);
//
//		System.out.println("Bytes read: " + bytesRead);
//		System.out.println("Data read: " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
//
//		buffer = new byte[1024];
//		bytesRead = inputStream.read(buffer);
//
//		System.out.println("Bytes read: " + bytesRead);
//		System.out.println("Data read: " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
//
//		// 关闭输入流和输出流
//		inputStream.close();
//		outputStream.close();
//	}

}