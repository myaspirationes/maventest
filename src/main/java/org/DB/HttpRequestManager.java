package org.DB;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * @author zhaohongtu
 * @date 2021/4/28 2:52 PM
 */
public class HttpRequestManager {
    /**
     * 获取加密key
     *
     * @param channel_code 渠道号
     * @param platform     平台类别  ios/android
     * @param time         请求时间
     * @param appVersion   app版本号
     */
    public static String generateEncryptKey(String channel_code, String platform, String time, String appVersion) {
        try {
            String originalKey = channel_code + platform + (Long.parseLong(time) / 3) + appVersion;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String md5Code = toHex(md5.digest(originalKey.getBytes("utf-8")));
            String encodeKey = Base64.encodeBase64String(md5Code.getBytes());
            String[] versionCode = appVersion.split("\\.");
            int length = versionCode.length;
            String key = "";
            if (length > 2) {
                int valuePosition = Integer.parseInt(versionCode[length - 1]);
                int startPosition = Integer.parseInt(versionCode[length - 2]);
                key = getEncryptCode(encodeKey, startPosition, valuePosition);
            }
            return key;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密秘钥 生成
     *
     * @param startPosition 取数的起始位置 偶数从前往后，奇数从后往前
     * @param valuePosition 取数位置，取奇数位还是偶数位 奇数取奇数位值，偶数取值偶数位值
     */
    private static String getEncryptCode(String rowKey, int startPosition, int valuePosition) {
        try {
            if (rowKey != null) {
                if (startPosition % 2 != 0) {
                    rowKey = new StringBuilder(rowKey).reverse().toString();
                }
                StringBuffer sbKey = new StringBuffer();
                if (valuePosition % 2 != 0) {
                    for (int i = 0; i < rowKey.length(); i++) {
                        sbKey.append(rowKey.charAt(i));
                        i++;
                    }
                } else {
                    for (int i = 0; i < rowKey.length(); i++) {
                        i++;
                        sbKey.append(rowKey.charAt(i));
                    }
                }
                if (sbKey.length() < 16) {
                    for (int i = rowKey.length() - 1; 0 < i; i--) {
                        sbKey.append(rowKey.charAt(i));
                        if (sbKey.length() == 16) {
                            return sbKey.toString();
                        }
                    }
                } else {
                    return sbKey.substring(0, 16);
                }
                return String.valueOf(sbKey);
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * to String
     */
    private static String toHex(byte[] bytes) {
        final char[] hexDigits = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            ret.append(hexDigits[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
}
