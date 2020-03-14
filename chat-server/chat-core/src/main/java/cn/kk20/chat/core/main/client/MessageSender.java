package cn.kk20.chat.core.main.client;

import cn.kk20.chat.base.message.ForwardMessage;
import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    RedisUtil redisUtil;
    @Autowired
    UserChannelManager userChannelManager;
    @Autowired
    ChatConfigBean chatConfigBean;

    public void sendMessage(Long targetUserId, Message message) {
        // 实时发给目标客户
        UserWrapper userWrapper = userChannelManager.getClient(targetUserId);
        if (null == userWrapper) {
            // 发送给中心主机，由中心主机转发
            Channel centerChannel = userChannelManager.getCenterChannel();
            ForwardMessage forwardMessage = new ForwardMessage();
            forwardMessage.setTargetUserId(targetUserId);
            forwardMessage.setMessage(message);
            sendMessage(centerChannel, forwardMessage);
            return;
        }

        Channel channel = userWrapper.getChannel();
        if (userWrapper.isWebUser()) {
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(message));
            channel.writeAndFlush(textWebSocketFrame);
            return;
        }

        sendMessage(channel, message);
    }

    public void sendMessage(Channel channel, Message message) {
        if (channel == null || !channel.isActive()) {
            logger.info("指定的消息接收者已断开连接");
            return;
        }

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        MessageType messageType = message.getMessageType();
        if (messageType == MessageType.HEARTBEAT) {
            logger.debug("发送消息，消息接收者：{},类型为：{}，发送：{}", channel.remoteAddress(), messageType.getDes(),
                    channelFuture.isSuccess() ? "成功" : "失败");
        } else {
            logger.info("发送消息，消息接收者：{},类型为：{}，发送：{}", channel.remoteAddress(), messageType.getDes(),
                    channelFuture.isSuccess() ? "成功" : "失败");
        }
    }

}
