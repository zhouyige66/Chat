package cn.kk20.chat.core.handler;

import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.message.ChatMessage;
import cn.kk20.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        LogUtil.log("收到消息：" + msg);
        ChatMessage chatMessage = JSON.parseObject(msg, ChatMessage.class);
        ClientManager.getInstance().handleMessage(ctx, chatMessage, true);
    }

}
