package com.amituofo.common.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 通用 AES-GCM 加密工具类 支持 String 和 byte[] 两种输入输出模式
 */
public class AESUtils {

	// --- 算法常量配置 ---
	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 65536; // 迭代次数
	private static final int KEY_LENGTH = 256; // 密钥长度 (bits)
	private static final int SALT_LENGTH = 16; // Salt 长度 (bytes)
	private static final int IV_LENGTH = 12; // GCM 推荐 IV 长度 (bytes)
	private static final int TAG_LENGTH_BIT = 128; // 认证标签长度 (bits)

	// ================= String 模式 (原有功能) =================

	public static String encrypt(String plainText, char[] pepper) {
		if (plainText == null || plainText.isEmpty()) {
			return "";
		}
		byte[] data;
		try {
			data = plainText.getBytes("UTF-8");
		} catch (Exception e) {
			throw new RuntimeException("String Encryption failed", e);
		}

		byte[] encryptedBytes = encryptBytes(data, pepper);
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String cipherTextBase64, char[] pepper) {
		if (cipherTextBase64 == null || cipherTextBase64.isEmpty()) {
			return "";
		}
		byte[] combined = Base64.getDecoder().decode(cipherTextBase64);
		byte[] decryptedBytes = decryptBytes(combined, pepper);
		try {
			return new String(decryptedBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException("String Decryption failed", e);
		}
	}

	// ================= Byte[] 模式 (新增功能) =================

	/**
	 * 加密字节数组
	 *
	 * @param data   原始二进制数据 (如图片字节、文件流等)
	 * @param pepper 加密密钥种子
	 * @return 加密后的字节数组 (结构: Salt + IV + Ciphertext)
	 */
	public static byte[] encryptBytes(byte[] data, char[] pepper) {
		if (data == null || data.length == 0) {
			return new byte[0];
		}
		if (pepper == null || pepper.length == 0) {
			throw new IllegalArgumentException("Pepper cannot be empty");
		}

		try {
			// 1. 生成随机 Salt 和 IV
			byte[] salt = new byte[SALT_LENGTH];
			byte[] iv = new byte[IV_LENGTH];
			SecureRandom random = new SecureRandom();
			random.nextBytes(salt);
			random.nextBytes(iv);

			// 2. 派生密钥
			SecretKeySpec aesKey = deriveKey(pepper, salt);

			// 3. 执行加密
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, parameterSpec);
			byte[] encryptedBytes = cipher.doFinal(data);

			// 4. 组合数据: [Salt] + [IV] + [Ciphertext]
			byte[] combined = new byte[salt.length + iv.length + encryptedBytes.length];
			System.arraycopy(salt, 0, combined, 0, salt.length);
			System.arraycopy(iv, 0, combined, salt.length, iv.length);
			System.arraycopy(encryptedBytes, 0, combined, salt.length + iv.length, encryptedBytes.length);

			return combined;

		} catch (Exception e) {
			throw new RuntimeException("Byte Encryption failed", e);
		}
	}

	/**
	 * 解密字节数组
	 *
	 * @param encryptedData 加密后的字节数组 (必须包含 Salt + IV + Ciphertext)
	 * @param pepper        解密密钥种子
	 * @return 解密后的原始二进制数据
	 * @throws RuntimeException 如果解密失败 (Pepper 错误或数据损坏)
	 */
	public static byte[] decryptBytes(byte[] encryptedData, char[] pepper) {
		if (encryptedData == null || encryptedData.length == 0) {
			return new byte[0];
		}
		if (pepper == null || pepper.length == 0) {
			throw new IllegalArgumentException("Pepper cannot be empty");
		}

		try {
			// 1. 校验长度并拆分数据
			if (encryptedData.length < SALT_LENGTH + IV_LENGTH) {
				throw new IllegalArgumentException("Invalid ciphertext format: too short");
			}

			byte[] salt = new byte[SALT_LENGTH];
			byte[] iv = new byte[IV_LENGTH];
			// 剩余部分全是密文 (包含 Tag)
			byte[] actualCipherText = Arrays.copyOfRange(encryptedData, SALT_LENGTH + IV_LENGTH, encryptedData.length);

			System.arraycopy(encryptedData, 0, salt, 0, salt.length);
			System.arraycopy(encryptedData, salt.length, iv, 0, iv.length);

			// 2. 重新派生密钥
			SecretKeySpec aesKey = deriveKey(pepper, salt);

			// 3. 执行解密
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.DECRYPT_MODE, aesKey, parameterSpec);

			// doFinal 会自动验证 Tag，如果失败会抛出 AEADBadTagException
			return cipher.doFinal(actualCipherText);

		} catch (javax.crypto.AEADBadTagException e) {
			throw new RuntimeException("Decryption failed: Invalid Pepper or corrupted data", e);
		} catch (Exception e) {
			throw new RuntimeException("Byte Decryption failed", e);
		}
	}

	// ================= 内部辅助方法 =================

	private static SecretKeySpec deriveKey(char[] password, byte[] salt) throws Exception {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		byte[] keyBytes = skf.generateSecret(spec).getEncoded();
		// 清除敏感信息 (可选，但在 Java 中 spec.clearPassword() 更关键)
		spec.clearPassword();
		return new SecretKeySpec(keyBytes, "AES");
	}
}