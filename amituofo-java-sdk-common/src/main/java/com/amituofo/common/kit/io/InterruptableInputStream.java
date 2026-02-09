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

import com.amituofo.common.api.IOAbortable;
import com.amituofo.common.api.Interruptable;

public class InterruptableInputStream extends FilterInputStream implements IOAbortable {
	private Interruptable interrupter;
	private boolean closed = false;
	private boolean aborted = false;

	public InterruptableInputStream(InputStream in) {
		super(in);
		this.interrupter = null;
	}

	public InterruptableInputStream(InputStream in, Interruptable interrupter) {
		super(in);
		this.interrupter = interrupter;
	}

	public int read() throws IOException {
		if (aborted) {
			return -1;
		}
		
		return in.read();
	}

	public int read(byte[] b) throws IOException {
		if (aborted) {
			return -1;
		}
		
		return in.read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		if (aborted) {
			return -1;
		}
		
		return in.read(b, off, len);
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			closed = true;
			super.close();
		}
	}
	
	public boolean isAbored() {
		return aborted;
	}
	
	public boolean isClosed() {
		return closed;
	}

	@Override
	public void abort() throws IOException {
		if (!aborted) {
			aborted = true;
			if (in instanceof IOAbortable) {
				((IOAbortable) in).abort();

				if (interrupter != null) {
					if (!interrupter.isInterrupted()) {
						try {
							interrupter.interrupt();
						} catch (InterruptedException e) {
						}
					}
				}
			}
			
//			close();
		}
	}

}
