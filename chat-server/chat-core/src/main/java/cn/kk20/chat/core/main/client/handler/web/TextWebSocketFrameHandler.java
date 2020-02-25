package cn.kk20.chat.core.main.client.handler.web;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.util.LogUtil;
import cn.kk20.chat.core.main.client.handler.HandlerManager;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: Web消息处理器
 * @Author: Roy
 * @Date:
 * @Version: v1.0
 */
@Component
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    HandlerManager handlerManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        LogUtil.log("收到消息：" + msg);
        ChatMessage chatMessage = JSON.parseObject(msg, ChatMessage.class);
        handlerManager.handleMessage(ctx, chatMessage, true);
    }

}
