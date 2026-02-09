package com.amituofo.common.api;

public interface HandleMountpoint<T> {
	T handle(String mountpoint) throws Exception;
}
