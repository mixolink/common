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

public class MetaValue<META, V> {
	private final META meta;
	private final V value;

	public MetaValue(META meta, V value) {
		super();
		this.meta = meta;
		this.value = value;
	}

	public META getMeta() {
		return meta;
	}

	public V getValue() {
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
		return StringUtils.nullToString(meta) + "=" + StringUtils.nullToString(value);
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
		MetaValue other = (MetaValue) obj;
		return Objects.equals(value, other.value);
	}

}
