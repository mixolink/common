package com.amituofo.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.amituofo.common.api.Callback;

public class FileEditWatcher3 {
	String cmd = "C:\\VDisk\\DriverE\\GIT-Local\\Mixolink\\utils\\isof.exe";

	byte[] orginalFileMd5;

	Timer timer;

	private long period = 1000 * 2;

	private long delay = 1000 * 7;

	public FileEditWatcher3(final File file, final Callback<File> modifiedCallback) {
		try {
			orginalFileMd5 = DigestUtils.calcMD5(file);
		} catch (IOException e) {
			e.printStackTrace();
			orginalFileMd5 = new byte[0];
		}

		timer = new Timer("File Edit Watch Timer");
		timer.schedule(new TimerTask() {
			boolean inedit = true;

			@Override
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec(cmd + " " + file.getAbsolutePath());
					process.waitFor();
					inedit = (process.exitValue() == 1);

				} catch (Exception e) {
					e.printStackTrace();
					inedit = false;
				} finally {
					if (!inedit) {
						timer.cancel();
						timer = null;

						byte[] md5New = new byte[0];
						try {
							md5New = DigestUtils.calcMD5(file);
						} catch (IOException e) {
							e.printStackTrace();
						}

						boolean equals = Arrays.equals(orginalFileMd5, md5New);

						if (!equals) {
							modifiedCallback.callback(file);
						}
					}
				}
			}
		}, delay, period);
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public static void main(String[] s) throws IOException {
		FileEditWatcher3 w = new FileEditWatcher3(new File("C:\\11\\apache-tomcat-7.0.78\\RUNNING.txt"), new Callback<File>() {

			@Override
			public void callback(File data) {

			}
		});
	}

}
