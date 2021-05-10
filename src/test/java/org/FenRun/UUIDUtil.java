package org.FenRun;

import java.util.UUID;

public class UUIDUtil {

	private static String UUIDKEYS = "0123456789ABCDEF0123456789ABCDEF";

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	
	/**
	 * 产生32个字节长度的随机字符串，由0-9，A-F组成，大写 
	 */
	public static String get32Key() {
		int len = UUIDKEYS.length();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < 32; i++) {
	       sb.append(UUIDKEYS.charAt((int) Math.round(Math.random() * (len - 1))));
	    }
	    return sb.toString();
	}
	
	/**
	 * 产生16个字节长度的随机字符串，由0-9，A-F组成，大写 
	 */
	public static String get16Key() {
		int len = UUIDKEYS.length();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < 16; i++) {
	       sb.append(UUIDKEYS.charAt((int) Math.round(Math.random() * (len - 1))));
	    }
	    return sb.toString();
	}
	
	public static void main(String[] a){
		System.out.println("UUID:"+get16Key());
		System.out.println("UUID:"+get32UUID());
		System.out.println("UUID KEY:"+get32Key());
	}
}
