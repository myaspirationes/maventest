package org.FenRun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.DB.DbOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/9 16:06
 */
public class FenRunRequest {


    private static RestTemplate restTemplate = new RestTemplate();
    static String  LoginUrl="http://172.16.200.215:18010/partner-job/login";
    static String PageListUrl= "http://172.16.200.215:18010/partner-job/jobinfo/pageList";
    static String url="jdbc:mysql://172.16.200.150:3306/opartner?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    static String userName="root";
    static String passWord="CeShi#0301!";


    public String LoginCookie=null;



    /**
     * 登录获取Cookie，只执行一次
     *若果采用测试方法调用这个方法，则不必注解为@BeforeClass，但要声明为static
     * @return 登录Cookie
     */
    @BeforeClass
    public  void  login(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        //HashMap<String,String> body =new HashMap();
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("userName","userbai");
        body.add("password",123456);

        HttpEntity<String> request = new HttpEntity(body,headers);

        ResponseEntity<String> response= restTemplate.postForEntity(LoginUrl,request,String.class);
        String loginCookie= response.getHeaders().get("Set-Cookie").get(0);//获取响应头中的cookie
        System.out.println("获取的cookie 是 ："+loginCookie);
        LoginCookie = loginCookie;
    }

    /**
     * 先清空两张表 opartner.income 和 opartner.wpincome
     *只执行一次
     */
    @BeforeClass
    public static void deleteData(){
        String Income_sql = "delete  from opartner.income ";
        String Wpincome_sql = "delete  from opartner.wpincome ";

        DbOperation.MysqlDelete(url,userName,passWord, Income_sql);
        DbOperation.MysqlDelete(url,userName,passWord,Wpincome_sql);
    }





    @Test
    public  void  pagelistTest(){

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        //headers.add("Cookie",FenRunRequest.login());
        headers.add("Cookie",LoginCookie);
        //HashMap<String,String> body =new HashMap();
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("jobGroup",4);
        body.add("triggerStatus",-1);
        body.add("jobDesc",null);
        body.add("executorHandler",null);
        body.add("start",0);
        body.add("length",10);

        HttpEntity<String> request = new HttpEntity(body,headers);

        ResponseEntity<String> response= restTemplate.postForEntity(PageListUrl,request,String.class);
        //String Cookie= response.getHeaders().get("Set-Cookie").get(0);
        System.out.println(response);

    }


    @Test
    public  void  pagelistTest50(){

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Cookie",LoginCookie);
        //HashMap<String,String> body =new HashMap();
        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("jobGroup",4);
        body.add("triggerStatus",-1);
        body.add("jobDesc",null);
        body.add("executorHandler",null);
        body.add("start",0);
        body.add("length",50);

        HttpEntity<String> request = new HttpEntity(body,headers);

        ResponseEntity<String> response= restTemplate.postForEntity(PageListUrl,request,String.class);
        //String Cookie= response.getHeaders().get("Set-Cookie").get(0);
        System.out.println(response);

        JSONObject jsonResponse = restTemplate.postForObject(PageListUrl, request, JSONObject.class);
        System.out.println(jsonResponse);

        System.out.println(jsonResponse.getJSONArray("data").getJSONObject(0).get("id") );


        Assert.assertEquals(44,jsonResponse.getJSONArray("data").getJSONObject(0).get("id"));
        Assert.assertEquals(200,response.getStatusCode().value());

    }





    /**
     * 分润基础数据统计
     * 参数：id 34
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun1step(){

        String url= "http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Type","");
        headers.add("Cookie",LoginCookie);

        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("id","34");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request= new HttpEntity(body,headers);

        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println("第一步的响应是：" + response);

    }

    /**
     * 分润定档
     * 参数：id 35
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun2step(){
        String url="http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers= new HttpHeaders();
        headers.add("Cookie",LoginCookie);
        headers.add("Content-Type","");

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("id","35");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request = new HttpEntity(body,headers);
        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println(response);
    }

    /**
     * 分润计算
     * 参数：id 36
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun3step(){
        String url="http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers= new HttpHeaders();
        headers.add("Cookie",LoginCookie);
        headers.add("Content-Type","");

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("id","36");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request = new HttpEntity(body,headers);
        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println(response);
    }


    /**
     * 旺铺分润基础数据（易多付之后）
     * 参数：id 38
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun4step(){
        String url="http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers= new HttpHeaders();
        headers.add("Cookie",LoginCookie);
        headers.add("Content-Type","");

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("id","38");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request = new HttpEntity(body,headers);
        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println(response);
    }



    /**
     * 旺铺分润定档（易多付之后）
     * 参数：id 39
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun5step(){
        String url="http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers= new HttpHeaders();
        headers.add("Cookie",LoginCookie);
        headers.add("Content-Type","");

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("id","39");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request = new HttpEntity(body,headers);
        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println(response);
    }


    /**
     * 旺铺分润计算
     * 参数：id 40
     * 参数：executorParam  月份
     * 参数：addresslist 可以为空
     *
     */
    @Test
    public void FenRun6step(){
        String url="http://172.16.200.215:18010/partner-job/jobinfo/trigger";

        HttpHeaders headers= new HttpHeaders();
        headers.add("Cookie",LoginCookie);
        headers.add("Content-Type","");

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("id","40");
        body.add("executorParam",2020-12);
        body.add("addressList",null);

        HttpEntity<String> request = new HttpEntity(body,headers);
        ResponseEntity<String> response= restTemplate.postForEntity(url,request,String.class);
        System.out.println(response);
    }

    @Test
    public void time(){
        Date now= new Date();

        SimpleDateFormat ft=new SimpleDateFormat();
        String time= ft.format(now);
        System.out.printf(time);
        System.out.println();
        System.out.printf("%tb%n",now);
        System.out.println(now);

        long start = System.currentTimeMillis();
        System.out.println(start);


    }


    /**
     * 手动添加对象到一个JSONObject
     */
    @Test
    private  void writeStrToJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","tom");
        jsonObject.put("age",20);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonArrayObject1 = new JSONObject();
        jsonArrayObject1.put("name","alibaba");
        jsonArrayObject1.put("info","www.alibaba.com");
        JSONObject jsonArrayObject2 = new JSONObject();
        jsonArrayObject2.put("name","baidu");
        jsonArrayObject2.put("info","www.baidu.com");

        jsonArray.add(jsonArrayObject1);
        jsonArray.add(jsonArrayObject2);

        jsonObject.put("sites",jsonArray);

        System.out.println(jsonObject);
    }

}
