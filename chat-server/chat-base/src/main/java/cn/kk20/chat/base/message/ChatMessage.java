package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.chat.BodyType;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.base.message.chat.body.BodyData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: 聊天消息
 * @Author: Roy
 * @Date: 2020/2/15 12:00 下午
 * @Version: v1.0
 */
public class ChatMessage extends Message {
    // 聊天信息类型（一对一、群聊）
    private ChatMessageType chatMessageType;
    // 消息记录ID
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    // 通过bodyType读取类型，然后通过JSON工具转实体类
    private BodyType bodyType;
    private String body;
    private Long sendTimestamp;

    public ChatMessageType getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(ChatMessageType chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    @JSONField(serialize = false, deserialize = false)
    public void setBody(BodyData body) {
        this.bodyType = body.getMessageBodyType();
        this.body = JSON.toJSONString(body);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

}
