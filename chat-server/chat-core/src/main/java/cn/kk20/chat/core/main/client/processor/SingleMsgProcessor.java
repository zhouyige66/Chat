package cn.kk20.chat.core.main.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 点对点消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@ChatMsgProcessor(messageType = ChatMessageType.SINGLE)
public class SingleMsgProcessor implements MessageProcessor {
    @Autowired
    MessageSender messageSender;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage,
                               boolean isFromWeb) {
        Long fromUserId = chatMessage.getFromUserId();
        Long toUserId = chatMessage.getToUserId();

        String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + fromUserId);
        JSONObject hostJson = JSON.parseObject(host);
        if (hostJson != null && hostJson.size() > 0) {

        }
        messageSender.sendMessage2Target(toUserId, chatMessage);
    }

}
