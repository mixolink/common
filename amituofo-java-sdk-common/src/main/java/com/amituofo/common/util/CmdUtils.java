package com.amituofo.common.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.amituofo.common.define.Constants;
import com.amituofo.common.kit.thread.ProcessControl;
import com.amituofo.common.kit.thread.ProcessControlHandler;

public class CmdUtils {
	public static int DEFAULT_WAITFOR_SECOND = 15;
	public static boolean sudo = true;
	public static String shellbin = "/bin/sh";

	public static void execShellCommandAsSuper(boolean enabled) {
		CmdUtils.sudo = enabled;
	}

	public static class ExeResult {
		private int exit = 0;
		private String output = null;
		private List<String> outputLines = null;

		public boolean isExit0() {
			return exit == 0;
		}

		public boolean isExitNot0() {
			return exit != 0;
		}

		public int getExit() {
			return exit;
		}

		public String getOutput() {
			if (output != null) {
				return output;
			}

			if (outputLines != null) {
				StringBuilder o = new StringBuilder();
				for (String line : outputLines) {
					o.append(line).append("\n");
				}
				output = o.toString();
			} else {
				output = "";
			}

			return output;
		}

		public List<String> getOutputLines() {
			if (outputLines != null) {
				return outputLines;
			}

			outputLines = new ArrayList<>();

			if (output != null) {
				String[] lines;
				if (output.indexOf("\r\n") != -1) {
					lines = output.split("\r\n");
				} else {
					lines = output.split("\n");
				}

				for (String line : lines) {
					outputLines.add(line);
				}
			}

			return outputLines;
		}
	}

	public static ExeResult exec(boolean outputAsList, boolean withErrorOutput, int waitForSecond, ProcessControl psctl, String... cmdarray) throws Exception {
		StringBuilder output = null;
		List<String> outputLines = null;

		Process process = null;
		BufferedReader bufIn = null;
		BufferedReader bufError = null;

		ExeResult exeresult = new ExeResult();

		try {
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(cmdarray);
//			ProcessBuilder psb = new ProcessBuilder(cmdarray);
//			process = psb.start();

			if (psctl != null) {
				final Process ps = process;
				psctl.setHandler(new ProcessControlHandler() {

					@Override
					public void interrupted() {
						ps.destroy();
					}

					@Override
					public boolean isAlive() {
						return ps.isAlive();
					}

					@Override
					public int getProcessId() {
						return SystemUtils.getProcessID(ps);
					}
				});
			}

			// 方法阻塞, 等待命令执行完成（成功会返回0）
			try {
				if (waitForSecond > 0) {
					process.waitFor(waitForSecond, TimeUnit.SECONDS);
				} else {
					process.waitFor();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				process = null;
			}

		} finally {
			if (process == null) {
				exeresult.exit = -1;
				return exeresult;
			}

			if (process.isAlive() && waitForSecond > 0) {
				int maxtimes = waitForSecond * Constants.TIME_MILLISECONDS_1_SECOND;
				do {
					Thread.sleep(100);
					maxtimes -= 100;
				} while (process.isAlive() && maxtimes > 0);

				if (maxtimes <= 0) {
					process.destroy();
//					process.destroyForcibly();
					throw new TimeoutException("Command " + StringUtils.toString(cmdarray, ' ') + " execute timeount!");
				}
			}

			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
			bufIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
			if (withErrorOutput) {
				bufError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
			}

			if (outputAsList) {
				outputLines = new ArrayList<>();
			} else {
				output = new StringBuilder();
			}

			// 读取输出
			String line = null;
			while ((line = bufIn.readLine()) != null) {
				if (outputAsList) {
					outputLines.add(line);
				} else {
					output.append(line).append('\n');
				}
			}
			if (withErrorOutput) {
				while ((line = bufError.readLine()) != null) {
					if (outputAsList) {
						outputLines.add(line);
					} else {
						output.append(line).append('\n');
					}
				}
			}

			exeresult.outputLines = outputLines;
			if (output != null) {
				exeresult.output = output.toString();
			}

			try {
				exeresult.exit = process.exitValue();
			} catch (java.lang.IllegalThreadStateException e) {
				e.printStackTrace();
				process.destroy();
			} finally {
				closeStream(bufIn);
				if (withErrorOutput) {
					closeStream(bufError);
				}
			}
		}

		// 返回执行结果
		return exeresult;
	}

//	public static String exec(boolean withErrorOutput, String... cmdarray) throws Exception {
//		StringBuilder result = new StringBuilder();
//
//		Process process = null;
//		BufferedReader bufIn = null;
//		BufferedReader bufError = null;
//
//		try {
////			System.out.println(Arrays.toString(cmdarray));
//			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
//			process = Runtime.getRuntime().exec(cmdarray);
//
//			// 方法阻塞, 等待命令执行完成（成功会返回0）
//			process.waitFor();
//
//			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
//			bufIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//			if (withErrorOutput) {
//				bufError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
//			}
//			// 读取输出
//			String line = null;
//			while ((line = bufIn.readLine()) != null) {
//				result.append(line).append('\n');
//			}
//			if (withErrorOutput) {
//				while ((line = bufError.readLine()) != null) {
//					result.append(line).append('\n');
//				}
//			}
//		} finally {
//			closeStream(bufIn);
//			if (withErrorOutput) {
//				closeStream(bufError);
//			}
//
////			System.out.println(result);
//		}
//
//		// 返回执行结果
//		return result.toString();
//	}

//	public static List<String> execOutputAsList(boolean withErrorOutput, String... cmdarray) throws Exception {
//		List<String> result = new ArrayList<>();
//
//		Process process = null;
//		BufferedReader bufIn = null;
//		BufferedReader bufError = null;
//
//		try {
////			System.out.println(Arrays.toString(cmdarray));
//			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
//			process = Runtime.getRuntime().exec(cmdarray);
//
//			// 方法阻塞, 等待命令执行完成（成功会返回0）
//			process.waitFor();
//
//			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
//			bufIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//			if (withErrorOutput) {
//				bufError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
//			}
//
//			// 读取输出
//			String line = null;
//			while ((line = bufIn.readLine()) != null) {
//				result.add(line);
//			}
//			if (withErrorOutput) {
//				while ((line = bufError.readLine()) != null) {
//					result.add(line);
//				}
//			}
//
//		} finally {
//			closeStream(bufIn);
//			if (withErrorOutput) {
//				closeStream(bufError);
//			}
//
////			System.out.println(result);
//
//		}
//
//		// 返回执行结果
//		return result;
//	}

	public static ExeResult execShellCommand(String cmd, boolean withErrorOutput) throws Exception {
		return execShellCommand(cmd, DEFAULT_WAITFOR_SECOND, withErrorOutput);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd, boolean withErrorOutput) throws Exception {
		return execShellCommandOutputAsList(cmd, DEFAULT_WAITFOR_SECOND, withErrorOutput);
	}

	public static ExeResult execShellCommand(String cmd) throws Exception {
		return execShellCommand(cmd, DEFAULT_WAITFOR_SECOND);
	}

	public static ExeResult execShellCommand(String cmd, ProcessControl psctl) throws Exception {
		return execShellCommand(cmd, DEFAULT_WAITFOR_SECOND, psctl);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd) throws Exception {
		return execShellCommandOutputAsList(cmd, null);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd, ProcessControl psctl) throws Exception {
		return execShellCommandOutputAsList(cmd, DEFAULT_WAITFOR_SECOND, psctl);
	}

//

	public static ExeResult execShellCommand(String cmd, int waitSecond, boolean withErrorOutput) throws Exception {
		if (sudo) {
			cmd = "sudo " + cmd;
		}

		String[] cmdarray = new String[] { shellbin, "-c", cmd };

		return exec(false, withErrorOutput, waitSecond, null, cmdarray);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd, int waitSecond, boolean withErrorOutput) throws Exception {
		if (sudo) {
			cmd = "sudo " + cmd;
		}

		String[] cmdarray = new String[] { shellbin, "-c", cmd };

		return exec(true, withErrorOutput, waitSecond, null, cmdarray);
	}

	public static ExeResult execShellCommand(String cmd, int waitSecond) throws Exception {
		if (sudo) {
			cmd = "sudo " + cmd;
		}

		String[] cmdarray = new String[] { shellbin, "-c", cmd };

		return exec(false, false, waitSecond, null, cmdarray);
	}

	public static ExeResult execShellCommand(String cmd, int waitSecond, ProcessControl psctl) throws Exception {
		if (sudo) {
			cmd = "sudo " + cmd;
		}

		String[] cmdarray = new String[] { shellbin, "-c", cmd };

		return exec(false, false, waitSecond, psctl, cmdarray);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd, int waitSecond) throws Exception {
		return execShellCommandOutputAsList(cmd, waitSecond, null);
	}

	public static ExeResult execShellCommandOutputAsList(String cmd, int waitSecond, ProcessControl psctl) throws Exception {
		if (sudo) {
			cmd = "sudo " + cmd;
		}

		String[] cmdarray = new String[] { shellbin, "-c", cmd };

		return exec(true, false, waitSecond, psctl, cmdarray);
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				// nothing
			}
		}
	}

	/**
	 * 命令行工具 路径 配置环境变量 eg： ln -s /opt/MegaRAID/storcli/storcli64 /bin/storcli ln -s /opt/MegaRAID/storcli/storcli64 /sbin/storcli
	 * 
	 * @return
	 */
	public static String getCmdPath() {
		if (SystemUtils.isWindows()) {
			return "";
		} else if (SystemUtils.isLinux() || SystemUtils.isMacOS()) {
			return "/usr/sbin/";
		}
		return "";
	}

//    public static void main(String[] args) throws Exception {
//        String which_smartctl = execCmd("which smartctl", null);
//        execCmd("/usr/local/Cellar/smartmontools/7.2/bin/smartctl -H -j /dev/disk1", null);
//    }
}
