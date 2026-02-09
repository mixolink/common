package com.amituofo.common.type;

import com.amituofo.common.define.Constants;

public enum SizeSeries {
    LESS_THAN_1K("~1KB", -1L, 1024L),
    LESS_THAN_1MB("~1MB", 1024L, Constants.SIZE_1MB),
    LESS_THAN_10MB("~10MB", Constants.SIZE_1MB, 10 *Constants.SIZE_1MB),
    LESS_THAN_50MB("~50MB", 10 * Constants.SIZE_1MB, 50 * Constants.SIZE_1MB),
    LESS_THAN_100MB("~100MB", 50 * Constants.SIZE_1MB, 100 * Constants.SIZE_1MB),
    LESS_THAN_500MB("~500MB", 100 * Constants.SIZE_1MB, 500 * Constants.SIZE_1MB),
    LESS_THAN_1GB("~1GB", 500 * Constants.SIZE_1MB, 1024 * Constants.SIZE_1MB),
    LESS_THAN_5GB("~5GB", 1024 * Constants.SIZE_1MB, 5L * 1024 * Constants.SIZE_1MB),
    GREATER_THAN_5GB(">5GB", 5L * 1024 * Constants.SIZE_1MB, Long.MAX_VALUE),
	UNKNOWN("Unknown", 0L, 0L);

    private final String label;
    private final long minSize;
    private final long maxSize;

    SizeSeries(String label, long minSize, long maxSize) {
        this.label = label;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public String getLabel() {
    	
        return label;
    }

    public long getMinSize() {
        return minSize;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public boolean inRange(long size) {
        return size > minSize && size <= maxSize;
    }

	public static SizeSeries ofSizeSeries(long size) {
		for (SizeSeries group : values()) {
			if (group.inRange(size)) {
				return group;
			}
		}
		return LESS_THAN_1K; // Should never happen unless size is negative
	}

	public static SizeSeries ofSizeSeries(Long size) {
		if (size == null) {
			return UNKNOWN;
		}
		for (SizeSeries group : values()) {
			if (group.inRange(size)) {
				return group;
			}
		}
		return UNKNOWN; // Should never happen unless size is negative
	}
}
