package cn.kk20.chat.client.handler.web;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.core.coder.StringToObjectDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: Web消息处理器
 * @Author: Roy
 * @Date:
 * @Version: v1.0
 */
@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        logger.debug("收到消息：{}",msg);
        Message message = StringToObjectDecoder.convertToMessage(msg);
        ctx.fireChannelRead(message);// 向下级传递
    }

}
