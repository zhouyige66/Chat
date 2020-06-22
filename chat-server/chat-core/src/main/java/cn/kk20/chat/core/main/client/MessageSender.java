package cn.kk20.chat.core.main.client;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ForwardMessage;
import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.kk20.chat.base.message.login.ClientType;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 17:53
 * @Version: v1.0
 */
@ClientComponent
public class MessageSender {
    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ChannelManager channelManager;

    /**
     * 同步消息（相同账号在多个平台登录，发送的消息需要同步到其他登录平台）
     *
     * @param channel
     * @param message
     */
    public void syncMessage(Channel channel, Message message) {
        Long userId = channelManager.getUserIdByChannel(channel);
        if (userId == null) {
            return;
        }

        UserWrapper userWrapper = channelManager.getClient(userId);
        Map<ClientType, Channel> channelMap = userWrapper.getChannelMap();
        if (!CollectionUtils.isEmpty(channelMap)) {
            Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
            for (Map.Entry<ClientType, Channel> entry : entries) {
                Channel value = entry.getValue();
                if (channel != value) {
                    sendMessage(value, message);
                }
            }
        }
    }

    public void sendMessage2Target(Long targetUserId, Message message) {
        // 实时发给目标客户
        UserWrapper userWrapper = channelManager.getClient(targetUserId);
        if (null == userWrapper) {
            // 发送给中心主机，由中心主机转发
            Channel centerChannel = channelManager.getCenterChannel();
            ForwardMessage forwardMessage = new ForwardMessage();
            forwardMessage.setTargetUserId(targetUserId);
            forwardMessage.setMessage(message);
            sendMessage(centerChannel, forwardMessage);
            return;
        }

        final Map<ClientType, Channel> channelMap = userWrapper.getChannelMap();
        final Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
        for (Map.Entry<ClientType, Channel> entry : entries) {
            ClientType clientType = entry.getKey();
            Channel channel = entry.getValue();
            if (clientType == ClientType.WEB) {
                TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(message));
                channel.writeAndFlush(textWebSocketFrame);
            } else {
                sendMessage(channel, message);
            }
        }
    }

    public void sendMessage(Channel channel, Message message) {
        if (channel == null || !channel.isActive()) {
            logger.info("指定的消息接收者已断开连接");
            return;
        }

        final MessageType messageType = message.getMessageType();
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                String channelId = channel.id().asShortText();
                boolean success = future.isSuccess();
                switch (messageType) {
                    case CHAT:
                        ChatMessage chatMessage = (ChatMessage) message;
                        logger.info("发送消息，传输通道：{}，消息id：{}，发送：{}", channelId,
                                chatMessage.getId(),
                                success ? "成功" : "失败");
                        break;
                    default:
                        logger.debug("发送消息，传输通道：{}，类型为：{}，发送：{}", channelId,
                                messageType.getDes(),
                                success ? "成功" : "失败");
                        break;
                }
            }
        });
    }

}
