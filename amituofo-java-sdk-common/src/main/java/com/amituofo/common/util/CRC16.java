package com.amituofo.common.util;

public class CRC16 {

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
	 * @param bytes 编码内容
	 * @return 编码结果
	 */
	public static int crc16(int[] bytes) {
		int res = INITIAL_VALUE;
		for (int data : bytes) {
			res = res ^ data;
			for (int i = 0; i < BITS_OF_BYTE; i++) {
				res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
			}
		}
		return res;// return convertToHexString(revert(res));
	}

	int crc = INITIAL_VALUE;

	public void calc(int[] bytes) {
		calc(bytes, 0, bytes.length);
	}

	public void calc(int[] bytes, int offset, int len) {
		for (int i = offset; i < len; i++) {
			int data = bytes[i];
			crc = crc ^ data;

			for (int j = 0; j < BITS_OF_BYTE; j++) {
				crc = (crc & 0x0001) == 1 ? (crc >> 1) ^ POLYNOMIAL : crc >> 1;
			}
		}
	}

	public void calc(int data) {
		crc = crc ^ data;
		for (int i = 0; i < BITS_OF_BYTE; i++) {
			crc = (crc & 0x0001) == 1 ? (crc >> 1) ^ POLYNOMIAL : crc >> 1;
		}
	}

	public int getCRCCode() {
		return crc;
	}
}