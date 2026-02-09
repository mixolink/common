package com.amituofo.common.kit.kv;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Key {
	static final Charset UTF_8 = Charset.forName("UTF-8");
	static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

	protected final byte[] value;

	public Key(byte[] value) {
		super();
		this.value = value;
	}

	public Key(String utf8String) {
		super();
		this.value = utf8String.getBytes(UTF_8);
	}

	public String nextKey() {
		return new String(prefixNext(value));
	}

	public static String nextUtf8Key(String utf8key) {
		byte[] value = utf8key.getBytes(UTF_8);
		return new String(prefixNext(value));
	}

	static byte[] prefixNext(byte[] value) {
		int i;
		byte[] newVal = Arrays.copyOf(value, value.length);
		for (i = newVal.length - 1; i >= 0; i--) {
			newVal[i]++;
			if (newVal[i] != 0) {
				break;
			}
		}
		if (i == -1) {
			return Arrays.copyOf(value, value.length + 1);
		} else {
			return newVal;
		}
	}
}
