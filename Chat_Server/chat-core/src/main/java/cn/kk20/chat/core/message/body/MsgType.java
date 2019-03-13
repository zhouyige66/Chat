package cn.kk20.chat.core.message.body;

/**
 * 消息格式
 */
public enum MsgType {
    TEXT(1, "text"),
    IMG(2, "img"),
    VIDEO(3, "video"),
    AUDIO(4, "audio"),
    HYBRID(5, "hybrid");

    private int code;
    private String des;

    MsgType(int code, String des) {
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
