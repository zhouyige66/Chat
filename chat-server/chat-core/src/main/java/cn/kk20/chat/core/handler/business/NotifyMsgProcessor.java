package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.MessageSender;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.common.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 通知消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgProcessor(messageType = ChatMessageType.NOTIFY)
public class NotifyMsgProcessor implements MessageProcessor {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        //TODO 用户量大，可能需要分批推送
    }

}
