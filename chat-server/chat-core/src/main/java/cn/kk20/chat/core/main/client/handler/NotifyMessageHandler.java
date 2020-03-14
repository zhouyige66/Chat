package cn.kk20.chat.core.main.client.handler;

import cn.kk20.chat.base.message.NotifyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 通知消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgHandler
@ChannelHandler.Sharable
public class NotifyMessageHandler extends SimpleChannelInboundHandler<NotifyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NotifyMessage notifyMessage)
            throws Exception {

    }

}
