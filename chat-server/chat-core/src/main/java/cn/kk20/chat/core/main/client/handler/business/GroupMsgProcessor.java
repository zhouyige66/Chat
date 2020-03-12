package cn.kk20.chat.core.main.client.handler.business;

import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
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
@MsgProcessor(messageType = ChatMessageType.GROUP)
public class GroupMsgProcessor implements MessageProcessor {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        Long fromUserId = chatMessage.getFromUserId();
        Long toUserId = chatMessage.getToUserId();
        Set groupMemberSet = redisUtil.getSetValue(ConstantValue.MEMBER_OF_GROUP + toUserId);
        for (Object object : groupMemberSet) {
            Long targetId;
            if (object instanceof Integer) {
                targetId = ((Integer) object).longValue();
            } else {
                targetId = (Long) object;
            }
            if (targetId == fromUserId) {
                continue;
            }
            messageSender.sendMessage(targetId, chatMessage);
        }
    }

}
