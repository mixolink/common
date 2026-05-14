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
package com.amituofo.common.kit.event;

import com.amituofo.common.api.StepProgressListener;
import com.amituofo.common.type.ReadProgressEvent;

public class SyncStepProgressPusher {
	private static final long THROTTLE_MS = 800;

	private StepProgressListener progressListener;
	private long lastPushTime = 0;
	private int pendingStep = 0;
//	private long step = 0;

	public SyncStepProgressPusher(StepProgressListener progressListener) {
		this.progressListener = progressListener;
	}

//	public SyncStepProgressPusher(StepProgressListener progressListener, long length) {
//		this.progressListener = progressListener;
//
////		if (length > Constants.SIZE_10MB) {
////			step = length / 100;
////		}
//	}

	public void push(ReadProgressEvent event, int bytes) {
		pendingStep += bytes;

		if (event == ReadProgressEvent.BYTE_READ_END_EVENT) {
			if (pendingStep > 0) {
				progressListener.progressChanged(ReadProgressEvent.BYTE_READING_EVENT, pendingStep);
			}
			progressListener.progressChanged(ReadProgressEvent.BYTE_READ_END_EVENT, 0);
			reset();
		} else {
//			if (step == 0) {
//				// 未知总长度：按时间节流
				if (System.currentTimeMillis() - lastPushTime >= THROTTLE_MS) {
					progressListener.progressChanged(event, pendingStep);
					reset();
				}
//			} else {
//				// 已知总长度：累计够一个step再推
//				if (pendingStep >= step) {
//					progressListener.progressChanged(event, pendingStep);
//					reset();
//				}
//			}
		}
	}

	public void stop() {
		progressListener = null;
	}

	private void reset() {
		pendingStep = 0;
		lastPushTime = System.currentTimeMillis();
	}
}
