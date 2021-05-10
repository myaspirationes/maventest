package org.DB;

import org.FenRun.Unpack;
import org.apache.log4j.Logger;

public class TypeConvert {
	
	private static final Logger logger = Logger.getLogger(TypeConvert.class);
	
	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8
				| (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}

	public static int byte2short(byte b[]) {
		return b[1] & 0xff | (b[0] & 0xff) << 8;
	}

	public static int byte2short(byte b[], int offset) {
		return b[offset + 1] & 0xff | (b[offset + 0] & 0xff) << 8;
	}

	public static long byte2long(byte b[]) {
		return (long) b[7] & 255L | ((long) b[6] & 255L) << 8
				| ((long) b[5] & 255L) << 16 | ((long) b[4] & 255L) << 24
				| ((long) b[3] & 255L) << 32 | ((long) b[2] & 255L) << 40
				| ((long) b[1] & 255L) << 48 | (long) b[0] << 56;
	}

	public static long byte2long(byte b[], int offset) {
		return (long) b[offset + 7] & 255L | ((long) b[offset + 6] & 255L) << 8
				| ((long) b[offset + 5] & 255L) << 16
				| ((long) b[offset + 4] & 255L) << 24
				| ((long) b[offset + 3] & 255L) << 32
				| ((long) b[offset + 2] & 255L) << 40
				| ((long) b[offset + 1] & 255L) << 48 | (long) b[offset] << 56;
	}

	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	public static byte[] short2byte(int n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	public static void short2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	public static void long2byte(long n, byte buf[], int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[offset + 1] = (byte) (int) (n >> 48);
		buf[offset + 2] = (byte) (int) (n >> 40);
		buf[offset + 3] = (byte) (int) (n >> 32);
		buf[offset + 4] = (byte) (int) (n >> 24);
		buf[offset + 5] = (byte) (int) (n >> 16);
		buf[offset + 6] = (byte) (int) (n >> 8);
		buf[offset + 7] = (byte) (int) n;
	}

	public static String byte2hex(byte b[]) {
		if (b == null)
			return "字节数组为空";
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}

		return hs.toUpperCase();
	}

	public static String byte2hex_BCD(byte b[]) {
		if (b == null)
			return "字节数组为空";
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}

		return hs.toUpperCase();
	}

	public static byte[] hexStr2hexByte(String orgHexStr) {
		if (orgHexStr == null || orgHexStr.trim() == "")
			return null;
		int len = orgHexStr.trim().length();
		if (len % 2 != 0)
			return null;
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (Integer.valueOf(orgHexStr.substring(i * 2, (i * 2 + 2)),
					16)).byteValue();
		}
		return ret;
	}

	public static String byte2hexStr(byte[] orgByteArr) {
		if (orgByteArr == null || orgByteArr.length == 0)
			return null;
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < orgByteArr.length; i++) {
			String tempStr = Integer.toHexString(orgByteArr[i] & 0xff);
			if (tempStr.length() == 1)
				ret.append("0");
			ret.append(tempStr);

		}
		return ret.toString();
	}

	public static byte[] byte2BCDy(byte[] orgByteArr) {
		int length = orgByteArr.length;
		int orgByteLen = orgByteArr.length;
		if (length % 2 == 0)
			length = length / 2;
		else
			length = length / 2 + 1;
		byte[] rtn = new byte[length];
		int index = 0;
		for (int i = 0; i < orgByteLen; i++) {
			byte b1 = orgByteArr[i];
			byte b2;
			if (i + 1 != orgByteLen)
				b2 = orgByteArr[i + 1];
			else
				b2 = 0x30;

			rtn[index++] = (byte) ((b1 - 0x30) * 10 + (b2 - 0x30));
			i = i + 1;
		}

		return rtn;
	}

	public static byte[] str2Bcd(String asc) {
		asc = asc.trim();
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++) {
			try{
				ret[i] = (Integer.valueOf(asc.substring(i + i, (i+i+ 2)),16)).byteValue();
			}catch(Exception e){ 
				logger.error("TypeConvert Error",e);
			}
		}
		return ret;
	}
	
	/**
	 * str1:32字节，0-9A-F组成
	 * str2:32字节，0-9A-F组成
	 */
	public static String xorTwoStrings(String str1, String str2){
		byte[] _str1 = str2Bcd(str1);
		byte[] _str2 = str2Bcd(str2);
		byte[] tmp = new byte[8];
		for(int ttt = 0;ttt<8;ttt++)
			tmp[ttt] = (byte)(_str1[ttt]^_str2[ttt]);
		
		return Unpack.unpack_hex(tmp);
	}

//	public static void main(String str[]) {
//		byte f[] = { '1', '2', '3' };
//		System.out.println(byte2hex(str2Bcd("104")));
//		// System.out.println(byte2hex("6000510000602100000000".getBytes()));
//		// System.out.println(byte2hex_BCD("`".getBytes()));
//	}
}
