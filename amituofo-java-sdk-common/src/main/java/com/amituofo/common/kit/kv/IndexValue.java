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
package com.amituofo.common.kit.kv;

import java.util.Objects;

import com.amituofo.common.util.StringUtils;

public class IndexValue<T> {
	private final int index;
	private final T value;

	public IndexValue(int index, T value) {
		super();
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public T getValue() {
		return value;
	}

	public String getStringValue() {
		if (value != null) {
			return value.toString();
		}

		return "";
	}

	@Override
	public String toString() {
		return index + "=" + StringUtils.nullToString(value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexValue other = (IndexValue) obj;
		return Objects.equals(value, other.value);
	}

}
