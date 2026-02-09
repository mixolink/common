package com.amituofo.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.amituofo.common.api.DataHandler;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.kit.file.DirMaker;

public class ZipUtils {
	private static final int BUFFER_SIZE = 2 * 1024;
	public static final char PATH_SEPERATOR = '/';

	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
//		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;

		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));

				int len;
				FileInputStream in = new FileInputStream(srcFile);

				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}

				zos.closeEntry();
				in.close();
			}

//			long end = System.currentTimeMillis();
//            System.out.println("压缩完成，耗时：" + (end - start) +" ms");

		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String toEntryName(String name) {
		if (File.separatorChar != PATH_SEPERATOR && name.indexOf(File.separatorChar) != -1) {
			name = name.replace(File.separatorChar, PATH_SEPERATOR);
		}

		return name;
	}

	public static String toLocalFilePath(String name) {
		if (File.separatorChar != PATH_SEPERATOR && name.indexOf(PATH_SEPERATOR) != -1) {
			name = name.replace(PATH_SEPERATOR, File.separatorChar);
		}

		return name;
	}

	public static void zipInputStream(ZipOutputStream zos, InputStream in, String name, String relativeDir) throws IOException {
		ZipEntry entry = new ZipEntry(toEntryName(relativeDir + name));
		zos.putNextEntry(entry);
		byte[] buffer = new byte[4096];
		int readLen = 0;
		try {
			while ((readLen = in.read(buffer, 0, 4096)) != -1) {
				zos.write(buffer, 0, readLen);
			}
		} catch (IOException e) {
//			e.printStackTrace();
			zos.write(new byte[0], 0, 0);
			throw e;
		} finally {
			in.close();
		}
	}

	public static void zipFile(ZipOutputStream zos, File file, String name) throws IOException {
		if (file.isFile()) {
			ZipEntry entry = new ZipEntry(toEntryName(name));
			zos.putNextEntry(entry);
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[4096];
			int readLen = 0;
			try {
				while ((readLen = is.read(buffer, 0, 4096)) != -1) {
					zos.write(buffer, 0, readLen);
				}
			} catch (IOException e) {
//				e.printStackTrace();
				zos.write(new byte[0], 0, 0);
				throw e;
			} finally {
				is.close();
			}
		}
	}

	public static void zipFileOrDirectory(ZipOutputStream zos, File fileOrDirectory, String relativeDir) throws IOException {
		if (fileOrDirectory.isDirectory()) {
			File[] files = fileOrDirectory.listFiles();
			for (int i = 0; i < files.length; ++i) {
				if (files[i].isDirectory()) {
					zipFileOrDirectory(zos, files[i], relativeDir + files[i].getName() + PATH_SEPERATOR);
				} else {
					zipFile(zos, files[i], relativeDir + files[i].getName());
				}
			}
		} else if (fileOrDirectory.isFile()) {
			zipFile(zos, fileOrDirectory, relativeDir + fileOrDirectory.getName());
		}
	}

	/**
	 * 将目录压缩为 ZIP 文件，ZIP 内部使用相对路径（不包含源目录名）
	 *
	 * @param dir        要压缩的源目录（必须是目录）
	 * @param targetFile 目标 ZIP 文件（如 /path/to/archive.zip）
	 * @throws IOException 如果 I/O 出错
	 */
	public static void zipDirectory(File dir, File targetFile) throws IOException {
		if (!dir.exists() || !dir.isDirectory()) {
			throw new IllegalArgumentException("Source must be dir " + dir);
		}

		// 确保目标文件的父目录存在
		File parent = targetFile.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)))) {
			Path sourcePath = dir.toPath().toAbsolutePath().normalize();

			// 递归遍历目录并添加到 ZIP
			Files.walk(sourcePath).filter(path -> !Files.isDirectory(path)) // 只处理文件（目录会自动创建）
					.forEach(path -> {
						try {
							// 计算相对路径（相对于源目录）
							String relativePath = sourcePath.relativize(path).toString();
							// 在 Windows 上，路径分隔符是 \，但 ZIP 规范要求使用 /
							String zipEntryName = relativePath.replace(File.separatorChar, '/');

							ZipEntry entry = new ZipEntry(zipEntryName);
							// 可选：设置时间戳以保持一致性（避免每次压缩内容不同）
							entry.setTime(Files.getLastModifiedTime(path).toMillis());
							zos.putNextEntry(entry);

							// 写入文件内容
							try (InputStream is = Files.newInputStream(path)) {
								byte[] buffer = new byte[8192];
								int bytesRead;
								while ((bytesRead = is.read(buffer)) != -1) {
									zos.write(buffer, 0, bytesRead);
								}
							}

							zos.closeEntry();
						} catch (IOException e) {
							throw new UncheckedIOException(e); // 用于在 lambda 中抛出 checked 异常
						}
					});
		} catch (UncheckedIOException e) {
			throw e.getCause(); // 还原为 IOException
		}
	}

	public static void unzip(InputStream zipin, DataHandler<InputStream, ZipEntry, Void> handler) throws IOException {
		ZipInputStream zipIn = new ZipInputStream(zipin);
		try {
			ZipEntry entry = zipIn.getNextEntry();
			int i = 0;
			while (entry != null) {
//			String filePath = destDir + File.separator + entry.getName();
				if (entry.isDirectory()) {
//				File dir1 = new File(filePath);
//				dir1.mkdirs();
				} else {
					try {
						handler.handle(i++, entry, zipIn);
					} catch (HandleException e) {
						e.printStackTrace();
					}
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		} finally {
			zipIn.close();
		}
	}

	public static void unzip(File srczipfile, File destdir) throws IOException {
		unzip(new FileInputStream(srczipfile), destdir);
	}

	public static void unzip(String srczipfile, String destdir) throws IOException {
		unzip(new FileInputStream(srczipfile), new File(destdir));
	}

	public static void unzip(InputStream zipin, String destdir) throws IOException {
		unzip(zipin, new File(destdir));
	}

	public static void unzip(InputStream zipin, File destdir) throws IOException {
		DirMaker dm = DirMaker.newMaker();
		try {
			dm.mkdirIfNotExist(destdir);

			unzip(zipin, new DataHandler<InputStream, ZipEntry, Void>() {

				@Override
				public Void handle(int action, ZipEntry entry, InputStream in) throws HandleException {
					File filePath = new File(URLUtils.catFilePath(destdir.getPath(), toLocalFilePath(entry.getName())));

//				System.out.println(filePath);
					if (entry.isDirectory()) {
						dm.mkdirIfNotExist(filePath);
					} else {
						try {
							dm.mkdirIfNotExist(filePath.getParentFile());
							extractFile(in, filePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			});
		} finally {
			dm.close();
			zipin.close();
		}
	}

	private static void extractFile(InputStream in, File filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		try {
			byte[] bytesIn = new byte[1024];
			int read = 0;
			while ((read = in.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		} finally {
			bos.close();
		}
	}

}