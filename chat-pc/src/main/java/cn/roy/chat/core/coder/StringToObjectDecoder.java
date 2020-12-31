package cn.roy.chat.core.coder;

import cn.kk20.chat.base.message.*;
import cn.roy.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/13 10:30
 * @Version: v1.0
 */
public class StringToObjectDecoder extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list)
            throws Exception {
        LogUtil.d(this, "收到的内容原文：" + s);
        JSONObject jsonObject = JSON.parseObject(s);
        if (!jsonObject.containsKey("messageType")) {
            list.add(s);
            return;
        }

        String type = jsonObject.getString("messageType");
        MessageType messageType;
        try {
            messageType = MessageType.valueOf(type);
        } catch (Exception e) {
            list.add(s);
            return;
        }
        Object object = null;
        if (messageType == MessageType.FORWARD) {// 转发的消息需要重解析
            ForwardMessage forwardMessage = JSON.parseObject(s, ForwardMessage.class);
            JSONObject messageJson = jsonObject.getJSONObject("message");
            Message message = convertToMessage(messageJson.toJSONString());
            forwardMessage.setMessage(message);
            object = forwardMessage;
        } else {
            object = convertToMessage(s);
        }
        if (object != null) {
            list.add(object);
        } else {
            // 直接把原内容发送出去
            list.add(s);
        }
    }

    private Message convertToMessage(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (!jsonObject.containsKey("messageType")) {
            return null;
        }
        String type = jsonObject.getString("messageType");
        MessageType messageType;
        try {
            messageType = MessageType.valueOf(type);
        } catch (Exception e) {
            return null;
        }

        Message message = null;
        switch (messageType) {
            case HEARTBEAT:
                message = JSON.parseObject(jsonStr, HeartbeatMessage.class);
                break;
            case LOGIN:
                message = JSON.parseObject(jsonStr, LoginMessage.class);
                break;
            case NOTIFY:
                message = JSON.parseObject(jsonStr, NotifyMessage.class);
                break;
            case APPLY:
                message = JSON.parseObject(jsonStr, ApplyMessage.class);
                break;
            case CHAT:
                message = JSON.parseObject(jsonStr, ChatMessage.class);
                break;
            default:
                break;
        }
        return message;
    }
}
