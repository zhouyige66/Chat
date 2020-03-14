package cn.roy.demo.chat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;
import java.util.Set;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.body.TextData;
import cn.roy.demo.util.CacheManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
public class MessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                ChatMessage chatMessage) throws Exception {
        switch (chatMessage.getChatMessageType()) {
            default:
                CacheManager.getInstance().cacheMessage(chatMessage);
                break;
        }
    }

}
