package org.DB;

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
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RSAUtils.class);
	
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
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(2048);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
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
	  //Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
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
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
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
	//    private static String RSA_ANDROID = "RSA";
	private static String RSA_JAVA = "RSA/None/PKCS1Padding";
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
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		byte[] keyBytes = decryptBASE64(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(RSA_ANDROID);
//		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
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
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
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
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
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
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/*
   */
	public static String getsec(String source) throws Exception {
		publicKey = getpublickey();
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
		return encryptBASE64(encodedData);
	}

	public static String dosec(String getsec ) throws Exception {
		privateKey = getprivatekey();
		byte[] decodedData = RSAUtils.decryptByPrivateKey(
				decryptBASE64(getsec), privateKey);
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
		ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(
				PUBLIC_KEY_FILE));
		ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
				PRIVATE_KEY_FILE));
		try { 
			oos1.writeObject(publicKey);
			oos2.writeObject(privateKey);
		} catch (Exception e) {
			logger.error("生成秘钥对的时候异常",e);
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
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {

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
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
			throws Exception {
		try {
			byte[] buffer = decryptBASE64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}


	public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = decryptBASE64(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}
	/**
	 * 加密方法 source： 源数据
	 * 回复客户端加密
	 */
	public static String RSPencrypt(String source) throws Exception {
		// generateKeyPair();
		/** 将文件中的公钥对象读出 */
//		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
//				PUBLIC_KEY_FILE));
//		Key key = (Key) ois.readObject();
//		ois.close();
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, loadPublicKeyByStr(getRSPpublickey()));
		int MaxBlockSize = KEYSIZE / 8;
		String[] datas = splitString(source, MaxBlockSize - 11);
		String mi = "";
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;

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
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}
 
	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
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
		return   "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtqbboLCGCEcYIyByHAp" +
		"9Zq4ymPEoY9jZquXkQ4N1Y9wC6BaySxVF3VXb+ZONLWe5xoKqffRp6WQ31vpbj+q" +
		"XO6H4msKQrXZXU/e/rkw6fh/SV9+FcD6oXs3kf5R8THra+7zMD8upVipVoERPIPb" +
		"ph3FLtOb9MceJdNUrUZnS8CK/jMs5FhuDV/zmAC5Xqch3UT8BgUJc2SRJ8I2Y1Xc" +
		"ngbgEGMonpxlZ+yxi41+EcSr/NfkdMySoBe6egUbL1bs0EQCbvLU0yhyYaV/Ki4d" +
		"obJaIYO9r8zVBkLpOKjFe0wxrtXDJbRU/p91chJekKMbWAuCfdJgTgKmiIiAz+pd" +
		"7QIDAQAB";
	}

	public static String getprivatekey() throws Exception {
		/** 将文件中的私钥对象读出 */
		return   "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+2ptugsIYIRxg" +
		"jIHIcCn1mrjKY8Shj2Nmq5eRDg3Vj3ALoFrJLFUXdVdv5k40tZ7nGgqp99GnpZDf" +
		"W+luP6pc7ofiawpCtdldT97+uTDp+H9JX34VwPqhezeR/lHxMetr7vMwPy6lWKlW" +
		"gRE8g9umHcUu05v0xx4l01StRmdLwIr+MyzkWG4NX/OYALlepyHdRPwGBQlzZJEn" +
		"wjZjVdyeBuAQYyienGVn7LGLjX4RxKv81+R0zJKgF7p6BRsvVuzQRAJu8tTTKHJh" +
		"pX8qLh2hslohg72vzNUGQuk4qMV7TDGu1cMltFT+n3VyEl6QoxtYC4J90mBOAqaI" +
		"iIDP6l3tAgMBAAECggEASgPtk4YST9lHcAoNqvYUz7MZRyQ59Y3uNrsFJLQWmWPv" +
		"SBMAIlSXC2nuZu8iFT76SRXIXPJG1IVZEcZLWP5Hy5KWEDEC4aw0iAOZr5+SYKqG" +
		"cqYd98HHcSif/HZswOVjL5CTr3pRVL51rZ6QWK+qV1nkRqy6Jsux3Wl/xVMwbCes" +
		"TLufVOE1cRvNm9HnOturAz3yiWCxieytr7pk5KoCyNgzeE8oamujHcK4Nc2Q7mdd" +
		"Wd6qAA6ur98YiSVo2L/q8wSPgfZgrgMWeSUqFdRYlg3jLxfc6MrAke+Wjuh0HBNE" +
		"yvugSsn3RYz2ZIV3QuI2AuChRfv3lyafuvjan1Dr4QKBgQDeWfgLGBBQWQVPz7eX" +
		"AZym2+nSAq+JSAUB+bI8ff8xURGC/YhvJR2QYCjbZneSqTR8YpjZ5ANtM6G00zw9" +
		"BWA+5A9T95VXtBhf5uw0i2ZMiLOnOIJprOjh9bw3FiJ8ND7E+Pg5Vcbi0GymnnAC" +
		"NQOTeV/mqky4Uf7FFyQ3FJ0B7wKBgQDbvGfQAUwfIa/8bFmKql4koqp59FBQcLB2" +
		"9jhhXnXHO5qWhX34eLq6LvLBByHwSKahNOr1TjhwS9lKn/TDbiA4keGo1zHzC4ZR" +
		"NTpYxaKvjXJ6iQE1D/KZ5ZNsIHB/3Va+MYzYeTOcFiSqtlhZ2z7oRZVoWaI0pO1U" +
		"ac+5aaPJ4wKBgBHP30D0QopNN1G3cBuWs3ficg6yzGEQ9eW8nPyfk0veFKsLqFxO" +
		"+tlP+rXotXd4Oo4kT95Ul7mtwyx7J34z6gvftSKi2MHX2YQFObLT3/QNl2az7i6M" +
		"/XLsfpFjIowUm1qoHypoprAQxkNqucpTeN/df0M5LUAuwVwEiJApBJLdAoGBAJ2w" +
		"/+9Ofwp4aouEVl4lFZrFAgKzSk/wDzPQT3XRDEUGiVIsStLeEWM7/gp4y+pcuPhH" +
		"ubaBhA9S34WmdUkq5wSx7Hf2t2nsg2AUCuNaMf46TEWTvMIPx9KZ5spaHWIlvfKQ" +
		"LLvNfzQsz/OTGKSjqumVxjs02CtfkNJbfS9amKhxAoGAOmTdTzRbNRI8fEX8X3YA" +
		"tlTEEPjPx0VwHeBpRNIxXqBrQCwVzWUGSvga6ocM0ultKFFEXfkpqd6gL9C16k19" +
		"PVxYXzlCOX2ELJvQ9Y1acQXbAjveGBy6uKlKBYTanjZrbqYTDLUl3YzNa8jGH5uv" +
		"LVjwYoR9I4wKkf5BdNJnr5c=";
	}

	public static String getprivatekeyShiyi() throws Exception {
		/** 将文件中的私钥对象读出 */
		return   "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+okvkQRhoBHLz\n" +
				"cDkApa9xTGZW64WwoWWM8LUiAnPzZJuTkYdyvCZXBeuHR4E4lifvs8Cwd9Go6YCQ\n" +
				"PUSR6XeuvYLhnn/fpM1ihsjDYLuIH3srxJKJNQWV94AyaSZYnC36+aLo0pWS5cD1\n" +
				"jiQjV4T1NYZLIZvRM/xJojIs2lUIuzgsNI3yvFaw4kaksPahLmWFf1rsbpQc6Kio\n" +
				"jNHyOCb9JxadUlAjlTrq87KPjfNJDvo5cKaDcmgutG4wBWg2hY4Jly7+hxgk0Z2b\n" +
				"6b5aOC64C9FqTaaZzYXEqtP1A8TNMan9+Tu7sjp6Z98daduCUeVzdovkq3odGe0Y\n" +
				"zv26oRRPAgMBAAECggEAAgTJ9PLThVZ2uSk5EU6v2u1KMCVyVvp+js2FvYR1IeCw\n" +
				"3REvdAGWDlnbsqUJM0YzyhAHPFcoezdtyxk6/qiQQeNz3VvGUuH14tM2kQsXMUvU\n" +
				"DBSa0KUlF/909hOy+kE9I2k9VmXUe6e/MVp9+3nTyJz2fRkwAzkftM/o5ByzxdHh\n" +
				"CQbllA/xMDS+7Ca9tZKjlk+oslaZrarABZ4P5gOjSYGvhzR4Lybvr5lERy95tAUd\n" +
				"TI9ChkoDt+6YNdYhR6eGV44eHxWJUQ90pgVIRsjlxzXfMxXYF+uu133TDULE15Kf\n" +
				"fHsgzgFPPslcYi8BTp0UDIMQGRs+HzdvYRy66Dh+wQKBgQDor79sSMjzKcSV3Suz\n" +
				"W7r9MTCKVfvzinCnQBmL1NgHPnEDgQpVOijmHcDCsdm5w4qVqSjIoJmGS8ApXvRY\n" +
				"aZEeU2UgQH/Q4QSSpq2RW6m2UPav+7nX/sHtVH8wBbkRhkx3pi2G2MhkhHka2Qai\n" +
				"QPB8/eWai3M4zyKFKuYUc04/8QKBgQDRu+3mSjhGs2fJiDKvmubOhsDLbKe9PvHZ\n" +
				"bw8eP3uttX/WSUjxdeunO4Ko4AIZPyEMJ1BR4Sw+3O/KMNlRikKjsZl2UnFd6j+A\n" +
				"dCdoukAPmh3VFyM74TbXc50NxU5Z4gVEYSjtRnfCvmCvxzH7bMQFwGgkMOL14msD\n" +
				"ZKXExyzYPwKBgQDU681chxXq/PN4t1Q/4davzY0/aQMOjkYbpZo6zF+J8Wd072XH\n" +
				"K1s1h1BWWGyFghuUa6B4Rg12SGbk9JDfPmqet204IA1V1DJdB8yX0KrxvBCgZNZg\n" +
				"gixuwf1IslPxZ1Udv6C1XjWxVU+Ec8eQPaBSn/q0eprLBy/tb53uZLzb4QKBgAP+\n" +
				"cClSgH/YKsHLtith58NHDaTSDN+tsy1Q0BjjXJeSPTz3aEww0qv8liBhI6kXaqmw\n" +
				"lfb5Mr5/tpFDHOBQobQ8TY/TQhVnwI3RqG52HYIujSLhreoV/9VafkMk3rkXdMI4\n" +
				"l27tSJqMxYqTYIBJMfJqF5GY3LmR9je6Uu3MXS5BAoGAIj6GRu4dbc5u+2ZH+Km3\n" +
				"QBYIJGNxZ4ND9uW9h5WM7aznMU+POZxLD9osWRAp+vOaScMFsSfSXPQLxOvQ/dG3\n" +
				"o0HKinvUkqYtP2jgdUBDNrEIUmfkM0H1+knY+GlQSRtFdOLRFgb8+rw9mh2l9912\n" +
				"HGqobEpPQbe2fMGEWGG/6z0=";
	}


	public static String getRSPpublickey() throws Exception {
		/** 将文件中的公钥对象读出 */
		return    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgJpEXPiqVrcWMPtXm5YLclEVe"
				+ "CQD8z6E0ak+M2p5reaTzzBzyRTl8iWUvB4UWboeAhENjctL7WOrIsGtX9wrNA3M7"
				+ "W3saTTM//5ElaA/f5fLargsKeQMeqemVOqU5jVPqRtoGKTuJ3ha9kBzMj5dJZYSI"
				+ "FV7GNV1SQ50vrdXq4QIDAQAB";
	}

	public static String getRSPprivatekey() throws Exception {
		/** 将文件中的私钥对象读出 */
		return   "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqw20Q7RkqbFx6hhwSUxb\n" +
				"M9Fa2l5g+raw5tSJxYAJZby+wlFF/p3TNupd5gDozEw680w+NFXLplrzPvQRnxTj\n" +
				"jakY4V5PorhAIRtHz3O5o6qstiqyfYGSDUbEfPLzKjLVrXCv3kDT49d2PAD+03WV\n" +
				"t34C3I9/ANrluDH+5BxYB2SiAi8tkpJLAHy7GZQKqxxo0KsaBoECVRCZehyW9w0+\n" +
				"1TcqDBVguLYo/D5otk0COc3AqMULUwtqzsu8gYVLwQZr5LgvvFtcQm5E2Kp/54Ee\n" +
				"Ht8PcjbVhpwInldt20WhOjBXjNftemz7JAO6KftWjzi+KzoZqQuRA27hqJeVtIL+\n" +
				"5wIDAQAB";
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
	 * @throws UnsupportedEncodingException 
	 */  
	public static String unicode2String(String unicode) throws UnsupportedEncodingException {  
	   
		String result = URLDecoder.decode(unicode, "UTF-8");   
	    return result;  
	}

	public static void main(String[] args) throws Exception {
        /* String random = UUIDUtil.get32UUID();
         System.out.println(random);
         System.out.println(getsec(random));*/
        /* String tel = "15618173341";
         String sec = getsec(tel);
         System.out.println(sec);
         RDVDN0M1RjA2ODQ3MDFFREI0NjhERDI0OERERjlFMzU=
         */

		String tel = "15618173341";
		String sec = getsec(tel);
		System.out.println(sec);
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

		String tokenServie =
				"iZkdxqC9GK51DPfXU4/OFqY010ax2xaxqqzUM8qUmXePo4kOTe0Z/ZD4rY2cnAt4LFh5I3LRnbBBGA2xU2ZiGsPJpsfey4HO9t3G6ifeYSIVl9cAu6DJGGUDYdvzxNTjj4Tt4jYp5cZyEVnkBogI3NknGTtyD6tp4oBaqd6QFuXoV8u8XzesyGb9SNK19ztYWJmEdvPn3KlppJY/8ZZpfhPzFGqzdDSDKgajKDXnM1N5lMqB2BIN0VF+RJDwfjS9Dis3fk3dHa7N+hOWTujq2Y7jUHI0Y0RoTqx0i3iPsXMtF8ogmen7a9s1axoVHJtH0Nu7fPIpoZTSiN8yDNMA3g==";
		System.out.println(tokenServie);
		String encodedData = decryptWXByPrivateKey(tokenServie);
		System.out.println(encodedData);
		String token = decryptBASE64StringToString(encodedData);
		System.out.println(token);
*/
	}

}