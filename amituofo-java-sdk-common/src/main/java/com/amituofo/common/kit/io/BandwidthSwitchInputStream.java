package com.amituofo.common.kit.io;

import java.io.FilterInputStream;
import java.io.InputStream;

public class BandwidthSwitchInputStream extends FilterInputStream {
	private final BandwidthInputStream bandin;
	private InputStream origin;

	public BandwidthSwitchInputStream(InputStream is, Bandwidth bw) {
		super(is);
		origin = is;
		bandin = new BandwidthInputStream(is, bw);
	}

	public void setEnableBandwidth(boolean enable) {
		in = (enable ? bandin : origin);
	}
}
