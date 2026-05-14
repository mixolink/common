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

public class KeyValues<T> {
	protected final Map<String, KeyValue<T>> map = new HashMap<String, KeyValue<T>>();

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public T get(String name) {
		KeyValue<T> nvp = map.get(name.toLowerCase());
		if (nvp != null) {
			return nvp.getValue();
		}

		return null;
	}

	public void put(String name, T value) {
		map.put(name.toLowerCase(), new KeyValue<T>(name, value));
	}

	public void putAll(Collection<KeyValue<T>> values) {
		for (KeyValue<T> nameValuePair : values) {
			map.put(nameValuePair.getKey().toLowerCase(), nameValuePair);
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

	public Collection<KeyValue<T>> values() {
		return map.values();
	}

	public Collection<KeyValue<T>> sortedValues() {
		List<KeyValue<T>> v = new ArrayList<KeyValue<T>>();
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
