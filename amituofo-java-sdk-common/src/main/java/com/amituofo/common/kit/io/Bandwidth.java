package com.amituofo.common.kit.io;

public class Bandwidth {

	/* KB */
	private final static long KB = 1024l;

	/* The smallest count chunk length in bytes */
	private final static long CHUNK_LENGTH = 1024l;

	/* How many bytes will be sent or receive */
	private int bytesWillBeSentOrReceive = 0;

	/* When the last piece was sent or receive */
	private long lastPieceSentOrReceiveTick = System.nanoTime();

	/* Default rate is 1024KB/s */
	private int maxBandwidth;// = 1024;

	/* Time cost for sending CHUNK_LENGTH bytes in nanoseconds */
	private long timeCostPerChunk;// = (1000000000l * CHUNK_LENGTH) / (this.maxRate * KB);

	private boolean enabled = true;

	/**
	 * Initialize a BandwidthLimiter object with a certain rate.
	 * 
	 * @param maxRateInKB the download or upload speed in KBytes
	 */
	public Bandwidth(int maxRateInKB) {
		this.setMaxBandwidth(maxRateInKB);
	}

	public boolean isEnabled() {
		return enabled && maxBandwidth > 0;
	}

	public void setEnable(boolean enable) {
		this.enabled = enable;
	}

	/**
	 * Set the max upload or download rate in KB/s. maxBandwidth must be grater than 0. If maxRate is zero, it means there is no bandwidth limit.
	 * 
	 * @param  maxBandwidth         If maxBandwidth is zero, it means there is no bandwidth limit.
	 * @throws IllegalArgumentException
	 */
	public Bandwidth setMaxBandwidth(int maxBandwidth) throws IllegalArgumentException {
		synchronized (this) {
//			if (maxRate < 0) {
//				throw new IllegalArgumentException("maxRate can not less than 0");
//			}
			this.maxBandwidth = maxBandwidth < 0 ? 0 : maxBandwidth;
			if (this.maxBandwidth == 0)
				this.timeCostPerChunk = 0;
			else
				this.timeCostPerChunk = (1000000000l * CHUNK_LENGTH) / (this.maxBandwidth * KB);
		}
		return this;
	}

	public int getMaxBandwidth() {
		return this.maxBandwidth;
	}

	/**
	 * Next 1 byte should do bandwidth limit.
	 */
	public void limitNextBytes() {
		this.limitNextBytes(1);
	}

	/**
	 * Next len bytes should do bandwidth limit
	 * 
	 * @param len
	 */
	public void limitNextBytes(int len) {
		if (!isEnabled()) {
			return;
		}

		synchronized (this) {
			this.bytesWillBeSentOrReceive += len;

			/* We have sent CHUNK_LENGTH bytes */
			while (this.bytesWillBeSentOrReceive > CHUNK_LENGTH) {
				long nowTick = System.nanoTime();
				long missedTime = this.timeCostPerChunk - (nowTick - this.lastPieceSentOrReceiveTick);
				if (missedTime > 0) {
					try {
						Thread.sleep(missedTime / 1000000, (int) (missedTime % 1000000));
					} catch (Exception e) {
					}
				}
				this.bytesWillBeSentOrReceive -= CHUNK_LENGTH;
				this.lastPieceSentOrReceiveTick = nowTick + (missedTime > 0 ? missedTime : 0);
			}
		}
	}

}
