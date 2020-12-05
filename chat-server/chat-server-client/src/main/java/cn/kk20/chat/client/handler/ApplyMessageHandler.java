package cn.kk20.chat.client.handler;

import cn.kk20.chat.base.message.ApplyMessage;
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
public class ApplyMessageHandler extends SimpleChannelInboundHandler<ApplyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ApplyMessage notifyMessage)
            throws Exception {

    }

}
