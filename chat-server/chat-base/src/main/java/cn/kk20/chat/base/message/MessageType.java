package cn.kk20.chat.base.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息主体格式
 * @Author: Roy
 * @Date: 2020/2/15 11:58 上午
 * @Version: v1.0
 */
public enum MessageType {
    HEARTBEAT(0, "心跳"),
    LOGIN(1, "登录"),
    NOTIFY(2, "通知"),
    APPLY(3, "申请"),
    CHAT(4, "聊天"),
    FORWARD(5, "转发");

    private static Map<Integer, MessageType> messageTypeMap;
    private final int code;
    private final String des;

    static {
        MessageType[] values = MessageType.values();
        messageTypeMap = new HashMap<>(values.length);
        for (MessageType type : values) {
            messageTypeMap.put(type.code, type);
        }
    }

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

    public static MessageType getMessageTypeByCode(int code) {
        return messageTypeMap.get(code);
    }

}
