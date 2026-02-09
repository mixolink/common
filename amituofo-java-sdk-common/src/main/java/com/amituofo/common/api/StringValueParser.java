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

public interface StringValueParser<RETURN> extends ValueParser<String, RETURN> {

	public final static StringValueParser<String> STRING_TYPE_PARSER = new StringValueParser<String>() {
		public String parse(String value) {
			return value;
		}
	};

	public final static StringValueParser<Boolean> BOOLEAN_TYPE_PARSER = new StringValueParser<Boolean>() {
		public Boolean parse(String value) {
			if ("true".equalsIgnoreCase(value)) {
				return Boolean.TRUE;
			}

			return Boolean.FALSE;
		}
	};

	public final static StringValueParser<Long> LONG_TYPE_PARSER = new StringValueParser<Long>() {
		public Long parse(String value) {
			if (value != null && value.length() > 0) {
				return (long) Double.parseDouble(value);
			}

			return null;
		}
	};

	public final static StringValueParser<Integer> INTEGER_TYPE_PARSER = new StringValueParser<Integer>() {
		public Integer parse(String value) {
			if (value != null && value.length() > 0) {
				return Integer.valueOf(value);
			}

			return null;
		}
	};

	public final static StringValueParser<Long> TIMESTAMP_TYPE_PARSER = new StringValueParser<Long>() {
		public Long parse(String value) {
			if (value != null) {
				final int len = value.length();
				if (len == 10) {
					return (long) Double.parseDouble(value) * 1000;
				} else if (len > 0) {
					return (long) Double.parseDouble(value);
				}
			}

			return null;
		}
	};
}
