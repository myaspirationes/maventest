package org.DB;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * @author zhaohongtu
 * @date 2021/4/28 2:42 PM
 */
public class PostUtils {
    public static String restTemplatePost(RestTemplate template, String url, Map<String, String> headerMap,
                                          MultiValueMap<String, String> paramMap) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", "application/json");
            for (String str : headerMap.keySet()) {
                headers.add(str, headerMap.get(str));
            }
            // 不好使的话就把paramMap转为JSONString body里面
            //paramMap:from请求
            String paramStr = JSON.toJSONString(paramMap);
            HttpEntity<String> requestParam = new HttpEntity("", headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramMap);
            return template.postForObject(new URI(builder.toUriString()), requestParam, String.class);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
