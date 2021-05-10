package org.DB;


import java.security.MessageDigest;

/**
 * @author cjq
 * @date 2020/12/8 11:45
 * @describe
 */
public class EncryptData {


    /**
     * 获取加密key
     *
     * @param channelCode 渠道号
     * @param platform     平台类别  ios/android
     * @param time         请求时间 (动态变量)
     * @param appVersion   app版本号
     */
    public static String getEncryptKey(String channelCode, String platform, String time, String appVersion) {
        try {
            String originalKey = channelCode + platform + Long.parseLong(time)/3  + appVersion;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String md5Code = EncryptData.toHex(md5.digest(originalKey.getBytes("utf-8")));
            String encodeKey = Base64Utils.encode(md5Code.getBytes());
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

    public static String toHex(byte[] bytes) {
        final char[] hexDigits = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            ret.append(hexDigits[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static void main(String[] args) throws Exception {
        String originalData = "qwert";
        String encrptKey = getEncryptKey("AppStore", "IOS", "1608184102000", "1.0.0");
        System.out.println("originalData ->" +originalData);
        System.out.println("encrptKey   ->" +encrptKey);
        String encryData = TDES_3DESUtil.encode(originalData, encrptKey);
        System.out.println("encryData   ->" +encryData);
        System.out.println(TDES_3DESUtil.decode("jBo2IrNcXOkvlLfENhvso5vQXdymgMGZV4pS4baMi+sSVwgjedgNmxqdHSZMshGyw/ShS3CEyBiAtcpH7X/RMrbMnFdZcUJIUkOaunxoTbBzBM8KSWHlD7HcEK0V/onsMZe91eoZ31b08MkaFFU9mBKRrQzY0cFy6zYBoJ0+puAqWbYJ+B5MxwcL6GjWl4tVX4njC8d4KuM=", encrptKey));
    }

}
