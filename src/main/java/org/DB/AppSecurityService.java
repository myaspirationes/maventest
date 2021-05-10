package org.DB;


import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service("appSecurityService")
public class AppSecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSecurityService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 通过手机号产生会话密钥明文
     * 
     * @param tel
     *            手机号码
     * @param sessionKey
     *            app上送的会话密钥
     * 
     * @throws Exception
     * 
     */
    public String produceSessionKey(String tel, String sessionKey) throws Exception {
        String key = RSAValueConstant.RSA_REDIS_KEY + tel;
        String tempKey = RSAValueConstant.RSA_REDIS_TEMP_KEY + tel;
        String _tmpKey = UUIDUtil.get32Key();
        if (StringUtils.isNotBlank(stringRedisTemplate.opsForValue().get(key))
            && StringUtils.isNotBlank(stringRedisTemplate.opsForValue().get(tempKey))) {
            LOGGER.debug("已生成key={}", key);
            _tmpKey = stringRedisTemplate.opsForValue().get(tempKey);
            return RSAUtils.encryptBASE64(_tmpKey.getBytes());
        }
        LOGGER.debug("新生成key={}", key);
        stringRedisTemplate.opsForValue().set(key, TypeConvert.xorTwoStrings(sessionKey, _tmpKey));
        stringRedisTemplate.opsForValue().set(tempKey, _tmpKey);
        return RSAUtils.encryptBASE64(_tmpKey.getBytes());
    }

    /**
     * 通过手机号获取会话密钥
     * 
     * @param tel
     *            手机号码
     * 
     * @return String or null
     * @throws Exception
     * 
     */
    public String getSessionKey(String tel) throws Exception {
        String key = RSAValueConstant.RSA_REDIS_KEY + tel;
        LOGGER.debug("key={}", key);
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 通过手机号解密数据
     * 
     * @param encTel
     *            密文手机号码
     * @param data
     *            密文数据
     * 
     * @return TreeMap<String,String> 解密之后的参数
     * 
     * @throws Exception
     * 
     */
    public Map<String, Object> decryptData(String encKey, String data) throws Exception {
        Map<String, Object> map = new TreeMap<String, Object>();
        // logger.info("解密APP密文数据，encTel："+encTel+",data:"+data);
        //String key = RSAUtils.dosec(encKey);
//        LOGGER.debug("rsa 解密用户唯一标识，结果：{} ", key);
//        String sessionKey = getSessionKey(key);
//        if (StringUtils.isBlank(sessionKey)) {
//            throw new Exception(key + "会话密钥不存在");
//        }
        data = TDES_3DESUtil.decode(data, encKey);
        // logger.info("解密数据,data:"+data);
        if (StringUtils.contains(data, "&")) {
            String[] tmp = StringUtils.splitByWholeSeparator(data, "&");
            for (String t : tmp) {
                String[] tt = StringUtils.splitByWholeSeparator(t, "=");
                if (!StringUtil.isEmpty(tt[0])) {
                    if (!StringUtil.isEmpty(tt[1])) {
                        map.put(tt[0], tt[1]);
                    } else {
                        map.put(tt[0], "");
                    }
                } else {
                    continue;
                }
            }
        } else {
            LOGGER.info("数据解密失败");
            throw new Exception("数据解密失败");
        }
        return map;
    }

    /**
     * 通过手机号加密数据
     * 
     * @param tel
     *            手机号码
     * @param data
     *            json数据
     * 
     * @return String 密文json
     * 
     * @throws Exception
     * 
     */
    public String encryptData(String tel, String data) throws Exception {
        String sessionKey = getSessionKey(tel);
        if (StringUtils.isBlank(sessionKey)) {
            throw new Exception(tel + "会话密钥不存在");
        }
        return TDES_3DESUtil.encode(data, sessionKey);
    }

    /**
     * 通过加密的手机号加密数据
     * 
     * @param encTel
     *            手机号码
     * @param data
     *            json数据
     * 
     * @return String 密文json
     * 
     * @throws Exception
     * 
     */
    public String encTelencryptData(String encTel, String data) throws Exception {
        return TDES_3DESUtil.encode(data, encTel);
    }

    /**
     * 将Map中的key，value数据，按照key=value&key=value.. ascii码升序的方式，得到String字符串，不包括sign字段
     * 
     * @param TreeMap <String,String>
     *            map
     *
     * @return String
     */
    public String sortMapToString(TreeMap<String, String> map) throws Exception {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!StringUtils.equalsIgnoreCase("sign", key)) {
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
                if (iter.hasNext()) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 得到Map中的数据，按照key=value&key=value.. ascii码升序的方式，得到sha256字符串，不包括sign字段
     * 
     * @param TreeMap<String,String>
     *            map
     *
     * @return String
     */
    public String getSortMapSHA256(Map<String, Object> map) throws Exception {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!StringUtils.equalsIgnoreCase("sign", key)) {
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
                if (iter.hasNext()) {
                    sb.append("&");
                }
            }
        }
        return EncryptUtil.sha2Encrypt(sb.toString());
    }

    /**
     * App上送数据安全性检查。如果安全性通过，返回解密之后的参数。否则抛出异常。
     * 
     * @param pathStr
     *
     * 
     */
    public Map<String, Object> appSercurityCheck(String encKey, String data, String pathStr) throws Exception {
        Map<String, Object> map = decryptData(encKey, data);
        String sign = getSortMapSHA256(map);
        if (!StringUtils.equalsIgnoreCase(sign, map.get("sign").toString())) {
            LOGGER.info("本地签名：{}", sign);
            LOGGER.info("App签名：{}", map.get("sign"));
            throw new Exception("签名验证失败");
        }
        return map;
    }

    /**
     * 获取map
     * 
     * @param properties
     * @return
     */
    public Map<String, String> getMap(Map<String, String[]> properties) {
        Map<String, String> returnMap = new HashMap<String, String>();
        Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
        Entry<String, String[]> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry<String, String[]>)entries.next();
            name = (String)entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[])valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        /*String key = "15810000000";
        String p = RSAUtils.getsec(key);
        System.out.println(p);*/
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("action", "login/do_login");
        returnMap.put("timestamp", "1591600346592");
        returnMap.put("mobile", "15810000000");
        returnMap.put("password", "e10adc3949ba59abbe56e057f20f883e");
        //String signcontext = EncryptUtil.getSignContext(returnMap);
        String signcontext = "action=login&mobile=15810000000&password=e10adc3949ba59abbe56e057f20f883e&timestamp=1591760076138";
        System.out.println(signcontext);
//        String sign = EncryptUtil.sha2Encrypt(signcontext);
//        System.out.println(sign);
//        String data = signcontext + "&sign=" + sign;
//        String enData = TDES_3DESUtil.encode(data, "F46DC561884BF16F");
//        System.out.println(enData);
//        String dec = TDES_3DESUtil.decode(enData, "F46DC561884BF16F");
//        System.out.println(dec);


        String encrptKey = EncryptData.getEncryptKey("AppStore", "IOS", "1608184102000", "1.0.0");
        System.out.println("originalData ->" +signcontext);
        System.out.println("encrptKey   ->" +encrptKey);
        String encryData = TDES_3DESUtil.encode("123456", "T5kBjwU4DE0Fz2k2");
        System.out.println("encryData   ->" +encryData);
        System.out.println(TDES_3DESUtil.decode("jBo2IrNcXOkvlLfENhvso5vQXdymgMGZV4pS4baMi+sSVwgjedgNmxqdHSZMshGyw/ShS3CEyBiAtcpH7X/RMrbMnFdZcUJIUkOaunxoTbBzBM8KSWHlD7HcEK0V/onsMZe91eoZ31b08MkaFFU9mBKRrQzY0cFy6zYBoJ0+puAqWbYJ+B5MxwcL6GjWl4tVX4njC8d4KuM=", "T5kBjwU4DE0Fz2k2"));
    }

}
