package cn.kk20.chat.base.message.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息主体格式
 * @Author: Roy
 * @Date: 2020/2/15 11:58 上午
 * @Version: v1.0
 */
public enum BodyType {
    TEXT(0, "text"),
    IMG(1, "img"),
    VIDEO(2, "video"),
    AUDIO(3, "audio"),
    FILE(4, "file"),
    LINK(5, "link"),
    HYBRID(6, "hybrid");// 混合的

    private static Map<Integer, BodyType> messageBodyTypeMap;
    private int code;
    private String des;

    static {
        BodyType[] values = BodyType.values();
        messageBodyTypeMap = new HashMap<>(values.length);
        for (BodyType type : values) {
            messageBodyTypeMap.put(type.code, type);
        }
    }

    BodyType(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static BodyType getMessageBodyTypeByCode(int code) {
        return messageBodyTypeMap.get(code);
    }

}
