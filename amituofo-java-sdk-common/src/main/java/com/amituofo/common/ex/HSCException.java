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
package com.amituofo.common.ex;

/**
 * Base type for all client exceptions thrown by the SDK.
 *
 * This exception is thrown when service could not be contacted for a response,
 * or when client is unable to parse the response from service.
 * 
 * @author sohan
 *
 */
public class HSCException extends Exception {
	private static final long serialVersionUID = 1L;

	public HSCException() {
		// TODO Auto-generated constructor stub
	}

	public HSCException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public HSCException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public HSCException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public HSCException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
