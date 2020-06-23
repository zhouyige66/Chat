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
import com.alibaba.fastjson.JSONObject;
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
     * 消息分配与发送
     *
     * @param targetUserId
     * @param message
     */
    public void send2Receiver(Long targetUserId, Message message) {
        JSONObject hostJson = channelManager.getCacheFromRedis(targetUserId);
        UserWrapper userWrapper = channelManager.getClient(targetUserId);
        if (userWrapper != null) {// 本服务器有该用户
            Map<ClientType, Channel> channelMap = userWrapper.getChannelMap();
            Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
            for (Map.Entry<ClientType, Channel> entry : entries) {
                ClientType clientType = entry.getKey();
                Channel channel = entry.getValue();
                if (hostJson != null && hostJson.containsKey(clientType.getIdentify())) {
                    hostJson.remove(clientType.getIdentify());
                }
                // 发送消息
                if (clientType == ClientType.WEB) {
                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(message));
                    channel.writeAndFlush(textWebSocketFrame);
                } else {
                    sendMessage(channel, message);
                }
            }
        }
        // 判断是否还有未通知的客户端
        send2CenterServer(hostJson, targetUserId, message);
    }

    /**
     * 发送消息给指定用户（绑定到该client server上的用户）
     *
     * @param targetUserId
     * @param message
     */
    public void send2BindUser(Long targetUserId, Message message) {
        UserWrapper userWrapper = channelManager.getClient(targetUserId);
        if (userWrapper == null || !userWrapper.isOnline()) {
            logger.error("Client Server上未找到该用户");
            return;
        }
        Map<ClientType, Channel> channelMap = userWrapper.getChannelMap();
        Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
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

    /**
     * 同步消息（相同账号在多个平台登录，发送的消息需要同步到其他登录平台）
     *
     * @param channel
     * @param message
     */
    public void send2Sender(Long userId, Channel channel, Message message) {
        JSONObject hostJson = channelManager.getCacheFromRedis(userId);
        UserWrapper userWrapper = channelManager.getClient(userId);
        Map<ClientType, Channel> channelMap = userWrapper.getChannelMap();
        if (!CollectionUtils.isEmpty(channelMap)) {
            Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
            for (Map.Entry<ClientType, Channel> entry : entries) {
                ClientType clientType = entry.getKey();
                if (hostJson != null && hostJson.containsKey(clientType.getIdentify())) {
                    hostJson.remove(clientType.getIdentify());
                }
                Channel value = entry.getValue();
                if (channel != value) {
                    sendMessage(value, message);
                }
            }
        }
        send2CenterServer(hostJson, userId, message);
    }

    private void send2CenterServer(JSONObject hostJson, Long targetUserId, Message message) {
        if (hostJson != null && hostJson.size() > 0) {
            for (Object object : hostJson.values()) {
                // 发送给中心主机，由中心主机转发
                Channel centerChannel = channelManager.getCenterChannel();
                ForwardMessage forwardMessage = new ForwardMessage();
                forwardMessage.setTargetHost((String) object);
                forwardMessage.setTargetUserId(targetUserId);
                forwardMessage.setMessage(message);
                sendMessage(centerChannel, forwardMessage);
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
