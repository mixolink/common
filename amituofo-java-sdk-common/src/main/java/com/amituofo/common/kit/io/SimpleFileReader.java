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
package com.amituofo.common.kit.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleFileReader {
	private String defaultEncoding = "UTF-8";

	private BufferedReader bufferedReader = null;
	private InputStreamReader streamReader = null;
	private InputStream inStream = null;

	private int currentLineNumber = 0;

	private File file = null;

	private InputStream customInStream = null;

	public SimpleFileReader(InputStream customInStream) {
		this.customInStream = customInStream;
		// try {
		// this.defaultEncoding = detectEncoding(customInStream).name();
		// } catch (Exception e) {
		// defaultEncoding = "UTF-8";
		// }
	}

	public SimpleFileReader(InputStream customInStream, String charsetName) {
		this.customInStream = customInStream;
		this.defaultEncoding = charsetName;
	}

	public SimpleFileReader(File file) {
		this.file = file;
		// try {
		// this.defaultEncoding = detectEncoding(new BufferedInputStream(new FileInputStream(file))).name();
		// } catch (Exception e) {
		// defaultEncoding = "UTF-8";
		// }
	}

	public SimpleFileReader(String file) {
		this(new File(file));
	}

	public SimpleFileReader(File file, String charsetName) {
		this.file = file;
		// this.defaultEncoding.charsetName = charsetName;
		this.defaultEncoding = charsetName;
	}

	public SimpleFileReader(String file, String charsetName) {
		this(new File(file), charsetName);
	}

	/**
	 * Read all data to buffer
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws IOException
	 */
	public StringBuffer readAll() throws IOException {
		StringBuffer content = null;

		closeReader();
		if (createReader()) {
			content = new StringBuffer();
			try {
				char[] buf = new char[512];
				int size = 0;
				while ((size = bufferedReader.read(buf, 0, 512)) != -1) {
					content.append(buf, 0, size);
				}
			} finally {
				closeReader();
			}
		}

		return content;
	}

	public List<String> readLines(int fromRow, int toRow) throws IOException {
		if (fromRow > toRow || fromRow <= 0) {
			return null;
		}

		List<String> content = null;

		closeReader();
		if (createReader()) {
			int currentLineNum = 1;
			String line;
			try {
				while (currentLineNum < fromRow) {
					line = bufferedReader.readLine();
					if (line == null) {
						return content;
					}
					currentLineNum++;
				}

				content = new ArrayList<String>();

				while (currentLineNum <= toRow) {
					line = bufferedReader.readLine();
					if (line == null) {
						return content;
					}
					content.add(line);
					currentLineNum++;
				}
			} finally {
				closeReader();
			}
		}

		return content;
	}

	public String readFirstLine() throws IOException {
		closeReader();
		if (createReader()) {
			return readNextLine();
		}

		return null;
	}

	public String readNextLine() throws IOException {
		String row = null;

		if (bufferedReader != null) {
			row = bufferedReader.readLine();
			if (row != null) {
				currentLineNumber++;
			} else {
				closeReader();
			}
		}

		return row;
	}

	public long getTotalLines() throws IOException {
		int count = 0;
		String record = this.readFirstLine();
		if (record == null) {
			this.closeReader();
			return 0;
		} else {
			do {
				count++;
				record = this.readNextLine();
			} while (record != null);

			this.closeReader();

			return count;
		}
	}

	public List<String> readFirstLines(int readCount) throws IOException {
		if (readCount <= 0) {
			return null;
		}

		closeReader();
		if (createReader()) {
			return readNextLines(readCount);
		}

		return null;
	}

	public List<String> readNextLines(int readCount) throws IOException {
		if (readCount <= 0) {
			return null;
		}

		List<String> content = null;

		if (bufferedReader != null) {
			int index = 1;
			String row = null;
			content = new ArrayList<String>();

			do {
				row = bufferedReader.readLine();
				if (row == null) {
					closeReader();
					break;
				} else {
					content.add(row);
					currentLineNumber++;
					index++;
				}
			} while (index <= readCount);
		}

		return content;
	}

	private boolean createReader() throws IOException {
		if (this.file != null) {
			try {
				// Bom size will be not 0 when UTF format file
				int bomsize = bomSize();

				// reset line number
				currentLineNumber = 0;
				inStream = new FileInputStream(file);
				if (defaultEncoding != null) {
					streamReader = new InputStreamReader(inStream, defaultEncoding);
				} else {
					streamReader = new InputStreamReader(inStream);
				}
				bufferedReader = new BufferedReader(streamReader);

				if (bomsize != 0) {
					byte[] buf = new byte[bomsize];
					inStream.read(buf);
				}
			} catch (IOException e) {
				closeReader();
				throw e;
				// return false;
			}
			return true;
		}

		if (this.customInStream != null) {
			try {
				// reset line number
				currentLineNumber = 0;
				if (defaultEncoding != null) {
					streamReader = new InputStreamReader(customInStream, defaultEncoding);
				} else {
					streamReader = new InputStreamReader(customInStream);
				}
				bufferedReader = new BufferedReader(streamReader);
			} catch (IOException e) {
				closeReader();
				throw e;
				// return false;
			}
			return true;
		}

		return false;
	}

	protected int bomSize() throws IOException {
		int BOM_SIZE = 4;
		byte bom[] = new byte[BOM_SIZE];
		int bomsize = 0;
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			is.read(bom, 0, bom.length);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			defaultEncoding = "UTF-8";
			bomsize = 3;
		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			defaultEncoding = "UTF-32BE";
			bomsize = 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			defaultEncoding = "UTF-32LE";
			bomsize = 4;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			defaultEncoding = "UTF-16BE";
			bomsize = 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			defaultEncoding = "UTF-16LE";
			bomsize = 2;
		} else {
			bomsize = 0;
		}

		return bomsize;
	}

	public void closeReader() {
		// do not reset line number
		// currentLineNumber = 0;
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
			}
			bufferedReader = null;
		}
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
			}
			inStream = null;
		}
		if (streamReader != null) {
			try {
				streamReader.close();
			} catch (IOException e) {
			}
			streamReader = null;
		}
	}

	public int getCurrentLineNumber() {
		return currentLineNumber;
	}
}
