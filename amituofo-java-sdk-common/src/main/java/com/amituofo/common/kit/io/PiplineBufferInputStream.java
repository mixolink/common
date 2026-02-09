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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PiplineBufferInputStream extends InputStream {
	private Queue<Integer> buffer = new ConcurrentLinkedQueue<Integer>();
	private boolean end = false;

	public PiplineBufferInputStream() {
	}

	public void append(int b) {
		buffer.add(b);
	}

	public void end() {
		end = true;
	}

	@Override
	public int read() throws IOException {
		if (end) {
			return -1;
		}

		if (buffer.size() == 0) {
			do {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}

				if (end) {
					return -1;
				}
			} while (buffer.size() == 0);
		}

		return buffer.poll();
	}

	@Override
	public int read(byte[] b) throws IOException {
		int max = b.length;
		int i = 0;
		for (; i < max; i++) {
			byte d = (byte) read();
			if (d == -1) {
				return i;
			}
			b[i] = d;
		}

		return i;
	}

//	@Override
//	public int read(byte[] b, int off, int len) throws IOException {
//		// TODO Auto-generated method stub
//		return super.read(b, off, len);
//	}

}
