package org.Client;

import com.alibaba.fastjson.JSON;
import org.DB.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/4/25 16:26
 */
public class LoginAndRegist {

    private Logger logger = LoggerFactory.getLogger("LoginAndRegist");
    private AppSecurityService appSecurityService;
    private static RestTemplate restTemplate = new RestTemplate();
    private static String H5_URL = "http://hybrid-h5.qianziworth.cn/api/";
    private static String URL = "http://172.16.200.215:17001/";
    private String token;

    @Test
    public void userloginTest() {

        String loginUrl = URL + "login/do_login";
        ///login/do_login
        final HashMap<String, String> headers = getHeader();
        final String key = HttpRequestManager.generateEncryptKey(
                headers.get("channelCode"),
                headers.get("platform"),
                headers.get("time"),
                headers.get("appVersion")
        );

        logger.info("-----> key:"+key);

        //token是请求token接口获取的
        final HashMap<String, String> params = new HashMap<String, String>() {{
            put("action", "do_login");
            put("timestamp", String.valueOf(System.currentTimeMillis()));
            put("mobile", "13817391487");
            put("password", DigestUtils.md5DigestAsHex("qwas1212".getBytes()));
        }};

        final String pData = PasswordUtils.createMapP(params, key);

        logger.info(pData);

        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("p", pData);

        final String result = PostUtils.restTemplatePost(restTemplate, URL + "login/do_login", headers, bodyMap);

        int code = JSON.parseObject(result).getIntValue("code");
        if (code == 0) {
            final String data = JSON.parseObject(result).getString("data");
            try {
                final String reqInfo = DesUtils.decodeBack(data, key);
                token = JSON.parseObject(reqInfo).getString("data");
                logger.info("----->token {}", token);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void urlDecode(Map<String, Object> returnMap) throws UnsupportedEncodingException {
        Iterator<Map.Entry<String, Object>> entries = returnMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String key = entry.getKey();
            String value = URLDecoder.decode(entry.getValue().toString(), "UTF-8");
            returnMap.put(key, value);
        }
    }


    private HashMap<String, String> getHeader() {
        return new HashMap<String, String>(6) {
            {
                put("Content_Type", "application/json");
                put("channelCode", "110");
                put("platform", "android");
                put("time", String.valueOf(System.currentTimeMillis()));
                put("appVersion", "1.0.1");
                put("appId", "APP");
                put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10; Android SDK built for x86 Build/QSR1.190920.001)");
            }
        };
    }


}
