package com.amituofo.common.util;

public class PathUtils {
	public static String PATH_SEPARATOR = "/";
	public static char PATH_SEPARATOR_CHAR = '/';

	public static String toSimpleBalancePath(String path, char sp) {
		if (path == null || path.length() < 4) {
			return path;
		}
		String firstDir = path.substring(0, 2);
		String secondDir = path.substring(2, 4);
		return firstDir + sp + secondDir + sp + path;
	}

	public static String toSimpleBalancePath(String path) {
		if (path == null || path.length() < 4) {
			return path;
		}
		return path.substring(0, 2) + PATH_SEPARATOR + path.substring(2, 4) + PATH_SEPARATOR + path;
	}

}
