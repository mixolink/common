package com.amituofo.common.kit.io;

import java.util.Arrays;


public class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {

    public ByteArrayOutputStream() {
		super();
	}

	public ByteArrayOutputStream(int size) {
		super(size);
	}

	public synchronized byte toByteArray()[] {
        return Arrays.copyOf(buf, count);
    }

}
