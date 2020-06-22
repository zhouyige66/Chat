package cn.kk20.chat.core.main.server.handler;

import cn.kk20.chat.base.message.ForwardMessage;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.ClientChannelManager;
import cn.kk20.chat.core.main.server.MessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
@ServerComponent
@ChannelHandler.Sharable
public class ForwardMessageHandler extends SimpleChannelInboundHandler<ForwardMessage> {
    private final Logger logger = LoggerFactory.getLogger(ForwardMessageHandler.class);

    @Autowired
    ClientChannelManager clientChannelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ForwardMessage message) throws Exception {
        String targetHost = message.getTargetHost();
        Channel channel = clientChannelManager.getChannel(targetHost);
        if (channel == null) {
            logger.debug("转发的目标主机已离线，无法完成该请求");
            return;
        }
        messageSender.sendMessage(channel, message);
    }

}
