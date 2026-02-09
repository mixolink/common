package com.amituofo.common.kit.file;

import java.io.Closeable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.amituofo.common.ex.IDConflictException;
import com.amituofo.common.kit.retry.RetryHelper;
import com.amituofo.common.kit.retry.ReturnValue;
import com.amituofo.common.kit.retry.ReturnableTry;
import com.amituofo.common.util.DateUtils;

public class DirMaker implements Closeable {
//	private static DirMaker instance = new DirMaker(Constants.TIME_MILLISECONDS_3_MIN);
//
//	static {
//		defaultMaker();
//	}

	private long lastAccessTime = 0;
	private Set<String> dirExistionCache = new HashSet<>();
//	private LifecycleMap<String, String> dirExistionCache;

	public DirMaker() {
		this(-1);
	}

	public DirMaker(long maxCacheTime) {
//		dirExistionCache = new LifecycleMap<>(Constants.TIME_MILLISECONDS_1_MIN, Constants.TIME_MILLISECONDS_15_SECOND);
		if (maxCacheTime > 300) {
			try {
				RetryHelper.asyncRetryForever("clear-dir-existion-cache-" + this, new ReturnableTry<Void>() {

					@Override
					protected ReturnValue<Void> execute(int retryTimes, long time) {
						if (DateUtils.passMoreThan(lastAccessTime, maxCacheTime)) {
							dirExistionCache.clear();
						}
						return null;
					}
				}, maxCacheTime);
			} catch (IDConflictException e) {
				e.printStackTrace();
			}
		}
	}

//	public static DirMaker defaultMaker() {
//		if (instance == null) {
//			instance = new DirMaker(-1);
//		}
//		return instance;
//	}

	public static DirMaker newMaker() {
		return new DirMaker(-1);
	}

	public static DirMaker newMaker(long maxCacheTime) {
		return new DirMaker(maxCacheTime);
	}

	public void close() {
		RetryHelper.stopRetry("clear-dir-existion-cache-" + this);
	}

	public boolean mkdirIfNotExist(String dir) {
		if (!isExist(dir)) {
			boolean mkok = new File(dir).mkdirs();
			if (mkok) {
				dirExistionCache.add(dir);
			}

			return mkok;
		}

		return true;
	}

	public boolean mkdirIfNotExist(File dir) {
		if (!isExist(dir)) {
			boolean mkok = dir.mkdirs();
			if (mkok) {
				dirExistionCache.add(dir.getPath());
			}

			return mkok;
		}

		return true;
	}

	public boolean isExist(File file) {
		lastAccessTime = System.currentTimeMillis();

		String path = file.getPath();
		boolean exist = dirExistionCache.contains(path);
		if (exist) {
			return exist;
		}

		exist = file.exists();
		if (exist) {
			dirExistionCache.add(path);
		}

		return exist;
	}

	public boolean isExist(String filepath) {
		lastAccessTime = System.currentTimeMillis();

		boolean exist = dirExistionCache.contains(filepath);
		if (exist) {
			return exist;
		}

		exist = new File(filepath).exists();
		if (exist) {
			dirExistionCache.add(filepath);
		}

		return exist;
	}

}
