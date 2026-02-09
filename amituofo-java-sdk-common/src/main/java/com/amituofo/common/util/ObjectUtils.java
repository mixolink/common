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
package com.amituofo.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.amituofo.common.util.StreamUtils;

public class ObjectUtils {

	/**
	 * Write object to file
	 * 
	 * @author             hansong
	 * @param  file
	 * @param  obj
	 * @throws IOException
	 */
	public static void writeObject(File file, Serializable obj) throws IOException {
		FileOutputStream o = null;
		ObjectOutputStream out = null;

		try {
			o = new FileOutputStream(file);
			out = new ObjectOutputStream(o);
			out.writeObject(obj);
		} finally {
			StreamUtils.close(out);
			StreamUtils.close(o);
		}
	}

	/**
	 * Read object from file
	 * 
	 * @author                        hansong
	 * @param  file
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(File file) throws IOException, ClassNotFoundException {
		if (!file.exists()) {
			return null;
		}

		FileInputStream i = null;
		ObjectInputStream in = null;
		Object obj = null;

		try {
			i = new FileInputStream(file);
			in = new ObjectInputStream(i);
			obj = in.readObject();
		} finally {
			StreamUtils.close(in);
			StreamUtils.close(i);
		}

		return obj;
	}

	/**
	 * 对象转数组
	 * 
	 * @param  obj
	 * @return
	 * @throws IOException 
	 */
	public static byte[] toByteArray(Object obj) throws IOException {
		if (obj == null) {
			return null;
		}

		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		return bytes;
	}

	/**
	 * 数组转对象
	 * 
	 * @param  bytes
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
		if (bytes == null) {
			return null;
		}

		Object obj = null;
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		return obj;
	}

//	public static void writeObject(File file, Object obj) throws IOException {
//		FileOutputStream o = new FileOutputStream(file);
//		ObjectOutputStream out = new ObjectOutputStream(o);
//		out.writeObject(obj);
//		out.flush();
//		out.close();
//	}
//
//	/**
//	 * Write object to file
//	 * 
//	 * @author hansong
//	 * @param filepath
//	 * @param obj
//	 * @throws IOException
//	 */
//	public static void writeObject(String filepath, Object obj) throws IOException {
//		writeObject(new File(filepath), obj);
//	}
//
//	public static Object readObject(File file) throws IOException, ClassNotFoundException {
//		if (!file.exists()) {
//			return null;
//		}
//		FileInputStream i = new FileInputStream(file);
//		ObjectInputStream in = new ObjectInputStream(i);
//		Object obj = in.readObject();
//		in.close();
//		return obj;
//	}
//
//	/**
//	 * 
//	 * @author hansong
//	 * @param filepath
//	 * @return
//	 * @throws IOException
//	 * @throws ClassNotFoundException
//	 */
//	public static Object readObject(String filepath) throws IOException, ClassNotFoundException {
//		return readObject(new File(filepath));
//	}



}
