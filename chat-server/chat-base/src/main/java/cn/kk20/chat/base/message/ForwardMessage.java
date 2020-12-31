package cn.kk20.chat.base.message;

/**
 * @Description: 服务器中转消息
 * @Author: Roy
 * @Date: 2020/3/13 09:09
 * @Version: v1.0
 */
public class ForwardMessage extends Message {
    // 用于中央server转发
    private String targetHost;
    private Long targetUserId;
    private Message message;

    @Override
    public MessageType getMessageType() {
        return MessageType.FORWARD;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
