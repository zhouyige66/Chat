package cn.kk20.chat.core.bean;

/**
 * @Description: 消息类型
 * @Author: Roy
 * @Date: 2020/2/15 12:05 下午
 * @Version: v1.0
 */
public enum ChatMessageType {
    LOGIN(0, "登录"),
    HEARTBEAT(1, "心跳"),
    SINGLE(2, "点对点"),
    GROUP(3, "群聊"),
    NOTIFY(4, "通知");

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

    public static ChatMessageType convertCode2ChatMessageTypeEnum(int code) {
        ChatMessageType[] values = ChatMessageType.values();
        for (ChatMessageType chatMessageTypeEnum : values) {
            if (chatMessageTypeEnum.code == code) {
                return chatMessageTypeEnum;
            }
        }
        return null;
    }

}
