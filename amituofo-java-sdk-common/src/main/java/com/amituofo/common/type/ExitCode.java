package com.amituofo.common.type;

public enum ExitCode {
//	UNKNOWN(99), START_FAIL(4), TERMINATE(3), KILLED(9), COMPLETE(2), COMPLETE_OK(0), COMPLETE_NG(1), COMPLETE_PART(5), COMPLETE_ALMOST(6), COMPLETE_ALMOST_OK(7), COMPLETE_ALMOST_NG(8),
//
//	FAILED_TO_CONNECT(43), ERROR_QUERY(44), DATA_NOT_FOUND(41), EXCEED_MAX_LIMIT(42);
//
//	public final int exitCode;
//
//	private ExitResult(int exitCode) {
//		this.exitCode = exitCode;
//	}
//
//	public static ExitResult valueOf(int exitCode) {
//		switch (exitCode) {
//		case 0:
//			return COMPLETE_OK;
//		case 1:
//			return COMPLETE_NG;
//		case 2:
//			return COMPLETE;
//		case 3:
//			return TERMINATE;
//		case 4:
//			return START_FAIL;
//		case 5:
//			return COMPLETE_PART;
//		case 6:
//			return COMPLETE_ALMOST;
//		case 7:
//			return COMPLETE_ALMOST_OK;
//		case 8:
//			return COMPLETE_ALMOST_NG;
//		case 99:
//			return UNKNOWN;
//		case 9003:
//			return FAILED_TO_CONNECT;
//		case 9004:
//			return ERROR_QUERY;
//		case 9001:
//			return DATA_NOT_FOUND;
//		case 9002:
//			return EXCEED_MAX_LIMIT;
//		}
//		return UNKNOWN;
//	}

	COMPLETE(0), OK(0), NG(1), INIT_FAIL(2), LAUNCHED(3);

	public final int code;

	private ExitCode(int exitCode) {
		this.code = exitCode;
	}

	public static ExitCode valueOf(int exitCode) {
		switch (exitCode) {
		case 0:
			return COMPLETE;
		case 1:
			return NG;
		case 2:
			return INIT_FAIL;
		case 3:
			return LAUNCHED;
		default:
			return NG;
		}
	}
}
