package cn.kk20.chat.core.main.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @Description: 群消息处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@ChatMsgProcessor(messageType = ChatMessageType.GROUP)
public class GroupMsgProcessor implements MessageProcessor {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage,
                               boolean isFromWeb) {
        Long fromUserId = chatMessage.getFromUserId();
        Long toUserId = chatMessage.getToUserId();
        Set<Long> groupMemberSet = redisUtil.getLongSetValue(ConstantValue.MEMBER_OF_GROUP + toUserId);
        for (Long memberId : groupMemberSet) {
            if (memberId == fromUserId) {
                continue;
            }
            messageSender.sendMessage(memberId, chatMessage);
        }
    }

}
