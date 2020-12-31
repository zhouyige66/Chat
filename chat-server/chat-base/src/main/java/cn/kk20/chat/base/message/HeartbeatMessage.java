package cn.kk20.chat.base.message;

/**
 * @Description: 心跳消息
 * @Author: Roy
 * @Date: 2020/3/13 08:59
 * @Version: v1.0
 */
public class HeartbeatMessage extends Message {
    // 心跳消息如果要传递数据，尽量传递小数据，因为心跳比较频繁，耗资源
    private String data;

    @Override
    public MessageType getMessageType() {
        return MessageType.HEARTBEAT;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
