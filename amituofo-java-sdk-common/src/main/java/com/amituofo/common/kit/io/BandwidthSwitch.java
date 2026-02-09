package com.amituofo.common.kit.io;

import java.io.InputStream;
import java.io.OutputStream;

public class BandwidthSwitch {
//	public final static Bandwidth IN_BANDWIDTH = new Bandwidth(10240);
//	public final static Bandwidth OUT_BANDWIDTH = new Bandwidth(10240);

//	private Set<BandwidthSwitchInputStream> ins = new HashSet<>();
//	private Set<BandwidthSwitchOutputStream> outs = new HashSet<>();

	private final Bandwidth inBandwidth;
	private final Bandwidth outBandwidth;

//	public final static BandwidthSwitch DEFAULT = new BandwidthSwitch();

	public BandwidthSwitch() {
		this(new Bandwidth(-1), new Bandwidth(-1));
	}

	public BandwidthSwitch(Bandwidth inBandwidth, Bandwidth outBandwidth) {
		this.inBandwidth = inBandwidth;
		this.outBandwidth = outBandwidth;
	}

//	public void setEnableBandwidth(boolean enable) {
//		for (BandwidthSwitchInputStream in : ins) {
//			in.setEnableBandwidth(enable);
//		}
//
//		for (BandwidthSwitchOutputStream out : outs) {
//			out.setEnableBandwidth(enable);
//		}
//	}
//
//	public void addBandwidthInputStream(InputStream in) {
//		ins.add(new BandwidthSwitchInputStream(in, inBandwidth));
//	}
//
//	public void addBandwidthOutputStream(OutputStream out) {
//		outs.add(new BandwidthSwitchOutputStream(out, outBandwidth));
//	}
	public boolean isInBandwidthEnabled() {
		return inBandwidth != null && inBandwidth.isEnabled();
	}

	public boolean isOutBandwidthEnabled() {
		return outBandwidth != null && outBandwidth.isEnabled();
	}

	public Bandwidth getInBandwidth() {
		return inBandwidth;
	}

	public Bandwidth getOutBandwidth() {
		return outBandwidth;
	}

	public InputStream newBandwidthInputStream(InputStream in) {
		if (inBandwidth == null || !inBandwidth.isEnabled()) {
			return in;
		}

		return new BandwidthInputStream(in, inBandwidth);
	}

	public OutputStream newBandwidthOutputStream(OutputStream out) {
		if (outBandwidth == null || !outBandwidth.isEnabled()) {
			return out;
		}

		return new BandwidthOutputStream(out, outBandwidth);
	}
}
