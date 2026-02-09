package com.amituofo.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.amituofo.common.ex.EncodeException;

/**
 * A utility that encrypts or decrypts a file.
 *
 */
public class CryptoUtils {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	public static void encrypt(String key, File inputFile, File outputFile) throws EncodeException {
		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}

	public static void encrypt(String key, File[] sourceDirs, String filePattern) throws EncodeException {
		for (File dir : sourceDirs) {
			encrypt(key, dir, filePattern);
		}
	}

	public static void encrypt(String key, File sourceDir, String filePattern) throws EncodeException {
		List<File> files = FileUtils.findFiles(sourceDir, filePattern, true);
		for (File file : files) {
			doCrypto(Cipher.ENCRYPT_MODE, key, file, file);
		}
	}

	public static void decrypt(String key, File inputFile, File outputFile) throws EncodeException {
		doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
	}

	private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws EncodeException {
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (Exception ex) {
			throw new EncodeException("Error when " + (cipherMode == Cipher.ENCRYPT_MODE ? "encrypting" : "decrypting") + " file", ex);
		}
	}
}