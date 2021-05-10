package org.DB;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/1/22 9:59
 */
public class HttpRequest {
    private RestTemplate restTemplate = new RestTemplate();


    /**
     * 发送Form请求
     */
    @Test
    public void doPostWithForm() {

        String url = "http://esop.qianziworth.cn/spay-web/page/iempCoreTrans/list.do";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //headers.set("Accept", "application/json;charset=UTF-8");
        headers.add("cookie", "__snaker__id=ui630Gz1SAdqv8Bl; JSESSIONID=967D7F27DFF98D1EC9F2F8BFB8CA3FE7; gdxidpyhxdE=xlNw%2Bd0r47VR8WODMTRTJ4YTCGG4%2F60Dz5IA%5CdXhDBfrBLg2YBr9EW%2B6LWW9YZSKa90Dn4eilTlmeA2jcWVACQkx%5CRmLkxucCOVZnpGctWiVv8KicIoz%2FsjNXsLaxMzPpvLyQ29n3GXe8Y74%5CD%2Bo5hBqAx%2Fbqvpgyy6J0wGRcrrqB%2Fr2%3A1611735610251; _9755xjdesxxd_=32; YD00938875056089%3AWM_NI=tw%2BTdaFNblUY8TtKI3%2BtSk3etdT2wWceRAEfiiXEMv1UeV83j1cwCLEg6OB%2Bc6Du0EW7NodaVH%2FdkqswSR9eoGCk6n1QOJcJpmHQfrK8uruXwnjg8IhbKju10E3ygWBWT2I%3D; YD00938875056089%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee8ded5b9b90bcafbb6fb89e8aa6d85b828b8e85ae41bb8f96daaa25f5b9ff84d22af0fea7c3b92ab8a9e5b5e76b8692ae8dd853fc8cbcaccc39e9ac9d8cb26fb8bc9cd3f65da1bb9ea2c86eabb798a6d07ef38babb7bb39aaefad98c24887b9fab0c121a1ea8c96b34f95e98290e76b9698e185ea7bb1a99db3bc6fad998d8cb566f38f81abe242b4b388b2e57a89e8bb93f854b5b2a688bb43acbe858bf83bf49bfd9ae65d82899cd3cc37e2a3; YD00938875056089%3AWM_TID=77TIXcr9iuxEURERVVZ%2Bb2QY8vuq01dS");
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //Map<String, Object> body = new HashMap<>();
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("start", 0);
        body.add("length", 10);
        body.add("trade_time_startDate", "2021-01-27");
        body.add("trade_time_endDate", "2021-01-27");

        HttpEntity<String> request = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());



    }

    /**
     * 发送Json格式的请求
     */
    @Test
    public  void DoPostWithJson(){

        String url = "http://esop.qianziworth.cn/spay-web/page/iempCoreTrans/list.do";
        HttpHeaders headers = new HttpHeaders();
        //header.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.add("cookie", "__snaker__id=ui630Gz1SAdqv8Bl; JSESSIONID=967D7F27DFF98D1EC9F2F8BFB8CA3FE7; gdxidpyhxdE=xlNw%2Bd0r47VR8WODMTRTJ4YTCGG4%2F60Dz5IA%5CdXhDBfrBLg2YBr9EW%2B6LWW9YZSKa90Dn4eilTlmeA2jcWVACQkx%5CRmLkxucCOVZnpGctWiVv8KicIoz%2FsjNXsLaxMzPpvLyQ29n3GXe8Y74%5CD%2Bo5hBqAx%2Fbqvpgyy6J0wGRcrrqB%2Fr2%3A1611735610251; _9755xjdesxxd_=32; YD00938875056089%3AWM_NI=tw%2BTdaFNblUY8TtKI3%2BtSk3etdT2wWceRAEfiiXEMv1UeV83j1cwCLEg6OB%2Bc6Du0EW7NodaVH%2FdkqswSR9eoGCk6n1QOJcJpmHQfrK8uruXwnjg8IhbKju10E3ygWBWT2I%3D; YD00938875056089%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee8ded5b9b90bcafbb6fb89e8aa6d85b828b8e85ae41bb8f96daaa25f5b9ff84d22af0fea7c3b92ab8a9e5b5e76b8692ae8dd853fc8cbcaccc39e9ac9d8cb26fb8bc9cd3f65da1bb9ea2c86eabb798a6d07ef38babb7bb39aaefad98c24887b9fab0c121a1ea8c96b34f95e98290e76b9698e185ea7bb1a99db3bc6fad998d8cb566f38f81abe242b4b388b2e57a89e8bb93f854b5b2a688bb43acbe858bf83bf49bfd9ae65d82899cd3cc37e2a3; YD00938875056089%3AWM_TID=77TIXcr9iuxEURERVVZ%2Bb2QY8vuq01dS");
        headers.add("Content-Type", "application/json; charset=UTF-8");

        JSONObject jsonBody = new JSONObject();

        jsonBody.put("start", 0);
        jsonBody.put("length", 10);
        jsonBody.put("trade_time_startDate", "2021-01-27");
        jsonBody.put("trade_time_endDate", "2021-01-27");

        HttpEntity<JSONObject> jsonRequest = new HttpEntity<>(jsonBody, headers);
        JSONObject jsonResponse = restTemplate.postForObject(url, jsonRequest, JSONObject.class);
        System.out.println(jsonResponse.get("msg"));
        System.out.println(jsonResponse);
        assert (jsonResponse.get("msg").equals("请求成功"));
        assert (jsonResponse.get("msg").equals("请求成功"));

    }

    @Test
    public void DoGet(){

        String url = "http://esop.qianziworth.cn/spay-web/page/page.do?param={param}&uuid={uuid}";

        Map<String,Object> map = new HashMap<>();
        map.put("param","/whiteList/iempCostFbWhite");
        map.put("uuid","0.29341014879683525");

        ResponseEntity<String> Response = restTemplate.getForEntity(url, String.class,map);
//        ResponseEntity<String> Response = restTemplate.getForEntity(url, String.class);//不带参数
        System.out.println(Response.getBody());
    }



}
