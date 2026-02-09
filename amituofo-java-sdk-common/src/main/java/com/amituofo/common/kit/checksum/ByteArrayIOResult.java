package com.amituofo.common.kit.checksum;

import java.io.IOException;

public class ByteArrayIOResult {
	private long sourceLength;
	private long targetLength;
	private Checksum sourceChecksum = null;
	private Checksum targetChecksum = null;
	private ChecksumGetter targetChecksumGetter = null;

	public static enum Result {
		Unknow, Equal, Diff
	}

	public ByteArrayIOResult(long sourceLength, Checksum sourceChecksum, long targetLength, ChecksumGetter targetChecksumGetter) {
		super();
		this.sourceLength = sourceLength;
		this.targetLength = targetLength;
		this.sourceChecksum = sourceChecksum;
		this.targetChecksumGetter = targetChecksumGetter;
	}

	public ByteArrayIOResult(long sourceLength, Checksum sourceChecksum, long targetLength, Checksum targetChecksum) {
		super();
		this.sourceLength = sourceLength;
		this.targetLength = targetLength;
		this.sourceChecksum = sourceChecksum;
		this.targetChecksum = targetChecksum;
	}

	public void setSourceLength(long sourceLength) {
		this.sourceLength = sourceLength;
	}

	public void setSourceChecksum(Checksum sourceChecksum) {
		this.sourceChecksum = sourceChecksum;
	}

	public long getSourceLength() {
		return sourceLength;
	}

	public long getTargetLength() {
		return targetLength;
	}

	public Checksum getSourceChecksum() {
		return sourceChecksum;
	}

	public Checksum getTargetChecksum() throws IOException {
		if (targetChecksum != null) {
			return targetChecksum;
		}

		if (targetChecksumGetter != null) {
			targetChecksum = targetChecksumGetter.getChecksum();
		}

		return targetChecksum;
	}

	public Result validateResult() throws IOException {
		if (sourceLength >= 0 && targetLength >= 0 && sourceLength != targetLength) {
			return Result.Diff;
		}

		Checksum srcchecksum = getSourceChecksum();
		Checksum tarchecksum = getTargetChecksum();

		if (srcchecksum == null && tarchecksum == null) {
			return Result.Equal;
		}

		if (srcchecksum != null && tarchecksum != null) {
			if (srcchecksum.getChecksumName() != tarchecksum.getChecksumName()) {
				return Result.Equal;
			}

			return Checksum.equals(srcchecksum, tarchecksum) ? Result.Equal : Result.Diff;
		}

		return Result.Unknow;
	}

	public Result validateResultByLength() throws IOException {
		if (sourceLength >= 0 && targetLength >= 0) {
			if (sourceLength == targetLength) {
				return Result.Equal;
			} else {
				return Result.Diff;
			}
		}

		return Result.Unknow;
	}

	@Override
	public String toString() {
		return "SourceLength=" + sourceLength + " TargetLength=" + targetLength + " SourceChecksum=" + sourceChecksum + " TargetChecksum=" + targetChecksum;
	}

}
