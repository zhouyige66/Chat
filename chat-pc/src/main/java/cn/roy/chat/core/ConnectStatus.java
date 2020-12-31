package cn.roy.chat.core;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/12 13:25
 * @Version: v1.0
 */
public enum ConnectStatus {
    INIT(0, "未连接"),
    CONNECTING(1, "连接中..."),
    CONNECTED(2, "在线"),
    SERVER_ERROR(3, "无服务");
    private int code;
    private String des;

    ConnectStatus(int code, String des) {
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
