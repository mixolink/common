package com.amituofo.common.api;

import java.io.IOException;
import java.io.InputStream;

public interface DataReader {

	void close();

	InputStream content(long offset, long length) throws IOException;

	long length() throws IOException;

	int read(byte[] data) throws IOException;

	byte[] read(long offset, int length) throws IOException;

	int read(long offset, int length, byte[] targetBuffer, int targetOffset) throws IOException;

	void seek(long offset) throws IOException;
}
