package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.body.MessageBody;

/**
 * @Description: 聊天消息
 * @Author: Roy
 * @Date: 2020/2/15 12:00 下午
 * @Version: v1.0
 */
public class ChatMessage {
    private String id;
    private String fromUserId;
    private String toUserId;
    private int type;
    private MessageBody body;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", body=" + body +
                '}';
    }
}
