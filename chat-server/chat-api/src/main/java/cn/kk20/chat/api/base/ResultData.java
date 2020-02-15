package cn.kk20.chat.api.base;

import cn.kk20.chat.api.base.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @Description: http请求返回实体
 * @Author: Roy Z
 * @Date: 2019-10-25 09:47
 * @Version: v1.0
 */
public class ResultData implements Serializable {
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
    @JsonIgnore
    private String exception;
    // 返回实体必须是BaseDto及其子类
    private BaseDto data;

    public static ResultData success() {
        return success(BaseDto.EMPTY);
    }

    public static ResultData success(BaseDto dto) {
        ResultData resultData = new ResultData().setCode(CODE_SUCCESS).setData(dto).setMsg("操作成功");
        return resultData;
    }

    public static ResultData requestError() {
        return requestError("请求参数不符");
    }

    public static ResultData requestError(String msg) {
        return fail(CODE_REQUEST_ERROR, msg);
    }

    public static ResultData serverError() {
        return serverError("服务器处理出错");
    }

    public static ResultData serverError(String msg) {
        return fail(CODE_SERVER_ERROR, msg);
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

    public BaseDto getData() {
        return data;
    }

    public ResultData setData(BaseDto data) {
        this.data = data;
        return this;
    }
}
