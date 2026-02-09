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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SimpleFileWriter {
	private String defaultEncoding = "UTF-8";

	private OutputStreamWriter streamWriter = null;
	private OutputStream outStream = null;

	private File file = null;

	private OutputStream customOutStream = null;

	public SimpleFileWriter(OutputStream customOutStream) {
		this.customOutStream = customOutStream;
	}

	public SimpleFileWriter(OutputStream customOutStream, String charsetName) {
		this.customOutStream = customOutStream;
		this.defaultEncoding = charsetName;
	}

	public SimpleFileWriter(File file) {
		this.file = file;
	}

	public SimpleFileWriter(String file) {
		this(new File(file));
	}

	public SimpleFileWriter(File file, String charsetName) {
		this.file = file;
		this.defaultEncoding = charsetName;
	}

	public SimpleFileWriter(String file, String charsetName) {
		this(new File(file), charsetName);
	}

	public boolean createWriter() throws IOException {
		return createWriter(false);
	}

	public boolean createWriter(boolean append) throws IOException {
		// try {
		closeWriter();

		if (this.file != null) {
			// file.
			File parentFolder = new File(file.getCanonicalFile().getParent());
			if (!parentFolder.exists()) {
				if (!parentFolder.mkdirs()) {
					return false;
				}
			}
			outStream = new FileOutputStream(file, append);

			if (defaultEncoding != null) {
				streamWriter = new OutputStreamWriter(outStream, defaultEncoding);
			} else {
				streamWriter = new OutputStreamWriter(outStream);
			}
			// } catch (Throwable e) {
			// return false;
			// }
			return true;
		}

		if (this.customOutStream != null) {
			return createWriter(customOutStream);
		}

		return false;
	}

	public boolean createWriter(OutputStream outStream) throws IOException {
		this.customOutStream = outStream;
		if (defaultEncoding != null) {
			streamWriter = new OutputStreamWriter(customOutStream, defaultEncoding);
		} else {
			streamWriter = new OutputStreamWriter(customOutStream);
		}

		return true;
	}

	/**
	 * Write buffer data to file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws IOException
	 */
	public void write(String content) throws IOException {
		if (content == null) {
			content = "";
		}

		streamWriter.write(content);
	}

	public void writeLine(String content) throws IOException {
		if (content == null) {
			content = "";
		}

		streamWriter.write(content + "\n");
	}

	public void closeWriter() {
		try {
			if (streamWriter != null) {
				streamWriter.flush();
				streamWriter.close();
				streamWriter = null;
			}

			if (outStream != null) {
				outStream.close();
				outStream = null;
			}
		} catch (IOException e) {
		}
	}

	public static void write(File file, String content, boolean append) throws IOException {
		if (content == null) {
			content = "";
		}

		BufferedWriter out = null;
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, append);
			out = new BufferedWriter(writer);
			out.write(content);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}
}
