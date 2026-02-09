package com.amituofo.common.kit.datetime.accepter.impl;

import com.amituofo.common.ex.InvalidParameterException;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepter;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepterBase;
import com.amituofo.common.kit.datetime.accepter.DatetimeAccepters;
import com.amituofo.common.type.TimeUnit;

public class DatetimeExceedDurationAccepter extends DatetimeAccepterBase {
	protected final long timeOffsetMillisecond;
	protected final Long timeOffset;
	protected final TimeUnit timeOffsetUnit;
//	protected final CompareMethod method;

	public DatetimeExceedDurationAccepter(Long timeOffset, TimeUnit timeOffsetUnit) throws InvalidParameterException {
		super(DatetimeAccepters.IS_EXCEEDS_DURATION);

		if (timeOffset == null || timeOffsetUnit == null) {
			throw new InvalidParameterException("Offset datetime or Unit required!");
		}

//		if (method != CompareMethod.TIME_OFFSET_FROM_NOW && method != CompareMethod.BEFORE_TIME) {
//			throw new InvalidParameterException("Only support " + CompareMethod.TIME_OFFSET_FROM_NOW.name() + " or " + CompareMethod.BEFORE_TIME.name());
//		}

		this.timeOffsetMillisecond = timeOffsetUnit.toMilliseconds(timeOffset);
		this.timeOffset = timeOffset;
		this.timeOffsetUnit = timeOffsetUnit;
	}

	@Override
	protected boolean accept0(long millisecond) {
		return (System.currentTimeMillis() - millisecond) >= timeOffsetMillisecond;
	}

}
