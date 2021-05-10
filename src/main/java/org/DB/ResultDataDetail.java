package org.DB;

import java.util.HashMap;

/**
 * resultData的封装
 *
 * @Author Administrator wangyongzhi
 * @Create 2017/11/23
 */
public class ResultDataDetail {

    public static HashMap<String,String> resultMap = new HashMap<String,String>();

    /**
     * 初始化返回码
     * 1000以内的属于 公共的错误码
     * 1000以上 前两位错误码代表的是业务模块 后两位错误码代表的是业务模块中的具体错误
     */
    public static void initResultMap(){
        resultMap.put("0","操作成功");
        resultMap.put("1","网络繁忙，请稍后重试！");
        resultMap.put("2","参数错误!");
        resultMap.put("3","不合法的请求！");
        resultMap.put("4","用户名或密码错误！");
        resultMap.put("5","上传文件失败，请重新上传！");
        resultMap.put("6","获取微信唯一码失败，请在微信中打开！");
        resultMap.put("7","您当前未登录，请先登录！");
        resultMap.put("8","权限不足");
        resultMap.put("9","请求签名不合法！");
        resultMap.put("10","您的时间与系统时间相差太大，请校验您的时间!");
        //11 已用
        resultMap.put("11","支付密码还未设置，请设置后提交");
        resultMap.put("12","您还未实名认证，请先去实名认证！");
        resultMap.put("13","老用户首次登录，请重置密码!");


        //合伙人机构相关
        resultMap.put("1101","绑定合伙人手机号未注册成为合伙人或者还未实名认证审核通过，或者该合伙人已经绑定了其他机构！");
    }


}
