package cn.roy.demo.util.http;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019/2/13 10:29
 * @Version: v1.0
 */
public class HttpResponseException extends Exception {

    private int code;
    private String msg;

    public HttpResponseException(int code, String msg) {
        super(msg);

        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
