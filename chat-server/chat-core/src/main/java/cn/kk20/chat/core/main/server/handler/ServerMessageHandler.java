package cn.kk20.chat.core.main.server.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.main.ServerComponent;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
@ServerComponent
@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {
    private final Logger logger = LoggerFactory.getLogger(ServerMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) throws Exception {
        logger.debug("收到消息：{}", JSON.toJSONString(chatMessage));
    }

}
