package cn.kk20.chat.client.handler.web;

import cn.kk20.chat.base.message.LoginMessage;
import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.login.ClientType;
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
        logger.debug("收到消息：{}", msg);
        Message message = StringToObjectDecoder.convertToMessage(msg);
        if (message instanceof LoginMessage) {
            if (((LoginMessage) message).getClientType() != ClientType.WEB) {
                logger.debug("登录消息填充客户端类型不对，采取修正处理");
                ((LoginMessage) message).setClientType(ClientType.WEB);
            }
        }
        ctx.fireChannelRead(message);// 向下级传递
    }

}
