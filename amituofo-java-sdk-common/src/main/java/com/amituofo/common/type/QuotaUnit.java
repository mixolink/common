package com.amituofo.common.type;

import java.text.DecimalFormat;

public enum QuotaUnit {
	Bytes(1), KB(1024L), MB(1024L * KB.toBytes()), GB(1024L * MB.toBytes()), TB(1024L * GB.toBytes()), PB(1024L * TB.toBytes());

	private long bytes;
	
	private final static DecimalFormat FLOAT_FORMAT_WITH_COMMA_2 = new DecimalFormat(",###,###.00");
	private final static DecimalFormat INT_FORMAT_WITH_COMMA = new DecimalFormat(",###,###");

	QuotaUnit(long bytes) {
		this.bytes = bytes;
	}

	public long toBytes() {
		return bytes;
	}

	public long toBytes(float quota) {
		return (long) (toBytes() * quota);
	}

	public float toQuota(long byteSize) {
		float a = ((float) (byteSize / ((double) this.bytes)));
		return (float) (Math.round(a * 100)) / 100f;
	}
	
//	public static void main(String[] a) {
//		System.out.println(QuotaUnit.GB.toQuota(45874584523378569L));
//	}

	public static String toReadable(long byteSize, boolean withOrginalSize) {
		if (byteSize <= 0) {
			return "0 Byte";
		}
		
		byteSize = Math.abs(byteSize);

		StringBuilder bytes = new StringBuilder();

		SizeUnit unit = SizeUnit.kindOf(byteSize);
		float unitsize = unit.toUnitSize(byteSize);
		bytes.append(FLOAT_FORMAT_WITH_COMMA_2.format(unitsize)).append(" ").append(unit.name());
		if (withOrginalSize && unit!= SizeUnit.Bytes) {
			bytes.append(" ( " + INT_FORMAT_WITH_COMMA.format(byteSize)).append(" bytes ) ");
		}
		
		return bytes.toString();
	}
	
	public static QuotaUnit kindOf(long byteSize) {
		if (byteSize < KB.toBytes()) {
			return Bytes;
		}

		if (byteSize < MB.toBytes()) {
			return KB;
		}

		if (byteSize < GB.toBytes()) {
			return MB;
		}

		if (byteSize < TB.toBytes()) {
			return GB;
		}

		if (byteSize < PB.toBytes()) {
			return TB;
		}

		return PB;
	}
}
