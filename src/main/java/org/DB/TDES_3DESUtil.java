package org.DB;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

/***
 * 可以和c#加密结果相同 需要到两个jar包： bcprov-jdk15on-148.jar bcprov-ext-jdk15on-148.jar
 * 在D:/jar/
 * 
 * @ClassName Test3DES_2
 * @author fdd
 * @date 2015年4月16日
 */
public class TDES_3DESUtil {

	private static final Logger logger = Logger.getLogger(TDES_3DESUtil.class);
	
//	public static void main(String[] args) throws Exception {
//		String key =   "00000000000000000000000000000000";
//		String shuju = "0123456789ABCDEFFEDCBA9876543210";
//		String jiamiResult = encryptStr(shuju, key);
//		System.out.println("加密后：" + jiamiResult);// 【与C# CBC 零填充模式 互相加解密】
// 
//	}

    // 向量
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";
    /**
     *
     *           @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText,String secretKey) throws Exception {
        Key deskey = null;
        secretKey = secretKey+secretKey.substring(0, 8);
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return RSAUtils.encryptBASE64(encryptData);        
     }
    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText,String secretKey) throws Exception {
        Key deskey = null;
        secretKey = secretKey+secretKey.substring(0, 8);
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(RSAUtils.decryptBASE64(encryptText));
        return new String(decryptData, encoding);
    } 
	
	/**
	 * 3DES加密 (亦称为：DESede加密)
	 * 
	 * CBC模式 填充模式：零字节填充 ZeroBytePadding
	 * 
	 * @Method: encrypt3Str
	 * @param @param shuju
	 * @param @param key
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	public static String encryptStr(String shuju, String key) throws Exception {
		String result = "";
		try {
			Security.addProvider(new BouncyCastleProvider());
			byte[] bKey = Hex.decode(key);// 十六进制转换成字节数据
			byte[] bMsg = Hex.decode(shuju);

			byte[] keyBytes = Arrays.copyOf(bKey, 24);
			int j = 0, k = 16;
			while (j < 8) {
				keyBytes[k++] = keyBytes[j++];
			}

			SecretKey key3 = new SecretKeySpec(keyBytes, "DESede");
			Cipher cipher3 = Cipher.getInstance("DESede/ECB/ZeroBytePadding");
			cipher3.init(Cipher.ENCRYPT_MODE, key3);// , iv3

			byte[] bMac = cipher3.doFinal(bMsg);
			result = new String(Hex.encode(bMac));// encode方法字节数组转换成十六进制
		} catch (Exception e) { 
			logger.error("TDE_3DES error",e);
			throw e;
		}
		return result;
	}

	/**
	 * 3DES 解密
	 * 
	 * @Method: decryptStr
	 * @param @param shuju
	 * @param @param key
	 * @param @return
	 * @param @throws Exception
	 * @return String
	 * @throws
	 */
	public static String decryptStr(String shuju, String key) throws Exception {
		String result = "";
		try {
			Security.addProvider(new BouncyCastleProvider());
			byte[] bKey = Hex.decode(key);// 十六进制转换成字节数据
			byte[] bMsg = Hex.decode(shuju);

			byte[] keyBytes = Arrays.copyOf(bKey, 24);
			int j = 0, k = 16;
			while (j < 8) {
				keyBytes[k++] = keyBytes[j++];
			}

			SecretKey key3 = new SecretKeySpec(keyBytes, "DESede");
			Cipher cipher3 = Cipher.getInstance("DESede/ECB/ZeroBytePadding");
			cipher3.init(Cipher.DECRYPT_MODE, key3);// , iv3

			byte[] bMac = cipher3.doFinal(bMsg);
			result = new String(Hex.encode(bMac));// encode方法字节数组转换成十六进制
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	public static String getExclusiveOR(String s1, String s2) throws Exception {
		byte[] a = hexStringToByte(s1);
		byte[] b = hexStringToByte(s2);
		int leng = s1.length()/2;
		try {
			byte[] c = new byte[leng];
			for (int i = 0; i < leng; i++) {
				c[i] = (byte) (a[i] ^ b[i]);
			}
			return bytesToHexString(c);
		} catch (Exception e) {
			throw new Exception("异或运算失败,a = " + a + ", b = " + b);
		}
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 数组转换成十六进制字符串
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 获取工作密钥
	 * @throws Exception 
	 */
	public static String getWorkKey(String equipmentKey,String random,String ksn) throws Exception{
//		String encKsn1 = encryptStr(ksn, key).toUpperCase().substring(0,16);//主密钥对ksn加密
//		String orx1 = "C0C0C0C000000000C0C0C0C000000000";
//		String orxKey = TDES_3DESUtil.getExclusiveOR(key,orx1);//主密钥和C0C0C0C0 00000000 C0C0C0C0 00000000 异或
//		String encKsn2 = encryptStr(ksn, orxKey).toUpperCase().substring(0,16);//异或后的结果加密ksn
//		String equipmentKey = encKsn1+encKsn2;//合成设备主密钥
		String encRandom1 = encryptStr(random, equipmentKey).toUpperCase().substring(0,16);//设备主密钥加密随机数
		String orx2 = "FFFFFFFFFFFFFFFF";
		String orxRandom = TDES_3DESUtil.getExclusiveOR(random,orx2);//随机数异或FFFFFFFFFFFFFFFF
		String encRandom2 = encryptStr(orxRandom, equipmentKey).toUpperCase().substring(0,16);//设备主密钥加密异或随机数
		String workKey = encRandom1+encRandom2;
		return workKey;
	}
	
	/**
	 * 获取工作密钥
	 * @param equipmentKey 传输密钥
	 * @param random 随机数
	 * @param ksn 机身号
	 * @throws Exception 
	 */
	public static String getWork(String equipmentKey,String random,String ksn) throws Exception{
		String orx1 = "C0C0C0C000000000C0C0C0C000000000";
		String orx2 = "FFFFFFFFFFFFFFFF";
		String encKsn1 = encryptStr(ksn, equipmentKey).toUpperCase().substring(0,16);
		String orxKey = getExclusiveOR(equipmentKey,orx1);//主密钥和C0C0C0C0 00000000 C0C0C0C0 00000000 异或
		String encKsn2 = encryptStr(ksn, orxKey).toUpperCase().substring(0,16);//异或后的结果加密ksn
		String xx = encKsn1+encKsn2;
		String encRandom1 = encryptStr(random, xx).toUpperCase().substring(0,16);//设备主密钥加密随机数
		String orxRandom = getExclusiveOR(random,orx2);//随机数异或FFFFFFFFFFFFFFFF
		String encRandom2 = encryptStr(orxRandom, xx).toUpperCase().substring(0,16);//设备主密钥加密异或随机数
		String workKey = encRandom1+encRandom2;
		return workKey;
	}
}
