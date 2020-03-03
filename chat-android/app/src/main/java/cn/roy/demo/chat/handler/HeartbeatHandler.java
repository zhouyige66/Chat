package cn.roy.demo.chat.handler;

import com.alibaba.fastjson.JSON;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Description: 心跳包处理器
 * @Author: Roy
 * @Date: 2019/1/17 15:13
 * @Version: v1.0
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    // 发送心跳未收到回复的指令次数
    private int heartFailCount = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        // 如果是心跳消息重连失败次数置零
        heartFailCount = 0;
        if (obj instanceof ChatMessage || obj instanceof String) {
            ChatMessage chatMessage;
            if (obj instanceof ChatMessage) {
                chatMessage = (ChatMessage) obj;
            } else {
                chatMessage = JSON.parseObject((String) obj, ChatMessage.class);
            }

            if (chatMessage.getMessageType() != ChatMessageType.HEARTBEAT) {
                LogUtil.d(this, "收到消息：" + chatMessage.toString());
                ctx.fireChannelRead(chatMessage);
            }
        } else {
            LogUtil.e(this, "收到消息类型未知");
            ctx.fireChannelRead(obj);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                heartFailCount++;
                if (heartFailCount > ChatClient.getInstance().getConfig().getHeartbeatFailCount()) {
                    heartFailCount = 0;
                    LogUtil.i(this, "客户端读超时，关闭通道：" + ctx.channel().remoteAddress().toString());
                    ctx.close();
                    return;
                }

                ChatMessage heartbeatMessage = new ChatMessage();
                heartbeatMessage.setToUserId(0L);
                heartbeatMessage.setMessageType(ChatMessageType.HEARTBEAT);
                ChatClient.getInstance().sendMessage(heartbeatMessage);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        LogUtil.d(this, "发送异常：" + cause.getMessage());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        LogUtil.d(this, "channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        LogUtil.d(this, "channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LogUtil.d(this, "channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LogUtil.d(this, "channelInactive");
    }
}
