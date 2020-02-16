package cn.kk20.chat.core.handler;

import cn.kk20.chat.core.MessageProcessor;
import cn.kk20.chat.core.bean.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
public class MessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) throws Exception {
        MessageProcessor.getInstance().processMessage(channelHandlerContext, chatMessage,false);
    }

}
