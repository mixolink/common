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
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.amituofo.common.api.ByteArrayOutputListener;

public class AsynchronousFileWriter {
	public static final int MINIMUM_WRITE_BUFFER_SIZE = 1024 * 8;

	public final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 4;

	private final Queue<ByteBuffer> bufferQueue = new ConcurrentLinkedQueue<ByteBuffer>();

	private final File file;
	private final long offset;
	private final InputStream in;

	private long writeLength = 0;
	private boolean readFinished = false;
	private boolean writeFinished = false;
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private boolean waitForComplete = true;

	private int id;
	private ByteArrayOutputListener listener = null;

	public AsynchronousFileWriter(File file, int id, long offset, InputStream in) {
		this.file = file;
		this.offset = offset;
		this.id = id;
		this.in = in;
	}

	// public FileWriter(String file, long offset, long length, InputStream in) {
	// this.file = file;
	// this.offset = offset;
	// this.length = length;
	// this.in = in;
	// }

	public long getBufferSize() {
		return bufferSize;
	}

	public ByteArrayOutputListener getListener() {
		return listener;
	}

	public void setListener(ByteArrayOutputListener listener) {
		this.listener = listener;
	}

	public void setBufferSize(int bufferSize) {
		if (bufferSize < MINIMUM_WRITE_BUFFER_SIZE) {
			this.bufferSize = MINIMUM_WRITE_BUFFER_SIZE;
		} else {
			this.bufferSize = bufferSize;
		}
	}

	public void write() throws IOException {
		new Thread(new Writer()).start();

		ByteBuffer buffer = null;

		try {
			buffer = readNext();
			while (buffer != null) {
				if (bufferQueue.size() > 1000) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
					}
					continue;
				}

				bufferQueue.add(buffer);

				buffer = readNext();
			}
		} finally {
			readFinished = true;

			try {
				in.close();
			} catch (Exception e1) {
			}

			if (waitForComplete) {
				while (true) {
					if (!writeFinished) {
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
						}
					} else {
						break;
					}
				}
			}
		}
	}

	private ByteBuffer readNext() throws IOException {
		byte[] buf = new byte[bufferSize];

		int totalLen = 0;
		int len = in.read(buf);
		if (len > 0) {
			totalLen += len;
			int remainSize = bufferSize - len;
			while (remainSize > 0) {
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

			return buffer;
		} else {
			return null;
		}
	}

	public void waitForComplete(boolean wait) {
		waitForComplete = wait;
	}

	public long getWriteLength() {
		return writeLength;
	}

	// class PartBuffer {
	// public PartBuffer(int bufferSize) {
	// buffer = new byte[bufferSize];
	// }
	//
	// public byte[] buffer = null;
	// public int length = 0;
	// }

	class Writer implements Runnable {
		private RandomAccessFile raf = null;
		private FileChannel fc = null;
		// private PartBuffer buffer = null;
		private ByteBuffer buffer = null;

		public void run() {
			try {
				raf = new RandomAccessFile(file, "rw");
				raf.seek(offset);

				fc = raf.getChannel();

				buffer = bufferQueue.poll();
				while (!readFinished || buffer != null) {
					if (buffer == null) {
						Thread.sleep(2);
					} else {
						// raf.write(buffer.buffer, 0, buffer.length);
						fc.write(buffer);

						writeLength += buffer.capacity();

						if (listener != null) {
							listener.outProgress(id, offset, buffer.capacity());
						}
					}

					buffer = bufferQueue.poll();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				writeFinished = true;

				try {
					fc.close();
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
