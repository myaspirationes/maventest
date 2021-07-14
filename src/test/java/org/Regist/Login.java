package org.Regist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.DB.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.DB.RSAUtils.getsec;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/2/5 11:24
 */


public class Login {
    private static RestTemplate restTemplate = new RestTemplate();
    private static String URL = "http://ysf.worthinfo.cn/oemp/app/1.0/";

    /**
     * 连接数据库
     */
    DbOperation db = new DbOperation();
    private static String spos_url = "jdbc:mysql://172.16.200.87:3306/spos";
    private static String nemp_url = "jdbc:mysql://172.16.200.87:3306/nemp";
    private static String nemp_saas_url = "jdbc:mysql://172.16.200.87:3306/nemp-saas";
    private static String mysql_userName = "root";
    private static String mysql_passWord = "CeShi#0301!";


    /**
     * 未注册nemp-saas
     * 或在其他系统未激活，
     * 或白名单的手机号注册，注册成功
     * 1.获取图形验证码
     * 2.获取token
     * 3.获取验证码
     * 4.注册接口
     */
    @Test
    public void newUserRegistTest() {
        String registURL = URL + "register.do";

        /*  待注册的手机号 是否已注册 */
        String tel = "13866667777";

        String sql = "select count(*) as count,\n" +
                "\tcast(AES_DECRYPT(UNHEX(code),'1qaz2WSX3edc')as char)code, regist_addr from `nemp-saas`.customer \n" +
                "\twhere code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        String telphone=  String.valueOf(selectResult.get(0).get("code"));


        if (count_ysf>0&telphone!=null){
            System.out.println("手机号在系统中,删除这个测试手机号！！！");
           // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{
            System.out.println("手机号不在系统中,Go ON！！！");
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);
    }

    /**
     * 已注册壹生付（nemp-saas）手机号注册，不能注册
     * 或在其他系统中已经激活的手机号注册，不能注册
     * 1.在获取短信验证码时即被拦截，所以请求的不是注册regist.do接口，而是get_newMessageCheckCode.do接口
     */
    @Test
    public void existUserRegistTest() {

        String Url = "http://ysf.worthinfo.cn/oemp/app/1.0/get_newMessageCheckCode.do";

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /*  手机号 */
        String tel = "16621181425";


        String sql = "select count(*) as count,\n" +
                "\tcast(AES_DECRYPT(UNHEX(code),'1qaz2WSX3edc')as char)code, regist_addr from `nemp-saas`.customer \n" +
                "\twhere code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        //String insertTel="insert into `nemp-saas`.customer () values();";
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);


        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        String telphone=  String.valueOf(selectResult.get(0).get("code"));


        if (count_ysf>0&telphone!=null){
            System.out.println("手机号在系统中,Go ON！！！");
        }else{
            System.out.println("手机号不在系统中,请检查！！！");
            //db.MysqlInsert(nemp_saas_url, mysql_userName, mysql_passWord, insertTel);
        }

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
        Map<String, String> map = Login.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");
        /*  获取图形验证码 */
        Map<String, String> mapImageCode = Login.getImageCode();
        String imageCode = mapImageCode.get("imageCode");
        String imageKey = mapImageCode.get("imageKey");

        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        /*  拼接参数type=01 注册 */
        String param = "app_down_platform=appstore&app_id=APP&appid=APP&i_version=1.0.0&imageCode=" + imageCode + "&imageKey=" + imageKey + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp + "&type=01";
        System.out.println("param" + param);
        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        System.out.println("getMsgCheckCode----加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        System.out.println("连接后待加密字符串：" + p);

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
        System.out.println(response);

        System.out.println(response.getStatusCode());
        String msg = JSONObject.parseObject(response.getBody()).getString("msg");
        System.out.println("getMsg----" + msg);

        Assert.assertEquals("该手机号已注册", msg);
        Assert.assertEquals("200 OK", response.getStatusCode().toString());


    }


    /**
     * 在spos系统中已经激活的手机号&不在白名单，不能注册
     */
    @Test
    public void sposActiveUserRegistTest() {

        String registURL = URL + "register.do";
        /*  手机号 */
        String tel = "15656542530";


        /**
         * 判断手机号在spos中是否存在，是否是jihuo用户 ，TIMESTAMPDIFF(DAY,spos.customer.lastTransTime,NOW())<=90 表示是90天内有交易是 激活状态,不可以注册。
         */
        String sql = "select\n" +
                "count(*) as count,\n" +
                "\tcast(AES_DECRYPT(UNHEX(code),'1qaz2WSX3edc')as char ) code ,\n" +
                "\tlastTransTime\n" +
                "from spos.customer\n" +
                "where\n" +
                "\t(TIMESTAMPDIFF(DAY,spos.customer.lastTransTime,NOW())<=90)\n" +
                "\tand code = hex(AES_ENCRYPT(\'" + tel + "', '1qaz2WSX3edc'))\n" +
                "\tand enable_flag = '100030001';";

        List<Map<String, Object>> selectResult = db.MySqlSelect(spos_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_spos= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        String telphone=  String.valueOf(selectResult.get(0).get("code"));
        //System.out.println(count_spos);

        if (count_spos>0&telphone!=null){
            System.out.println("手机号在系统中，并且是激活用户，继续执行！！！");

        }else{
            System.out.println("手机号不在系统中，请检查！！！");

            Assert.assertEquals("null", telphone);
            Assert.assertEquals("0", count_spos);
              return;
        }

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        //String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        //System.out.println(STR);
        String message = Sha256.getSHA256(timeStamp + "1qaz2wsx1qaz2wsx");
        //System.out.println(message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;
        System.out.println("param：" + param);

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        System.out.println("register.do-------------" + response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        System.out.println("register.do-------------" + loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("手机号已注册", loginSuccessMsg);
        Assert.assertEquals("83", loginSuccessRet);
    }


    /**
     * 18712149007 在nemp系统中已经激活的手机号&不在白名单，不能注册
     */
    @Test
    public void nempActiveUserRegistTest(){

        String registURL = URL + "register.do";
        /*  手机号 */
        String tel = "18712149007";

        /**
         * 判断手机号在spos中是否存在，是否是jihuo用户 ，TIMESTAMPDIFF(DAY,spos.customer.lastTransTime,NOW())<=90 表示是90天内有交易是 激活状态,不可以注册。
         */
        String sql = "select\n" +
                "count(*) as count,\n" +
                "\tcast(AES_DECRYPT(UNHEX(code),'1qaz2WSX3edc')as char ) code \n"+
                "from nemp.customer\n" +
                "where\n" +
                "\tcode = hex(AES_ENCRYPT(\'" + tel + "', '1qaz2WSX3edc'))";

        List<Map<String, Object>> selectResult = db.MySqlSelect(spos_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_spos= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        String telphone=  String.valueOf(selectResult.get(0).get("code"));
        //System.out.println(count_spos);

        if (count_spos>0&telphone!=null){
            System.out.println("手机号在系统中，并且是激活用户，继续执行！！！");

        }else{
            System.out.println("手机号不在系统中，请检查！！！");

            Assert.assertEquals("null", telphone);
            Assert.assertEquals("0", count_spos);
            return;
        }

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        //String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        //System.out.println(STR);
        String message = Sha256.getSHA256(timeStamp + "1qaz2wsx1qaz2wsx");
        //System.out.println(message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;
        System.out.println("param：" + param);

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        System.out.println("register.do-------------" + response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        System.out.println("register.do-------------" + loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("手机号已注册", loginSuccessMsg);
        Assert.assertEquals("83", loginSuccessRet);


    }




    /**
     * 在dly系统中已经激活的手机号&不在白名单，不能注册
     */
    @Test
    public void dlyActiveUserRegistTest(){}




    /**
     * 16621181425 在spos系统中沉默用户，能注册
     */
    @Test
    public void sposSlienceUserRegistTest(){

        String registURL = URL + "register.do";

        /*  待注册的手机号 是否已注册 */
        String tel = "13866667777";

        String sql = "select count(*) as count,\n" +
                "\tnegativeFlag ,cast(AES_DECRYPT(UNHEX(code),'1qaz2WSX3edc')as char)code, regist_addr from `spos`.customer \n" +
                "\twhere code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        //String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        int negativeFlag=  Integer.valueOf(selectResult.get(0).get("negativeFlag").toString()).intValue();


        if (count_ysf > 0 & negativeFlag ==0){
            System.out.println("手机号spos系统中,是沉默用户！！！");
            // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{
            System.out.println("手机号不在系统中,Go ON！！！");
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);





    }


    /**
     * 在nemp系统中沉默用户，能注册
     * 13020282661
     */
    @Test
    public void nempSlienceUserRegistTest(){

        String registURL = URL + "register.do";

        /*  待注册的手机号 是否已注册 */
        String tel = "13020282661";

        String sql = "SELECT\n" +
                " count(*) as count,is_silent_customer,silent_status,\n" +
                " CAST( AES_DECRYPT( UNHEX( man_name ), '1qaz2WSX3edc' ) AS CHAR ) man_name,\n" +
                " CAST( AES_DECRYPT( UNHEX( CODE ), '1qaz2WSX3edc' ) AS CHAR ) CODE,\n" +
                " counter_fee \n" +
                "from `nemp`.customer where  CODE=HEX(AES_ENCRYPT("+tel+",'1qaz2WSX3edc'));";
        //String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        int silent_status=  Integer.valueOf(selectResult.get(0).get("silent_status").toString()).intValue();


        if (count_ysf > 0 & silent_status ==1){
            System.out.println("手机号nemp系统中,是沉默用户！！！");
            // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{
            System.out.println("手机号不在系统中or 非沉默用户,请检查！！！");
            return;
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);












    }


    /**
     * 在ysf系统中沉默用户，能注册
     */
    @Test
    public void ysfSlienceUserRegistTest(){


        String registURL = URL + "register.do";

        /*  待注册的手机号 是否已注册 */
        String tel = "13922223636";

        String sql = "SELECT\n" +
                " count(*) as count,create_time,is_silent_customer,silent_type,silent_status,\n" +
                " CAST( AES_DECRYPT( UNHEX( man_name ), '1qaz2WSX3edc' ) AS CHAR ) man_name,\n" +
                " CAST( AES_DECRYPT( UNHEX( CODE ), '1qaz2WSX3edc' ) AS CHAR ) CODE,\n" +
                " counter_fee\n" +
                "FROM\n" +
                " `nemp-saas`.customer"+
                "\twhere code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";;
        //String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        int silent_status=  Integer.valueOf(selectResult.get(0).get("silent_status").toString()).intValue();


        if (count_ysf > 0 & silent_status ==0){
            System.out.println("手机号ysf系统中,是沉默用户！！！");
            // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{
            System.out.println("手机号不在系统中,Go ON！！！");
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);










    }





    /**
     * 在dly系统中沉默用户，能注册
     */
    @Test
    public void dlySlienceUserRegistTest(){


        String registURL = URL + "register.do";

        /*  待注册的手机号  */
        String tel = "15921401883";

        String sql = "SELECT \n" +
                " count(*) as count,negativeFlag,\n" +
                " CAST( AES_DECRYPT( UNHEX( username ), '1qaz2WSX3edc' ) AS CHAR ) man_name,\n" +
                " CAST( AES_DECRYPT( UNHEX( code ), '1qaz2WSX3edc' ) AS CHAR ) CODE\n" +
                " from `handbrushposp_dly`.customer  where code = HEX(AES_ENCRYPT("+tel+",'1qaz2WSX3edc'));";
        //String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        int silent_status=  Integer.valueOf(selectResult.get(0).get("negativeFlag").toString()).intValue();


        if (count_ysf > 0 &silent_status==1){
            System.out.println("手机号dly系统中,是沉默用户！！！");
            // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{
            System.out.println("手机号不在系统中or 非沉默用户,请检查！！！");
            return;
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);





    }


    /**
     * 在spos系统中是激活用户，在nemp-saas中是白名单用户，能注册
     */
    @Test
    public void spos_White_UserRegistTest(){




        String registURL = URL + "register.do";

        /*  待注册的手机号 是否已注册 */
        String tel = "17786869898";
        /*  查询是否白名单中  */
        String sql = "SELECT\n" +
                " count(*) as count,  channel_code,create_time,update_time,\n" +
                "CAST( AES_DECRYPT( UNHEX( man_id_card ), '1qaz2WSX3edc' ) AS CHAR ) ID,\n" +
                "CAST( AES_DECRYPT( UNHEX( man_name ), '1qaz2WSX3edc' ) AS CHAR ) name,\n" +
                "CAST( AES_DECRYPT( UNHEX( man_tel ), '1qaz2WSX3edc' ) AS CHAR ) tel\n" +
                "FROM\n" +
                "`nemp-saas`.customer_white_list where man_tel = HEX(AES_ENCRYPT("+tel+",'1qaz2WSX3edc'));\n";;
        //String deleteTel="delete from  `nemp-saas`.customer where code = hex(AES_ENCRYPT('" + tel + "', '1qaz2WSX3edc'));";
        System.out.println(sql);
        List<Map<String, Object>> selectResult = db.MySqlSelect(nemp_saas_url, mysql_userName, mysql_passWord, sql);
        //System.out.println(selectResult.get(0).get("count"));
        //System.out.println(selectResult.get(0).get("code"));

        int count_ysf= Integer.valueOf(selectResult.get(0).get("count").toString()).intValue();
        //int silent_status=  Integer.valueOf(selectResult.get(0).get("negativeFlag").toString()).intValue();
        //String telephone=  selectResult.get(0).get("tel").toString();

        if (count_ysf > 0 ){
            System.out.println("手机号在壹生付系统中,是白名单用户！！！");
            // db.MysqlDelete(nemp_saas_url, mysql_userName, mysql_passWord, deleteTel);

        }else{

            System.out.println("手机号在壹生付系统中,不是白名单用户,请检查！！！");
            //System.out.println("手机号不在系统中or 非沉默用户,请检查！！！");
            return;
        }


        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        String pwd = "2020qwas";
        String password = EncryptUtil.sha1Encrypt(pwd);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();

        String encodeTel = null;
        try {
            encodeTel = RSAUtils.getsec(tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  手机号加密为参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        Map<String, String> msgCodeMap = Login.getMsgCheckCode(tel);
        String msgCode = msgCodeMap.get("msgCheck_code");
        String key = msgCodeMap.get("key");
        //String msg=msgCodeMap.get("msg");

        String param = "app_down_platform=appstore&app_id=APP&appid=APP&check_code=" + msgCode + "&i_version=1.0.0&oen_id=2&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;

        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            System.out.println("加密后p：" + enBody);
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
        ResponseEntity<String> response = restTemplate.postForEntity(registURL, request, String.class);
        //System.out.println(response.getBody());
        String loginSuccessMsg = JSONObject.parseObject(response.getBody()).getString("msg");
        //System.out.println(loginSuccessMsg);
        String loginSuccessRet = JSONObject.parseObject(response.getBody()).getString("ret");

        Assert.assertEquals("断言1：", "success", loginSuccessMsg);
        Assert.assertEquals("断言2：", "00", loginSuccessRet);









    }


    /**
     * 实名认证用户登录
     */
    @Test
    public void CertificationUserloginTest() {
        String loginUrl = URL + "login.do";

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /*  登录的手机号 */
        String tel = "13817391487";
        String pwd = "123456xy";
        String password = EncryptUtil.sha1Encrypt(pwd);
        //System.out.println("password:" + password);
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        //System.out.println("时间戳" + STR);
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();
        /**
         * key: token中的tt值
         * encodeTel:加密以后的手机号
         * */
        Map<String, String> map = Login.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");

        /*  手机号加密位参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        /*  拼接参数 */
        String param = "app_down_platform=appstore&app_id=APP&appid=APP&i_version=1.0.0&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;
        //System.out.println("param：" + param);
        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            //System.out.println("加密后p：" + enBody);
        } catch (Exception e) {
            e.getStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("random_str", timeStp);
        headers.add("app_id", "APP");
        headers.add("message", message);
        headers.add("appId", "APP");
        headers.add("channelCode", "110");


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("p", enBody);
        body.add("q", encodeTel);

        HttpEntity<String> request = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);
        System.out.println(response);
        String loginSuccessBody = JSONObject.parseObject(response.getBody()).getString("body");
        System.out.println(loginSuccessBody);

        String loginName = null;
        String loginMsg = null;
        try {
            String deResponse = TDES_3DESUtil.decode(loginSuccessBody, key);
            System.out.println(deResponse);
            loginName = JSON.parseObject(deResponse).getJSONObject("user").get("manName").toString();
            loginMsg = JSON.parseObject(deResponse).get("msg").toString();
            System.out.println(loginName);
            System.out.println(loginMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(loginName, "汪小明");
        Assert.assertEquals(loginMsg, "已经实名认证");
    }


    /**
     * 非实名认证用户登录
     */
    @Test
    public void unCertificationUserloginTest(){

        String loginUrl = URL + "login.do";

        Long timeStamp = System.currentTimeMillis();
        String timeStp = timeStamp + "";
        /*  登录的手机号 */
        String tel = "13817391487";
        String pwd = "123456xy";
        String password = EncryptUtil.sha1Encrypt(pwd);
        //System.out.println("password:" + password);
        String STR = timeStamp + "1qaz2wsx1qaz2wsx";
        //System.out.println("时间戳" + STR);
        String message = Sha256.getSHA256(STR);
        //System.out.println("message" + message);

        /*  32位随机数 */
        String random = UUIDUtil.get32UUID().toUpperCase();
        /**
         * key: token中的tt值
         * encodeTel:加密以后的手机号
         * */
        Map<String, String> map = Login.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");
        //System.out.println("shoujihao ---------------" + encodeTel);
        /*  手机号加密位参数sign*/
        String sign = EncryptUtil.md5Encrypt(tel + "handbrush");

        /*  拼接参数 */
        String param = "app_down_platform=appstore&app_id=APP&appid=APP&i_version=1.0.0&password=" + password + "&platform_id=ios&random_str=" + random + "&sign=" + sign + "&tel=" + tel + "&timestamp=" + timeStp;
        //System.out.println("param：" + param);
        String sign_Hash = Sha256.getSHA256(param).toUpperCase();
        //System.out.println("加密后signHash：" + sign_Hash);
        String p = param + "&sign_hash=" + sign_Hash;
        //System.out.println("连接后待加密字符串p：" + p);

        /*  拼接参数后再加密 key是token接口返回的tt */
        String enBody = null;
        try {
            //System.out.println("加密前p：" + p);
            enBody = TDES_3DESUtil.encode(p, key);
            //System.out.println("加密后p：" + enBody);
        } catch (Exception e) {
            e.getStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("random_str", timeStp);
        headers.add("app_id", "APP");
        headers.add("message", message);
        headers.add("appId", "APP");
        headers.add("channelCode", "110");


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("p", enBody);
        body.add("q", encodeTel);

        HttpEntity<String> request = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);
        //System.out.println(response);
        String loginSuccessBody = JSONObject.parseObject(response.getBody()).getString("body");
        //System.out.println(loginSuccessBody);

        String loginName = null;
        String loginMsg = null;
        try {
            String deResponse = TDES_3DESUtil.decode(loginSuccessBody, key);
            System.out.println(deResponse);
            loginName = JSON.parseObject(deResponse).getJSONObject("user").get("manName").toString();
            loginMsg = JSON.parseObject(deResponse).get("msg").toString();
//            System.out.println(loginName);
//            System.out.println(loginMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(loginName, "汪小明");
        Assert.assertEquals(loginMsg, "已经实名认证");

    }


    /**
     * 获取token响应中的 tt
     *
     * @return tt, 加密的手机号
     */
    private static Map<String, String> token(String tel) {
        String tokenUrl = "http://ysf.worthinfo.cn/oemp/app/1.0/token.do";
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
        headers.add("appId", "APP");
        headers.add("channelCode", "111");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("i_version", "1.0.0");


        /**
         * 请求体，form 格式
         */
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tel", encodeTel);
        body.add("token_client", token_client);
        body.add("app_id", "APP");
        //body.add();

        /**
         * 发送请求并返回tt
         */
        HttpEntity<String> jsonRequest = new HttpEntity(body, headers);
        ResponseEntity<String> Response = restTemplate.postForEntity(tokenUrl, jsonRequest, String.class);
        //System.out.println("token响应体：" + Response.getHeaders().get("Set-Cookie"));
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
        String tokenUrl = "http://ysf.worthinfo.cn/oemp/app/1.0/get_newImageCode.do";
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
        headers.add("appId", "APP");
        headers.add("channelCode", "111");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("i_version", "1.0.0");

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
        String Url = "http://ysf.worthinfo.cn/oemp/app/1.0/get_newMessageCheckCode.do";

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
        Map<String, String> map = Login.token(tel);
        String key = map.get("tt");
        String encodeTel = map.get("encodeTel");
        /*  获取图形验证码 */
        Map<String, String> mapImageCode = Login.getImageCode();
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
