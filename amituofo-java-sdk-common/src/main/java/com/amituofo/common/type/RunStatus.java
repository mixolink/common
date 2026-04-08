package com.amituofo.common.type;

public enum RunStatus {
	Unknown(-1),
	//
	Pending(1),
	//
	Starting(2), Initializing(3), Restarting(4),
	//
	Running(10), // Pause(11), Idle(12),
	//
	Interrupting(20), Stopping(21),
	//
	Stoped(00);

	private int code;
//	private String aliasName = null;

	RunStatus(int code) {
		this.code = code;
	}

//	public String getAliasName() {
//		if (aliasName == null) {
//			return name();
//		}
//		return aliasName;
//	}
//
//	public void setAliasName(String aliasName) {
//		this.aliasName = aliasName;
//	}
//
//	public void resetAliasName() {
//		this.aliasName = null;
//	}

	public boolean isRunningStatus() {
		return isRunningStatus(this);
	}

	public boolean isStoppingStatus() {
		return isStoppingStatus(this);
	}

	public boolean isStopStatus() {
		return isStopStatus(this);
	}

	public static boolean isRunningStatus(RunStatus status) {
		return (status.code >= 10 && status.code <= 19) || (status.code == 2 || status.code == 3);
	}

	public static boolean isStoppingStatus(RunStatus status) {
		return status.code >= 20 && status.code < 29;
	}

	public static boolean isStopStatus(RunStatus status) {
		return status.code == 00;
	}

}
