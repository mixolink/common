package com.amituofo.common.api;

public interface DataFilter <DATA>{
	boolean accept(DATA data);
}
