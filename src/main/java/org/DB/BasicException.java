package org.DB;




/**
 * 基础异常
 *
 * @Author Administrator wangyongzhi
 * @Create 2017/12/20
 */
public class BasicException extends RuntimeException{
    /**  异常信息 */
    protected String msg;
    /** 具体异常码 */
    protected Integer code;
    /** 捕获的异常 */
    protected Exception e;
    /** 异常Id */
    protected String id;


    public BasicException() {
        super();
    }

    public BasicException(Integer code, Object... args){
        String msg = ResultDataDetail.resultMap.get(code.toString());
        this.code = code;
        this.msg = msg;
        newInstance(msg,args);
    }

    public BasicException(Integer code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public BasicException(String message, Throwable cause) {
        super(message, cause);
    }

    public BasicException(Throwable cause) {
        super(cause);
    }

    public BasicException(String message) {
        super(message);
        this.msg = message;
        this.code = ResultData.FAIL;
    }

    public BasicException(String id, String message, Exception e) {
        super(message);
        this.msg = message;
        this.code = ResultData.FAIL;
        this.id = id;
        this.e = e;
    }

    public BasicException(String id, String message) {
        super(message);
        this.msg = message;
        this.code = ResultData.FAIL;
        this.id = id;
    }


    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public Exception getE() {
        return e;
    }

    public String getId() {
        return id;
    }

    /**
     * 实例化异常
     *
     * @param msgFormat
     * @param args
     * @return
     */
    public BasicException newInstance(String msgFormat, Object... args) {
        return new BasicException(this.code, msgFormat, args);
    }
}
