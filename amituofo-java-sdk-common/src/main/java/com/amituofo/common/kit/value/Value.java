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

import com.amituofo.common.api.ValueBuilder;
import com.amituofo.common.ex.HSCException;

public class Value<T> {
	private T value = null;
	private ValueBuilder<T, String> vb = null;

	public Value() {
		super();
		this.value = null;
	}

	public Value(Value<T> value) {
		super();
		if (value != null) {
			this.value = value.value;
			this.vb = value.vb;
		}
	}

	public Value(T value) {
		super();
		this.value = value;
	}

	public Value(T value, ValueBuilder<T, String> vb) {
		super();
		this.value = value;
		this.vb = vb;
	}

	public T getValue() {
		return value;
	}

	public boolean hasValue() {
		return value != null;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String buildStringValue() {
		return this.toString();
	}

	@Override
	public String toString() {
		if (vb != null) {
			try {
				return vb.build(value);
			} catch (HSCException e) {
				e.printStackTrace();
			}
		} else if (value != null) {
			return value.toString();
		}

		// return "";
		return null;
	}

	public void clearValue() {
		this.value = null;
	}

}