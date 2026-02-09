package com.amituofo.common.kit.counter;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.amituofo.common.util.FormatUtils;
import com.amituofo.common.util.StringUtils;

public class CountSizeStatistic implements Serializable {
	private final AtomicLong sumsize = new AtomicLong(0);
	private final AtomicInteger sumcount = new AtomicInteger(0);

	private long snapshotTime;
	private CountSizeStatistic snapshot = null;
//	private String sizeTitle="size";
	private String countTitle = " Count(s) ";

	public CountSizeStatistic() {
	}

	public CountSizeStatistic(String countTitle) {
		this.countTitle = " " + countTitle + " ";
//		this.sizeTitle=sizeTitle;
	}

	public CountSizeStatistic(CountSizeStatistic summary) {
		this.sumsize.set(summary.getTotalSize());
		this.sumcount.set(summary.getTotalCount());
	}

	public void resetTo(CountSizeStatistic summary) {
		reset();
		sum(summary);
	}

	public void reset() {
		sumsize.set(0);
		sumcount.set(0);
	}

	public void sum(File file) {
		if (file == null) {
			return;
		}

		if (file.isFile()) {
			sumsize.addAndGet(file.length());
			sumcount.incrementAndGet();
		}
	}

	public void sum(CountSizeStatistic summary) {
		sumsize.addAndGet(summary.getTotalSize());
		sumcount.addAndGet(summary.getTotalCount());
	}

	public void sumCount(int count) {
		sumcount.addAndGet(count);
	}

	public void sumSize(long filesize) {
		sumsize.addAndGet(filesize);
	}

	public void subtract(File file) {
		if (file.isFile()) {
			sumsize.addAndGet(file.length() * -1);
			sumcount.decrementAndGet();
		}
	}

	public long getTotalSize() {
		return sumsize.get();
	}

	public int getTotalCount() {
		return sumcount.get();
	}

	public void subtractCount(int count) {
		sumcount.addAndGet(count * -1);
	}

	public void subtract(CountSizeStatistic summary) {
		sumsize.addAndGet(summary.getTotalSize() * -1);
		sumcount.addAndGet(summary.getTotalCount() * -1);
	}

	public void subtractSize(int size) {
		this.sumsize.addAndGet(size * -1);
	}

	public long getSnapshotTime() {
		return snapshotTime;
	}

	public CountSizeStatistic snapshot() {
		snapshotTime = System.currentTimeMillis();
		snapshot = new CountSizeStatistic(this);
		return snapshot;
	}

	public CountSizeStatistic getSnapshotStatistics() {
		return snapshot;
	}

	public void print(PrintStream print, String head) {
		print.println(head + FormatUtils.formatNumber(sumcount.get()) + countTitle + FormatUtils.getPrintSize(sumsize.get(), true));
	}

	public String toStringCount() {
		return FormatUtils.formatNumber(sumcount.get()) + countTitle;
	}

	public String toStringCountAndSize(boolean showByteSize) {
		return toStringCount() + FormatUtils.getPrintSize(sumsize.get(), showByteSize);
	}

	public String toString() {
		return toStringCountAndSize(true);
	}

	public void sum(int count, int size) {
		sumcount.addAndGet(count);
		sumsize.addAndGet(size);
	}

	public void subtract(int count, int size) {
		sumcount.addAndGet(count * -1);
		sumsize.addAndGet(size * -1);
	}
}
