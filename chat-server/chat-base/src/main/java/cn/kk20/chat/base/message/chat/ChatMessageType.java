package cn.kk20.chat.base.message.chat;

/**
 * @Description: 消息类型
 * @Author: Roy
 * @Date: 2020/2/15 12:05 下午
 * @Version: v1.0
 */
public enum ChatMessageType {
    SINGLE(0, "点对点"),
    GROUP(1, "群聊");

    private final int code;
    private final String des;

    ChatMessageType(int code, String des) {
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
