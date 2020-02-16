package cn.kk20.chat.core.handler;

import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.util.IdGeneratorUtil;
import cn.kk20.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.text.SimpleDateFormat;

/**
 * @Description: 心跳连接处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    // 发送心跳未收到回复的指令次数
    private int heartFailCount = 0;
    private SimpleDateFormat simpleDateFormat;

    public HeartbeatHandler() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        heartFailCount = 0;

        if (obj instanceof ChatMessage || obj instanceof String) {
            ChatMessage chatMessage;
            if (obj instanceof ChatMessage) {
                chatMessage = (ChatMessage) obj;
            } else {
                try {
                    chatMessage = JSON.parseObject((String) obj, ChatMessage.class);
                } catch (Exception e) {
                    LogUtil.log("数据转换出错");
                    return;
                }
            }

            if (chatMessage.getType() == ChatMessageType.HEARTBEAT.getCode()) {
                ChatMessage heartbeatMessage = new ChatMessage();
                heartbeatMessage.setFromUserId("server");
                heartbeatMessage.setToUserId(ctx.channel().toString());
                heartbeatMessage.setId(IdGeneratorUtil.generateId());
                heartbeatMessage.setType(ChatMessageType.HEARTBEAT.getCode());
                ctx.writeAndFlush(heartbeatMessage);
            } else {
                String time = simpleDateFormat.format(System.currentTimeMillis());
                LogUtil.log(String.format("%s收到***%s***消息：%s", time, chatMessage.getFromUserId(), chatMessage.toString()));
                ctx.fireChannelRead(chatMessage);
            }
        } else {
            ctx.fireChannelRead(obj);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                heartFailCount++;
                if (heartFailCount > 3) {
                    String s = String.format("客户端读超时，关闭通道：%s", new SimpleDateFormat("HH:mm:ss")
                            .format(System.currentTimeMillis()));
                    LogUtil.log(s);
                    ctx.close();
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
