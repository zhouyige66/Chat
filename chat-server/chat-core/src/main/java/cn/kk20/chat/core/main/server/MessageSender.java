package cn.kk20.chat.core.main.server;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ServerComponent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 17:53
 * @Version: v1.0
 */
@ServerComponent
public class MessageSender {
    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    ClientChannelManager clientChannelManager;

    public void sendMessage(String clientId, Message message) {
        Channel channel = clientChannelManager.getClient(clientId);
        sendMessage(channel, message);
    }

    public void sendMessage(Channel channel, Message message) {
        if (channel == null || !channel.isActive()) {
            logger.debug("指定的消息接收者已断开连接");
            return;
        }

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                logger.debug("发送消息，收到回调，发送：{}", channel.remoteAddress(),
                        channelFuture.isSuccess() ? "成功" : "失败");
            }
        });
        MessageType messageType = message.getMessageType();
        if (messageType == MessageType.HEARTBEAT) {
            logger.debug("发送消息，心跳接收者：{}，发送：{}", channel.remoteAddress(),
                    channelFuture.isSuccess() ? "成功" : "失败");
        } else {
            logger.debug("发送消息，消息类型：{}，发送：{}", messageType, channelFuture.isSuccess() ? "成功" : "失败");
        }
    }

}
