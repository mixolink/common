package com.amituofo.common.kit.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BandwidthInputStream extends FilterInputStream {
	private Bandwidth bandwidthLimiter = null;// Bandwidth.IN_BANDWIDTH;

	public BandwidthInputStream(InputStream is, Bandwidth bw) {
		super(is);
		this.bandwidthLimiter = bw;
	}

//	public static void enableBandwidth(Bandwidth bw) {
//		bandwidthLimiter = bw;
//	}
//
//	public static Bandwidth enableBandwidth() {
//		bandwidthLimiter = Bandwidth.IN_BANDWIDTH;
//		return bandwidthLimiter;
//	}

//	public void disableBandwidth() {
//		bandwidthLimiter = null;
//	}
//
//	public boolean isBandwidthEnabled() {
//		return bandwidthLimiter != null;
//	}

	@Override
	public int read() throws IOException {
		bandwidthLimiter.limitNextBytes(1);
		return in.read();
	}

	@Override
	public int read(byte b[]) throws IOException {
		int readlen = in.read(b);
		bandwidthLimiter.limitNextBytes(readlen);
		return readlen;
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		int readlen = in.read(b, off, len);
		bandwidthLimiter.limitNextBytes(readlen);
		return readlen;
	}

}
