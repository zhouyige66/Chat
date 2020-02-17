package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.ChatServer;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.service.impl.MessageServiceImpl;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgProcessor(messageType = ChatMessageType.SINGLE)
public class SingleMsgProcessor implements MessageProcessor {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {

    }

}
