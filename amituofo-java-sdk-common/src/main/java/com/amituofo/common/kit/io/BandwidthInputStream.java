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
		if (bandwidthLimiter != null) {
			try {
				bandwidthLimiter.limitNextBytes(1);
			} catch (Exception e) {
			}
		}
		return in.read();
	}

	@Override
	public int read(byte b[]) throws IOException {
		if (bandwidthLimiter != null) {
			try {
				bandwidthLimiter.limitNextBytes(b.length);
			} catch (Exception e) {
			}
		}
		return in.read(b);
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if (bandwidthLimiter != null) {
//			long t1 = System.currentTimeMillis();
			try {
				bandwidthLimiter.limitNextBytes(len);
			} catch (Exception e) {
			}
//			long t2 = System.currentTimeMillis();

//			System.out.println((t2 - t1) + "-" + this);
		}
		return in.read(b, off, len);
	}

//	public InputStream getInputStream() {
//		return in;
//	}

}
