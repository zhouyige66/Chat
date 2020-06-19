package cn.roy.chat.core.handler;

import cn.kk20.chat.base.message.ApplyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/17 13:53
 * @Version: v1.0
 */
public class ApplyMessageHandler extends SimpleChannelInboundHandler<ApplyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ApplyMessage applyMessage) throws Exception {

    }
}
