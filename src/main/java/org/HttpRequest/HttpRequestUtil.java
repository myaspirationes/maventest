package org.HttpRequest;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/20 16:18
 */
public class HttpRequestUtil {

    private RestTemplate restTemplate = new RestTemplate();


    @Test
    public void doPost() {

        String url = "https://alpha-zeus.top-2.com/zeus/goods-center/goodsTag/listAll";
        /**
         * 发送Form请求体
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.add("token", "8afcedc5-ec9d-4830-927b-a9c7df5ac69c");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //Map<String, Object> body = new HashMap<>();
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("pageNumber", 1);
        body.add("pageSize", 2000);
        body.add("tagType", "GOODS");

        HttpEntity<String> request = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());

        /**
         * 发送Json格式的请求体
         */
        HttpHeaders header = new HttpHeaders();
        //header.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json;charset=UTF-8");
        header.add("token", "8afcedc5-ec9d-4830-927b-a9c7df5ac69c");
        header.add("Content-Type", "application/json;charset=UTF-8");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNumber", 1);
        jsonBody.put("pageSize", 2000);
        jsonBody.put("tagType", "GOODS");

        HttpEntity<JSONObject> jsonRequest = new HttpEntity<>(jsonBody, header);
        JSONObject jsonResponse = restTemplate.postForObject(url, jsonRequest, JSONObject.class);
        System.out.println(jsonResponse.get("msg"));
        System.out.println(jsonResponse);
        assert (jsonResponse.get("msg").equals("请求成功"));
        assert (jsonResponse.get("msg").equals("请求成功"));

    }




}
