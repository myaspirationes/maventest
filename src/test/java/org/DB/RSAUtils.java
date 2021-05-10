package org.DB;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static String publicKey;
    private static String privateKey;

    /** 指定加密算法为RSA */
    private static String ALGORITHM = "RSA/ECB/PKCS1Padding";
    /** 指定key的大小 */
    private static int KEYSIZE = 2048;
    /** 指定公钥存放文件 */
    private static String PUBLIC_KEY_FILE = "PublicKey";
    /** 指定私钥存放文件 */
    private static String PRIVATE_KEY_FILE = "PrivateKey";

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    /** */
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /** */
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** */
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 247;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /** */
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData
     *            已加密数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);// TODO ALGORITHM
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(RSA_ANDROID);
        // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** */
    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData
     *            已加密数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    private static String RSA_ANDROID = "RSA/ECB/PKCS1Padding";
    // private static String RSA_ANDROID = "RSA";
    // private static String RSA_JAVA = "RSA/None/PKCS1Padding";

    /** */
    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data
     *            源数据
     * @param publicKey
     *            公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_ANDROID);
        // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** */
    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data
     *            源数据
     * @param privateKey
     *            私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** */
    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap
     *            密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /** */
    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap
     *            密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 公钥加密
     * 
     * @param source
     * @return
     * @throws Exception
     */
    public static String getsec(String source) throws Exception {
        publicKey = getpublickey();
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
        return encryptBASE64(encodedData);
    }

    /**
     * 私钥解密
     * 
     * @param getsec
     * @return
     * @throws Exception
     */
    public static String dosec(String getsec) throws Exception {
        privateKey = getprivatekey();
        byte[] decry = decryptBASE64(getsec);
        byte[] decodedData = RSAUtils.decryptByPrivateKey(decry, privateKey);
        String target = new String(decodedData);
        return target;
    }

    /**
     * wx公钥加密
     * 
     * @param source
     * @return
     * @throws Exception
     */
    public static String encryWXByPublicKey(String source) throws Exception {
        publicKey = getWXPublickey();
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
        return encryptBASE64(encodedData);
    }

    /**
     * wx 私钥解密
     * 
     * @param getsec
     * @return
     * @throws Exception
     */
    public static String decryptWXByPrivateKey(String getsec) throws Exception {
        privateKey = getWXPrivatekey();
        byte[] decry = decryptBASE64(getsec);
        byte[] decodedData = RSAUtils.decryptByPrivateKey(decry, privateKey);
        String target = new String(decodedData);
        return target;
    }

    /**
     * 生成密钥对
     */
    public static void generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEYSIZE, sr);
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = kp.getPublic();
        /** 得到私钥 */
        Key privateKey = kp.getPrivate();
        /** 用对象流将生成的密钥写入文件 */
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
        try {
            oos1.writeObject(publicKey);
            oos2.writeObject(privateKey);
        } catch (Exception e) {
            LOGGER.error("生成秘钥对的时候异常：{}", e.getMessage(), e);
        } finally {
            /** 清空缓存，关闭文件输出流 */
            oos1.close();
            oos2.close();
        }

    }

    /**
     * 产生签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 验证签名
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * BASE64解密
     *
     * @param decodeStr
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String decodeStr) throws Exception {
        return ApacheCodecBase64Util.decodeStringToByte(decodeStr);
    }

    /**
     * BASE64解密
     *
     * @param decodeStr
     * @return
     * @throws Exception
     */
    public static String decryptBASE64StringToString(String decodeStr) throws Exception {
        return ApacheCodecBase64Util.decodeStringToString(decodeStr);
    }



    /**
     * BASE64加密
     *
     * @param encodeByte
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] encodeByte) throws Exception {
        return ApacheCodecBase64Util.encodeByteToString(encodeByte);
    }

    /**
     * BASE64加密
     *
     * @param encodeByte
     * @return
     * @throws Exception
     */
    public static String encryptBASE64StrintToString(String encodeByte) throws Exception {
        return ApacheCodecBase64Util.encodeStrintToString(encodeByte);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = decryptBASE64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = decryptBASE64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char)(((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char)(val > 9 ? val + 'A' - 10 : val + '0');

            val = (char)(bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char)(val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte)(asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte)(asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte)(asc - 'a' + 10);
        else
            bcd = (byte)(asc - 48);
        return bcd;
    }

    /**
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static String getpublickey() throws Exception {
        /** 将文件中的公钥对象读出 */
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtqbboLCGCEcYIyByHAp"
            + "9Zq4ymPEoY9jZquXkQ4N1Y9wC6BaySxVF3VXb+ZONLWe5xoKqffRp6WQ31vpbj+q"
            + "XO6H4msKQrXZXU/e/rkw6fh/SV9+FcD6oXs3kf5R8THra+7zMD8upVipVoERPIPb"
            + "ph3FLtOb9MceJdNUrUZnS8CK/jMs5FhuDV/zmAC5Xqch3UT8BgUJc2SRJ8I2Y1Xc"
            + "ngbgEGMonpxlZ+yxi41+EcSr/NfkdMySoBe6egUbL1bs0EQCbvLU0yhyYaV/Ki4d"
            + "obJaIYO9r8zVBkLpOKjFe0wxrtXDJbRU/p91chJekKMbWAuCfdJgTgKmiIiAz+pd" + "7QIDAQAB";
    }

    public static String getprivatekey() throws Exception {
        /** 将文件中的私钥对象读出 */
        return "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+2ptugsIYIRxg"
            + "jIHIcCn1mrjKY8Shj2Nmq5eRDg3Vj3ALoFrJLFUXdVdv5k40tZ7nGgqp99GnpZDf"
            + "W+luP6pc7ofiawpCtdldT97+uTDp+H9JX34VwPqhezeR/lHxMetr7vMwPy6lWKlW"
            + "gRE8g9umHcUu05v0xx4l01StRmdLwIr+MyzkWG4NX/OYALlepyHdRPwGBQlzZJEn"
            + "wjZjVdyeBuAQYyienGVn7LGLjX4RxKv81+R0zJKgF7p6BRsvVuzQRAJu8tTTKHJh"
            + "pX8qLh2hslohg72vzNUGQuk4qMV7TDGu1cMltFT+n3VyEl6QoxtYC4J90mBOAqaI"
            + "iIDP6l3tAgMBAAECggEASgPtk4YST9lHcAoNqvYUz7MZRyQ59Y3uNrsFJLQWmWPv"
            + "SBMAIlSXC2nuZu8iFT76SRXIXPJG1IVZEcZLWP5Hy5KWEDEC4aw0iAOZr5+SYKqG"
            + "cqYd98HHcSif/HZswOVjL5CTr3pRVL51rZ6QWK+qV1nkRqy6Jsux3Wl/xVMwbCes"
            + "TLufVOE1cRvNm9HnOturAz3yiWCxieytr7pk5KoCyNgzeE8oamujHcK4Nc2Q7mdd"
            + "Wd6qAA6ur98YiSVo2L/q8wSPgfZgrgMWeSUqFdRYlg3jLxfc6MrAke+Wjuh0HBNE"
            + "yvugSsn3RYz2ZIV3QuI2AuChRfv3lyafuvjan1Dr4QKBgQDeWfgLGBBQWQVPz7eX"
            + "AZym2+nSAq+JSAUB+bI8ff8xURGC/YhvJR2QYCjbZneSqTR8YpjZ5ANtM6G00zw9"
            + "BWA+5A9T95VXtBhf5uw0i2ZMiLOnOIJprOjh9bw3FiJ8ND7E+Pg5Vcbi0GymnnAC"
            + "NQOTeV/mqky4Uf7FFyQ3FJ0B7wKBgQDbvGfQAUwfIa/8bFmKql4koqp59FBQcLB2"
            + "9jhhXnXHO5qWhX34eLq6LvLBByHwSKahNOr1TjhwS9lKn/TDbiA4keGo1zHzC4ZR"
            + "NTpYxaKvjXJ6iQE1D/KZ5ZNsIHB/3Va+MYzYeTOcFiSqtlhZ2z7oRZVoWaI0pO1U"
            + "ac+5aaPJ4wKBgBHP30D0QopNN1G3cBuWs3ficg6yzGEQ9eW8nPyfk0veFKsLqFxO"
            + "+tlP+rXotXd4Oo4kT95Ul7mtwyx7J34z6gvftSKi2MHX2YQFObLT3/QNl2az7i6M"
            + "/XLsfpFjIowUm1qoHypoprAQxkNqucpTeN/df0M5LUAuwVwEiJApBJLdAoGBAJ2w"
            + "/+9Ofwp4aouEVl4lFZrFAgKzSk/wDzPQT3XRDEUGiVIsStLeEWM7/gp4y+pcuPhH"
            + "ubaBhA9S34WmdUkq5wSx7Hf2t2nsg2AUCuNaMf46TEWTvMIPx9KZ5spaHWIlvfKQ"
            + "LLvNfzQsz/OTGKSjqumVxjs02CtfkNJbfS9amKhxAoGAOmTdTzRbNRI8fEX8X3YA"
            + "tlTEEPjPx0VwHeBpRNIxXqBrQCwVzWUGSvga6ocM0ultKFFEXfkpqd6gL9C16k19"
            + "PVxYXzlCOX2ELJvQ9Y1acQXbAjveGBy6uKlKBYTanjZrbqYTDLUl3YzNa8jGH5uv" + "LVjwYoR9I4wKkf5BdNJnr5c=";
    }

    public static String getWXPublickey() throws Exception {
        /** 将文件中的公钥对象读出 */
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyDvkygCCeYoRAbK7Zxio"
            + "i+sSUmjyiBCFtkj1puHsVRa8v1UrmTlN9NZ+De2YGAPp+cqucdQUlVXOYFG/T5db"
            + "s5eyqMS6Ncwjd/LhCnadcFv6j9K7utNer3vhENo8MVGlVOG/oRHc1hxQkELVnTBA"
            + "Zv7LFgL/Ol85Abe8WPSOkzj1HgbbH3d4zBXqoHDEzoTzRCQTNQjV4AucBtTl6Uy6"
            + "xEj97S4jOA+VAqDGw/XywvErq8KrMTFDmqnvbbpPwucNxU8ZoCaqqnzGmGn/ubIJ"
            + "5m9h+9Fa+AYt68mKgrwqjnJm6KNHjY+uI5FEW919yvmiLnY+XL2Fe4eOSCRLHcAY" + "fwIDAQAB";
    }

    public static String getWXPrivatekey() throws Exception {
        /** 将文件中的私钥对象读出 */
        return "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDIO+TKAIJ5ihEB"
            + "srtnGKiL6xJSaPKIEIW2SPWm4exVFry/VSuZOU301n4N7ZgYA+n5yq5x1BSVVc5g"
            + "Ub9Pl1uzl7KoxLo1zCN38uEKdp1wW/qP0ru6016ve+EQ2jwxUaVU4b+hEdzWHFCQ"
            + "QtWdMEBm/ssWAv86XzkBt7xY9I6TOPUeBtsfd3jMFeqgcMTOhPNEJBM1CNXgC5wG"
            + "1OXpTLrESP3tLiM4D5UCoMbD9fLC8SurwqsxMUOaqe9tuk/C5w3FTxmgJqqqfMaY"
            + "af+5sgnmb2H70Vr4Bi3ryYqCvCqOcmboo0eNj64jkURb3X3K+aIudj5cvYV7h45I"
            + "JEsdwBh/AgMBAAECggEAXthX+AMjynRGt/o0YJJCi/wuMpD5iM/itkR677CGJIvf"
            + "nfW28B4/ueC71tcbOwznkGWcGdjuYwpA9kjiiM3WJwyNGPoOhhsLFe8a/7zNuIIo"
            + "/mVAfS9P42mXY0csSeQ1ny5Lef0fEOBanOq7inhnmSHJisSipsCiHRkuAyjcPAlg"
            + "lyOk6QxWmW9HNjI/iDT0GIqbFi2FAVZRzQkJRZ9i6btS3YsI/hi8gwn5OzNmx2kF"
            + "rnilBZ7ZqGLMtCdnkPF0YIMqinm7v594Dkk5vq+RxnTqtsmiR8zcvPv8GsGOZNys"
            + "mU9vc+tg3K+oslBKzI0FEZSlBeNFOgHaL4QjHZR82QKBgQD0tNdwhFKHJw8PgmCl"
            + "g5rpZEWU7b4Et13Ey6xXLPJ1cUhBh5mRwW/CYFIVWVoLylRXGLWjOm2agd9OW4fy"
            + "Wg4hItKg+22tNonqAOit/zJWwaA5Ov9ldf9cxdqt7Dqtw7v+hjsJLZcanVU1wfRe"
            + "wagAX5xAzQ+LW8ttr0FwV4Ry8wKBgQDReZ5qZOd5+s8nI6ArFODZbgKqgcW3COby"
            + "jrbu2l1qpcF81TlW8blTF/MjAyRAig9b1vnFa3JlX6SLp6yXJknmhjQ9FtqJFPQ5"
            + "m4b1ri8cJdnP+xAt4vKy80a8qthD9f2BoKCxmiCqN/HxAzY+JNsZI9Xam4+BPADv"
            + "gqA3deOvRQKBgGv52A5n9Np6G6UmTGr8NUDC5AkhFnWSS6fojzUwOhQkVlPD97le"
            + "92d3hQCymo8VSj3cQ1mxEGfa4qP6s3P21ME+Ul6CUxTZOeWtXP5Z2uZ5La/THpqw"
            + "hGt1SlbwI7xE4BEhPa+BFGnD/rNnz+J35R+1EIdvdGpzqMHPUTq3A1SNAoGANpBW"
            + "amJURVrGgOxvuSkcFKbqPN3G0ZGZDs+L6RWqRbBdgRXNdM9MNc/EPneABuuvan3J"
            + "29nMF4ndQ59ufoSztT959fuw+wohP6w6cF5p2Qwm+1zrbvhxvZlO8LAT20gFO7HK"
            + "idia75vZAq2SgId7JsZF20awD7rAh6Lf9vJXV30CgYBP4ZZpkVojddcYXnk3tL9V"
            + "v9MOkE7OvQbX/QCDQIF5oPZ7ChZXs6aGTXE31yEffkneEb35f6JfHUKByUeb/h5d"
            + "XkC4rtRAtk2iE7/DtHEuQVWTbdmVxXP9briAmyX5Ls/QCbPEx/yWiMYhaDaUmAwq" + "GsMVk0aHt7P6EQU7AfqGKQ==";
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     * 
     * @throws UnsupportedEncodingException
     */
    public static String unicode2String(String unicode) throws UnsupportedEncodingException {

        String result = URLDecoder.decode(unicode, "UTF-8");
        return result;
    }

@Test
    public  void  RSAtest() {

        try{
           String random = UUIDUtil.get32UUID();
         System.out.println(random.toUpperCase());
         System.out.println(getsec(random));
        /* String tel = "15618173341";
         String sec = getsec(tel);
         System.out.println(sec);
         RDVDN0M1RjA2ODQ3MDFFREI0NjhERDI0OERERjlFMzU=
         */

            String tel = "15618173341";
            String sec = getsec(tel);
            System.out.println("tel加密："+sec);
            String tt = getsec("3e1b507a49e7498f9ae8a9710846ff34");
            System.out.println(tt);
        /* String random = "D5C7C5F0684701EDB468DD248DDF9E35";
        String tokenServie =  encryptBASE64(random.getBytes());
        System.out.println(tokenServie);
        System.out.println(new String(RSAUtils.decryptBASE64(tokenServie)));
        byte[] data = tokenServie.getBytes("UTF-8");
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, ValueConstant.privateKey);
        tokenServie = RSAUtils.encryptBASE64(encodedData);
        System.out.println(tokenServie);

        byte[] encodedData2 = RSAUtils.decryptBASE64(tokenServie);
        byte[] data2 = RSAUtils.decryptByPublicKey(encodedData2, ValueConstant.publicKey);
        byte[] vau = decryptBASE64(new String(data2));
        System.out.println(new String(vau));*/

        /* String random = "D5C7C5F0684701EDB468DD248DDF9E35";
        String baseRamdom = encryptBASE64StrintToString(random);
        String tokenServie =  encryWXByPublicKey(baseRamdom);
        */
            String tokenServie =
                    "iZkdxqC9GK51DPfXU4/OFqY010ax2xaxqqzUM8qUmXePo4kOTe0Z/ZD4rY2cnAt4LFh5I3LRnbBBGA2xU2ZiGsPJpsfey4HO9t3G6ifeYSIVl9cAu6DJGGUDYdvzxNTjj4Tt4jYp5cZyEVnkBogI3NknGTtyD6tp4oBaqd6QFuXoV8u8XzesyGb9SNK19ztYWJmEdvPn3KlppJY/8ZZpfhPzFGqzdDSDKgajKDXnM1N5lMqB2BIN0VF+RJDwfjS9Dis3fk3dHa7N+hOWTujq2Y7jUHI0Y0RoTqx0i3iPsXMtF8ogmen7a9s1axoVHJtH0Nu7fPIpoZTSiN8yDNMA3g==";
            System.out.println(tokenServie);
            String encodedData = decryptWXByPrivateKey(tokenServie);
            System.out.println(encodedData);
            String token = decryptBASE64StringToString(encodedData);
            System.out.println(token);
        }catch (Exception e){
            e.getStackTrace();
        }


    }


}