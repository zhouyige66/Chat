package cn.roy.demo.model;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/12 13:25
 * @Version: v1.0
 */
public enum ChatServerStatus {
    INIT(0, "未连接"),
    CONNECTING(1, "连接中..."),
    CONNECTED(2, "已连接"),
    SERVER_ERROR(3, "服务无响应");
    private int code;
    private String des;

    ChatServerStatus(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
