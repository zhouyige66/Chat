package cn.kk20.chat.model;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019-02-12 17:48
 * @Version: v1.0
 */
public class RestModel<T> {
    private int code;
    private boolean success;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> RestModel success(T data) {
        RestModel restModel = new RestModel();
        restModel.code = 200;
        restModel.success = true;
        restModel.data = data;
        return restModel;
    }

    public static RestModel fail(int code, String msg) {
        RestModel restModel = new RestModel();
        restModel.code = code;
        restModel.success = false;
        restModel.msg = msg;

        return restModel;
    }

}
