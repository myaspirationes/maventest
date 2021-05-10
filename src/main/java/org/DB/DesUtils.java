package org.DB;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * author: dn
 * created on: 2017/9/19 13:40
 * description: des加解密
 * version:
 * changetime:
 */
public class DesUtils {
    // 向量
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText, String secretKey) throws Exception {
        Key deskey = null;

        secretKey = (secretKey + secretKey).substring(0, 24);
//        System.out.println("secretKey = "+secretKey);
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return encodeByteToString(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText, String secretKey) throws Exception {
        Key deskey = null;

        secretKey = (secretKey + secretKey).substring(0, 24);
        System.out.println("secretKey = " + secretKey);
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(decodeStringToByte(encryptText));
        return new String(decryptData, encoding);
    }


    public static String decodeBack(String encryptText, String secretKey) throws Exception {

        Key deskey = null;
        secretKey = (secretKey + secretKey).substring(0, 24);
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(decodeStringToByte(encryptText));
        return new String(decryptData, encoding);
    }

    public static byte[] decodeStringToByte(String decodeStr) throws UnsupportedEncodingException {
        byte[] b = decodeStr.getBytes("UTF-8");
        Base64 base64 = new Base64();
        return base64.decode(b);
    }

    public static String encodeByteToString(byte[] encodeByte)
            throws UnsupportedEncodingException {
        Base64 base64 = new Base64();
        byte[] b = base64.encode(encodeByte);
        return new String(b, "UTF-8");
    }
}
