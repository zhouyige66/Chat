package cn.kk20.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.core.common.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: Web登录处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 15:58
 * @Version: v1.0
 */
@Component
public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    HandlerManager handlerManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            LogUtil.log("收到消息：" + JSON.toJSONString(msg));
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            try {
                ctx.fireChannelRead(msg);
            } catch (Exception e) {
                LogUtil.log("捕获异常");
            }
        } else {
            ChatMessage chatMessage;
            if (msg instanceof ChatMessage) {
                LogUtil.log("上级传递而来");
                chatMessage = (ChatMessage) msg;
            } else {
                chatMessage = JSON.parseObject(msg.toString(), ChatMessage.class);
            }
            handlerManager.handleMessage(ctx, chatMessage, true);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LogUtil.log("通道异常");
    }

    /**********功能：WebSocket功能**********/
    private WebSocketServerHandshaker handshaker;

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, request,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 正常WebSocket的Http连接请求，构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://" + request.headers().get(HttpHeaderNames.HOST),
                null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) { // 无法处理的websocket版本
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 向客户端发送WebSocket握手,完成握手
            handshaker.handshake(ctx.channel(), request);
        }
    }

    /**
     * Http返回
     *
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request,
                                         FullHttpResponse response) {
        // 返回应答给客户端
        if (response.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response, response.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (!HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
