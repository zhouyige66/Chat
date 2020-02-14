package cn.kk20.chat.core.message;

/**
 * 消息类型
 */
public enum ChatType {
    HEARTBEAT(-1, "heartbeat"),
    LOGIN(0, "login"),
    SINGLE(1, "single"),
    GROUP(2, "group"),
    LOGIN_REPLY(3, "loginReply");

    int code;
    String des;

    ChatType(int code, String des) {
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
