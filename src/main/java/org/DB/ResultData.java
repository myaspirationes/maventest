package org.DB;






import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据 实体
 *
 * @Author Administrator wangyongzhi
 * @Create 2017/11/23
 */
public class ResultData<T extends Object> implements Serializable {

    public static Integer SUCCESS = 0;
    public static Integer FAIL = 1;
    private Integer code;
    private T data; 
    private String rtnInfo;
    private Map<String,Object> expandData = new HashMap<String,Object>();
    public static ResultData TableList(ResultData resultData, Object object) {
        ArrayList<Object> lists = new ArrayList<>();
        PageInfo<Object> objectPageInfo = new PageInfo<>(lists);
        resultData.setData(objectPageInfo);
        resultData.initCodeAndDesp();
        return resultData;
    }
    public ResultData() {
        this.code = FAIL;
        this.rtnInfo = "网络异常，请稍后重试！";
    };

    public ResultData(int code, String rtnInfo) {
        this.code = code;
        this.rtnInfo = rtnInfo;
    }


    public ResultData(int code, String rtnInfo, T data){
        this.code = code;
        this.rtnInfo = rtnInfo;
        this.data = data;
    }

    //成功直接返回
    public static ResultData ok() {
        return new ResultData(SUCCESS, "操作成功!");
    }

    //成功直接返回成功信息
    public static ResultData ok(String msg) {
        return new ResultData(SUCCESS, msg);
    }

    //成功直接返回成功信息和所带的数据
    public static ResultData ok(String msg, Object data) {
        return new ResultData(SUCCESS, msg, data);
    }

    public static ResultData ok(Object data) {
        return ResultData.ok("处理成功",data);
    }

    //失败直接返回 ResultData.fail()
    public static ResultData fail() {
        return new ResultData(ResultData.FAIL, "服务器异常!");
    }

    //失败直接返回 ResultData.fail(msg)
    public static ResultData fail(String msg) {
        return new ResultData(ResultData.FAIL, msg);
    }

    //失败直接返回 ResultData.fail(code,msg)
    public static ResultData fail(Integer code, String msg) {
        return new ResultData(code, msg);
    }

    //失败 根据code返回错误原因
    public static ResultData fail(Integer code){
        return new ResultData().initCodeAndDesp(code);
    }

    public ResultData initCodeAndDesp(int code){
        this.code = code;
        this.rtnInfo = ResultDataDetail.resultMap.get(String.valueOf(code));
        return this;
    }

    public ResultData initCodeAndDesp(int code, String rtnInfo){
        this.code = code;
        this.rtnInfo = rtnInfo;
        return this;
    }

    public ResultData initCodeAndDesp(){
        this.code = ResultData.SUCCESS;
        this.rtnInfo = "操作成功！";
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRtnInfo() {
        return rtnInfo;
    }

    public void setRtnInfo(String rtnInfo) {
        this.rtnInfo = rtnInfo;
    }

    public Map<String, Object> getExpandData() {
        return expandData;
    }

    public void setExpandData(Map<String, Object> expandData) {
        this.expandData = expandData;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", data=" + data +
                ", rtnInfo='" + rtnInfo + '\'' +
                ", expandData=" + expandData +
                '}';
    }
//@Override
    //public String toString() {
    //    return "ResultData{" +
    //            "code=" + code +
    //            ", data=" + data +
    //            ", rtnInfo='" + rtnInfo + '\'' +
    //            ", expandData=" + expandData +
    //            '}';
    //}
}
