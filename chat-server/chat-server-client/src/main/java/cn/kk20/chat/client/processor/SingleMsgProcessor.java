package cn.kk20.chat.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.client.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 点对点消息处理器
 * @Author: Roy
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@ChatMsgProcessor(messageType = ChatMessageType.SINGLE)
public class SingleMsgProcessor implements MessageProcessor {
    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage,
                               boolean isFromWeb) {
        Long fromUserId = chatMessage.getFromUserId();
        messageSender.sync2OtherClient(fromUserId, channelHandlerContext.channel(), chatMessage);
        Long toUserId = chatMessage.getToUserId();
        messageSender.send2Receiver(toUserId, chatMessage);
    }

}
