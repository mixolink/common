package com.amituofo.common.kit.datetime.accepter;

import java.time.DayOfWeek;

import com.amituofo.common.type.TimeUnit;

public class DatetimeAccepterOption {
	DayOfWeek[] dayOfWeeks;

	long datetimeOffset = 0;

	TimeUnit timeOffsetUnit = TimeUnit.MS;
	Long timeOffset;
	Long timeOffsetInMillisecond = null;
	Long fromMillisecond;
	Long toMillisecond;
	Integer fromDay;
	Integer toDay;

//	Integer offsetDays;

	public DayOfWeek[] getDayOfWeeks() {
		return dayOfWeeks;
	}

	public DatetimeAccepterOption setDayOfWeeks(DayOfWeek[] dayOfWeeks) {
		this.dayOfWeeks = dayOfWeeks;
		return this;
	}

	public Long getTimeOffsetInMillisecond() {
		if (timeOffsetInMillisecond == null) {
			this.timeOffsetInMillisecond = this.timeOffsetUnit.toMilliseconds(this.timeOffset);
		}

		return timeOffsetInMillisecond;
	}

	public Long getFromMillisecond() {
		return fromMillisecond;
	}

	public TimeUnit getTimeOffsetUnit() {
		return timeOffsetUnit;
	}

	public void setTimeOffsetUnit(TimeUnit timeOffsetUnit) {
		this.timeOffsetUnit = timeOffsetUnit;
	}

	public Long getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(Long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public void setTimeOffset(Long timeOffset, TimeUnit unit) {
		this.timeOffset = timeOffset;
		this.timeOffsetUnit = unit;
	}

	public DatetimeAccepterOption setFromMillisecond(Long fromMillisecond) {
		this.fromMillisecond = fromMillisecond;
		return this;
	}

	public Long getToMillisecond() {
		return toMillisecond;
	}

	public DatetimeAccepterOption setToMillisecond(Long toMillisecond) {
		this.toMillisecond = toMillisecond;
		return this;
	}

	public long getDatetimeOffset() {
		return datetimeOffset;
	}

	public void setDatetimeOffset(long datetimeOffset) {
		this.datetimeOffset = datetimeOffset;
	}

	public Integer getFromDay() {
		return fromDay;
	}

	public DatetimeAccepterOption setFromDay(Integer fromDay) {
		this.fromDay = fromDay;
		return this;
	}

	public Integer getToDay() {
		return toDay;
	}

	public DatetimeAccepterOption setToDay(Integer toDay) {
		this.toDay = toDay;
		return this;
	}

}