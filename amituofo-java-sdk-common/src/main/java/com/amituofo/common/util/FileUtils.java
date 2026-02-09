/*                                                                             
 * Copyright (C) 2019 Rison Han                                     
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,         
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package com.amituofo.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.alibaba.fastjson.JSON;
import com.amituofo.common.api.HandleBytes;
import com.amituofo.common.api.ObjectHandler;
import com.amituofo.common.api.RecordHandler;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.kit.counter.Counter;
import com.amituofo.common.kit.file.FileSummary;
import com.amituofo.common.type.HandleFeedback;

/**
 * Collection of methods to make work with files easier.
 */
public class FileUtils {
	/**
	 * Default 10 digit file storage distribution array. This means that if I want to name file as 10 digit number e.g. number 123 as 0000000123 or number
	 * 123456789 as 01234567890. Then the path constructed from number 1234567890 using distribution 2/2/2/4 would be 12/34/56/0123456789
	 */
	public static final int[] DEFAULT_STRORAGE_TREE_DISTRIBUTION = { 2, 2, 2, 4 };

//	public static void main(String[] a) throws IOException {
//		moveFile(new File("C:\\Temp\\TEST-SRC1\\apache-maven-4"), new File("C:\\Temp\\TEST-SRC1\\apache-maven-3"));
//
//		moveDirectory(new File("C:\\Temp\\TEST-SRC1\\apache-maven-3"), "", true, true, new File("C:\\Temp\\TEST-SRC1\\apache-maven-4"), MovingOption.REPLACE_EXIST);
//
//	}

	// 定义文件名非法字符
	private static final String INVALID_CHARS = "<>:\"/\\|?*";

	public static boolean containsInvalidFileNameChars(String fileName) {
		for (char c : INVALID_CHARS.toCharArray()) {
			if (fileName.indexOf(c) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * How big buffer to use to process files.
	 */
//	public static final int BUFFER_SIZE = 1024 * 64;
//	public static int DEFAULT_BUFFER_SIZE = 1024 *1024 * 4;

	/**
	 * Temporary directory to use. It is guarantee that it ends with \ (or /)
	 */
//	protected static String s_strTempDirectory;

//	private static LifecycleMap<String, String> alreadyMkdirMap = new LifecycleMap<>();
	public static int countLines(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		try {
			while (reader.readLine() != null)
				lines++;
		} finally {
			reader.close();
		}
		return lines;
	}

	public static boolean mkdirsIfNotExist(File dir) {
//		return DirMaker.defaultMaker().mkdirIfNotExist(dir);
		if (!dir.exists()) {
//			mkok = dir.mkdirs();
			// 1. 创建 Path 对象
			Path path = dir.toPath();

			try {
				// 2. 使用 createDirectories() 创建目录及其所有不存在的父目录
				// 如果目录已存在，此方法不做任何操作，也不抛出异常
				Files.createDirectories(path);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public static boolean mkdirsIfNotExist(String dir) {
		return mkdirsIfNotExist(new File(dir));
	}

	public static boolean testFolder(String directoryPath) {
		try {
			mkdirsIfNotExist(directoryPath);
			Path path = Paths.get(directoryPath);

			if (Files.exists(path) && Files.isDirectory(path)) {
				if (Files.isReadable(path) && Files.isWritable(path)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean createNewFile(File file, boolean createEvenExist) throws IOException {
		if (file == null) {
			return false;
		}

		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			return file.createNewFile();
		} else {
			if (createEvenExist) {
				return file.createNewFile();
			}

			return true;
		}
	}

	public static boolean createNewFile(File file, boolean createEvenExist, long size) throws IOException {
		boolean created = createNewFile(file, createEvenExist);

		if (created && size > 0) {
			RandomAccessFile r = null;
			try {
				r = new RandomAccessFile(file, "rw");
				r.setLength(size);
			} finally {
				if (r != null) {
					try {
						r.close();
					} catch (IOException e) {
					}
				}
			}
		}

		return created;
	}

	public static String catPath(String path1, String path2) {
		if (path1 != null && path1.length() > 0) {
			char lastChar = path1.charAt(path1.length() - 1);

			if (lastChar == '/' || lastChar == '\\') {
				path1 = path1.substring(0, path1.length() - 1);
			}
		}

		if (path2 != null && path2.length() > 0) {
			char firstChar = path2.charAt(0);

			if (firstChar == '/' || firstChar == '\\') {
				path2 = path2.substring(1);
			}
		}

		return path1 + File.separator + path2;
	}

	/**
	 * Move file to a new location. If the destination is on different volume, this file will be copied and then original file will be deleted. If the
	 * destination already exists, this method renames it with different name and leaves it in that directory and moves the new file along side the
	 * renamed one.
	 * 
	 * @param  srcfile      - file to move
	 * @param  destfile     - destination file
	 * @throws IOException  - error message
	 * @throws OSSException - error message
	 */
	public static void moveFile(File srcfile, File destfile) throws IOException {
		if (!srcfile.exists()) {
			throw new IOException("Source file not exist! " + srcfile);
		}

		if (destfile.exists()) {
			if (!destfile.delete()) {
				throw new IOException("Distination file could not delete! " + destfile);
			}
		}

		// Make sure the directory exists and if not create it
		File flFolder;

		flFolder = destfile.getParentFile();
		if ((flFolder != null) && (!flFolder.exists())) {
			if (!flFolder.mkdirs()) {
				throw new IOException("Cannot create directory! " + flFolder);
			}

			if (!flFolder.exists()) {
				throw new IOException("Target directory not found! " + flFolder);
			}
		}
		
		try {
			Files.move(srcfile.toPath(), destfile.toPath(), 
			        StandardCopyOption.ATOMIC_MOVE,     // 优先原子移动
			        StandardCopyOption.REPLACE_EXISTING // 如果目标已存在就覆盖
					);
		} catch (Exception e) {
			copyFile(srcfile, destfile);

			if (!srcfile.delete()) {
				destfile.delete();
				throw new IOException("Cannot delete already copied file " + srcfile);
			}
		}
		
//		if (!srcfile.renameTo(destfile)) {
//			copyFile(srcfile, destfile);
//
//			if (!srcfile.delete()) {
//				destfile.delete();
//				throw new IOException("Cannot delete already copied file " + srcfile);
//			}
//		}
	}

	/**
	 * Copy the current file to the destination file.
	 * 
	 * @param  srcfile      - source file
	 * @param  destfile     - destination file
	 * @throws IOException  - error message
	 * @throws OSSException - error message
	 */
	public static void copyFile(File srcfile, File destfile) throws IOException {
		File flFolder;
		flFolder = destfile.getParentFile();
		if ((flFolder != null) && (!flFolder.exists())) {
			if (!flFolder.mkdirs()) {
				if (!flFolder.exists()) {
					throw new IOException("Cannot create directory " + flFolder);
				}
			}
		}

//		FileInputStream finInput = null;
//
//		try {
//			finInput = new FileInputStream(flCurrent);
//		} catch (Exception e) {
//			if (finInput != null) {
//				try {
//					finInput.close();
//				} catch (Throwable thr) {
//
//				}
//			}
//			throw e;
//		}

//		copyStreamToFile(finInput, flDestination, StreamUtils.DEFAULT_BUFFER_SIZE);
//		Files.copy(flCurrent.toPath(), flCurrent.toPath(), StandardCopyOption.REPLACE_EXISTING);

		FileInputStream fis = null;
		FileOutputStream fos = null;
		long bytesTransferred = 0;
		try {
			fis = new FileInputStream(srcfile);
			fos = new FileOutputStream(destfile);
			FileChannel sourceChannel = fis.getChannel();
			FileChannel destinationChannel = fos.getChannel();

			bytesTransferred = sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			StreamUtils.close(fis);
			StreamUtils.close(fos);

			if (srcfile.length() != bytesTransferred) {
				destfile.delete();
				throw new IOException("Transfer length invalid! " + srcfile.length() + "!=" + bytesTransferred);
			}
		}
	}

	public static enum MovingOption {
		REPLACE_EXIST, IGNORE_EXIST
	}

	public static boolean moveDirectory(File srcDir, String patten, boolean subfolder, boolean tryRename, File targetDir, MovingOption option) throws IOException {
		if (tryRename && StringUtils.isEmpty(patten)) {
			if (srcDir.renameTo(targetDir)) {
				return true;
			}
		}

		if (!srcDir.exists() || !srcDir.isDirectory()) {
			throw new IOException("Source directory not correct" + srcDir);
		}

		if (!targetDir.exists()) {
			if (!targetDir.mkdirs()) {
				throw new IOException("Cannot create directory " + targetDir);
			}
		}

		final String targetBase = targetDir.getPath();
		final int scrBaseStart = srcDir.getPath().length();

		Counter failed = Counter.newCounter();
		Counter succeed = Counter.newCounter();

		listDirectoryStream(srcDir, patten, subfolder, new ObjectHandler<Void, File>() {

			@Override
			public void exceptionCaught(File data, Throwable e) {

			}

			@Override
			public HandleFeedback handle(Void meta, File file) {
				if (file.isDirectory()) {
					return HandleFeedback.ignored;
				}

				String relatePath = file.getPath().substring(scrBaseStart);

				File flDestination = new File(URLUtils.catFilePath(targetBase, relatePath));

				if (option == MovingOption.IGNORE_EXIST) {
					if (flDestination.exists()) {
						return HandleFeedback.ignored;
					}
				}

				if (tryRename && StringUtils.isEmpty(patten)) {
					if (srcDir.renameTo(targetDir)) {
						return HandleFeedback.succeed;
					}
				}

//				FileInputStream finInput = null;
//
//				try {
//					finInput = new FileInputStream(file);
//				} catch (IOException ioExec) {
//					if (finInput != null) {
//						try {
//							finInput.close();
//						} catch (Throwable thr) {
//						}
//					}
//
//					failed.plus1();
//					return HandleFeedback.failed;
//				}

				try {
//					copyStreamToFile(finInput, flDestination, StreamUtils.DEFAULT_BUFFER_SIZE);
					Files.copy(file.toPath(), flDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (Throwable thr) {
					flDestination.delete();
//					flDestination.deleteOnExit();
					failed.plus1();
					return HandleFeedback.failed;
				}

				// Now delete the file
				if (!file.delete()) {
					flDestination.delete();
					failed.plus1();
					return HandleFeedback.failed;
				} else {
					succeed.plus1();
				}

				return null;
			}
		});

		if (succeed.n > 0 && failed.n == 0) {
			deleteEmptyDirectorys(srcDir, false);
		}

		return failed.n == 0;
	}

	/**
	 * Rename the file to temporaty name with given prefix
	 * 
	 * @param  flFileToRename - file to rename
	 * @param  strPrefix      - prefix to use
	 * @throws IOException    - error message
	 */
	public static void renameToTemporaryName(File flFileToRename, String strPrefix) throws IOException {
		assert strPrefix != null : "Prefix cannot be null.";

		String strParent;
		StringBuffer sbBuffer = new StringBuffer();
		File flTemp;
		int iIndex = 0;

		strParent = flFileToRename.getParent();

		// Generate new name for the file in a deterministic way
		do {
			iIndex++;
			sbBuffer.delete(0, sbBuffer.length());
			if (strParent != null) {
				sbBuffer.append(strParent);
				sbBuffer.append(File.separatorChar);
			}

			sbBuffer.append(strPrefix);
			sbBuffer.append("_");
			sbBuffer.append(iIndex);
			sbBuffer.append("_");
			sbBuffer.append(flFileToRename.getName());

			flTemp = new File(sbBuffer.toString());
		} while (flTemp.exists());

		// Now we should have unique name
		if (!flFileToRename.renameTo(flTemp)) {
			throw new IOException("Cannot rename " + flFileToRename.getAbsolutePath() + " to " + flTemp.getAbsolutePath());
		}
	}

	public static int deleteEmptyDirectorys(File fDir, boolean skipSelf) {
		int filecount = 0;

		if (fDir != null && fDir.isDirectory()) {
			File[] files = fDir.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						int filecountInThisFolder = deleteEmptyDirectorys(file, false);
						if (filecountInThisFolder == 0) {
							if (!file.delete()) {
								filecount += 1;
							}
						} else {
							filecount += filecountInThisFolder;
						}
					} else {
						filecount++;
					}
				}
			}

			if (filecount == 0 && !skipSelf) {
				fDir.delete();
			}
		}

		return filecount;
	}

	/**
	 * Delete all files and directories in directory but do not delete the directory itself.
	 * 
	 * @param  strDir - string that specifies directory to delete
	 * @return        boolean - sucess flag
	 */
	public static boolean deleteDirectoryContent(String strDir) {
		return ((strDir != null) && (strDir.length() > 0)) ? deleteDirectoryContent(new File(strDir)) : false;
	}

	/**
	 * Delete all files and directories in directory but do not delete the directory itself.
	 * 
	 * @param  fDir - directory to delete
	 * @return      boolean - sucess flag
	 */
	public static boolean deleteDirectoryContent(File fDir) {
		boolean bRetval = false;

		if (fDir != null && fDir.isDirectory()) {
			File[] files = fDir.listFiles();

			if (files != null) {
				bRetval = true;
				boolean dirDeleted;

				for (int index = 0; index < files.length; index++) {
					if (files[index].isDirectory()) {
						// TODO: Performance: Implement this as a queue where you add to
						// the end and take from the beginning, it will be more efficient
						// than the recursion
						dirDeleted = deleteDirectoryContent(files[index]);
						if (dirDeleted) {
							bRetval = bRetval && files[index].delete();
						} else {
							bRetval = false;
						}
					} else {
						bRetval = bRetval && files[index].delete();
					}
				}
			}
		}

		return bRetval;
	}

	/**
	 * Deletes all files and subdirectories under the specified directory including the specified directory
	 * 
	 * @param  strDir - string that specifies directory to be deleted
	 * @return        boolean - true if directory was successfully deleted
	 */
	public static boolean deleteDir(String strDir) {
		return ((strDir != null) && (strDir.length() > 0)) ? deleteDir(new File(strDir)) : false;
	}

	/**
	 * Deletes all files and subdirectories under the specified directory including the specified directory
	 * 
	 * @param  fDir - directory to be deleted
	 * @return      boolean - true if directory was successfully deleted
	 */
	public static boolean deleteDir(File fDir) {
		boolean bRetval = false;
		if (fDir != null && fDir.exists()) {
			bRetval = deleteDirectoryContent(fDir);
			if (bRetval) {
				bRetval = bRetval && fDir.delete();
			}
		}
		return bRetval;
	}

	public static List<FileSummary> deleteFilesKeepMoreThanHours(File dirOrFile, String filenamePattern, int hours) {
		final List<FileSummary> deletedfiles = new ArrayList<>();
		final List<File> todelfiles = new ArrayList<>();

		if (dirOrFile.isDirectory()) {
			try {
				FileUtils.listFiles(dirOrFile.getPath(), true, new FileFilter() {

					@Override
					public boolean accept(File file) {
						if (StringUtils.isEmpty(filenamePattern)) {
							return true;
						}

						return StringUtils.wildcardMatch(filenamePattern, file.getName());
					}
				}, new ObjectHandler<Void, File>() {

					@Override
					public void exceptionCaught(File data, Throwable e) {

					}

					@Override
					public HandleFeedback handle(Void meta, File file) {
						if (file.isDirectory()) {
							return null;
						}

						todelfiles.add(file);

						return null;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			todelfiles.add(dirOrFile);
		}

		if (todelfiles.size() > 0) {
			// 根据文件修改时间排序
			List<File> sortfiles = todelfiles.stream().sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList());

			// 输出排序后的文件名和修改时间
			for (File file : sortfiles) {
				long lastmodified = file.lastModified();
				if ((DateUtils.toHours(System.currentTimeMillis() - lastmodified) > hours)) {
					long size = file.length();
					String path = file.getPath();

					boolean deleted = file.delete();
					if (deleted) {
						FileSummary filesummary = new FileSummary();
						filesummary.setPath(path);
						filesummary.setSize(size);
						filesummary.setLastModifiedTime(lastmodified);
						deletedfiles.add(filesummary);
					}
				} else {
					break;
				}
			}
		}

		return deletedfiles;
	}

	/**
	 * Compare binary files. Both files must be files (not directories) and exist.
	 * 
	 * @param  first       - first file
	 * @param  second      - second file
	 * @return             boolean - true if files are binery equal
	 * @throws IOException - error in function
	 */
	public boolean isFileBinaryEqual(File first, File second) throws IOException {
		// TODO: Test: Missing test
		boolean retval = false;

		if ((first.exists()) && (second.exists()) && (first.isFile()) && (second.isFile())) {
			if (first.getCanonicalPath().equals(second.getCanonicalPath())) {
				retval = true;
			} else {
				FileInputStream firstInput = null;
				FileInputStream secondInput = null;
				BufferedInputStream bufFirstInput = null;
				BufferedInputStream bufSecondInput = null;

				try {
					firstInput = new FileInputStream(first);
					secondInput = new FileInputStream(second);
					bufFirstInput = new BufferedInputStream(firstInput, StreamUtils.DEFAULT_BUFFER_SIZE);
					bufSecondInput = new BufferedInputStream(secondInput, StreamUtils.DEFAULT_BUFFER_SIZE);

					int firstByte;
					int secondByte;

					while (true) {
						firstByte = bufFirstInput.read();
						secondByte = bufSecondInput.read();
						if (firstByte != secondByte) {
							break;
						}
						if ((firstByte < 0) && (secondByte < 0)) {
							retval = true;
							break;
						}
					}
				} finally {
					try {
						if (bufFirstInput != null) {
							bufFirstInput.close();
						}
					} finally {
						if (bufSecondInput != null) {
							bufSecondInput.close();
						}
					}
				}
			}
		}

		return retval;
	}

	/**
	 * Get path which represents temporary directory. It is guarantee that it ends with \ (or /).
	 * 
	 * @return String
	 */
//	public static String getTemporaryDirectory() {
//		return s_strTempDirectory;
//	}

	/**
	 * Copy any input stream to output file. Once the data will be copied the stream will be closed.
	 * 
	 * @param  input             - InputStream to copy from
	 * @param  output            - File to copy to
	 * @throws IOException       - error in function
	 * @throws OSSMultiException - double error in function
	 */
	public static void copyStreamToFile(InputStream input, File output, int bufferSize) throws IOException {
		File parent = output.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		FileOutputStream foutOutput = null;

		// open input file as stream safe - it can throw some IOException
		try {
			foutOutput = new FileOutputStream(output);
		} catch (IOException ioExec) {
			if (foutOutput != null) {
				try {
					foutOutput.close();
				} catch (IOException ioExec2) {

				}
			}

			throw ioExec;
		}

		// all streams including os are closed in copyStreamToStream function
		// in any case
		copyStreamToStream(input, foutOutput, bufferSize);
	}

	/**
	 * Copy any input stream to output stream. Once the data will be copied both streams will be closed.
	 * 
	 * @param  input             - InputStream to copy from
	 * @param  output            - OutputStream to copy to
	 * @throws IOException       - io error in function
	 * @throws OSSMultiException - double error in function
	 */
	public static void copyStreamToStream(InputStream input, OutputStream output, int bufferSize) throws IOException {
		InputStream is = null;
		OutputStream os = null;

		try {
			if (input instanceof BufferedInputStream) {
				is = input;
			} else {
				is = new BufferedInputStream(input);
			}
			if (output instanceof BufferedOutputStream) {
				os = output;
			} else {
				os = new BufferedOutputStream(output);
			}

			// while ((ch = is.read()) != -1) {
			// os.write(ch);
			// }
			byte[] buf = new byte[bufferSize];

			for (int readNum; (readNum = is.read(buf)) != -1;) {
				// System.out.println(readNum);
				os.write(buf, 0, readNum); // no doubt here is 0
			}

			os.flush();
		} finally {
			IOException exec1 = null;
			IOException exec2 = null;
			try {
				// because this close can throw exception we do next close in
				// finally statement
				if (os != null) {
					try {
						output.close();
						os.close();
					} catch (IOException exec) {
						exec1 = exec;
					}
				}
			} finally {
				if (is != null) {
					try {
						input.close();
						is.close();
					} catch (IOException exec) {
						exec2 = exec;
					}
				}
			}
			if ((exec1 != null) && (exec2 != null)) {
				throw exec1;
			} else if (exec1 != null) {
				throw exec1;
			} else if (exec2 != null) {
				throw exec2;
			}
		}
	}

	public static String readToString(File file) throws FileNotFoundException, IOException {
		return StreamUtils.inputStreamToString(new FileInputStream(file), true);
	}

	public static byte[] readToBytes(File file) throws FileNotFoundException, IOException {
		return StreamUtils.inputStreamToBytes(new FileInputStream(file), true);
	}

	public static long writeToFile(String content, File file, boolean append) throws IOException {
		return writeToFile(content, file, "utf-8", append);
	}

	public static long writeToFile(String content, File file, String charsetName, boolean append) throws IOException {
		OutputStream outStream = null;
		OutputStreamWriter streamWriter = null;

		if (content == null) {
			content = "";
		}

		try {
			File folder = file.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}

			outStream = new FileOutputStream(file, append);
			streamWriter = new OutputStreamWriter(outStream, charsetName);
			streamWriter.write(content);
		} finally {
			if (streamWriter != null) {
				try {
					streamWriter.flush();
					streamWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				streamWriter = null;
			}
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outStream = null;
			}
		}

		return file.length();
	}

	public static long writeToFile(InputStream in, File file, boolean append) throws IOException {
		OutputStream outStream = null;

		if (in == null) {
			return 0;
		}

		File folder = file.getParentFile();
		if (!folder.exists()) {
			folder.mkdirs();
		}

		outStream = new FileOutputStream(file, append);
		StreamUtils.inputStream2OutputStream(in, true, outStream, true);

		return file.length();
	}

	public static long writeToFile(byte[] content, File file, boolean append) throws IOException {
		OutputStream outStream = null;

		if (content == null) {
			return 0;
		}

		try {
			File folder = file.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}

			outStream = new FileOutputStream(file, append);
			outStream.write(content);
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outStream = null;
			}
		}

		return file.length();
	}

	public static long writeToFile(byte[] content, int offset, int length, File file, boolean append) throws IOException {
		OutputStream outStream = null;

		if (content == null) {
			return 0;
		}

		try {
			File folder = file.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}

			outStream = new FileOutputStream(file, append);
			outStream.write(content, offset, length);
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outStream = null;
			}
		}

		return file.length();
	}

	public static byte[] fileToByteArray(File file) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;

		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();

			byte[] buf = new byte[StreamUtils.DEFAULT_BUFFER_SIZE];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
				// Writes len bytes from the specified byte array starting at offset off to this
				// byte array output stream.
				// System.out.println("read " + readNum + " bytes,");
			}
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
			}
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
		byte[] bytes = bos.toByteArray();

		return bytes;
	}

//	public static void writeStringToFile(File file, String s) {
//		// TODO Auto-generated method stub
//
//	}

	public static String fileToString(File file, String charsetName) throws IOException {
		if (file == null || !file.exists()) {
			return null;
		}

		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		String content = "";
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();

			byte[] buf = new byte[StreamUtils.DEFAULT_BUFFER_SIZE];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
		} finally {
			try {
				bos.flush();

				if (charsetName != null && charsetName.length() > 0) {
					content = bos.toString(charsetName);
				} else {
					content = bos.toString();
				}

				bos.close();
			} catch (IOException e) {
			}
			try {
				fis.close();
			} catch (IOException e) {
			}

		}

		return content;
	}

	public static String fileToString(File file) throws IOException {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		String content = "";
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();

			byte[] buf = new byte[StreamUtils.DEFAULT_BUFFER_SIZE];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
		} finally {
			try {
				bos.flush();

				content = bos.toString();

				bos.close();
			} catch (IOException e) {
			}
			try {
				fis.close();
			} catch (IOException e) {
			}

		}

		return content;
	}

	public static boolean isFileExist(String file) {
		return StringUtils.isNotEmpty(file) && new File(file).exists();
	}

	public static boolean isFileExist(File file) {
		return file.exists();
	}

	public static boolean deleteFile(String file) {
		return deleteFile(new File(file));
	}

	public static boolean deleteFile(File file) {
		if (file != null)
			try {
				return file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return false;
	}

	public static List<File> findFiles(String baseDirName, String targetFileNamePattern, boolean subFolder) {
		return findFiles(new File(baseDirName), targetFileNamePattern, subFolder);
	}

	public static List<File> findFiles(File baseDir, String targetFileNamePattern, boolean subFolder) {
		List<File> fileList = new ArrayList<File>();
		if (baseDir == null || !baseDir.exists() || !baseDir.isDirectory()) {
			return fileList;
		}

		Queue<File> subFolders = new LinkedList<File>();

		targetFileNamePattern = targetFileNamePattern.toLowerCase();
		File folder = null;
		subFolders.offer(baseDir);
		while ((folder = subFolders.poll()) != null) {
			File[] allFiles = folder.listFiles();
			for (File file : allFiles) {
				if (subFolder && file.isDirectory()) {
					subFolders.offer(file);
				} else {
					String tempName = file.getName().toLowerCase();
					if (StringUtils.wildcardMatch(targetFileNamePattern, tempName)) {
						fileList.add(file);
					}
				}
			}
		}

		return fileList;
	}

//	public static boolean wildcardMatch(String pattern, String str) {
//		return wildcardMatch(pattern, str, false);
//	}
//
//	public static boolean wildcardMatch(String pattern, String str, boolean caseSensitive) {
//		if (StringUtils.isEmpty(pattern)) {
//			return true;
//		}
//
//		if (!caseSensitive) {
//			pattern = pattern.toLowerCase();
//			str = str.toLowerCase();
//		}
//
//		int patternLength = pattern.length();
//		int strLength = str.length();
//		int strIndex = 0;
//		char ch;
//		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
//			ch = pattern.charAt(patternIndex);
//			if (ch == '*') {
//				while (strIndex < strLength) {
//					if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex), caseSensitive)) {
//						return true;
//					}
//					strIndex++;
//				}
//			} else if (ch == '?') {
//				strIndex++;
//				if (strIndex > strLength) {
//					return false;
//				}
//			} else {
//				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
//					return false;
//				}
//				strIndex++;
//			}
//		}
//		return (strIndex == strLength);
//	}

//	public static boolean wildcardMatch(String filename, String pattern) {
//		// 调用辅助方法，开始匹配
//		return wildcardMatch(filename, pattern, 0, 0, true);
//	}
//
//	public static boolean wildcardMatch(String filename, String pattern, boolean caseSensitive) {
//		// 调用辅助方法，开始匹配
//		return wildcardMatch(filename, pattern, 0, 0, caseSensitive);
//	}
//
//	private static boolean wildcardMatch(String filename, String pattern, int i, int j, boolean caseSensitive) {
//		// 如果模式已经匹配完，检查文件名是否也匹配完
//		if (j == pattern.length()) {
//			return i == filename.length();
//		}
//
//		// 处理模式中的 * 通配符
//		if (pattern.charAt(j) == '*') {
//			// 试图匹配 0 个字符或者多个字符
//			for (int k = 0; k <= filename.length() - i; k++) {
//				if (wildcardMatch(filename, pattern, i + k, j + 1, caseSensitive)) {
//					return true;
//				}
//			}
//			return false;
//		}
//
//		// 处理模式中的 ? 通配符或字符匹配
//		if (i < filename.length()) {
//			char filenameChar = filename.charAt(i);
//			char patternChar = pattern.charAt(j);
//
//			// 如果大小写敏感，则直接比较；否则忽略大小写进行比较
//			boolean charsMatch = caseSensitive ? (patternChar == filenameChar) : (Character.toLowerCase(patternChar) == Character.toLowerCase(filenameChar));
//
//			if (charsMatch || patternChar == '?') {
//				return wildcardMatch(filename, pattern, i + 1, j + 1, caseSensitive);
//			}
//		}
//
//		// 如果字符不匹配
//		return false;
//	}

	public static void listFiles(String baseDirName, boolean subFolder, FileFilter filter, final ObjectHandler<Void, File> handler) {
		Queue<File> subFolders = new LinkedList<File>();

		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return;
		}

		File folder = null;
		subFolders.offer(baseDir);
		while ((folder = subFolders.poll()) != null) {
			File[] allFiles;
			if (filter != null) {
				allFiles = folder.listFiles(filter);
			} else {
				allFiles = folder.listFiles();
			}
			for (File file : allFiles) {
				if (subFolder && file.isDirectory()) {
					subFolders.offer(file);
				} else {
					// String tempName = file.getName();
					// if (wildcardMatch(targetFileName, tempName)) {
					handler.handle(null, file);
					// }
				}
			}
		}
	}

	public static List<File> listFiles(String targetPathPattern, boolean subFolder) {
		targetPathPattern = targetPathPattern.replace('\\', File.separatorChar);
		targetPathPattern = targetPathPattern.replace('/', File.separatorChar);

		String folder, fileNamePattern;
		if (targetPathPattern.contains("*")) {
			// /home/*.txt
			// /home/*
			// /*
			if (targetPathPattern.contains(File.separator)) {
				int i = targetPathPattern.lastIndexOf(File.separatorChar);
				folder = targetPathPattern.substring(0, i);
				fileNamePattern = targetPathPattern.substring(i + 1);
			} else {
				// *.txt
				folder = "./";
				fileNamePattern = targetPathPattern;
			}
		} else {
			if (FileUtils.isFileExist(targetPathPattern)) {
				// /home/folder
				if (FileUtils.isDirectory(targetPathPattern)) {
					folder = targetPathPattern;
					fileNamePattern = "*";
				} else {
					// /home/xxx.txt
					// xxx.txt

					folder = null;
					fileNamePattern = null;

					List<File> fileList = new ArrayList<File>();
					fileList.add(new File(targetPathPattern));
					return fileList;
				}
			} else {
				return new ArrayList<File>();
			}
		}

		return FileUtils.listFiles(folder, fileNamePattern, subFolder);
	}

	public static List<File> listFiles(String baseDirName, String targetFileName, boolean subFolder) {
		List<File> fileList = new ArrayList<File>();
		Queue<File> subFolders = new LinkedList<File>();

		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return fileList;
		}

		File folder = null;
		subFolders.offer(baseDir);
		while ((folder = subFolders.poll()) != null) {
			File[] allFiles = folder.listFiles();
			for (File file : allFiles) {
				if (subFolder && file.isDirectory()) {
					subFolders.offer(file);
				} else {
					String tempName = file.getName();
					if (StringUtils.wildcardMatch(targetFileName, tempName)) {
						fileList.add(file);
					}
				}
			}
		}

		return fileList;
	}

	public static void listDirectoryStream(File baseDir, FileFilter filter, boolean subFolder, final ObjectHandler<Void, File> handler) throws IOException {
		if (baseDir != null && !baseDir.exists() || !baseDir.isDirectory()) {
			return;
		}

		List<File> subFolders = new ArrayList<File>();

		DirectoryStream<Path> stream = null;
		try {
			Path dir = Paths.get(baseDir.toURI());
			stream = Files.newDirectoryStream(dir);
			for (Path path : stream) {
				File file = path.toFile();

				if (subFolder && file.isDirectory()) {
					subFolders.add(file);
				} else {
					if (filter == null) {
						handler.handle(null, file);
					} else if (filter.accept(file)) {
						handler.handle(null, file);
					}
				}
			}
		} catch (Throwable e) {
			// logger.error("Failed to access dir [" + baseDir + "]", e);
			handler.exceptionCaught(baseDir, e);
			// never return continue running
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

		for (File subDir : subFolders) {
			listDirectoryStream(subDir, filter, subFolder, handler);
		}
	}

	public static void listDirectoryStream(File baseDir, String targetFileNamePattern, boolean subFolder, final ObjectHandler<Void, File> handler) throws IOException {
		if (baseDir != null && !baseDir.exists() || !baseDir.isDirectory()) {
			return;
		}

		List<File> subFolders = new ArrayList<File>();

		DirectoryStream<Path> stream = null;
		try {
			Path dir = Paths.get(baseDir.toURI());
			stream = Files.newDirectoryStream(dir);
			for (Path path : stream) {
				File file = path.toFile();
				if (subFolder && file.isDirectory()) {
					subFolders.add(file);
				} else {
					String tempName = file.getName();
					if (StringUtils.wildcardMatch(targetFileNamePattern, tempName)) {
						HandleFeedback feedback = handler.handle(null, file);
						if (feedback == HandleFeedback.failed || feedback == HandleFeedback.interrupted) {
							return;
						}
					}
				}
			}
		} catch (Throwable e) {
			// logger.error("Failed to access dir [" + baseDir + "]", e);
			handler.exceptionCaught(baseDir, e);
			// never return continue running
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

		for (File subDir : subFolders) {
			listDirectoryStream(subDir, targetFileNamePattern, subFolder, handler);
		}
	}

	// public static boolean wildcardMatch(String pattern, String str) {
	// if (StringUtils.isEmpty(pattern) || StringUtils.isEmpty(str)) {
	// return true;
	// }
	//
	// int patternLength = pattern.length();
	// int strLength = str.length();
	// int strIndex = 0;
	// char ch;
	// for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
	// ch = pattern.charAt(patternIndex);
	// if (ch == '*') {
	// while (strIndex < strLength) {
	// if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
	// return true;
	// }
	// strIndex++;
	// }
	// } else if (ch == '?') {
	// strIndex++;
	// if (strIndex > strLength) {
	// return false;
	// }
	// } else {
	// if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
	// return false;
	// }
	// strIndex++;
	// }
	// }
	// return (strIndex == strLength);
	// }

	public static boolean isDirectory(String folderPath) {
		return StringUtils.isNotEmpty(folderPath) && new File(folderPath).isDirectory();
	}

	public static void readLines(File file, final RecordHandler<Long, String> recordHandler) throws IOException {
		StreamUtils.readLines(new FileInputStream(file), recordHandler);
	}

//	public static boolean isBinaryFile(File file) throws IOException {
//        String type = Files.probeContentType(file.toPath());
////        System.out.println(type+" "+file);
//        if (type == null) {
//            //type couldn't be determined, assume binary
//            return true;
//        } else if (type.startsWith("text")) {
//            return false;
//        } else {
//            //type isn't text
//            return true;
//        }
//    }

	public static boolean isBinaryFile(File f) throws FileNotFoundException, IOException {
		return !isAsciiFile(f);
	}

	public static boolean isAsciiFile(File f) throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(f);
		int size = in.available();
		if (size > 1024)
			size = 1024;
		byte[] data = new byte[size];
		in.read(data);
		in.close();

		int ascii = 0;
		int other = 0;

		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
//	        System.out.print((char)b);
//	        if( b < 0x09 ) return true;
			if (b > 31 && b < 127)
				ascii++;
			else if (b > 9 && b < 14)
//			else if (b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D)
				ascii++;
			else
				other++;
		}

		if (other == 0)
			return true;

//		System.out.println(f + " " + ((double) ascii / (double) (size)));
		return (((double) ascii / (double) (size)) >= 0.9);
	}

	public static void zipFolder(String sourceFolder, String zipFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		try {
			// Creating a ZipOutputStream by wrapping a FileOutputStream
			Path sourcePath = Paths.get(sourceFolder);
			// Walk the tree structure using WalkFileTree method
			Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				@Override
				// Before visiting the directory create the directory in zip archive
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					// Don't create dir for root folder as it is already created with .zip name
					if (!sourcePath.equals(dir)) {
//						System.out.println("Directory- " + dir);
						zos.putNextEntry(new ZipEntry(sourcePath.relativize(dir).toString() + "/"));
						zos.closeEntry();
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				// For each visited file add it to zip entry
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
//					System.out.println("File Name- " + sourcePath.relativize(file).toString());
					zos.putNextEntry(new ZipEntry(sourcePath.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		} finally {
			zos.close();
			fos.close();
		}
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();

		int i = fileName.lastIndexOf('.');
		if (i == -1) {
			return "";
		}

		return fileName.substring(i + 1);
	}

	public static String getParentPath(String path) {
		return new File(path).getParent();
	}

	public static void deleteParentFolder(String file) {
		File parent = new File(file).getParentFile();
//		if(parent.exists()) {
		parent.delete();
//		}
	}

	private static String USER_HOME_FOLDER = null;
	private static String INSTALL_FOLDER = null;
	private static String TEMP_WORKING_FOLDER = null;

	public static String getUserHomeFolder() {
		if (USER_HOME_FOLDER == null) {
			USER_HOME_FOLDER = System.getProperty("user.home");
		}
		return USER_HOME_FOLDER;
	}

	public static String getInstallFolder() {
		if (USER_HOME_FOLDER == null) {
			String installPath = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
			INSTALL_FOLDER = installPath;
		}

		return INSTALL_FOLDER;
	}

	public static String getTempWorkingFolder() {
		if (TEMP_WORKING_FOLDER == null) {
			final String systemTempFolder = System.getProperty("java.io.tmpdir");
			final String scetwf = URLUtils.catFilePath(systemTempFolder, "amituofo");
			File FD = new File(scetwf + File.separator + "temp");
			if (!FD.exists()) {
				FD.mkdirs();
			}
			TEMP_WORKING_FOLDER = FD.getPath();
		}

		return TEMP_WORKING_FOLDER;
	}

	public static String getTempWorkingFolder(String name) {
		File tempWorkingDir = new File(getTempWorkingFolder() + File.separator + name);
		tempWorkingDir.mkdirs();
		return tempWorkingDir.getPath();
	}

	public static String getTempRandomWorkingFolder() {
		File tempWorkingDir = new File(getTempWorkingFolder() + File.separator + RandomUtils.randomInt(100000, 999999));
		tempWorkingDir.mkdirs();
		return tempWorkingDir.getPath();
	}

	public static String getTempRemoteFileEditingFolder() {
		File tempWorkingDir = new File(getTempWorkingFolder() + File.separator + "edit");
		tempWorkingDir.mkdirs();
		return tempWorkingDir.getPath();
	}

	public static String getTempRemoteFileEditingRandomFolder() {
		File tempWorkingDir = new File(getTempWorkingFolder() + File.separator + "edit" + File.separator + System.currentTimeMillis() + "_" + RandomUtils.randomInt(100000, 999999));
		tempWorkingDir.mkdirs();
		return tempWorkingDir.getPath();
	}

	public static void clearTemp() {
		FileUtils.deleteDirectoryContent(getTempWorkingFolder());
	}

	public static void readFileContent(File file, HandleBytes handler) throws HandleException, FileNotFoundException, IOException {
		long remainlen = file.length();

		FileInputStream ins = new FileInputStream(file);

		StreamUtils.readContent(ins, remainlen, handler);
	}

	public static byte[] readFileContent(File file, int offset, int partlen) throws IOException {
		FileInputStream ins = null;
		try {
			ins = new FileInputStream(file);
			ins.skip(offset);
			byte[] data = new byte[partlen];
			ins.read(data);
			return data;
		} finally {
			if (ins != null)
				ins.close();
		}
	}

	public static boolean isExecutable(File file) {
		if (SystemUtils.isWindows()) {
			return isWindowsExecutable(file);
		}
		return isUnixExecutable(file);
	}

	// Windows: 检查扩展名是否在 PATHEXT 中
	public static boolean isWindowsExecutable(File file) {
		String name = file.getName().toLowerCase(Locale.ROOT);
		String pathext = System.getenv("PATHEXT");
		if (pathext != null) {
			for (String ext : pathext.split(";")) {
				if (name.endsWith(ext.toLowerCase(Locale.ROOT))) {
					return true;
				}
			}
		}

		String extname = file.getName();
		int i = extname.lastIndexOf('.');
		if (i > 0) {
			extname = extname.substring(i);
		}
		// 一般常见的默认扩展
		return ".com;.exe;.bat;.cmd;.vbs;.vbe;.js;.jse;.wsf;.wsh;.msc;.lnk".contains(extname);
	}

	// Linux/macOS: 检查可执行权限
	public static boolean isUnixExecutable(File file) {
		return file.canExecute();
	}

	public static File[] toFileList(String[] filePaths, boolean checkExistence) {
		List<File> files = new ArrayList<>();

		if (filePaths != null && filePaths.length > 0) {
			for (String filePath : filePaths) {
				File file = new File(filePath);
				if (checkExistence) {
					if (file.exists()) {
						files.add(file);
					}
				} else {
					files.add(file);
				}
			}
		}

		return files.toArray(new File[files.size()]);
	}

	public static boolean isFolderExist(String dirpath) {
		return isFolderExist(new File(dirpath));
	}

	public static boolean isFolderExist(File file) {
		return file != null && file.exists() && file.isDirectory();
	}

	public static boolean isFilePathEquals(String path1, String path2, boolean ignoreCase) {
		boolean path1empty = StringUtils.isEmpty(path1);
		boolean path2empty = StringUtils.isEmpty(path2);

		if (path1empty && path2empty) {
			return true;
		}

		if (path1empty && !path2empty || !path1empty && path2empty) {
			return false;
		}

		if (path1.length() != path2.length()) {
			return false;
		}

		if (path1.indexOf('\\') != -1) {
			path1 = path1.replace('\\', '/');
		}
		if (path2.indexOf('\\') != -1) {
			path2 = path2.replace('\\', '/');
		}

		boolean path1IsLastCharSeperator = StringUtils.isLastCharSeperator(path1);
		boolean path2IsLastCharSeperator = StringUtils.isLastCharSeperator(path2);

		if (path1IsLastCharSeperator != path2IsLastCharSeperator) {
			if (!path1IsLastCharSeperator) {
				path1 = path1 + "/";
			}
			if (!path2IsLastCharSeperator) {
				path2 = path2 + "/";
			}
		}

		if (ignoreCase) {
			return path1.equalsIgnoreCase(path2);
		} else {
			return path1.equals(path2);
		}
	}

	public static <T> T readJSONObject(File file, Class<T> class1) throws FileNotFoundException, IOException {
		String json = readToString(file);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseObject(json, class1);
	}

	public static <T> List<T> readJSONArray(File file, Class<T> class1) throws FileNotFoundException, IOException {
		String json = readToString(file);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return JSON.parseArray(json, class1);
	}

	public static String getPathWithoutExtension(File file) {
		if (file == null) {
			return "";
		}

		return getPathWithoutExtension(file.getPath());
	}

	public static String getPathWithoutExtension(String filePath) {
		if (filePath == null) {
			return "";
		}

		int dotIndex = filePath.lastIndexOf('.');

		if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
			return filePath.substring(0, dotIndex); // Remove extension
		} else {
			return filePath; // No extension found, return the full path
		}
	}

	/**
	 * 获取文件名后缀（不含点）
	 * 示例: "test.txt" -> "txt", "archive.tar.gz" -> "gz"
	 * @param name 文件名或路径
	 * @return 后缀名，如果没有后缀则返回空字符串 ""
	 */
	public static String getFileNameExtension(String name) {
		if (name == null) {
			return "";
		}

		// 1. 找到最后一个点的位置
		int i = name.lastIndexOf('.');
		if (i > 0) {
			String _ext_ = name.substring(i + 1);
			if (_ext_.length() > 15) {
				_ext_ = "";
			} else {
				_ext_ = _ext_.toLowerCase();
			}
			return _ext_;
		}

		return "";
	}
	
}