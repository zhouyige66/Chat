package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.MessageSender;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.common.RedisUtil;
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
        String toUserId = chatMessage.getToUserId();
        Set<String> groupMemberSet = redisUtil.getSetValue(ConstantValue.MEMBER_OF_GROUP + toUserId);
        for (String id : groupMemberSet) {
            messageSender.sendMessage(id, chatMessage);
        }
    }

}
