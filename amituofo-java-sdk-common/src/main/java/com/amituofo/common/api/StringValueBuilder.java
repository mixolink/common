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
package com.amituofo.common.api;

import com.amituofo.common.define.Constants;
import com.amituofo.common.util.URLUtils;

public interface StringValueBuilder<PARAM> extends ValueBuilder<PARAM, String> {

	public final static StringValueBuilder<Boolean> BOOLEAN_TYPE_BUILDER = new StringValueBuilder<Boolean>() {
		public String build(Boolean args) {
			if (args == null) {
				return "false";
			}

			return args.toString().toLowerCase();
		}
	};

	public final static StringValueBuilder<String> STRING_TYPE_BUILDER = new StringValueBuilder<String>() {
		public String build(String args) {
			return args;
		}
	};

	public static final StringValueBuilder<String> NULL_TYPE_BUILDER= new StringValueBuilder<String>() {
		public String build(String args) {
			return null;
		}
	};

	public static final StringValueBuilder<Integer> INT_TYPE_BUILDER= new StringValueBuilder<Integer>() {
		public String build(Integer args) {
			return args.toString();
		}
	};
	

	public static final StringValueBuilder<Long> LONG_TYPE_BUILDER= new StringValueBuilder<Long>() {
		public String build(Long args) {
			return args.toString();
		}
	};
	
	public final static ValueBuilder<String, String> DEFAULT_ENCODER = new ValueBuilder<String, String>() {
		@Override
		public String build(String input) {
			return URLUtils.encode(input, Constants.DEFAULT_URL_ENCODE);
		}
	};

}
