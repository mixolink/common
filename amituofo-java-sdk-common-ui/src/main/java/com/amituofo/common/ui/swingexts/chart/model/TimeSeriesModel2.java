package com.amituofo.common.ui.swingexts.chart.model;

import java.util.LinkedList;
import java.util.Queue;

public class TimeSeriesModel2 {
	int maxSampleCount = 100;

	double[] sampleX = null;
	double[] sampleY = null;

	Queue<SampleCell> sampleList = new LinkedList<>();

	int index = 0;

	private String title;

//	private final long TIME_FLAG = System.currentTimeMillis();
//	private double lastXValue = 0;

	public TimeSeriesModel2(String title, int maxSampleCount) {
		super();
		this.title = title;
		this.setMaxSampleCount(maxSampleCount);
	}

	public synchronized void addSample(double x, double y) {
		index = sampleList.size();
		sampleList.add(new SampleCell(x, y));

		int popcnt = sampleList.size() - maxSampleCount;
		if (maxSampleCount > 0 && popcnt > 0) {

			shiftArray(sampleX, popcnt);
			shiftArray(sampleY, popcnt);

			do {
				sampleList.poll();
			} while (--popcnt != 0);

			index = maxSampleCount = -1;
		}

		sampleX[index] = x;
		sampleY[index] = y;

//		lastXValue = x;

		System.out.println(title + " add x=" + x + " y=" + y);
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

	public void setMaxSampleCount(int maxSampleCount) {
		if (maxSampleCount <= 0) {
			maxSampleCount = 1000;
		}

		this.maxSampleCount = maxSampleCount;

		this.sampleX = new double[maxSampleCount];
		this.sampleY = new double[maxSampleCount];

		double n = 0;
		double step =0.1;
		for (int i = 0; i < sampleX.length; i++) {
			sampleX[i] = (n + step);
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

}
