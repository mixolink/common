package com.amituofo.common.util;

import javax.swing.JProgressBar;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.define.Constants;
import com.amituofo.common.type.ReadProgressEvent;

public class ProgressUtils {

	public static void updateReadingStepProgress(StepProgressListener progressListener, long step) {
		long len = step;
		if (len > Constants.SIZE_100MB) {
			int maxstep = Constants.SIZE_100MB;
			long remain = len;
			do {
				progressListener.progressChanged(ReadProgressEvent.BYTE_READING_EVENT, maxstep);
				remain -= maxstep;
				maxstep = (int) Math.min(remain, Constants.SIZE_100MB);
			} while (remain > 0);
		} else {
			progressListener.progressChanged(ReadProgressEvent.BYTE_READING_EVENT, (int) step);
		}
	}

	public static void updateEndStepProgress(StepProgressListener progressListener, long step) {
		long len = step;
		if (len > Constants.SIZE_100MB) {
			int maxstep = Constants.SIZE_100MB;
			long remain = len;
			do {
				progressListener.progressChanged(ReadProgressEvent.BYTE_READ_END_EVENT, maxstep);
				remain -= maxstep;
				maxstep = (int) Math.min(remain, Constants.SIZE_100MB);
			} while (remain > 0);
		} else {
			progressListener.progressChanged(ReadProgressEvent.BYTE_READ_END_EVENT, (int) step);
		}
	}

	public static void updateStepProgressBar(JProgressBar progressBar, long max, long step) {
		// 2. 计算百分比值 (0-100)
		int value;

		if (max <= 0) {
			return;
		} else if (step >= max) {
			// 如果当前步数已经超过或等于总量，强制设为100
			value = 100;
		} else if (step <= 0) {
			return;
		} else {
			// 正常计算比例：(当前 / 总量) * 100
			// 使用 double 计算以保证精度，最后四舍五入或强转
			value = (int) ((double) step / max * 100.0);

			// 再次确保不会因浮点误差超过100 (虽然上面有判断，但双重保险)
			if (value > 100)
				value = 100;
			if (value < 0)
				return;
		}

		// 3. 设置进度值
		progressBar.setValue(value);
	}

	public static void resetProgressBar(JProgressBar progressBar) {
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0); // 显式设置初始值
	}

	public static void finishProgressBar(JProgressBar progressBar) {
		progressBar.setValue(100); // 显式设置初始值
	}
}
