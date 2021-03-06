package cn.roy.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.roy.chat.core.ChatManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 消息处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                ChatMessage chatMessage) throws Exception {
        ChatManager.getInstance().receiveMessage(chatMessage);
    }

}
