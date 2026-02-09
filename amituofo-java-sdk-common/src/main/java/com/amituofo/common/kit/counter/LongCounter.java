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
package com.amituofo.common.kit.counter;

public class LongCounter {
	public static LongCounter newCounter() {
		return new LongCounter();
	}

	public static LongCounter newCounter(long n) {
		return new LongCounter(n);
	}

	public long n = 0;

	public LongCounter() {
	}

	public LongCounter(long i) {
		this.n = i;
	}

	public void reset() {
		n = 0;
	}

	public long plus1() {
		n++;
		return n;
	}

	public long plus(long num) {
		n += num;
		return n;
	}

	@Override
	public String toString() {
		return String.valueOf(n);
	}
}
