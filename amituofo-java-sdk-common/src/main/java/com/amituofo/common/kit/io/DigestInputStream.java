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

public class DigestInputStream extends BaseInputStream {
	private MessageDigest md = null;

	public DigestInputStream(InputStream in, String calcHashAlgorithmName) throws NoSuchAlgorithmException {
		super(in);
		this.md = MessageDigest.getInstance(calcHashAlgorithmName);
	}

	public DigestInputStream(InputStream in, MessageDigest md) {
		super(in);
		this.md = md;
	}

	// private double readBytes = 0;
	// Timer timer = null;
	// private void init() {
	// timer = new Timer();
	// timer.schedule(new TimerTask() {
	//
	// @Override
	// public void run() {
	// System.out.println(readBytes+" "+readBytes/(1024)+"KB/S "+readBytes/(1024*1024)+"MB/S "+(8*readBytes)/(1024*1024)+"Mbps");
	//
	// readBytes = 0;
	// }}, 1000,1000);
	// }

	public int read() throws IOException {
		int c = in.read();

		if (c > 0) {
			md.update((byte) c);
		}

		return c;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int readlen = in.read(b);

		if (readlen > 0) {
			md.update(b, 0, readlen);
		}

		return readlen;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readlen = in.read(b, off, len);

		if (readlen > 0) {
			md.update(b, 0, readlen);
		}

		return readlen;
	}

	public byte[] getDigest() {
		return md.digest();
	}

	public int hashCode() {
		return in.hashCode();
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
		in.close();
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
