package com.amituofo.common.kit.value;

public class ValueRange<T> {
	private T from;
	private T to;

	public ValueRange() {
	}

	public ValueRange(T from, T to) {
		super();
		this.from = from;
		this.to = to;
	}

	public T getFrom() {
		return from;
	}

	public void setFrom(T from) {
		this.from = from;
	}

	public T getTo() {
		return to;
	}

	public void setTo(T to) {
		this.to = to;
	}
	
}
