package com.amituofo.common.type;

public class Size {
	private double size;
	private SizeUnit unit;
	private long sizeInByte;

	public Size(double size, SizeUnit unit) {
		super();
		this.size = size;
		this.unit = unit;
		this.sizeInByte = (long) (size * unit.toBytes());
	}

	public Size(long sizeInByte) {
		super();
		this.unit = SizeUnit.kindOf(sizeInByte);
		this.size = unit.toUnitSize(sizeInByte);
		this.sizeInByte = sizeInByte;
	}

	protected Size(double size, long sizeInByte, SizeUnit unit) {
		super();
		this.size = size;
		this.sizeInByte = sizeInByte;
		this.unit = unit;
	}

	public long getSizeInByte() {
		return sizeInByte;
	}

	public double getSize() {
		return size;
	}

	public SizeUnit getUnit() {
		return unit;
	}

	@Override
	public String toString() {
		return size + "" + unit;
	}

	public int getSizeInMB() {
		if (unit == SizeUnit.MB) {
			return (int) size;
		}

		switch (unit) {
		case Bytes:
			return 0;
		case KB:
			return 0;
		case GB:
			return (int) (1024 * size);
		case TB:
			return (int) (1024 * 1024 * size);
		case PB:
			return (int) (1024 * 1024 * 1024 * size);
		}
		
		return 0;
	}

	public static Size parse(String unitsize) {
		return SizeUnit.parseToSize(unitsize);
	}

	public static Size sizeOfKBUnit(double size) {
		return new Size(size, SizeUnit.KB);
	}

	public static Size sizeOfMBUnit(double size) {
		return new Size(size, SizeUnit.MB);
	}

	public static Size sizeOfGBUnit(double size) {
		return new Size(size, SizeUnit.GB);
	}

}
