package com.amituofo.common.kit.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.amituofo.common.api.IOAbortable;

public class LinkInputStream extends FilterInputStream implements IOAbortable {
	private final PipedInputStream pis;
	private final PipedOutputStream pos;
	private boolean closed = false;

	public LinkInputStream() throws IOException {
		super(null);
		pos = new PipedOutputStream();
		pis = new PipedInputStream(pos);
		super.in = pis;
	}

	public void pipe(InputStream rawIn) throws IOException {
		byte[] buffer = new byte[8192];
		int len;
		try {
			while ((len = rawIn.read(buffer)) != -1 && closed == false) {
				pos.write(buffer, 0, len);
			}
		} finally {
			pos.close();
			rawIn.close();
		}
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			closed = true;
			super.close();
		}
	}

	@Override
	public void abort() throws IOException {
		if (in instanceof IOAbortable) {
			((IOAbortable) in).abort();
		}

	}

}
