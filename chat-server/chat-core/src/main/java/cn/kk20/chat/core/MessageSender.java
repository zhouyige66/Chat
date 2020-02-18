package cn.kk20.chat.core;

import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.service.MessageService;
import cn.kk20.chat.service.impl.MessageServiceImpl;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 17:53
 * @Version: v1.0
 */
@Component
public class MessageSender {

    @Autowired
    MessageService messageService;

    public void sendMessage(Channel channel, ChatMessage chatMessage) {
        if (channel == null || !channel.isActive()) {
            LogUtil.log("指定的消息接收者已断开连接");
            return;
        }

        if (ChatServer.getInstance().getCoderType() == CoderType.STRING) {
            // 方式一：发送字符串
            channel.writeAndFlush(JSON.toJSONString(chatMessage));
        } else {
            // 方式二或三：发送数据经过自定义编码器
            channel.writeAndFlush(chatMessage);
        }
    }

    public void sendMessage(String targetId, ChatMessage chatMessage) {
        // 存储到数据库
        MessageModel messageModel = new MessageModel();
        messageModel.setId(chatMessage.getId());
        messageModel.setFromUserId(chatMessage.getFromUserId());
        messageModel.setToUserId(chatMessage.getToUserId());
        messageModel.setContent(JSON.toJSONString(chatMessage));
        if (messageService == null) {
            ApplicationContext context = ChatServer.getInstance().getContext();
            messageService = context.getAutowireCapableBeanFactory().createBean(MessageServiceImpl.class);
        }
        int result = messageService.save(messageModel);
        LogUtil.log("存储消息：" + result);

        // 实时发给目标客户
        ClientWrapper clientWrapper = ClientManager.getInstance().getClient(targetId);
        if (null == clientWrapper) {
            LogUtil.log("指定的消息接收者未登录");
            return;
        }

        Channel channel = clientWrapper.getChannel();
        if (clientWrapper.isWebUser()) {
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(chatMessage));
            channel.writeAndFlush(textWebSocketFrame);
            return;
        }
        sendMessage(channel, chatMessage);
    }

}
