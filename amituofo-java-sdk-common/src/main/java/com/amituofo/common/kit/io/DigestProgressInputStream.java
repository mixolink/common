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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.kit.event.StepProgressPusher;
import com.amituofo.common.type.ReadProgressEvent;

public class DigestProgressInputStream extends BaseInputStream {
	private boolean closed = false;
	private final StepProgressPusher pusher;
	private final MessageDigest md;

	// private final AsynchronousProcess<byte[]> processer = new AsynchronousProcess<byte[]>(new ProcessEvent<byte[]>() {
	// @Override
	// public void valueChanged(int event, byte[] data) {
	// System.out.println("md="+c++);
	// md.update(data, 0, event);
	// }
	// });

	public DigestProgressInputStream(InputStream in, String calcHashAlgorithmName, StepProgressListener progressListener) throws NoSuchAlgorithmException {
		super(in);
		this.md = MessageDigest.getInstance(calcHashAlgorithmName);
		// Assert pl!=null
		this.pusher = new StepProgressPusher(progressListener);
	}

	public DigestProgressInputStream(InputStream in, MessageDigest md, StepProgressListener progressListener) {
		super(in);
		this.md = md;
		// Assert pl!=null
		this.pusher = new StepProgressPusher(progressListener);
	}

	public int hashCode() {
		return in.hashCode();
	}

	public int read() throws IOException {
		int c = in.read();

		if (c > 0) {
			md.update((byte) c);
			// processer.push(1, new byte[] {(byte) c});
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, 1);
		} else {
			pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
			pusher.stop();
			// processer.stopWhenFinished();
		}

		return c;
	}

	public int read(byte[] b) throws IOException {
		int readLen = in.read(b);

		if (readLen > 0) {
			md.update(b, 0, readLen);
			// processer.push(readLen, Arrays.copyOf(b, readLen));
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, readLen);
		} else {
			pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
			pusher.stop();
			// processer.stopWhenFinished();
		}

		return readLen;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int readLen = in.read(b, off, len);

		if (readLen > 0) {
			md.update(b, 0, readLen);
			// processer.push(readLen, Arrays.copyOf(b, readLen));
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, readLen);
		} else {
			pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
			pusher.stop();
			// processer.stopWhenFinished();
		}

		return readLen;
	}

	public byte[] getDigest() {
		// processer.stopWhenFinished();
		return md.digest();
	}

	public boolean equals(Object obj) {
		return in.equals(obj);
	}

	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	public String toString() {
		return in.toString();
	}

	public int available() throws IOException {
		return in.available();
	}

	public void close() throws IOException {
		if (!closed) {
			closed = true;
			try {
				in.close();
			} catch (IOException e) {
				throw e;
			} finally {
				pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
				pusher.stop();
				// processer.stopWhenFinished();
			}
		}
	}

	public void mark(int readlimit) {
		in.mark(readlimit);
	}

	public void reset() throws IOException {
		in.reset();
	}

	public boolean markSupported() {
		return in.markSupported();
	}

}
