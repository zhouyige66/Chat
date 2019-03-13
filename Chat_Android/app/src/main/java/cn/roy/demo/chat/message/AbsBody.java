package cn.roy.demo.chat.message;

public abstract class AbsBody {
    private int msgType;

    protected AbsBody(MsgType msgType) {
        this.msgType = msgType.code;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public enum MsgType{
        TEXT(1,"text"),
        IMG(2,"img"),
        VIDEO(3,"video"),
        AUDIO(4,"audio"),
        HYBRID(5,"hybrid");

        private int code;
        private String des;

        MsgType(int code, String des) {
            this.code = code;
            this.des = des;
        }
    }

    @Override
    public String toString() {
        return "AbsBody{" +
                "msgType=" + msgType +
                '}';
    }
}
