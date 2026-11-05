package com.amituofo.common.type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import com.amituofo.common.util.SystemUtils;

/** Controls metadata preservation independently of the native copy/cancellation mechanism. */
public enum FileAttributeCopyMode {
	AUTO, BASIC, FULL;

	public static FileAttributeCopyMode fromName(String value) {
		if (value != null) {
			try {
				return valueOf(value.trim().toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException ignored) {
			}
		}
		return AUTO;
	}

	/** Resolve a destination file's metadata policy for standalone copy/move callers. */
	public FileAttributeCopyMode resolve(Path target) {
		if (this != AUTO) return this;
		if (!SystemUtils.isMacOS()) return FULL;
		return resolveDirectory(target == null ? null : target.toAbsolutePath().getParent());
	}

	/** Resolve an entire task against its destination directory once, before workers start. */
	public FileAttributeCopyMode resolveDirectory(Path directory) {
		if (this != AUTO) return this;
		if (!SystemUtils.isMacOS() || directory == null) return FULL;
		try {
			Path existing = directory.toAbsolutePath();
			while (existing != null && !Files.exists(existing)) existing = existing.getParent();
			return existing == null ? FULL : forFileSystemType(Files.getFileStore(existing).type(), true);
		} catch (IOException | SecurityException e) {
			// Unknown stores retain metadata rather than silently discarding resource forks.
			return FULL;
		}
	}

	public static FileAttributeCopyMode forFileSystemType(String type, boolean macOS) {
		if (!macOS || type == null)
			return FULL;
		switch (type.trim().toLowerCase(Locale.ROOT)) {
		case "exfat":
		case "fat":
		case "fat12":
		case "fat16":
		case "fat32":
		case "vfat":
		case "msdos":
		case "msdosfs":
			return BASIC;
		default:
			return FULL;
		}
	}
}
