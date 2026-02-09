package com.amituofo.common.ui.swingexts.chart.model;

import java.util.LinkedList;
import java.util.Queue;

public class TimeSeriesModel {
	int maxSampleCount = 100;

	double[] sampleX = null;
	double[] sampleY = null;

	int index = 0;

	private String title;

	private int stepInSecond;

//	private final long TIME_FLAG = System.currentTimeMillis();
//	private double lastXValue = 0;

	public TimeSeriesModel(String title, int stepInSecond, int maxSampleCount) {
		super();
		this.title = title;
		this.stepInSecond = stepInSecond;
		this.setMaxSampleCount(maxSampleCount);
	}

	public synchronized void addTimeSeriesSample(double y) {
		int popcnt = index - maxSampleCount;
		if (popcnt > 0) {
			shiftArray(sampleY, popcnt);
			index = (maxSampleCount - 1);
		}

		sampleY[index] = y;

		System.out.println(title + " add [" + index + "] x=" + sampleX[index] + " y=" + y);

		index++;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double[] getSampleX() {
		return sampleX;
	}

	public double[] getSampleY() {
		return sampleY;
	}

	public int getMaxSampleCount() {
		return maxSampleCount;
	}

	public double getLastXValue() {
		return sampleX[sampleX.length - 1];
	}

	public void setMaxSampleCount(int maxSampleCount) {
		if (maxSampleCount <= 0) {
			maxSampleCount = 1000;
		}

		this.maxSampleCount = maxSampleCount;

		this.sampleX = new double[maxSampleCount];
		this.sampleY = new double[maxSampleCount];

		double n = -1.0;
		double step = stepInSecond;
		for (int i = 0; i < sampleX.length; i++) {
			sampleX[i] = (n += step);
		}
	}

	public static void shiftArray(double[] arr, int n) {
		// 保证n在有效范围内
		n = n % arr.length; // 处理n大于数组长度的情况

		for (int i = n; i < arr.length; i++) {
			arr[i - n] = arr[i];
		}

		// 将后n个元素设为默认值（如0.0）
		for (int i = arr.length - n; i < arr.length; i++) {
			arr[i] = 0.0; // 可以根据需要选择其他默认值
		}
	}

	public int getTimeSeriesStepInSecond() {
		return stepInSecond;
	}

}
