package com.amituofo.common.util;
import java.nio.charset.StandardCharsets;

public class XXHashUtils {

    // --------------------------------------------------------------------------
    // |                            XXHash Primes                               |
    // --------------------------------------------------------------------------

    // 32位哈希素数 (Primes for XXHash32)
    private static final int PRIME32_1 = 0x9E3779B1;
    private static final int PRIME32_2 = 0x85EBCA77;
    private static final int PRIME32_3 = 0xC2B2AE3D;
    private static final int PRIME32_4 = 0x27D4EB2F;
    private static final int PRIME32_5 = 0x165667B1;
    private static final int DEFAULT_SEED_32 = 0;

    // 64位哈希素数 (Primes for XXHash64)
    private static final long PRIME64_1 = 0x9E3779B185EBCA87L;
    private static final long PRIME64_2 = 0xC2B2AE3D27D4EB2FL;
    private static final long PRIME64_3 = 0x165667B19DEEADFAL;
    private static final long PRIME64_4 = 0x17060AA2D1B5B599L;
    private static final long PRIME64_5 = 0x5851F42D4C957F2DL;
    private static final long DEFAULT_SEED_64 = 0L;


    // ========================================================================
    // |                           XXHash32 Logic                              |
    // ========================================================================

    /**
     * 计算输入字符串的 XXHash32 值。
     * 使用默认种子 (0)。
     *
     * @param input 要哈希的字符串
     * @return 32位哈希值 (int)
     */
    public static int xxHash32(String input) {
        return xxHash32(input.getBytes(StandardCharsets.UTF_8), DEFAULT_SEED_32);
    }

    /**
     * 计算输入字节数组的 XXHash32 值。
     *
     * @param data 字节数组
     * @param seed 哈希种子 (int)
     * @return 32位哈希值 (int)
     */
    public static int xxHash32(byte[] data, int seed) {
        final int length = data.length;
        int index = 0;
        final int off = 0;
        int hash;

        // 1. Accumulate Stage (长度 >= 16 字节)
        if (length >= 16) {
            int v1 = seed + PRIME32_1 + PRIME32_2;
            int v2 = seed + PRIME32_2;
            int v3 = seed;
            int v4 = seed - PRIME32_1;

            int limit = off + length - 16;
            while (index <= limit) {
                int k1 = getIntLittleEndian(data, index); 
                v1 = round32(v1, k1); index += 4;
                
                int k2 = getIntLittleEndian(data, index); 
                v2 = round32(v2, k2); index += 4;
                
                int k3 = getIntLittleEndian(data, index); 
                v3 = round32(v3, k3); index += 4;
                
                int k4 = getIntLittleEndian(data, index); 
                v4 = round32(v4, k4); index += 4;
            }

            // 合并累加器
            hash = Integer.rotateLeft(v1, 1)
                 + Integer.rotateLeft(v2, 7)
                 + Integer.rotateLeft(v3, 12)
                 + Integer.rotateLeft(v4, 18);
        } else {
            // 长度 < 16 字节
            hash = seed + PRIME32_5;
        }

        hash += length;

        // 2. Remaining Blocks (处理 4 字节块)
        while (index + 4 <= off + length) {
            int k1 = getIntLittleEndian(data, index);
            hash += k1 * PRIME32_3;
            hash = Integer.rotateLeft(hash, 17) * PRIME32_4;
            index += 4;
        }

        // 3. Remaining Bytes (处理剩余字节 1-3 个)
        while (index < off + length) {
            int b = data[index++] & 0xFF;
            hash += b * PRIME32_5;
            hash = Integer.rotateLeft(hash, 11) * PRIME32_1;
        }

        // 4. Final Mix (Avalanche)
        hash ^= hash >>> 15;
        hash *= PRIME32_2;
        hash ^= hash >>> 13;
        hash *= PRIME32_3;
        hash ^= hash >>> 16;

        return hash;
    }
    
    /** 32位哈希核心操作 (Round Function) */
    private static int round32(int acc, int input) {
        acc += input * PRIME32_2;
        acc = Integer.rotateLeft(acc, 13);
        acc *= PRIME32_1;
        return acc;
    }


    // ========================================================================
    // |                           XXHash64 Logic                              |
    // ========================================================================

    /**
     * 计算输入字符串的 XXHash64 值。
     * 使用默认种子 (0L)。
     *
     * @param input 要哈希的字符串
     * @return 64位哈希值 (long)
     */
    public static long xxHash64(String input) {
        return xxHash64(input.getBytes(StandardCharsets.UTF_8), DEFAULT_SEED_64);
    }

    /**
     * 计算输入字节数组的 XXHash64 值。
     *
     * @param data 字节数组
     * @param seed 哈希种子 (long)
     * @return 64位哈希值 (long)
     */
    public static long xxHash64(byte[] data, long seed) {
        final int length = data.length;
        int index = 0;
        final int off = 0;
        long hash;

        // 1. Accumulate Stage (长度 >= 32 字节)
        if (length >= 32) {
            long v1 = seed + PRIME64_1 + PRIME64_2;
            long v2 = seed + PRIME64_2;
            long v3 = seed;
            long v4 = seed - PRIME64_1;

            int limit = off + length - 32;
            while (index <= limit) {
                long k1 = getLongLittleEndian(data, index); v1 = round64(v1, k1); index += 8;
                long k2 = getLongLittleEndian(data, index); v2 = round64(v2, k2); index += 8;
                long k3 = getLongLittleEndian(data, index); v3 = round64(v3, k3); index += 8;
                long k4 = getLongLittleEndian(data, index); v4 = round64(v4, k4); index += 8;
            }

            // 合并累加器
            hash = Long.rotateLeft(v1, 1)
                 + Long.rotateLeft(v2, 7)
                 + Long.rotateLeft(v3, 12)
                 + Long.rotateLeft(v4, 18);
            
            hash = (hash ^ (PRIME64_1 * 4L)) * PRIME64_2 + PRIME64_5; 

        } else {
            // 长度 < 32 字节
            hash = seed + PRIME64_5;
        }

        hash += length;

        // 2. Remaining Blocks (处理 8 字节块)
        while (index + 8 <= off + length) {
            long k1 = getLongLittleEndian(data, index);
            k1 *= PRIME64_2;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= PRIME64_1;
            hash ^= k1;
            hash = Long.rotateLeft(hash, 27) * PRIME64_1 + PRIME64_4;
            index += 8;
        }

        // 3. Remaining Bytes (处理剩余字节 1-7 个)
        while (index < off + length) {
            long b = data[index++] & 0xFFL; 
            hash ^= b * PRIME64_5;
            hash = Long.rotateLeft(hash, 11) * PRIME64_1;
        }

        // 4. Final Mix (Avalanche)
        hash ^= hash >>> 33;
        hash *= PRIME64_2;
        hash ^= hash >>> 29;
        hash *= PRIME64_3;
        hash ^= hash >>> 32;

        return hash;
    }

    /** 64位哈希核心操作 (Round Function) */
    private static long round64(long acc, long input) {
        acc += input * PRIME64_2;
        acc = Long.rotateLeft(acc, 31);
        acc *= PRIME64_1;
        return acc;
    }
    
    // ========================================================================
    // |                          Little Endian Utils                           |
    // ========================================================================

    /**
     * 从字节数组中以小端序 (Little Endian) 读取一个 32 位整数 (int)。
     */
    private static int getIntLittleEndian(byte[] data, int index) {
        return (data[index] & 0xFF)
             | ((data[index + 1] & 0xFF) << 8)
             | ((data[index + 2] & 0xFF) << 16)
             | ((data[index + 3] & 0xFF) << 24);
    }

    /**
     * 从字节数组中以小端序 (Little Endian) 读取一个 64 位长整数 (long)。
     */
    private static long getLongLittleEndian(byte[] data, int index) {
        return (data[index] & 0xFFL)
             | ((data[index + 1] & 0xFFL) << 8)
             | ((data[index + 2] & 0xFFL) << 16)
             | ((data[index + 3] & 0xFFL) << 24)
             | ((data[index + 4] & 0xFFL) << 32)
             | ((data[index + 5] & 0xFFL) << 40)
             | ((data[index + 6] & 0xFFL) << 48)
             | ((data[index + 7] & 0xFFL) << 56);
    }
    
    // ========================================================================
    // |                               Main Example                            |
    // ========================================================================

//    public static void main(String[] args) {
//        String testInput = "The quick brown fox jumps over the lazy dog";
//        
//        // ------------------ 32 位计算 ------------------
//        int hash32 = xxHash32(testInput);
//        
//        // ------------------ 64 位计算 ------------------
//        long hash64 = xxHash64(testInput);
//
//        System.out.println("原始输入: " + testInput);
//        System.out.println("---");
//        System.out.println("XXHash32 (int): " + hash32);
//        System.out.println("XXHash32 (Hex): " + String.format("0x%08X", hash32));
//        System.out.println("---");
//        System.out.println("XXHash64 (long): " + hash64);
//        System.out.println("XXHash64 (Hex): " + String.format("0x%016X", hash64));
//    }
    

	// === 对比测试 ===
//	public static void main(String[] args) {
//		{
//			String[] keys = { "", "a", "abc", "hello", "HELLO", "123456789", "The quick brown fox jumps over the lazy dog", "/usr/local/bin/java", "C:\\Users\\Admin\\file.txt", "路径/文件/测试",
//					"😀emoji测试123", "abcdefghijklmnopqrstuvwxyz0123456789", "这是一个很长的字符串，用来测试xxHash32算法在不同输入长度下的一致性。" };
//
//			XXHashFactory factory = XXHashFactory.fastestInstance();
//			XXHash32 hash32 = factory.hash32();
//			int seed = 0;
//			boolean allMatch = true;
//
//			System.out.printf("%-40s %-15s %-15s %-10s%n", "Input", "Official(xxhash)", "PureJava", "Match?");
////    		        System.out.println("=".repeat(90));
//
//			for (String key : keys) {
//				byte[] data = key.getBytes(StandardCharsets.UTF_8);
//				int h1 = hash32.hash(data, 0, data.length, seed);
//				int h2 = xxHash32(key);
//				boolean same = h1 == h2;
//				if (!same)
//					allMatch = false;
//				System.out.printf("%-40s %-15d %-15d %-10s%n", key.length() > 30 ? key.substring(0, 30) + "..." : key, h1, h2, same ? "✅" : "❌");
//			}
//
//			System.out.println("\nAll match: " + (allMatch ? "✅ YES" : "❌ NO"));
//		}
//		{
//			String[] keys = { "", "a", "abc", "hello", "HELLO", "123456789", "The quick brown fox jumps over the lazy dog", "/usr/local/bin/java", "C:\\Users\\Admin\\file.txt", "路径/文件/测试",
//					"😀emoji测试123", "abcdefghijklmnopqrstuvwxyz0123456789", "这是一个很长的字符串，用来测试xxHash32算法在不同输入长度下的一致性。" };
//
//			XXHashFactory factory = XXHashFactory.fastestInstance();
//			XXHash64 hash32 = factory.hash64();
//			int seed = 0;
//			boolean allMatch = true;
//
//			System.out.printf("%-40s %-15s %-15s %-10s%n", "Input", "Official(xxhash)", "PureJava", "Match?");
////    	        System.out.println("=".repeat(90));
//
//			for (String key : keys) {
//				byte[] data = key.getBytes(StandardCharsets.UTF_8);
//				long h1 = hash32.hash(data, 0, data.length, seed);
//				long h2 = xxHash64(key);
//				boolean same = h1 == h2;
//				if (!same)
//					allMatch = false;
//				System.out.printf("%-40s %-15d %-15d %-10s%n", key.length() > 30 ? key.substring(0, 30) + "..." : key, h1, h2, same ? "✅" : "❌");
//			}
//
//			System.out.println("\nAll match: " + (allMatch ? "✅ YES" : "❌ NO"));
//		}
//	}
}