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
package com.amituofo.common.kit.value;

import java.util.concurrent.atomic.AtomicInteger;

import com.amituofo.common.api.ValueBuilder;

public class ReferenceValue<T> extends Value<T> {
	private AtomicInteger refcnt = new AtomicInteger(0);

	public ReferenceValue() {
		super();
	}

	public ReferenceValue(ReferenceValue<T> refvalue) {
		super(refvalue);
		this.refcnt.set(refvalue.refcnt.get());
	}

	public ReferenceValue(T value) {
		super(value);
	}

	public ReferenceValue(T value, int refcnt) {
		super(value);
		this.refcnt.set(refcnt);
	}

	public ReferenceValue(T value, ValueBuilder<T, String> vb) {
		super(value, vb);
	}

	public void getReferenceCount(int refcnt) {
		this.refcnt.set(refcnt);
	}

	public int getReferenceCount() {
		return refcnt.get();
	}

	public int increaseReferenceCount() {
		return refcnt.incrementAndGet();
	}

	public int decreaseReferenceCount() {
		return refcnt.decrementAndGet();
	}
}