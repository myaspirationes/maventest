package org.FenRun;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密工具类
 * @author Elson
 * 
 */
public class EncryptUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
	
	
	public static void main(String[] args) {
		String s = "app_sys_version=10.3.3&app_version=1.0&nonce=5C6D78C2845FC6849128B6262A2BDBB1&timestamp=1545908615&token_client=NtcmTZmzMAGAIo7V8V1+P956ZLO7GqtJ632k0+bA/cr2zoz37ozeaQ/0BHHvrnszwOqlsoJMfTp8RBL6vavBxyeh2nlidTRnc1yOAELyM29rwAOuNevxCAOaE5/Fm+aggMDngPnEbVYT5ETfaJZyjSX/0+Zo6ImVhOnZ81WiWc6LXbyUSbt5KsGJYzxqDplI6o0kdcpkHj8G8s8wzFyTpvoSvY/DtIr7KXs6/Iitpit0eKOifPGwrbLNXOgB/riYIbBirCu8SFRVYa9pEFrugmupvpT7Qu3sChhmxBGcGJRcWbrxDmBFsqHyoINJONAirC/0EEzk0OvNUPyAHUJ2kQ==";
		System.out.println(sha2EncryptToBase64(s));
	}
	/**
	 * SHA256加密
	 * 
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sha2Encrypt(String text) {
		MessageDigest sha2 = null;
		try {
			sha2 = MessageDigest.getInstance("SHA-256");
			return hex(sha2.digest(text.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * SHA256加密-Base64
	 * 
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sha2EncryptToBase64(String text) {
		MessageDigest sha2 = null;
		try {
			sha2 = MessageDigest.getInstance("SHA-256");
			return Base64.encodeBase64String(sha2.digest(text.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static String sha1Encrypt(String text) {
		MessageDigest sha2 = null;
		try {
			sha2 = MessageDigest.getInstance("SHA1");
			return hex(sha2.digest(text.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5Encrypt(String text) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			return hex(md5.digest(text.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 返回16进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}
	
	
	public static final String KEY_MD5 = "MD5";
	/**
	 * MAC算法可选以下多种算法
	 * 
	 * <pre>
	 * HmacMD5   
	 * HmacSHA1   
	 * HmacSHA256   
	 * HmacSHA384   
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";



	
	/**
	 * MD5加密
	 * 
	 * @param data
	 *            = 需要加密的字符数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	
//	public static void main(String[] args) {
//		//System.out.println(EncryptUtil.sha2Encrypt("w123123"));
//		//System.out.println(EncryptUtil.md5Encrypt("w123123"));  //-->9a51d81c744ca5c8b5632a63fbd0ede7
//		System.out.println(EncryptUtil.sha1Encrypt("123456"));
//	}
	
}
