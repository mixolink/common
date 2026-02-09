package com.amituofo.common.util;

import java.nio.ByteBuffer;

public class ByteUtils {

	public static byte[] convertIntToByteArray(int value, int bytesize) {
		return ByteBuffer.allocate(bytesize).putInt(value).array();
	}

	// method 2, bitwise right shift
	public static byte[] convertIntToByteArray2(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}
}
