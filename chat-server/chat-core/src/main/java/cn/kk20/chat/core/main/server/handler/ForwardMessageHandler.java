package cn.kk20.chat.core.main.server.handler;

import cn.kk20.chat.base.message.ForwardMessage;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.ClientChannelManager;
import cn.kk20.chat.core.main.server.MessageSender;
import cn.kk20.chat.core.util.RedisUtil;
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
    RedisUtil redisUtil;
    @Autowired
    MessageSender messageSender;
    @Autowired
    ClientChannelManager clientChannelManager;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ForwardMessage message) throws Exception {
        Long targetUserId = message.getTargetUserId();
        String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + targetUserId);
        Channel channel = clientChannelManager.getChannel(host);
        messageSender.sendMessage(channel, message);
    }

}
