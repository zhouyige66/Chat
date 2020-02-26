package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.data.BodyData;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @Description: 聊天消息
 * @Author: Roy
 * @Date: 2020/2/15 12:00 下午
 * @Version: v1.0
 */
public class ChatMessage implements Serializable {
    private String id;
    private String fromUserId;
    private String toUserId;
    private ChatMessageType messageType;
    private MessageBody body;
    private String targetHost;

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

    public ChatMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ChatMessageType messageType) {
        this.messageType = messageType;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    /**
     * 设置body数据
     *
     * @param bodyData
     */
    @JSONField(serialize = false,deserialize = false)
    public void setBodyData(BodyData bodyData){
        MessageBody body = new MessageBody();
        body.setData(bodyData);
        body.setBodyType(bodyData.getMessageBodyType());
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", messageType=" + messageType +
                ", body=" + body +
                ", targetHost='" + targetHost + '\'' +
                '}';
    }
}
