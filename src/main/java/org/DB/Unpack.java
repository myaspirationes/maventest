package org.DB;

public class Unpack {

	public static String unpack_hex(byte[] binaryBytes) {
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < binaryBytes.length; i++) {
			
			int b=(int)binaryBytes[i];
			if(b<0){
				b+=256;
			}
			if(b <= 0x0F)
				buffer.append("0");
			buffer.append(Integer.toHexString(b));
		}
		return buffer.toString().toUpperCase();
		
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; ++n) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}
	

	/*public static void main(String[] args) {
		String len = Integer.toHexString(261);
		int len_Int = len.getBytes().length;
		if (len_Int % 2 != 0) {
			len_Int += 1;
		}

		String ttt = ParseBitMap.LeftFormatString(len, '0', len_Int);
		System.out.println(ttt);
		System.out.println(ttt.substring(0, 2).getBytes()[0]);
		byte[] y = new byte[1];
		String s = "10";
		y[0] = (byte) Integer.parseInt(s);

		System.out.println(y[0]);
	}*/
}
