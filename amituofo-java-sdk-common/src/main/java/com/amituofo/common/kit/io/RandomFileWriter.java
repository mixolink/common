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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.amituofo.common.api.Interruptable;
import com.amituofo.common.util.StreamUtils;

public class RandomFileWriter implements Runnable, Interruptable {
	public static final int MINIMUM_WRITE_BUFFER_SIZE = 512;
	public static final int DEFAULT_BUFFER_SIZE = MINIMUM_WRITE_BUFFER_SIZE * 1024 * 4;// * 1024 * 4;

	private final File file;
	private final long offset;
	private final InputStream in;

	private long writeLength = 0;
	private boolean finished = false;
	private int bufferSize = DEFAULT_BUFFER_SIZE;

	boolean interrupted = false;

	public RandomFileWriter(File file, long offset, InputStream in) {
		this.file = file;
		this.offset = offset;
		this.in = in;
	}

	public void run() {
		try {
			write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		if (bufferSize < MINIMUM_WRITE_BUFFER_SIZE) {
			this.bufferSize = MINIMUM_WRITE_BUFFER_SIZE;
		} else {
			this.bufferSize = bufferSize;
		}
	}

	public long write() throws IOException {
		if (interrupted) {
			return 0;
		}

		RandomAccessFile raf = null;
		FileChannel fc = null;

		FileOutputStream out = null;

		try {
//			if(!file.canWrite()) {
//				throw new IOException("File can not be written. Please check permission or disable readonly attribute.");
//			}

			if (file.exists() && this.offset > 0) {
				raf = new RandomAccessFile(file, "rw");
				// raf.seek(offset);

				fc = raf.getChannel();
				fc.position(offset);
			} else {
				File parentDir = file.getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				out = new FileOutputStream(file);
				fc = out.getChannel();
			}

//			raf = new RandomAccessFile(file, "rw");
//			fc = raf.getChannel();
//			if (this.offset > 0) {
//				fc.position(offset);
//			}

			ByteBuffer buff = readNext();
			while (buff != null) {
				if (interrupted) {
					break;
				}

				// raf.write(buffer, 0, len);
				fc.write(buff);

				// if (interrupter != null) {
				// if (interrupter.isInterrupted()) {
				// interrupter.interrupted();
				// break;
				// }
				// }

				int len = buff.capacity();
				writeLength += len;

				buff = readNext();
			}
		} catch (Exception e) {
			if (!interrupted) {
				e.printStackTrace();
				throw new IOException(e);
			}
		} finally {
			finished = true;

			StreamUtils.close(fc);
			StreamUtils.close(raf);
			StreamUtils.close(out);
			StreamUtils.close(in);
		}

		return writeLength;
	}

	private ByteBuffer readNext() throws IOException {
		byte[] buf = new byte[bufferSize];

		int totalLen = 0;
		int len = in.read(buf);
		if (len > 0) {
			totalLen += len;
			int remainSize = bufferSize - len;
			while (remainSize > 0) {
				if (interrupted) {
					return null;
				}
				
				len = in.read(buf, totalLen, remainSize);
				if (len <= 0) {
					break;
				} else {
					totalLen += len;
					remainSize -= len;
				}
			}

			ByteBuffer buffer = ByteBuffer.allocate(totalLen);
			buffer.put(buf, 0, totalLen);
			buffer.flip();

			buf = null;

			return buffer;
		} else {
			return null;
		}
	}

	public long getWriteLength() {
		return writeLength;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean interrupt() {
		interrupted = true;
		return true;
	}

	@Override
	public boolean isInterrupted() {
		return interrupted;
	}

}