package com.amituofo.common.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtils {

//    /**
//     * 对给定的字符串以指定的编码方式和密钥进行加密
//     * @param srcStr 待加密的字符串
//     * @param charset 字符集，如utf8
//     * @param sKey 密钥 
//     */
//    public static String encrypt(String srcStr, Charset charset, String sKey) {
//        byte[] src = srcStr.getBytes(charset);
//        byte[] buf = DesUtils.encrypt(src, sKey);
//        return DesUtils.parseByte2HexStr(buf);
//    }
//
//    /**
//     * 对给定的密文以指定的编码方式和密钥进行解密
//     * @param hexStr 需要解密的密文
//     * @param charset 字符集
//     * @param sKey 密钥
//     * @return 解密后的原文
//     * @throws Exception
//     */
//    public static String decrypt(String hexStr, Charset charset, String sKey) throws Exception {
//        byte[] src = DesUtils.parseHexStr2Byte(hexStr);
//        byte[] buf = DesUtils.decrypt(src, sKey);
//        return new String(buf, charset);
//    }
//
////    public static byte[] encrypt(byte[] data, String sKey) {
////        try {
////            byte[] key = sKey.getBytes();
////
//////            IvParameterSpec iv = new IvParameterSpec(key);
////            DESKeySpec desKey = new DESKeySpec(key);
////
////            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
////            SecretKey securekey = keyFactory.generateSecret(desKey);
////
////            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
////
////            cipher.init(Cipher.ENCRYPT_MODE, securekey);
////
////            return cipher.doFinal(data);
////        } catch (Throwable e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    /**
////     * 解密
////     * @param src
////     * @param sKey
////     * @return
////     * @throws InvalidKeyException 
////     * @throws NoSuchAlgorithmException 
////     * @throws InvalidKeySpecException 
////     * @throws NoSuchPaddingException 
////     * @throws InvalidAlgorithmParameterException 
////     * @throws BadPaddingException 
////     * @throws IllegalBlockSizeException 
////     * @throws Exception
////     */
////    public static byte[] decrypt(byte[] src, String sKey) throws Exception  {
////        byte[] key = sKey.getBytes();
////        // 初始化向量
//////        IvParameterSpec iv = new IvParameterSpec(key);
////        // 创建一个DESKeySpec对象
////        DESKeySpec desKey = new DESKeySpec(key);
////        // 创建一个密匙工厂
////        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
////        // 将DESKeySpec对象转换成SecretKey对象
////        SecretKey securekey = keyFactory.generateSecret(desKey);
////        // Cipher对象实际完成解密操作
////        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
////        // 用密匙初始化Cipher对象
////        cipher.init(Cipher.DECRYPT_MODE, securekey);
////        // 真正开始解密操作
////        return cipher.doFinal(src);
////    }
	
	 /**
     * Description 根据键值进行加密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     * 
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}