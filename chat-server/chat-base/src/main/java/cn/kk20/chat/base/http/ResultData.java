package cn.kk20.chat.base.http;

import java.io.Serializable;

/**
 * @Description: http请求返回实体
 * @Author: Roy Z
 * @Date: 2019-10-25 09:47
 * @Version: v1.0
 */
public class ResultData implements Serializable {

    public enum ResultCode {
        SUCCESS(200, "操作成功"),
        REDIRECT(300, "重定向"),
        REQUEST_ERROR(400, "错误请求"),
        CERTIFICATION_FAIL(401, "身份验证未通过"),
        SERVER_ERROR(500, "服务器内部出错"),
        CUSTOM_ERROR(600, "自定义错误信息");

        private int code;
        private String des;

        ResultCode(int code, String des) {
            this.code = code;
            this.des = des;
        }
    }

    private int code;
    private String msg;
    // 该属性用于开发测试阶段，用于定位问题
    private String exception;
    // 返回实体必须是BaseDto及其子类
    private Serializable data;

    public static ResultData success(String msg) {
        return createResultData(ResultCode.SUCCESS, msg);
    }

    public static ResultData success(Serializable data) {
        ResultData resultData = new ResultData().setCode(ResultCode.SUCCESS.code).setMsg("操作成功")
                .setData(data);
        return resultData;
    }

    public static ResultData requestError() {
        return createResultData(ResultCode.REQUEST_ERROR, null);
    }

    public static ResultData requestError(String msg) {
        return createResultData(ResultCode.REQUEST_ERROR, msg);
    }

    public static ResultData serverError() {
        return createResultData(ResultCode.SERVER_ERROR, null);
    }

    public static ResultData serverError(String msg) {
        return createResultData(ResultCode.SERVER_ERROR, msg);
    }

    public static ResultData createResultData(ResultCode resultCode, String msg) {
        ResultData resultData = new ResultData();
        resultData.setCode(resultCode.code);
        resultData.setMsg(msg == null ? resultCode.des : msg);
        return resultData;
    }

    public static ResultData fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static ResultData fail(int code, String msg, String exception) {
        ResultData resultData = new ResultData().setCode(code).setMsg(msg).setException(exception);
        return resultData;
    }

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

    public Serializable getData() {
        return data;
    }

    public ResultData setData(Serializable data) {
        this.data = data;
        return this;
    }
}
