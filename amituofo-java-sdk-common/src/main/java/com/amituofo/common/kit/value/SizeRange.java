package com.amituofo.common.kit.value;

public class SizeRange {
	private long min;
	private long max;

	public SizeRange() {
	}

	public SizeRange(long min, long max) {
		super();
		this.min = min;
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

}
