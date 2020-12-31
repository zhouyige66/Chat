package cn.kk20.chat.client.processor;

import cn.kk20.chat.base.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/2/17 16:31
 * @Version: v1.0
 */
public interface MessageProcessor {

    /**
     * message处理
     *
     * @param channelHandlerContext
     * @param chatMessage
     * @param isFromWeb
     */
    void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb);

}
