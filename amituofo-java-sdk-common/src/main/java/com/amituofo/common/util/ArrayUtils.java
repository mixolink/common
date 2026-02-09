package com.amituofo.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.amituofo.common.api.Filter;

public class ArrayUtils {

	public static <T> List<T> asList(T... a) {
		ArrayList<T> list = new ArrayList<T>();

		if (a != null) {
			for (T t : a) {
				list.add(t);
			}
		}

		return list;
	}

	public static <T> Set<T> asSet(T... a) {
		Set<T> set = new HashSet<T>();

		if (a != null) {
			for (T t : a) {
				set.add(t);
			}
		}

		return set;
	}

	public static String[] asStringArray(List<String> list) {
		String[] ts;

		if (list != null) {
			ts = new String[list.size()];
			for (int i = 0; i < ts.length; i++) {
				ts[i] = list.get(i).toString();
			}
		} else {
			ts = new String[0];
		}

		return ts;
	}

	public static <T> List<T> filter(T[] configs, Filter<T> filter) {
		List<T> list = new ArrayList<T>();

		if (filter != null) {
			for (T conf : configs) {
				if (filter.accept(conf)) {
					list.add(conf);
				}
			}
		} else {
			for (T conf : configs) {
				list.add(conf);
			}
		}

		return list;
	}

	public static <T> List<T> filter(List<T> configs, Filter<T> filter) {
		List<T> list = new ArrayList<T>();

		if (filter != null) {
			for (T conf : configs) {
				if (filter.accept(conf)) {
					list.add(conf);
				}
			}
		} else {
			for (T conf : configs) {
				list.add(conf);
			}
		}

		return list;
	}

	public static <T> List<T> removeNulls(T[] arrays) {
		List<T> list = new ArrayList<T>();
		if (arrays != null) {
			for (T t : arrays) {
				if (t != null)
					list.add(t);
			}
		}

		return list;
	}

	public static String[] asStringArray(Set<String> set) {
		String[] ts;

		if (set != null) {
			ts = new String[set.size()];
			int i = 0;
			for (String string : ts) {
				ts[i++] = string;
			}
		} else {
			ts = new String[0];
		}

		return ts;
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.size() == 0;
	}

	public static String[] add(String[] dstarray, String... appendarray) {
		if (appendarray == null || appendarray.length == 0) {
			return dstarray;
		}

		if (dstarray == null || dstarray.length == 0) {
			return appendarray;
		}

		String[] newTargets = new String[dstarray.length + appendarray.length];

//		System.arraycopy(dstarray, 0, newTargets, 0, dstarray.length);

		int n = 0;
		for (; n < dstarray.length; n++) {
			newTargets[n] = dstarray[n];
		}

		for (int i = 0; i < appendarray.length; i++) {
			newTargets[n] = appendarray[i];
			n++;
		}
		return newTargets;
	}

	public static <T> List<List<T>> splitList(List<T> originalList, int size) {
		List<List<T>> result = new ArrayList<>();
		if (originalList == null || originalList.isEmpty()) {
			return result;
		}
		int total = originalList.size();
		if (total <= size) {
			result.add(originalList);
			return result;
		}

		for (int i = 0; i < total; i += size) {
			int end = Math.min(i + size, total);
			result.add(new ArrayList<>(originalList.subList(i, end)));
		}
		return result;
	}

	public static <T, R> List<List<R>> splitList(List<T> originalList, int size, Function<T, R> mapper) {
        List<List<R>> result = new ArrayList<>();
        if (originalList == null || originalList.isEmpty()) {
            return result;
        }
        int total = originalList.size();
        if (total <= size) {
            List<R> mapped = new ArrayList<>();
            for (T item : originalList) {
                mapped.add(mapper.apply(item));
            }
            result.add(mapped);
            return result;
        }

        for (int i = 0; i < total; i += size) {
            int end = Math.min(i + size, total);
            List<R> chunk = new ArrayList<>();
            for (T item : originalList.subList(i, end)) {
                chunk.add(mapper.apply(item));
            }
            result.add(chunk);
        }
        return result;
    }
	
	public static boolean isEquals(char[] data0, char[] data1) {
	    if (data0 == null || data1 == null) {
	        return data0 == data1; // 两者都为 null 才相等
	    }

	    // 恒定时间比较：避免短路（short-circuit）
	    int length = data0.length;
	    int diff = length ^ data1.length; // 如果长度不同，diff != 0

	    // 遍历所有位置（即使长度不同也遍历到 max(len0, len1)，但 Java 数组越界需处理）
	    // 更安全做法：只遍历到 min(len0, len1)，但用恒定逻辑
	    int minLen = Math.min(length, data1.length);
	    for (int i = 0; i < minLen; i++) {
	        diff |= data0[i] ^ data1[i]; // 异或：相同为0，不同为非0
	    }

	    // 如果长度不同，diff 已经非0；如果长度同但内容不同，diff 也非0
	    return diff == 0;
	}

	public static boolean isEquals(byte[] data0, char[] data1) {
		// 正确处理 null：分别判断
	    if (data0 == null) {
	        return data1 == null;
	    }
	    if (data1 == null) {
	        return false;
	    }

	    // 先判断长度是否“逻辑相等”：char[] 按 UTF-8 编码后是否可能等于 data0 长度？
	    // 但注意：UTF-8 编码长度取决于字符内容（ASCII 占1字节，非ASCII占2～4字节）
	    // → 无法在不编码的情况下准确判断长度！

	    // 因此，若禁止使用 String/Charset，则必须做假设：
	    // 假设 data1 中所有字符都是 ASCII（0～127），这样 1 char = 1 byte
	    // 这是很多系统（如密码）的合理假设

	    if (data0.length != data1.length) {
	        return false;
	    }

	    // 恒定时间逐字节比较（假设 char 可安全转为 byte，即 ASCII）
	    int result = 0;
	    for (int i = 0; i < data0.length; i++) {
	        // 将 char 转为 byte（截断高8位，仅保留低8位）
	        // 对于 ASCII 字符（0～127），这是安全的
	        byte b1 = (byte) (data1[i] & 0xFF);
	        result |= (data0[i] ^ b1);
	    }
	    return result == 0;
	}
	
//	public static void main(String[] args) {
//		// 构造一个包含 1203 个整数的列表
//		List<Integer> bigList = new ArrayList<>();
//		for (int i = 1; i <= 1203; i++) {
//			bigList.add(i);
//		}
//
//		// 拆分列表，每个最多 500 个元素
//		List<List<Integer>> chunks = splitList(bigList, 500);
//
//		// 打印拆分后的子列表数量和每个子列表的大小
//		System.out.println("共拆分为 " + chunks.size() + " 个子列表");
//		for (int i = 0; i < chunks.size(); i++) {
//			List<Integer> chunk = chunks.get(i);
//			System.out.println("子列表 " + (i + 1) + " 大小: " + chunk.size() + ", 起始元素: " + chunk.get(0) + ", 结束元素: " + chunk.get(chunk.size() - 1));
//		}
//	}
}
