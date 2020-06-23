package cn.kk20.chat.core.main.client.handler;

import cn.kk20.chat.base.message.ForwardMessage;
import cn.kk20.chat.core.main.client.MessageSender;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/13 14:10
 * @Version: v1.0
 */
@MsgHandler
@ChannelHandler.Sharable
public class ForwardMessageHandler extends SimpleChannelInboundHandler<ForwardMessage> {

    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ForwardMessage forwardMessage)
            throws Exception {
        Long targetUserId = forwardMessage.getTargetUserId();
        messageSender.send2BindUser(targetUserId, forwardMessage.getMessage());
    }

}
