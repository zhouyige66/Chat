package cn.kk20.chat.core.main;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.core.main.client.UserChannelManager;
import cn.kk20.chat.core.main.client.UserWrapper;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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

    @Autowired
    ChatConfigBean chatConfigBean;

    public void sendMessage(String targetId, ChatMessage chatMessage) {
        // 实时发给目标客户
        UserWrapper userWrapper = UserChannelManager.getInstance().getClient(targetId);
        if (null == userWrapper) {
            LogUtil.log("指定的消息接收者未登录");
            return;
        }

        Channel channel = userWrapper.getChannel();
        if (userWrapper.isWebUser()) {
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(chatMessage));
            channel.writeAndFlush(textWebSocketFrame);
            return;
        }
        sendMessage(channel, chatMessage);
    }

    public void sendMessage(Channel channel, ChatMessage chatMessage) {
        if (channel == null || !channel.isActive()) {
            LogUtil.log("指定的消息接收者已断开连接");
            return;
        }

        if (chatConfigBean.getCoderType() == CoderType.STRING) {
            // 方式一：发送字符串
            channel.writeAndFlush(JSON.toJSONString(chatMessage));
        } else {
            // 方式二或三：发送数据经过自定义编码器
            channel.writeAndFlush(chatMessage);
        }
    }

}
