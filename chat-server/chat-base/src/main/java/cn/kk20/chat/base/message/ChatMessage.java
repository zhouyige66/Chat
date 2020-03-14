package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.chat.BodyType;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.base.message.chat.body.BodyData;
import cn.kk20.chat.base.message.chat.body.TextData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private BodyType bodyType;
    // 消息内容，通过bodyType读取类型，然后通过JSON工具转实体类
    private String body;

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

    @JSONField(serialize = false, deserialize = false)
    public void setBody(BodyData body) {
        this.bodyType = body.getMessageBodyType();
        this.body = JSON.toJSONString(body);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

    public static void main(String[] args) {
        ChatMessage chatMessage = new ChatMessage();
        TextData textData = new TextData();
        textData.setText("你好");
        chatMessage.setBody(textData);
        String s = JSON.toJSONString(chatMessage);
        JSONObject jsonObject = JSON.parseObject(s);
        String messageType = jsonObject.getString("messageType");
        MessageType messageType1 = MessageType.valueOf(messageType);
        if (messageType1 == MessageType.CHAT) {
            ChatMessage chatMessage1 = JSON.parseObject(s, ChatMessage.class);
            if (chatMessage1.getBodyType() == BodyType.TEXT) {
                TextData textData1 = JSON.parseObject(chatMessage1.getBody(), TextData.class);
            }
            System.out.println("完成");
        }

        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setTargetUserId(1L);
        forwardMessage.setMessage(chatMessage);
        String s1 = JSON.toJSONString(forwardMessage);
        JSONObject object = JSON.parseObject(s1);
        JSONObject messageJson = object.getJSONObject("message");
        ChatMessage c = JSON.parseObject(messageJson.toJSONString(), ChatMessage.class);
        JSON.parseObject(s1, ForwardMessage.class);
    }

}
