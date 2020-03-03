package cn.kk20.chat.core.main.server;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ServerComponent;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

    public void sendMessage(Channel channel, ChatMessage chatMessage) {
        if (channel == null || !channel.isActive()) {
            logger.debug("指定的消息接收者已断开连接");
            return;
        }

        ChannelFuture channelFuture;
        if (chatConfigBean.getCoderType() == CoderType.STRING) {
            // 方式一：发送字符串
            channelFuture = channel.writeAndFlush(JSON.toJSONString(chatMessage));
        } else {
            // 方式二或三：发送数据经过自定义编码器
            channelFuture = channel.writeAndFlush(chatMessage);
        }
        logger.debug("发送消息，消息id：{},类型为：{}，发送{}", chatMessage.getId(), chatMessage.getMessageType(),
                channelFuture.isSuccess() ? "成功" : "失败");
    }

}
