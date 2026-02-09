package com.amituofo.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Aes128CharEncryptor {

    private static final int KEY_LENGTH = 128;           // AES-128
    private static final int SALT_LENGTH = 16;           // 128-bit salt
    private static final int GCM_IV_LENGTH = 12;         // 96-bit IV
    private static final int GCM_TAG_LENGTH = 16;        // 128-bit auth tag
    private static final int ITERATION_COUNT = 600_000;  // PBKDF2 iterations

    public static byte[] encrypt(char[] plaintextData, char[] password)
            throws GeneralSecurityException {
        if (plaintextData == null || password == null || password.length == 0) {
            throw new IllegalArgumentException("Input required");
        }

        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        SecretKey key = deriveAes128Key(password, salt, ITERATION_COUNT);
        byte[] plaintext = toUtf8Bytes(plaintextData); // 可优化（见下文）

        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(plaintext);

        byte[] result = new byte[salt.length + iv.length + ciphertext.length];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(iv, 0, result, salt.length, iv.length);
        System.arraycopy(ciphertext, 0, result, salt.length + iv.length, ciphertext.length);

        Arrays.fill(plaintext, (byte) 0);
        return result;
    }

    public static char[] decrypt(byte[] encryptedData, char[] password)
            throws GeneralSecurityException {
        if (encryptedData == null || password == null) {
            throw new IllegalArgumentException("Input required");
        }
        if (encryptedData.length < SALT_LENGTH + GCM_IV_LENGTH + GCM_TAG_LENGTH) {
            throw new IllegalArgumentException("Ciphertext too short");
        }

        byte[] salt = Arrays.copyOfRange(encryptedData, 0, SALT_LENGTH);
        byte[] iv = Arrays.copyOfRange(encryptedData, SALT_LENGTH, SALT_LENGTH + GCM_IV_LENGTH);
        byte[] ciphertext = Arrays.copyOfRange(encryptedData, SALT_LENGTH + GCM_IV_LENGTH, encryptedData.length);

        SecretKey key = deriveAes128Key(password, salt, ITERATION_COUNT);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] plaintext = cipher.doFinal(ciphertext);

        char[] result = fromUtf8Bytes(plaintext);
        Arrays.fill(plaintext, (byte) 0);
        return result;
    }

    private static SecretKey deriveAes128Key(char[] password, byte[] salt, int iterations)
            throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } finally {
            spec.clearPassword();
        }
    }

    // === 安全编码（避免 String）===
    private static byte[] toUtf8Bytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        try {
            return StandardCharsets.UTF_8.newEncoder().encode(charBuffer).array();
        } catch (CharacterCodingException e) {
            throw new IllegalArgumentException("Invalid UTF-8", e);
        }
    }

    private static char[] fromUtf8Bytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        try {
            return StandardCharsets.UTF_8.newDecoder().decode(byteBuffer).array();
        } catch (CharacterCodingException e) {
            throw new IllegalArgumentException("Invalid UTF-8", e);
        }
    }

    // ========================
    // ✅ 测试 main 方法
    // ========================
    public static void main(String[] args) throws Exception {
        char[] original = "Hello, 世界! 🌍 123".toCharArray();
        char[] password = "MySecurePassword!2026".toCharArray();

        System.out.println("Original: " + new String(original));

        // 加密
        long start = System.currentTimeMillis();
        byte[] encrypted = encrypt(original, password);
        long encryptTime = System.currentTimeMillis() - start;
        System.out.println("Encrypted length: " + encrypted.length + " bytes");
        System.out.println("Encryption time: " + encryptTime + " ms");

        // 清空原始数据
        Arrays.fill(original, ' ');

        // 解密
        start = System.currentTimeMillis();
        char[] decrypted = decrypt(encrypted, password);
        long decryptTime = System.currentTimeMillis() - start;
        System.out.println("Decrypted: " + new String(decrypted));
        System.out.println("Decryption time: " + decryptTime + " ms");

        // 验证相等（使用恒定时间比较）
        boolean isEqual = isEquals("Hello, 世界! 🌍 123".toCharArray(), decrypted);
        System.out.println("Content matches: " + isEqual);

        // 清理
        Arrays.fill(decrypted, ' ');
        Arrays.fill(password, ' ');

        // 测试错误密码
        try {
            decrypt(encrypted, "WrongPass".toCharArray());
            System.out.println("ERROR: Should not decrypt with wrong password!");
        } catch (GeneralSecurityException e) {
            System.out.println("Correctly rejected wrong password: " + e.getClass().getSimpleName());
        }

        // 测试多次加密结果不同
        byte[] enc1 = encrypt("test".toCharArray(), password);
        byte[] enc2 = encrypt("test".toCharArray(), password);
        System.out.println("Two encryptions of same data are different: " + !Arrays.equals(enc1, enc2));
    }

    // 恒定时间比较（用于测试）
    private static boolean isEquals(char[] a, char[] b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}