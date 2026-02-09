package com.amituofo.common.ui.swingexts.chart.model;

public class SampleCell {
	long updateTime;
	double sampleX;
	double sampleY;

	public SampleCell(double x, double y) {
		this.sampleX = x;
		this.sampleY = y;
		this.updateTime = System.currentTimeMillis();
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public double getSampleX() {
		return sampleX;
	}

	public double getSampleY() {
		return sampleY;
	}

}
