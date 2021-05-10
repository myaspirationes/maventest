package org.DB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Name：tyf_partner_plus
 * @Description：hash算法
 * @Author：zhaohongtu
 * @Date：2020/6/10 11:04 AM
 * 修改人：zhaohongtu
 * 修改时间：2020/6/10 11:04 AM
 * 修改备注：
 */

public class EncrypSHA {
    //byte字节转换成16进制的字符串MD5Utils.hexString
    public  static byte[] eccrypt(String info, String shaType)  {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(shaType);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] srcBytes = info.getBytes();
        // 使用srcBytes更新摘要
        sha.update(srcBytes);
        // 完成哈希计算，得到result
        byte[] resultBytes = sha.digest();
        return resultBytes;
    }

    public static byte[] eccryptSHA1(String info)  {

        return eccrypt(info, "SHA1");


    }

    public byte[] eccryptSHA256(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-256");
    }

    public byte[] eccryptSHA384(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-384");
    }

    public byte[] eccryptSHA512(String info) throws NoSuchAlgorithmException {
        return eccrypt(info, "SHA-512");
    }


    public static String hexString(byte[] bytes){
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
