package com.amituofo.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.api.Callback;

public class ProcessUtils {
	private static final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private static Logger logger = null;

	public static final String getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//		System.out.println(runtimeMXBean.getName());
		return runtimeMXBean.getName().split("@")[0];
	}

	public static Process startInJVM(Class<? extends Object> clazz, String[] jvmparams, String[] params, Callback<Integer> stopcallback) throws IOException {
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String encoding = System.getProperty("file.encoding");// file.encoding=UTF-8
		encoding = "-Dfile.encoding=" + (StringUtils.isEmpty(encoding) ? "utf-8" : encoding);
		String path = System.getProperty("java.home") + separator + "bin" + separator + "java";

		List<String> command = new ArrayList<String>();
		command.add(path);
		command.add("-cp");
		command.add(classpath);
		command.add(encoding);
		if (jvmparams != null) {
			for (String jvmparam : jvmparams) {
				command.add(jvmparam);
			}
		}
		command.add(clazz.getCanonicalName());
		for (String param : params) {
			command.add(param);
		}

//		System.out.println("Cmd " + command.toString());

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();

		if (stopcallback != null) {
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// System.out.println("End " + file.getAbsolutePath());
				stopcallback.callback(process.exitValue());
			}
		}

		// System.out.println("Fin");
		return process;
	}

	public static Process startInProcess(String command, Callback<Integer> callback, String... params) throws IOException {
		return startInProcess(command, null, callback, params);
	}
	
	public static Process startInProcess(String command, File workdirectory, Callback<Integer> callback, String... params) throws IOException {
		List<String> commands = new ArrayList<String>();
		commands.add(command);
//		commands.add("\"" + command + "\"");
		for (String param : params) {
			commands.add(param);
		}

		ProcessBuilder processBuilder = new ProcessBuilder(commands.toArray(new String[commands.size()]));
		if (workdirectory != null) {
			processBuilder.directory(workdirectory);
		}
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();

		if (callback != null) {
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// System.out.println("End " + file.getAbsolutePath());
				callback.callback(process.exitValue());
			}
		}

		// System.out.println("Fin");
		return process;
	}

	private final static long jvmStartTime = System.currentTimeMillis();
//	private static long actionStartTime = System.currentTimeMillis();

	public static void readyTimeUsed() {
//		actionStartTime = System.currentTimeMillis();
		startTime.set(System.currentTimeMillis());
	}

	public static void calcTimeUsed(String msg) {
		long actionStartTime = startTime.get().longValue();
		long now = System.currentTimeMillis();
		msg = msg + " " + (now - actionStartTime) + "/" + (now - jvmStartTime);
		if (logger != null) {
			logger.info(msg);
		} else {
			System.out.println(msg);
		}
//		actionStartTime = now;
		startTime.set(now);
	}

	public static void setLogger(Logger logger) {
		ProcessUtils.logger = logger;
	}
}