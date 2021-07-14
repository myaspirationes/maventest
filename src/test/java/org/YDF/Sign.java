package org.YDF;

import org.DB.MD5Util;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/19 10:41
 * 壹生付注册
 *
 *
 */
public class Sign {

    private static RestTemplate restTemplate = new RestTemplate();

    String Url="https://semp.qianziworth.cn/semp/";


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





}
