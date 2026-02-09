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

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.kit.event.ProgressPusher;
import com.amituofo.common.kit.event.StepProgressPusher;
import com.amituofo.common.type.ReadProgressEvent;

public class ProgressOutputStream extends FilterOutputStream {
	private final StepProgressPusher pusher;

	public ProgressOutputStream(OutputStream out, StepProgressListener progressListener) {
		super(out);
		this.pusher = new StepProgressPusher(progressListener);
	}

	@Override
	public void write(int b) throws IOException {
		super.write(b);

		if (b != -1) {
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, 1);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);

		if (b.length > 0) {
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, b.length);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);

		if (len > 0) {
			pusher.push(ReadProgressEvent.BYTE_READING_EVENT, len);
		}
	}

//	@Override
//	public void flush() throws IOException {
//		super.flush();
//	}

	@Override
	public void close() throws IOException {
		pusher.push(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
		super.close();
		pusher.stop();
		out.close();
	}

}
