package com.amituofo.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.amituofo.common.api.Callback;

public class FileEditWatcher4 {
	private static class FileModifiedCallback {
		private Timer timer = null;
		private Callback<File> modifyCallback;

		public FileModifiedCallback(final Callback<File> modifyCallback) {
			this.modifyCallback = modifyCallback;
		}

		protected synchronized void callback(File file) {
			if (modifyCallback == null) {
				return;
			}

			cancel();
			timer = new Timer("File-Modified-Callback");
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					cancel();
					modifyCallback.callback(file);
				}
			}, 3000);
		}

		protected synchronized void cancel() {
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((modifyCallback == null) ? 0 : modifyCallback.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileModifiedCallback other = (FileModifiedCallback) obj;
			if (modifyCallback == null) {
				if (other.modifyCallback != null)
					return false;
			} else if (!modifyCallback.equals(other.modifyCallback))
				return false;
			return true;
		}

	}

	private static class WatchPoint {
		final Integer WID;
		final File watchFile;
		final FileEditWatcher4 watcher;
		final List<FileModifiedCallback> callbacks = new ArrayList<FileEditWatcher4.FileModifiedCallback>();

		long lastSize;
		long lastModifiedTime;

		public WatchPoint(FileEditWatcher4 watcher, Integer WID, File watchFile, FileModifiedCallback callback) {
			super();
			this.WID = WID;
			this.watchFile = watchFile;
			this.watcher = watcher;
			this.callbacks.add(callback);

			this.lastSize = watchFile.length();
			this.lastModifiedTime = watchFile.lastModified();
		}

		public boolean isModified() {
			if (Math.abs(this.lastModifiedTime - watchFile.lastModified()) > 500) {
//				this.lastModifiedTime = watchFile.lastModified();
				return true;
			}

			long newlen = watchFile.length();
			if (this.lastSize != newlen) {
				this.lastSize = newlen;
				return true;
			}

			return false;
		}

		public void callback(File watchFile) {
			for (FileModifiedCallback callback : callbacks) {
				callback.callback(watchFile);
			}
		}

		public boolean removeCallback(Callback<File> modifyCallback) {
			return callbacks.remove(new FileModifiedCallback(modifyCallback));
		}

//		public boolean isFinalWatch() {
//			// TODO Auto-generated method stub
//			return callbacks.;
//		}

	}

	public static class FileWatchKey {
		final Integer WID;
		final Callback<File> modifyCallback;

		private FileWatchKey(Integer wID, Callback<File> modifyCallback) {
			super();
			WID = wID;
			this.modifyCallback = modifyCallback;
		}

	}

	private final static Map<Integer, WatchPoint> WATCH_POINTS = new HashMap<>();
//	private final static Map<S, FileEditWatcher> WATCHERS = new HashMap<>();

	public static void stopAll() {
		for (WatchPoint wp : WATCH_POINTS.values()) {
			wp.watcher.stop();
		}
		WATCH_POINTS.clear();
	}

	public static void stopWatch(File watchFile) {
		if (watchFile == null) {
			return;
		}

		Integer WID = toID(watchFile);
		WatchPoint wp = WATCH_POINTS.remove(WID);
		if (wp != null) {
			wp.watcher.stop();
		}
	}

//	public static void stopFinalWatch(File watchFile) {
//		Integer WID = toID(watchFile);
//		WatchPoint wp = WATCH_POINTS.remove(WID);
//		if (wp != null && wp.isFinalWatch()) {
//			wp.watcher.stop();
//		}
//	}

	public static void stopWatch(FileWatchKey wkey) {
		if (wkey == null) {
			return;
		}

		WatchPoint wp = WATCH_POINTS.get(wkey.WID);
		if (wp != null) {
			if (wp.removeCallback(wkey.modifyCallback)) {
				if (wp.callbacks.size() == 0) {
					stopWatch(wp.watchFile);
				}
			}
		}
	}

	public static FileWatchKey watch(final File watchFile, final Callback<File> modifyCallback) throws IOException {
		if (watchFile == null || modifyCallback == null || !watchFile.exists()) {
			return null;
		}

		if (watchFile.isDirectory()) {
			return null;
		}

		final Integer WID = watchFile.getAbsolutePath().hashCode();

		if (WATCH_POINTS.containsKey(WID)) {
			WATCH_POINTS.get(WID).callbacks.add(new FileModifiedCallback(modifyCallback));
			return new FileWatchKey(WID, modifyCallback);
		}

		FileEditWatcher4 w = new FileEditWatcher4();

		new Thread(new Runnable() {
			@Override
			public void run() {
				WatchPoint wp = new WatchPoint(w, WID, watchFile, new FileModifiedCallback(modifyCallback));
				WATCH_POINTS.put(WID, wp);

				w.watchFile(wp);
			}
		}).start();

		return new FileWatchKey(WID, modifyCallback);
	}

	private static Integer toID(File file) {
		Integer tid = file.getAbsolutePath().hashCode();
		return tid;
	}

	// -----------------------------------------------------------------------------------------------------------------------

	private WatchService watchService = null;
	private Set<Integer> watchPoints = new HashSet<>();

	private FileEditWatcher4() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
	}

	private boolean watchFile(WatchPoint wp) {
		watchPoints.add(wp.WID);

		try {
			Path path = wp.watchFile.getParentFile().toPath();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					// System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");

//					WatchEvent<Path> ev = (WatchEvent<Path>) event;
//					Path name = ev.context();

//					File file = new File(path + File.separator + name.getFileName());
					if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
						if (wp.isModified()) {
							wp.callback(wp.watchFile);
						}
						// modifyCallback.callback(file);
//						synchronized (WATCH_POINTS) {
//							final FileModifiedEvent currentFileStatus = new FileModifiedEvent(file);
//							// 传递原来的附件数据
//							if (lastFileStatus.isModified(currentFileStatus)) {
//								editMap.put(id, currentFileStatus);
//								lastFileStatus.cancelCall();
//								currentFileStatus.laterCall();
//
//							}
//						}
					}
				}

				key.reset();
			}
		} catch (ClosedWatchServiceException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			stop();
			return false;
		}

		return true;
	}

	private void stop() {
		if (watchService != null) {
			for (Integer WID : watchPoints) {
				WatchPoint wp = WATCH_POINTS.remove(WID);
				if (wp != null) {
					wp.watcher.stop();
				}
			}
			watchPoints.clear();
			if (watchService != null) {
				try {
					watchService.close();
					watchService = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void main(String[] s) throws IOException {
	// FileEditWatcher w = new FileEditWatcher();
	// w.watch(new File("C:\\11\\apache-tomcat-7.0.78"), new Callback<FileModifiedEvent>() {
	//
	// @Override
	// public void callback(FileModifiedEvent file) {
	// // TODO Auto-generated method stub
	// System.out.println(file.file + " " + file.lastModifiedTime + " " + file.md5 + " " + file.lastSize);
	// }
	//
	// });
	// }
}
