package com.amituofo.common.type;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

import com.amituofo.common.define.Constants;
import com.amituofo.common.util.StringUtils;

public enum SizeUnit {
	Bytes(1), KB(1024L), MB(1024L * KB.toBytes()), GB(1024L * MB.toBytes()), TB(1024L * GB.toBytes()), PB(1024L * TB.toBytes());

	private long bytes;
	private final static DecimalFormat FLOAT_FORMAT_WITH_COMMA_2 = new DecimalFormat(",###,###.00");
	private final static DecimalFormat INT_FORMAT_WITH_COMMA = new DecimalFormat(",###,###");

	SizeUnit(long bytes) {
		this.bytes = bytes;
	}

	public long toBytes() {
		return bytes;
	}

	public long toBytes(float quota) {
		return (long) (toBytes() * quota);
	}

	public float toUnitSize(long byteSize) {
		float a = ((float) (byteSize / ((double) this.bytes)));
		return (float) (Math.round(a * 100)) / 100;
	}

	public static String toReadable(long byteSize, boolean withOrginalSize) {
		if (byteSize <= 0) {
			return "0 Byte";
		}

		byteSize = Math.abs(byteSize);

		StringBuilder bytes = new StringBuilder();

		SizeUnit unit = SizeUnit.kindOf(byteSize);
		float unitsize = unit.toUnitSize(byteSize);
		bytes.append(FLOAT_FORMAT_WITH_COMMA_2.format(unitsize)).append(" ").append(unit.name());
		if (withOrginalSize && unit != SizeUnit.Bytes) {
			bytes.append(" ( " + INT_FORMAT_WITH_COMMA.format(byteSize)).append(" bytes ) ");
		}

		return bytes.toString();
	}

	public static Size toSize(long byteSize) {
		return new Size(byteSize);
	}

	public static SizeUnit kindOf(long byteSize) {
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
	
	public static Size parseToSize(String unitsize, final long defaultByteSize) throws NumberFormatException {
		if (StringUtils.isEmpty(unitsize)) {
			return new Size(defaultByteSize);
		}

		Size size = parseToSize(unitsize);
		if (size == null) {
			return new Size(defaultByteSize);
		}
		return size;
	}

	public static Size parseToSize(String unitsize) throws NumberFormatException {
		if (StringUtils.isEmpty(unitsize)) {
			return null;
		}

		unitsize = unitsize.trim().toUpperCase();

		String value = unitsize;
		SizeUnit unit = SizeUnit.Bytes;

		int unitStartIndex = unitsize.lastIndexOf("M");
		if (unitStartIndex != -1) {
			unit = MB;
		} else {
			unitStartIndex = unitsize.lastIndexOf("G");
			if (unitStartIndex != -1) {
				unit = GB;
			} else {
				unitStartIndex = unitsize.lastIndexOf("K");
				if (unitStartIndex != -1) {
					unit = KB;
				} else {
					unitStartIndex = unitsize.lastIndexOf("B");
					int tindex = unitsize.lastIndexOf("T");
					int pindex = unitsize.lastIndexOf("P");
					if (unitStartIndex != -1 && pindex == -1 && (tindex == -1 || tindex > unitStartIndex)) {
						value = unitsize.substring(0, unitStartIndex);
						unit = Bytes;
					} else {
						int bindex = unitStartIndex;
						unitStartIndex = tindex;
						if (tindex != -1 && (bindex == -1 || bindex > tindex)) {
							unit = TB;
						} else {
							unitStartIndex = unitsize.lastIndexOf("P");
							if (unitStartIndex != -1) {
								unit = PB;
							} else {
								unit = SizeUnit.Bytes;
							}
						}
					}
				}
			}
		}

		if (unitStartIndex > 0) {
			value = unitsize.substring(0, unitStartIndex).trim();
		}

		if (value.indexOf('.') == -1) {
			long longvalue = Long.valueOf(value);
			return new Size(longvalue, longvalue * unit.bytes, unit);
		} else {
			double dvalue = Double.valueOf(value);
			return new Size(dvalue, (long) (dvalue * unit.bytes), unit);
		}
	}

	public static long parse(String unitsize, final long defaultValue) throws NumberFormatException {
		if (StringUtils.isEmpty(unitsize)) {
			return defaultValue;
		}

		Size size = parseToSize(unitsize);
		if (size == null) {
			return defaultValue;
		}
		return size.getSizeInByte();
	}

//	public static void main(String[] r) {
//		System.out.println(parse("1PB", 1) == Constants.SIZE_1TB * 1024);
//		System.out.println(parse("10 P", 1) == Constants.SIZE_1TB * 1024 * 10);
//		System.out.println(parse("1TB", 1) == Constants.SIZE_1TB);
//		System.out.println(parse("1 T", 1) == Constants.SIZE_1TB);
//		System.out.println(parse("10B", 1) == 10);
//		System.out.println(parse("10byte", 1) == 10);
//		System.out.println(parse("1MB", 1) == Constants.SIZE_1MB);
//		System.out.println(parse("1GB", 1) == Constants.SIZE_1GB);
//		System.out.println(parse("1 MB", 1) == Constants.SIZE_1MB);
//		System.out.println(parse("1 kB", 1) == Constants.SIZE_1KB);
//
//		long a = (long) (GB.bytes * 1.5678f);
//		System.out.println(SizeUnit.kindOf(a).toUnitSize(a));
//		a = (long) (TB.bytes * 1.5678f);
//		System.out.println(SizeUnit.kindOf(a).toUnitSize(a));
//	}
}
