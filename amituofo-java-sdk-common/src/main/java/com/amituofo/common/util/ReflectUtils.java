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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Reflect utils, provide the method to access getter/setter, private method.
 * 
 * @author
 * @version
 */
public class ReflectUtils {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	/**
	 * Call Getter method className.propertyName.methodName
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : propertyName.split("\\.")) {
			String getterMethodName = GETTER_PREFIX + capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * Call Setter method className.propertyName.methodName
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = propertyName.split("\\.");
		for (int i = 0; i < names.length; i++) {
			if (i < names.length - 1) {
				String getterMethodName = GETTER_PREFIX + capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			} else {
				String setterMethodName = SETTER_PREFIX + capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}

	/**
	 * Get the value of specific filed directly.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			// Just ignore
		}
		return result;
	}

	/**
	 * Set value to filed.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			// Just ignore
		}
	}

	/**
	 * Invoke method
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	public static Field getAccessibleField(final Object obj, final String fieldName) {
		// Validate.notNull(obj, "object can‘t be null");
		// Validate.notBlank(fieldName, "fieldName can‘t be blank");

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
		// Validate.notNull(obj, "object can‘t be null");
		// Validate.notBlank(methodName, "methodName can‘t be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		// Validate.notNull(obj, "object can‘t be null");
		// Validate.notBlank(methodName, "methodName can‘t be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))
				&& !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			// logger.warn(clazz.getSimpleName() + "‘s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			// logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "‘s Parameterized Type: "
			// + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			// logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class<?> getUserClass(Object instance) {
		// Validate.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}

	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static <T> boolean isDateType(Class<T> clazz, String fieldName) {
		boolean flag = false;
		try {
			Field field = clazz.getDeclaredField(fieldName);
			Object typeObj = field.getType().newInstance();
			flag = typeObj instanceof Date;
		} catch (Exception e) {
		}
		return flag;
	}

	public static <T> Object parseValueWithType(String value, Class<?> type) {
		Object result = null;
		try {
			if (Boolean.TYPE == type) {
				result = Boolean.parseBoolean(value);
			} else if (Byte.TYPE == type) {
				result = Byte.parseByte(value);
			} else if (Short.TYPE == type) {
				result = Short.parseShort(value);
			} else if (Integer.TYPE == type) {
				result = Integer.parseInt(value);
			} else if (Long.TYPE == type) {
				result = Long.parseLong(value);
			} else if (Float.TYPE == type) {
				result = Float.parseFloat(value);
			} else if (Double.TYPE == type) {
				result = Double.parseDouble(value);
			} else {
				result = (Object) value;
			}
		} catch (Exception e) {
		}
		return result;
	}

	private static String capitalize(String str) {
		if (str.length() >= 2) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
	}
}