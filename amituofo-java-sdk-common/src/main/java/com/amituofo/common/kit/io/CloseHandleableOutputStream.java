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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.IOAbortable;
import com.amituofo.common.api.IOCloseListener;

public class CloseHandleableOutputStream extends FilterOutputStream implements IOAbortable {
	private IOCloseListener ioCloseListener;
	private List<IOCloseListener> ioCloseListeners = null;
	private boolean closed = false;

	public CloseHandleableOutputStream(OutputStream out, IOCloseListener ioCloseListener) {
		super(out);
		this.ioCloseListener = ioCloseListener;
	}

	public void addCloseListener(IOCloseListener listener) {
		if (listener == null) {
			return;
		}
		if (ioCloseListeners == null) {
			ioCloseListeners = new ArrayList<>();
		}

		ioCloseListeners.add(listener);
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			closed = true;
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				throw e;
			} finally {
				if (ioCloseListener != null) {
					ioCloseListener.closed();
				}

				if (ioCloseListeners != null) {
					for (IOCloseListener listener : ioCloseListeners) {
						listener.closed();
					}
					ioCloseListeners.clear();
				}
			}
		}
	}

	@Override
	public void abort() throws IOException {
		if (out instanceof IOAbortable) {
			((IOAbortable) out).abort();
		}

	}

}
