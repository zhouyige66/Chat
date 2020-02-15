package cn.kk20.chat.core.handler;

import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.message.Message;
import cn.kk20.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Description: Web消息处理器
 * @Author: Roy
 * @Date:
 * @Version: v1.0
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        LogUtil.log("收到消息：" + msg);
        Message message = JSON.parseObject(msg, Message.class);
        ClientManager.getInstance().handleMessage(ctx, message, true);
    }

}
