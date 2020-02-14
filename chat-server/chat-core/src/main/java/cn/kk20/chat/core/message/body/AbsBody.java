package cn.kk20.chat.core.message.body;

public abstract class AbsBody {
    private int msgType;

    protected AbsBody(MsgType msgType) {
        this.msgType = msgType.getCode();
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

}
