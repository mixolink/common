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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.amituofo.common.api.Callback;

public class FileEditWatcher {
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
//		final FileEditWatcher watcher;
		final FileModifiedCallback callback;
//		final List<FileModifiedCallback> callbacks = new ArrayList<FileEditWatcher.FileModifiedCallback>();

		long lastSize;
//		long lastModifiedTime;
//		long hash = null;
		long hash = -1;
//		String _hash_ = null;

		public WatchPoint(Integer WID, File watchFile, FileModifiedCallback callback) {
			super();
			this.WID = WID;
			this.watchFile = watchFile;
//			this.watcher = watcher;
//			this.callbacks.add(callback);
			this.callback = callback;

			this.lastSize = watchFile.length();
//			this.lastModifiedTime = watchFile.lastModified();
			try {
				this.hash = DigestUtils.calcCrc32(watchFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public synchronized boolean isModified() {
//			if (Math.abs(this.lastModifiedTime - watchFile.lastModified()) > 500) {
////				this.lastModifiedTime = watchFile.lastModified();
//				return true;
//			}

			if (!watchFile.exists()) {
				return false;
			}

			long newlen = watchFile.length();
			long newhash = -1;
			try {
				newhash = DigestUtils.calcCrc32(watchFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (this.lastSize != newlen) {
				this.lastSize = newlen;
				this.hash = newhash;
				return true;
			}

			if (this.hash != -1 && newhash != -1) {
				if (this.hash != newhash) {
					this.hash = newhash;
					return true;
				}
			}

			return false;
		}

		public void callback(File watchFile) {
//			this.lastSize = watchFile.length();
//			this.lastModifiedTime = watchFile.lastModified();
//			this.hash = _hash_;

//			for (FileModifiedCallback callback : callbacks) {
//				callback.callback(watchFile);
//			}

			if (callback != null) {
				callback.callback(watchFile);
			}
		}

//		public boolean removeCallback(Callback<File> modifyCallback) {
//			return callbacks.remove(new FileModifiedCallback(modifyCallback));
//		}
	}

	public static class FileWatchKey {
		final Integer WCHID;
		final Integer WID;
		final Callback<File> modifyCallback;

		private FileWatchKey(Integer WCHID, Integer WID, Callback<File> modifyCallback) {
			super();
			this.WCHID = WCHID;
			this.WID = WID;
			this.modifyCallback = modifyCallback;
		}
	}

	private final static Map<Integer, FileEditWatcher> WATCHERS = new HashMap<>();

	public static void stopAll() {
		if (WATCHERS.size() > 0) {
			for (FileEditWatcher wp : WATCHERS.values()) {
				wp.close();
			}
			WATCHERS.clear();
		}
	}

	public static void stopWatch(File watchFile) {
		if (watchFile == null) {
			return;
		}

		Integer WCHID = toWatcherID(watchFile);
		FileEditWatcher w = WATCHERS.get(WCHID);
		if (w != null) {
			w.stop(watchFile);
		}
	}

	public static void stopWatch(FileWatchKey wkey) {
		if (wkey == null) {
			return;
		}

		FileEditWatcher w = WATCHERS.get(wkey.WCHID);
		if (w != null) {
			w.stop(wkey);
		}
	}

	public static FileWatchKey watch(final File watchFile, final Callback<File> modifyCallback) throws IOException {
		if (watchFile == null || modifyCallback == null || !watchFile.exists()) {
			return null;
		}

		if (watchFile.isDirectory()) {
			return null;
		}

		final Integer WCHID = toWatcherID(watchFile);
		final Integer WID = toID(watchFile);

		FileEditWatcher w = WATCHERS.get(WCHID);
		if (w == null) {
			w = new FileEditWatcher(watchFile.getParentFile());
			WATCHERS.put(WCHID, w);
		}

		WatchPoint wp = new WatchPoint(WID, watchFile, new FileModifiedCallback(modifyCallback));
		if (w.watch(wp)) {
			return new FileWatchKey(WCHID, WID, modifyCallback);
		} else {
			return null;
		}
	}

	private static Integer toID(File file) {
		Integer tid = file.getName().hashCode();
		return tid;
	}

	private static Integer toID(String filename) {
		Integer tid = filename.hashCode();
		return tid;
	}

	private static Integer toWatcherID(File file) {
		Integer tid = file.getParentFile().getAbsolutePath().hashCode();
		return tid;
	}

	// -----------------------------------------------------------------------------------------------------------------------

	private WatchService watchService = null;
	private final Map<Integer, WatchPoint> watchPoints = new HashMap<>();
	private final File directory;
//	private final Integer ID;
	private WatchKey watchkey;

	private FileEditWatcher(File directory) {
		this.directory = directory;
//		this.ID = toWatcherID(directory);
	}

	private void init() throws IOException {
		if (this.watchService != null) {
			return;
		}

		this.watchService = FileSystems.getDefault().newWatchService();
		Path path = directory.toPath();
		this.watchkey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WatchKey key;
					while ((key = watchService.take()) != null) {
						for (WatchEvent<?> event : key.pollEvents()) {
							// System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");

							WatchEvent<Path> ev = (WatchEvent<Path>) event;
							Path name = ev.context();

//							File file = new File(path + File.separator + name.getFileName());
							if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
								WatchPoint wp = watchPoints.get(toID(name.getFileName().toString()));
								if (wp != null && wp.isModified()) {
									wp.callback(wp.watchFile);
								}
							}
						}

						if (key.reset() == false) {
							close();
							return;
						}
					}
				} catch (ClosedWatchServiceException e) {
					// e.printStackTrace();
					close();
				} catch (Exception e) {
					e.printStackTrace();
					close();
				}
			}
		}).start();
	}

	private boolean watch(WatchPoint wp) throws IOException {
		if (!wp.watchFile.getParentFile().equals(directory)) {
			return false;
		}

		synchronized (watchPoints) {
			init();
			watchPoints.put(wp.WID, wp);
		}

		return true;
	}

	public void stop(File watchFile) {
		if (watchFile == null) {
			return;
		}

		synchronized (watchPoints) {
			Integer WID = toID(watchFile);
			watchPoints.remove(WID);
			if (watchPoints.size() == 0) {
				close();
			}
		}
	}

	public void stop(FileWatchKey wkey) {
		if (wkey == null) {
			return;
		}

		synchronized (watchPoints) {
			watchPoints.remove(wkey.WID);
			if (watchPoints.size() == 0) {
				close();
			}
		}
	}

	private void close() {
		if (watchService != null) {
			watchPoints.clear();
			try {
				watchkey.cancel();
				watchService.close();
				watchService = null;
			} catch (IOException e) {
				e.printStackTrace();
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
