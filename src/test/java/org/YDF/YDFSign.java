package org.YDF;

import com.alibaba.fastjson.JSONObject;
import org.DB.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.DB.RSAUtils.getsec;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/6/19 17:23
 */
public class YDFSign {

    private static RestTemplate restTemplate = new RestTemplate();

    String Url="http://nemp.qianziworth.cn/nemp/";

    @Test
    public void getsmscodeTest(){
        String signUrl= Url+"app/getsmscode.do/?tel=13817391499&type=1";
        HttpHeaders headers =new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("tel","13890909898");
        body.add("type","1");

        HttpEntity<String> request=  new HttpEntity(body ,headers);

        ResponseEntity<String> response = restTemplate.getForEntity(signUrl, String.class);
        System.out.println(response);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void registerTest(){
        String signUrl= Url+"app/register.do";
        String time= System.currentTimeMillis()+"";

        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("appid", "2132");
        headers.add("app_id", "243243435");
        headers.add("platform", "android");
        headers.add("random", time);
        headers.add("message", "2132");
        headers.add("i_version", "2132");


        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("tel","13890123456");
        body.add("type","1");
        body.add("check_code","13890234567");
        body.add("password", MD5Util.getMD5("123456"));
        body.add("i_version", "android");
        body.add("platform_id", "android");
        body.add("app_down_platform","self");
        body.add("appid", "003");
        body.add("app_id", "003");
        body.add("release_time", "erer");
        HttpEntity<String> request=  new HttpEntity(body ,headers);

        ResponseEntity<String> response = restTemplate.postForEntity(signUrl,request, String.class);
        System.out.println(response);

//        String time= System.currentTimeMillis()+"";
//
//                .addHeader(HEADER_X_HB_Client_Type, FROM_ANDROID)
//                .addHeader("appid", NetAddressManager.APP_ID)
//                .addHeader("app_id", NetAddressManager.APP_ID)
//                .addHeader("platform", "android")
//                .addHeader("random", time)
//                .addHeader("message", ToolUtils.sha2_get(MyAppc.getInstance(), time, time.length()))
//                .addHeader("i_version", AppUtils.getAppVersionName(MyAppc.getInstance().getPackageName()))
//
//
//
//        params.put("i_version", AppUtils.getAppVersionName());
//        params.put("platform_id", "android");    //ios/android
//        params.put("app_down_platform", "self");    //ios/android
//        params.put("appid", NetAddressManager.APP_ID);     //001:小蓝牙   002:大蓝牙  003:电签   004
//        params.put("app_id", NetAddressManager.APP_ID);     //001:小蓝牙   002:大蓝牙  003:电签   004
//        params.put("release_time", BuildConfig.RELEASE_TIME);



    }


    @Test
    public void testToken()  {
        String  tel= "13898987878";
        Map<String, String> map = YDFSign.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");
        System.out.println("shoujihao ---------------" + encodeTel);

    }

    /**
     * 获取token响应中的 tt
     *
     * @return tt, 加密的手机号
     */
    private static Map<String, String> token(String tel)  {
        String tokenUrl = "http://nemp.qianziworth.cn/nemp/app/token.do";
        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /**
         * 时间戳
         */
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        System.out.println(STR);
        /**
         * 生成message
         */
        String message = Sha256.getSHA256(STR);
        System.out.println("message参数：" + message);
        /**
         * 手机号加密
         */
        String encodeTel = null;
        try {
            //String tel = "13817391487";
            encodeTel = getsec(tel);
            System.out.println("tel加密：" + encodeTel);
        } catch (Exception e) {
            e.getStackTrace();
        }
        /**
         * 32位随机数加密生成客户端token_client
         */
        String random = UUIDUtil.get32UUID().toUpperCase();
        //System.out.println(random);
        String token_client = null;
        try {
            byte[] to = RSAUtils.encryptBASE64(RSAUtils.encryptByPublicKey(random.getBytes(), ValueConstant.publicKey)).getBytes();
            token_client = new String(to);
            System.out.println("token_client 加密:" + token_client);
        } catch (Exception e) {
            e.getStackTrace();
        }

        /**
         * 请求头，form 格式
         */
        HttpHeaders headers = new HttpHeaders();
        headers.add("random", timeStp);
        headers.add("message", message);
        headers.add("app_id", "004");
        //headers.add("channelCode", "111");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
//        headers.add("i_version", "1.0.6");


        /**
         * 请求体，form 格式
         */
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tel", encodeTel);
        body.add("token_client", token_client);
        body.add("i_version", "1.0.6");
        body.add("app_id", "004");
        //body.add();

        /**
         * 发送请求并返回tt
         */
        HttpEntity<String> jsonRequest = new HttpEntity(body, headers);
        ResponseEntity<String> Response = restTemplate.postForEntity(tokenUrl, jsonRequest, String.class);
        System.out.println("token响应体：" + Response);

        System.out.println("token响应体：" + Response.getHeaders().get("Set-Cookie"));
        //System.out.println("sessionId" + Response.getHeaders().get("Set-Cookie").get(0).substring(11, 43));
        String sessionId = Response.getHeaders().get("Set-Cookie").get(0).substring(11, 43);
        String strBody = Response.getBody();
        JSONObject jsonObject1 = JSONObject.parseObject(strBody);

        String tt = jsonObject1.getJSONObject("body").getString("tt");
        //System.out.println("tt值：" + jsonObject1.getJSONObject("body").getString("tt"));

        Map<String, String> map = new HashMap<String, String>();
        map.put("sessionId", sessionId);
        map.put("tt", tt);
        map.put("encodeTel", encodeTel);




        return map;
    }




    /**
     * 获取图形验证码 imagerCheck_code
     *
     * @return imageCode, imageKey,
     */
    private static Map<String, String> getImageCode() {
        String tokenUrl = "http://nemp.qianziworth.cn/nemp/app/get_newImageCode.do";
        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /**
         * 时间戳
         */
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        System.out.println(STR);
        /**
         * 生成message
         */
        String message = Sha256.getSHA256(STR);
        System.out.println("message参数：" + message);

        String imageKey = UUIDUtil.get16Key().toUpperCase();

        //String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        /**
         * 请求头，form 格式
         */
        HttpHeaders headers = new HttpHeaders();
        headers.add("random", timeStp);
        headers.add("message", message);
        headers.add("appId", "004");
        headers.add("channelCode", "111");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("i_version", "1.0.6");

        /**
         * 请求体，form 格式
         */
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageKey", imageKey);
        //body.add("sign", "9d0b2ab44e648f35e54758f492d3359b");
        body.add("platform_id", "android");
        /**
         * 发送请求并返回tt
         */
        HttpEntity<String> jsonRequest = new HttpEntity(body, headers);
        ResponseEntity<String> Response = restTemplate.postForEntity(tokenUrl, jsonRequest, String.class);
        System.out.println(Response);

        String strBody = Response.getBody();
        JSONObject jsonObject1 = JSONObject.parseObject(strBody);
        String imageCode = jsonObject1.getJSONObject("body").getString("imageCode");
        //System.out.println("imageCode值：" + jsonObject1.getJSONObject("body").getString("imageCode"));
        Map<String, String> mapImageCode = new HashMap<String, String>();
        mapImageCode.put("imageKey", imageKey);
        mapImageCode.put("imageCode", imageCode);
        return mapImageCode;
    }

    /**
     * 获取短信验证码 msgCheck_code
     *
     * @return msgCheck_code
     */
    private static Map<String, String> getMsgCheckCode(String tel) {
        String Url = "http://nemp.qianziworth.cn/nemp/app/1.0/get_newMessageCheckCode.do";

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /*  手机号 */
        //tel = "13300002234";

        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        System.out.println(STR);
        String message = Sha256.getSHA256(STR);
        System.out.println(message);


        String random = UUIDUtil.get32UUID().toUpperCase();
        System.out.println(random.toUpperCase());

        /**
         * key: token中的tt值
         * encodeTel:加密以后的手机号
         * */
        Map<String, String> map = YDFSign.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");
        /*  获取图形验证码 */
        Map<String, String> mapImageCode = YDFSign.getImageCode();
        String imageCode = mapImageCode.get("imageCode");
        String imageKey = mapImageCode.get("imageKey");

        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        /*  拼接参数type=01 注册 */
        String param = "app_down_platform=appstore&app_id=APP&appid=APP&i_version=1.0.0&imageCode=" + imageCode + "&imageKey=" + imageKey + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp + "&type=01";
        System.out.println("param" + param);
        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        System.out.println("getMsgCheckCode--------------加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        System.out.println("getMsgCheckCode--------------连接后待加密字符串：" + p);

        String enBody = null;
        try {
            System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("getMsgCheckCode----加密后p：" + enBody);
        } catch (Exception e) {
            e.getStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("random_str", timeStp);
        headers.add("app_id", "APP");
        headers.add("message", message);
        headers.add("appId", "APP");
        headers.add("channelCode", "111");


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("p", enBody);
        body.add("q", encodeTel);

        HttpEntity<String> request = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(Url, request, String.class);
        //System.out.println(response);
        String msgBodyCode = JSONObject.parseObject(response.getBody()).getString("body");
        System.out.println("getMsgCheckCode--------------未解密body" + msgBodyCode);

        String msg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println("getMsgCheckCode--------------响应code"+msg);

        String deBody = null;
        try {
            deBody = TDES_3DESUtil.decode(msgBodyCode, key);
            System.out.println("getMsgCheckCode----解密后body：" + deBody);
        } catch (Exception e) {
            e.getStackTrace();
        }

        String msgCheck_code = "000000";
        try {
            msgCheck_code = JSONObject.parseObject(deBody).getString("code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("getMsgCheckCode--------------解密后body中的短信验证码：" + msgCheck_code);

        Map<String, String> msgCheck_codeMapMap = new HashMap<>();
        msgCheck_codeMapMap.put("msgCheck_code", msgCheck_code);
        msgCheck_codeMapMap.put("key", key);
        msgCheck_codeMapMap.put("msg", msg);

        return msgCheck_codeMapMap;
    }

}
