package cn.kk20.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
@Component
public class MessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Autowired
    HandlerManager handlerManager;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) throws Exception {
        handlerManager.handleMessage(channelHandlerContext, chatMessage,false);
    }

}
