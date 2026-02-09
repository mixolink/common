package com.amituofo.common.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A flexible, recursive directory watcher based on Java NIO WatchService.
 * Allows dynamic registration/unregistration of directories at runtime. No root
 * directory is required at construction time.
 */
public class DirectoryWatcher {

	/** Optional callback interface for file system events */
	public static interface WatchListener {
		void onCreate(Path dir, Path fileOrDir); // File or directory created

		void onDelete(Path dir, Path fileOrDir); // File or directory deleted

		void onModify(Path dir, Path fileOrDir); // File modified (content/attributes)
	}

	private final List<WatchListener> listeners = new ArrayList<>(); // May be null
	private final WatchService watchService;
	private final Map<WatchKey, Path> keyToDirMap = new HashMap<>();
	private Thread watcherThread;
	private volatile boolean running = false;

	/**
	 * Constructs a watcher. Listener is optional.
	 *
	 * @param listener optional event callback (can be null)
	 * @throws IOException if WatchService cannot be created
	 */
	public DirectoryWatcher(WatchListener listener) throws IOException {
		if (listener != null)
			this.listeners.add(listener);
		this.watchService = FileSystems.getDefault().newWatchService();
	}

	/**
	 * Convenience constructor without listener.
	 */
	public DirectoryWatcher() throws IOException {
		this(null);
	}

	/** Starts the watcher thread (must be called before any registration) */
	public synchronized void start() {
		if (running)
			return;
		running = true;

		watcherThread = new Thread(this::watchLoop, "DirectoryWatcher-Thread");
		watcherThread.setDaemon(true);
		watcherThread.start();
	}

	/** Stops the watcher and cancels all registered keys */
	public synchronized void stop() {
		if (!running)
			return;
		running = false;

		// Cancel all keys
		for (WatchKey key : keyToDirMap.keySet()) {
			key.cancel();
		}
		keyToDirMap.clear();
		listeners.clear();
		watcherThread.interrupt();
		try {
			watchService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers a single directory for watching.
	 *
	 * @param dir the directory to watch
	 * @throws IOException if registration fails
	 */
	public void register(Path dir) throws IOException {
		if (dir == null) {
			return;
		}

		if (!Files.isDirectory(dir)) {
			throw new IllegalArgumentException("Not a directory: " + dir);
		}
		Path normalized = dir.toAbsolutePath().normalize();
		WatchKey key = normalized.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keyToDirMap.put(key, normalized);
	}

	/**
	 * Recursively registers a directory and all its existing subdirectories.
	 *
	 * @param start the starting directory
	 * @throws IOException if any registration fails
	 */
	public void registerAll(Path dir) throws IOException {
		if (dir == null) {
			return;
		}

		if (!Files.isDirectory(dir)) {
			throw new IllegalArgumentException("Not a directory: " + dir);
		}

		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Unregisters a directory and all its subdirectories.
	 *
	 * @param dir the directory to stop watching (exact match or ancestor)
	 * @throws IOException if cancellation fails (rare)
	 */
	public void unregister(Path dir) {
		if (dir == null) {
			return;
		}

		Path target = dir.toAbsolutePath().normalize();

		Iterator<Entry<WatchKey, Path>> iterator = keyToDirMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<WatchKey, Path> entry = iterator.next();
			Path watched = entry.getValue();

			// Remove if exact match or subdirectory of target
			if (watched.equals(target) || watched.startsWith(target.resolve(""))) {
				entry.getKey().cancel();
				iterator.remove();
			}
		}
	}

	/** Main event loop */
	private void watchLoop() {
		while (running) {
			WatchKey key;
			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} catch (ClosedWatchServiceException e) {
				break;
			}

			Path dir = keyToDirMap.get(key);
			if (dir == null) {
				key.cancel();
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == OVERFLOW)
					continue;

				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path child = ev.context();
				Path fullPath = dir.resolve(child);

				if (!listeners.isEmpty()) {
					for (WatchListener listener : listeners) {
						if (kind == ENTRY_CREATE) {
							listener.onCreate(dir, child);
						} else if (kind == ENTRY_DELETE) {
							listener.onDelete(dir, child);
						} else if (kind == ENTRY_MODIFY) {
							if (Files.isRegularFile(fullPath)) {
								listener.onModify(dir, child);
							}
						}
					}
				}
			}

			boolean valid = key.reset();
			if (!valid) {
				keyToDirMap.remove(key);
			}
		}
	}
}