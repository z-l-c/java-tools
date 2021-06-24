package com.akane.tools.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密
 *
 * @author akane
 */
public class AesUtils
{
    // 默认AES 算法名称/加密模式/数据填充方式
    private static final String AES_DEFAULT_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 16位密钥
    public static final String AES_DEFAULT_SECRET = "xxxxxxxxxxxxxxxx";

    /**
     * 加密
     * @param content 加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        Cipher cipher = Cipher.getInstance(AES_DEFAULT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        return org.apache.commons.codec.binary.Base64.encodeBase64String(b);

    }

    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        Cipher cipher = Cipher.getInstance(AES_DEFAULT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 使用默认key加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) throws Exception {
        return encrypt(content, AES_DEFAULT_SECRET);
    }

    /**
     * 使用默认key解密
     * @param encryptStr
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr) throws Exception {
        return decrypt(encryptStr, AES_DEFAULT_SECRET);
    }

}
