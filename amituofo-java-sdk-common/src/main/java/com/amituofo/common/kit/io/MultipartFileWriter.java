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

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultipartFileWriter {
	private File file;
	// private int writerCount;
	private ThreadPoolExecutor TP;

	private final List<RandomFileWriter> writers = new ArrayList<RandomFileWriter>();

	public MultipartFileWriter(File file, int writerCount) {
		this.file = file;
		// this.writerCount = writerCount;
		this.TP = new ThreadPoolExecutor(writerCount, writerCount, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public void writePart(long offset, InputStream in) {
		RandomFileWriter writer = new RandomFileWriter(file, offset, in);
		writers.add(writer);
		TP.execute(writer);
	}

	// public void writePart(long offset, long length, InputStream in) {
	// TP.execute(new FileWriter(file, offset, length, in));
	// }
	
	
}