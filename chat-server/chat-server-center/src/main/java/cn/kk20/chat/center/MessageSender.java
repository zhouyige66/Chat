package cn.kk20.chat.center;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.kk20.chat.core.config.ChatCoderConfigBean;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 17:53
 * @Version: v1.0
 */
@Component
public class MessageSender {
    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    ChatCoderConfigBean chatConfigBean;
    @Autowired
    ClientChannelManager clientChannelManager;

    public void sendMessage(String clientId, Message message) {
        Channel channel = clientChannelManager.getChannel(clientId);
        sendMessage(channel, message);
    }

    public void sendMessage(Channel channel, Message message) {
        final String channelId = channel.id().asShortText();
        if (channel == null || !channel.isActive()) {
            logger.info("发送消息，发送通道：{}，已断开连接", channelId);
            return;
        }

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                logger.info("发送消息，发送通道：{}，收到回调，发送：{}", channelId,
                        channelFuture.isSuccess() ? "成功" : "失败");
            }
        });
        MessageType messageType = message.getMessageType();
        if (messageType == MessageType.HEARTBEAT) {
            logger.debug("发送消息，发送通道：{}，心跳接收者：{}，发送：{}", channelId, channel.remoteAddress(),
                    channelFuture.isSuccess() ? "成功" : "失败");
        } else {
            logger.info("发送消息，发送通道：{}，消息类型：{}，发送：{}", channelId, messageType,
                    channelFuture.isSuccess() ? "成功" : "失败");
        }
    }

}
