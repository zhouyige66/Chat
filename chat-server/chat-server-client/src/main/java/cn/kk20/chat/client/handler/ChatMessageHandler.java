package cn.kk20.chat.client.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.client.processor.ProcessorManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
@MsgHandler
@ChannelHandler.Sharable
public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Autowired
    ProcessorManager processorManager;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) throws Exception {
        processorManager.handleMessage(channelHandlerContext, chatMessage,false);
    }

}
