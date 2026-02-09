package com.amituofo.common.kit.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BandwidthOutputStream extends FilterOutputStream {
	private Bandwidth bandwidthLimiter = null;// Bandwidth.OUT_BANDWIDTH;

	public BandwidthOutputStream(OutputStream os, Bandwidth bw) {
		super(os);
		this.bandwidthLimiter = bw;
	}

//	public static void enableBandwidth(Bandwidth bw) {
//		bandwidthLimiter = bw;
//	}
//
//	public static Bandwidth enableBandwidth() {
//		bandwidthLimiter = Bandwidth.OUT_BANDWIDTH;
//		return bandwidthLimiter;
//	}
//
//	public static void disableBandwidth() {
//		bandwidthLimiter = null;
//	}
//
//	public static boolean isBandwidthEnabled() {
//		return bandwidthLimiter != null;
//	}

	@Override
	public void write(int b) throws IOException {
		if (bandwidthLimiter != null) {
			try {
				bandwidthLimiter.limitNextBytes(1);
			} catch (Exception e) {
			}
		}
		out.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (bandwidthLimiter != null) {
			try {
				bandwidthLimiter.limitNextBytes(b.length);
			} catch (Exception e) {
			}
		}
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (bandwidthLimiter != null) {
			try {
				bandwidthLimiter.limitNextBytes(len);
			} catch (Exception e) {
			}
		}
		out.write(b, off, len);
	}

//	public OutputStream getOutputStream() {
//		return out;
//	}
}
