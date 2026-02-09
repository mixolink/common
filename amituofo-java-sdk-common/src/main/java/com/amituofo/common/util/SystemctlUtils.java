package com.amituofo.common.util;

import com.amituofo.common.util.CmdUtils.ExeResult;

public class SystemctlUtils {
	// active, reloading, inactive, failed, activating, deactivating
	public static int DEFAULT_WAIT_SECOND = 60 * 3;

	public static boolean isSystemdRunning(String serviceName) throws Exception {
//		systemctl is-active  oeos-ks01
		ExeResult result = CmdUtils.execShellCommand("systemctl is-active " + serviceName, 5, true);
//		if (result.isExitNot0()) {
//			throw new Exception("Command execute failed!" + result.getOutput());
//		}
		String output = result.getOutput();
		if (output.contains("inactive")) {
			return false;
		}

		return output.contains("active");
	}

	public static boolean stopSystemd(String serviceName) throws Exception {
		ExeResult result = CmdUtils.execShellCommand("systemctl --lines=0 stop " + serviceName, DEFAULT_WAIT_SECOND, true);
//		return result.isExit0() && (result.getOutput().length() == 0);
		return (result.getOutput().length() == 0);
	}

	public static boolean startSystemd(String serviceName) throws Exception {
		ExeResult result = CmdUtils.execShellCommand("systemctl --lines=0 start " + serviceName, DEFAULT_WAIT_SECOND, true);
//		return result.isExit0() && (result.getOutput().length() == 0);
		return (result.getOutput().length() == 0);
	}

	public static boolean restartSystemd(String serviceName) throws Exception {
		ExeResult result = CmdUtils.execShellCommand("systemctl --lines=0 restart " + serviceName, DEFAULT_WAIT_SECOND, true);
//		return result.isExit0() && (result.getOutput().length() == 0);
		return (result.getOutput().length() == 0);
	}

	public static int restartSystemd(String[] serviceNames) throws Exception {
		if (StringUtils.isEmpty(serviceNames)) {
			return 0;
		}

		int c = 0;
		for (String serviceName : serviceNames) {
			ExeResult result = CmdUtils.execShellCommand("systemctl --lines=0 restart " + serviceName, DEFAULT_WAIT_SECOND, true);
//			return result.isExit0() && (result.getOutput().length() == 0);
			if (result.getOutput().length() == 0) {
				c++;
			}
		}

		return c;
	}

//	public static void main(String[] args) throws Exception {
//		System.out.println(isSystemdRunning("oeos-bs-SS01"));
//		System.out.println(stopSystemd("oeos-bs-SS01"));
//		System.out.println(isSystemdRunning("oeos-bs-SS01"));
//		System.out.println(startSystemd("oeos-bs-SS01"));
//		System.out.println(isSystemdRunning("oeos-bs-SS01"));
//	}

}
