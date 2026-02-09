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
package com.amituofo.common.kit.parser;

public class SimpleJsonParser {
	private String jsonString;

	public SimpleJsonParser(String jsonString) {
		this.jsonString = jsonString;
	}

	private String[] toKeys(String name) {
		return name.split("\\.");
	}

	private int nextKey(int fromIndex, char key) {
		return key;

	}

	public String getString(String name) {
		int keyIndex = jsonString.indexOf("\"" + name + "\"");
		if (keyIndex != -1) {

		}
		return name;
	}

	// {
	// "name" : "cloud",
	// "description" : "test111",
	// "hardQuota" : "50.00 GB",
	// "softQuota" : 85,
	// "indexingDefault" : true,
	// "indexingEnabled" : true,
	// "searchEnabled" : true,
	// "enterpriseMode" : true,
	// "serviceRemoteSystemRequests" : true,
	// "customMetadataValidationEnabled" : true,
	// "tags" : {
	// "tag" : [ "qq1", "ssf" ]
	// },
	// "appendEnabled" : false,
	// "atimeSynchronizationEnabled" : false,
	// "allowPermissionAndOwnershipChanges" : false,
	// "servicePlan" : "Default",
	// "customMetadataIndexingEnabled" : true,
	// "aclsUsage" : "ENFORCED",
	// "owner" : "admin",
	// "ownerType" : "LOCAL",
	// "optimizedFor" : "CLOUD",
	// "multipartUploadAutoAbortDays" : 1,
	// "dpl" : "Dynamic",
	// "authUsersAlwaysGrantedAllPermissions" : true,
	// "authMinimumPermissions" : {
	// "permission" : [ ]
	// },
	// "authAndAnonymousMinimumPermissions" : {
	// "permission" : [ ]
	// }
	// }
}
