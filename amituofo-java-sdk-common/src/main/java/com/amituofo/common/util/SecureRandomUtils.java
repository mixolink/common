package com.amituofo.common.util;
import java.security.SecureRandom;

public class SecureRandomUtils {

    // 推荐的 Salt 长度：16 字节 (128 bit) 或 32 字节 (256 bit)
    // 16 字节已经足够安全，碰撞概率极低（接近于零）
    private static final int SALT_LENGTH = 16; 
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 生成一个独一无二的随机 Salt
     * @return 十六进制字符串格式的 Salt
     */
    public static String randomSalt() {
    	return randomSalt(SALT_LENGTH);
    }
    
    public static String randomSalt(int len) {
        byte[] saltBytes = new byte[len];
        
        // 关键步骤：使用 SecureRandom 填充字节数组
        secureRandom.nextBytes(saltBytes);
        
        // 将字节数组转换为可读的字符串 (Hex 或 Base64)
        return DigestUtils.bytesToHexString(saltBytes);
    }

    public static void main(String[] args) {
        System.out.println("生成 5 个 Salt 测试唯一性：");
        for (int i = 0; i < 5; i++) {
            String salt = randomSalt();
            System.out.println("Salt " + (i+1) + ": " + salt);
        }
    }
}