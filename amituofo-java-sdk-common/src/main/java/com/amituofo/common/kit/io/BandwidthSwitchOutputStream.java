package com.amituofo.common.kit.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public class BandwidthSwitchOutputStream extends FilterOutputStream {
	private final BandwidthOutputStream bandout;
	private OutputStream origout;

	public BandwidthSwitchOutputStream(OutputStream os, Bandwidth bw) {
		super(os);
		origout = os;
		bandout = new BandwidthOutputStream(os, bw);
	}

	public void setEnableBandwidth(boolean enable) {
		out = (enable ? bandout : origout);
	}
}
