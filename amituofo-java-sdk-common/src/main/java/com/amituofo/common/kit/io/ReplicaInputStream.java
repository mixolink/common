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

public class ReplicaInputStream extends InputStream {
	private byte[] buffer = new byte[1024];
	protected volatile InputStream in;
//	private int readindex = -1;
	private int[] readindexs;

	public ReplicaInputStream(InputStream in, int replicaCount) {
		this.in = in;
		this.readindexs = new int[replicaCount];
	}
	
//	public InputStream getInputStream(int index) {
//		
//	}

	@Override
	public int read() throws IOException {
//		readindexs[]++;
//
//		return (int) buffer[readindex];
		return 0;
	}

	@Override
	public int read(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		return super.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return super.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return super.skip(n);
	}

	@Override
	public int available() throws IOException {
		return super.available();
	}

	@Override
	public void close() throws IOException {
		super.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
	}

	@Override
	public boolean markSupported() {
		return super.markSupported();
	}

}
