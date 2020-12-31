package cn.roy.chat.enity;

import java.io.Serializable;

/**
 * @Description: http请求返回实体
 * @Author: Roy
 * @Date: 2019-10-25 09:47
 * @Version: v1.0
 */
public class ResultData<T> implements Serializable {
    // 操作成功
    public static int CODE_SUCCESS = 200;
    // 请求错误
    public static int CODE_REQUEST_ERROR = 400;
    // 服务器内部错误
    public static int CODE_SERVER_ERROR = 500;
    // 查询数据为空
    public static int CODE_DATA_NULL = 600;

    private int code;
    private String msg;
    // 该属性用于开发测试阶段，用于定位问题
    private String exception;
    // 返回实体必须是BaseDto及其子类
    private T data;

    public int getCode() {
        return code;
    }

    public ResultData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ResultData setException(String exception) {
        this.exception = exception;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData setData(T data) {
        this.data = data;
        return this;
    }

}
