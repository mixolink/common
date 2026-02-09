package com.amituofo.common.util;

import java.io.File;

import com.amituofo.common.api.ObjectHandler;

public class FileEditWatcher2 {
	private Thread thread;
	// private List<File> fileList = new ArrayList<File>();
	private File file = null;
	private boolean continueCheck = true;
	private ObjectHandler<WatchEvent, File> eventHandler;

	private long lastSize;
	private long lastModifiedTime;

	public static enum WatchEvent {
		EDIT_FINISHED_WITH_MODIFIED
	}

	// public static FileEditWatcher instance = new FileEditWatcher();

	public FileEditWatcher2() {
	}

	public void watch(final File file, final ObjectHandler<WatchEvent, File> eventHandler) {
		this.file = file;
		this.eventHandler = eventHandler;

		if (file == null || eventHandler == null) {
			return;
		}

		lastSize = file.length();
		lastModifiedTime = file.lastModified();

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
				}

				while (continueCheck && file != null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
					}

					if (file.exists()) {
						if (!isFileEditing(file)) {
							eventHandler.handle(WatchEvent.EDIT_FINISHED_WITH_MODIFIED, file);
							continueCheck = false;
							System.out.println("exit " + file);
						}
					}

				}
			}
		});

		thread.start();
	}

	private boolean isFileEditing(File file) {

		// try{
		// file.renameTo(new File(file.getAbsolutePath()+".tmp"));
		//
		//// Files.move(Paths.get(file.getPath()),
		//// Paths.get(file.getPath()), StandardCopyOption.ATOMIC_MOVE);
		//
		// // DO YOUR STUFF HERE SINCE IT IS NOT BEING WRITTEN BY ANOTHER PROGRAM
		// return false;
		// } catch (Exception e){
		//
		// // DO NOT WRITE THEN SINCE THE FILE IS BEING WRITTEN BY ANOTHER PROGRAM
		//
		// return true;
		// }

		// FileLock lock = null;
		// try {
		// FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
		//// lock = channel.lock();
		// lock = channel.tryLock();
		// System.out.print("file is not locked");
		// return false;
		// } catch (OverlappingFileLockException e) {
		// System.out.print("file is locked");
		// return true;
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		// try {
		// lock.release();
		// } catch (IOException e) {
		// }
		// }
		//
		// try {
		// org.apache.commons.io.FileUtils.touch(file);
		// return true;
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		//// e.printStackTrace();
		// return false;
		// }

		// FileWriter fw = null;
		// try {
		// fw = new FileWriter(file);
		// } catch (IOException e) {
		// System.out.println("File is open");
		// return true;
		// } finally {
		// if (fw != null)
		// try {
		// fw.close();
		// } catch (IOException e) {
		// }
		// }

		return false;
	}

}
