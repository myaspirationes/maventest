package org.DB;


import org.apache.commons.codec.binary.Hex;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Lost on 2018/3/1.
 */

public class PasswordUtils {

    private static final String APP_REQUEST_MD5_CODE = "O188W0UG7JTSEG27";
    
    private static Logger logger = LoggerFactory.getLogger("PasswordUtils");

    /**
     * 普通MD5
     *
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:00:28
     */
    public static String MD5(String input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "check jdk";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }


    /**
     * 加盐MD5
     *
     * @param password
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:45:04
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验加盐后是否和原文一致
     *
     * @param password
     * @param md5
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:45:39
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取sign的值
     *
     * @param requestString
     * @return
     */
    public static String signParams(String requestString) {
        Map<String, String> map = split(requestString);
        // 排序
        Map<String, String> sortMap = sortMapByKey(map);
        // 排序之后的URL字符串
        String urlParams = getUrlParamsByMap(sortMap);
        logger.info("排序之后的签名顺序为：" + urlParams);
        String stringB = MD5(urlParams);
        String stringC = MD5(stringB + APP_REQUEST_MD5_CODE);
        return stringC;
    }

    /**
     * 获取sign的值
     *
     * @param map
     * @return
     */
    public static String signParams(Map<String, String> map) {

        Map<String, String> sortMap = sortMapByKey(map);
        // 排序之后的URL字符串
        String urlParams = getUrlParamsByMap(sortMap);
        logger.info("排序之后的签名顺序为：" + urlParams);
        String stringB = MD5(urlParams);
        String stringC = MD5(stringB + APP_REQUEST_MD5_CODE);
        return stringC;
    }

    /**
     * 创建加密p
     *
     * @param stampParams 所有参数
     * @return
     */
    public static String createP(String stampParams, String tt) {
        Map<String, String> map = splitValueEmpty(stampParams);
        return createMapP(map, tt);
    }

    public static String createMapP(Map<String, String> map, String tt) {
        // 排序
        Map<String, String> sortMap = sortMapByKey(map);
        // 排序之后的URL字符串
        String urlParams = getUrlParamsByMap(sortMap);

        EncrypSHA sha = new EncrypSHA();
        String strHash = "";
        try {
            strHash = EncrypSHA.hexString(sha.eccryptSHA256(urlParams)).toLowerCase();
            String lastStr = urlParams + "&sign=" + strHash;

            String data = DesUtils.encode(lastStr, tt);
            return URLEncoder.encode(data, "UTF-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }


    /**
     * 根据url转换成map集合
     *
     * @param urlParams
     * @return
     */
    public static Map<String, String> split(String urlParams) {
        Map<String, String> map = new HashMap<>();
        String[] param = urlParams.split("&");
        for (String keyValue : param) {
            String[] pair = keyValue.split("=");
            if (pair.length == 2 && pair[1] != null && !pair[1].equals("")) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }


    /**
     * http 加密分割根据url转换成map集合
     *
     * @param urlParams
     * @return
     */
    public static Map<String, String> splitValueEmpty(String urlParams) {
        Map<String, String> map = new HashMap<>();
        String[] param = urlParams.split("&");
        for (String keyValue : param) {
            String[] pair = keyValue.split("=");
            if (pair.length == 2 && pair[1] != null && !pair[1].equals("")) {
                map.put(pair[0], pair[1]);
            } else if (pair.length > 0) {
                map.put(pair[0], "");
            }
        }
        return map;
    }


    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }


    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>(0);
        if (TextUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                if (entry.getValue() != null) {
                    sb.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 判断密码是否由字母
     *
     * @param pwd
     * @return
     */
    public static boolean pwdIsCorrect(String pwd) {
        return pwd.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$");
    }


}



