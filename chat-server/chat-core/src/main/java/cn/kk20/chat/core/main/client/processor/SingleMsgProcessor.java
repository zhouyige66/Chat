package cn.kk20.chat.core.main.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 点对点消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@ChatMsgProcessor(messageType = ChatMessageType.SINGLE)
public class SingleMsgProcessor implements MessageProcessor {
    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        Long toUserId = chatMessage.getToUserId();
        messageSender.sendMessage(toUserId, chatMessage);
    }

}
