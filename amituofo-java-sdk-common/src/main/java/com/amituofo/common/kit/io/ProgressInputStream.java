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

import java.io.IOException;
import java.io.InputStream;

import com.amituofo.common.api.Interruptable;
import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.define.Constants;
import com.amituofo.common.kit.event.ProgressPusher;
import com.amituofo.common.kit.event.StepProgressPusher;
import com.amituofo.common.type.ReadProgressEvent;

public class ProgressInputStream extends InterruptableInputStream {
	private static int DEFAULT_STEP_COUNT = 100;
	private static long MAX = 2147483647L * DEFAULT_STEP_COUNT - Constants.SIZE_10MB;

	private final StepProgressPusher pusher;
	private final int STEPLEN;
	private int pushlen;

	public ProgressInputStream(InputStream in, long length, StepProgressListener progressListener) {
		this(in, length, progressListener, null);
	}

	public ProgressInputStream(InputStream in, long length, StepProgressListener progressListener, Interruptable interrupter) {
		super(in, interrupter);
		this.pusher = new StepProgressPusher(progressListener);

		if (length > 0) {
			int stepcount = DEFAULT_STEP_COUNT;
			if (length > MAX) {
				stepcount = (int) (length / (2147483647L + Constants.SIZE_1MB));
			}

			this.STEPLEN = (int) (length / stepcount);
		} else {
			this.STEPLEN = this.pushlen = 0;
		}
	}

	public int read() throws IOException {
		int v = super.read();
		if (v != -1) {
			progressChanged(1);
		} else {
			progressChanged(-1);
		}
		return v;
	}

	public int read(byte[] b) throws IOException {
		int readLen = super.read(b);
		progressChanged(readLen);
		return readLen;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int readLen = super.read(b, off, len);
		progressChanged(readLen);
		return readLen;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public long skip(long n) throws IOException {
		return super.skip(n);
	}

	public String toString() {
		return super.toString();
	}

	public int available() throws IOException {
		return super.available();
	}

	public void close() throws IOException {
		if (!super.isClosed()) {
			try {
				super.close();
			} finally {
				if (pushlen > 0) {
					pusher.push(ReadProgressEvent.BYTE_READING_EVENT, pushlen);
				}

				if (pushlen != -1) {
					pushlen = -1;
					pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
				}
				pusher.stop();
			}
		}
	}

	public void mark(int readlimit) {
		super.mark(readlimit);
	}

	public void reset() throws IOException {
		super.reset();
	}

	public boolean markSupported() {
		return super.markSupported();
	}

	private void progressChanged(int len) {
		if (len == -1) {
			synchronized (pusher) {
				if (pushlen > 0) {
					pusher.push(ReadProgressEvent.BYTE_READING_EVENT, pushlen);
				}
				if (pushlen != -1) {
					pushlen = -1;
					pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
				}
			}
		} else if (len > 0) {
			if (STEPLEN == 0) {
				pusher.push(ReadProgressEvent.BYTE_READING_EVENT, len);
			} else {
				pushlen += len;
				if (pushlen >= STEPLEN) {
					pusher.push(ReadProgressEvent.BYTE_READING_EVENT, pushlen);
					pushlen = 0;
				}
			}
		}
	}

}
