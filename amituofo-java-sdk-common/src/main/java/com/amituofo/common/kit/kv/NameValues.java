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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amituofo.common.kit.value.Value;

public class NameValues<T> {
	protected final Map<String, NameValuePair<T>> map = new HashMap<String, NameValuePair<T>>();

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Value<T> get(String name) {
		NameValuePair<T> nvp = map.get(name.toLowerCase());
		if (nvp != null) {
			return nvp.getValue();
		}

		return null;
	}

	public void put(String name, Value<T> value) {
		map.put(name.toLowerCase(), new NameValuePair<T>(name, value));
	}

	public void putAll(Collection<NameValuePair<T>> values) {
		for (NameValuePair<T> nameValuePair : values) {
			map.put(nameValuePair.getName().toLowerCase(), nameValuePair);
		}
	}

	public void remove(String key) {
		map.remove(key.toLowerCase());
	}

	public void clear() {
		map.clear();
	}

	public Set<String> getNames() {
		return map.keySet();
	}

	public Collection<NameValuePair<T>> values() {
		return map.values();
	}

	public Collection<NameValuePair<T>> sortedValues() {
		List<NameValuePair<T>> v = new ArrayList<NameValuePair<T>>();
		String[] arr = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(arr);
		for (String key : arr) {
//			System.out.println(key);
			v.add(map.get(key));
		}

		return v;
	}

	public boolean contains(String key) {
		return map.containsKey(key.toLowerCase());
	}
}
