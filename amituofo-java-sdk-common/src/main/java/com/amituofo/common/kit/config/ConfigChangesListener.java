package com.amituofo.common.kit.config;

public interface ConfigChangesListener<CONFIG> {// extends ProcessEvent<CONFIG> {
	public final static int SAVING_EVENT = 1;
	public final static int SAVED_EVENT = 1;
	public final static int DELETING_EVENT = 2;
	public final static int DELETED_EVENT = 2;
	public static final int ALL_CHANGED_EVENT = 0;

	boolean acceptChanges(int event, CONFIG config);
}
