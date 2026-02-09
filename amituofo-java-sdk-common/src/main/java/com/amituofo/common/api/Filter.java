package com.amituofo.common.api;

public interface Filter<T> {
	boolean accept(T data);
}
