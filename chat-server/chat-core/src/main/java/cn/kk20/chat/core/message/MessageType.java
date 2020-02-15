package cn.kk20.chat.core.message;

/**
 * @Description: 消息类型
 * @Author: Roy
 * @Date: 2020/2/15 12:05 下午
 * @Version: v1.0
 */
public enum MessageType {
    LOGIN(0, "登录信息"),
    LOGIN_REPLY(1, "登录回复消息"),
    HEARTBEAT(2, "心跳信息"),
    SINGLE(3, "点对点信息"),
    GROUP(4, "群消息"),
    NOTIFY(5,"通知消息");

    private final int code;
    private final String des;

    MessageType(int code, String des) {
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
