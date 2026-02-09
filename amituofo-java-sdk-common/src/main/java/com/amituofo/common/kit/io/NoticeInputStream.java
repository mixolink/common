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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NoticeInputStream extends FilterInputStream {
	private boolean closed = false;
	private FinalReadListener listener;

	public NoticeInputStream(InputStream in, FinalReadListener listener) {
		super(in);
		this.listener = listener;
	}

	public int read() throws IOException {
		int v = in.read();
		if (v == -1) {
			listener.allReaded();
		}
		return v;
	}

	public int read(byte[] b) throws IOException {
		int readLen = in.read(b);
		if (readLen == -1) {
			listener.allReaded();
		}
		return readLen;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int readLen = in.read(b, off, len);
		if (readLen == -1) {
			listener.allReaded();
		}
		return readLen;
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			closed = true;
			super.close();
			listener.closed();
		}
	}

}
