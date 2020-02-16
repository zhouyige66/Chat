package cn.roy.demo.chat.message;

/**
 * @Description: 消息主体格式
 * @Author: Roy
 * @Date: 2020/2/15 11:58 上午
 * @Version: v1.0
 */
public enum MessageBodyTypeEnum {
    TEXT(1, "text"),
    IMG(2, "img"),
    VIDEO(3, "video"),
    AUDIO(4, "audio"),
    HYBRID(5, "hybrid");

    private int code;
    private String des;

    MessageBodyTypeEnum(int code, String des) {
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
