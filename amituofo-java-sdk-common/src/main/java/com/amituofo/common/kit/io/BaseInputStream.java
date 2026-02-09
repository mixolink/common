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

public class BaseInputStream extends FilterInputStream {
	long totalReadLength = 0;

	protected BaseInputStream(InputStream in) {
		super(in);
	}

	public int read() throws IOException {
		totalReadLength++;
		return in.read();
	}

	public int read(byte b[]) throws IOException {
		int len = read(b, 0, b.length);
		totalReadLength += len;
		return len;
	}

	public int read(byte b[], int off, int len) throws IOException {
		int readlen = in.read(b, off, len);
		totalReadLength += readlen;
		return readlen;
	}

	public long getTotalReadLength() {
		return totalReadLength;
	}

}
