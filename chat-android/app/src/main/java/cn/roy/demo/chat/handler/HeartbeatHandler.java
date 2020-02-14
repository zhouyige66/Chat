package cn.roy.demo.chat.handler;

import com.alibaba.fastjson.JSON;

import java.util.UUID;

import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.util.LogUtil;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Description: 心跳包处理器
 * @Author: Roy Z
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

            if (chatMessage.getType() == ChatMessage.ChatType.HEARTBEAT) {
//                LogUtil.log("收到心跳消息");
            } else {
                LogUtil.log("收到消息：" + chatMessage.toString());
                ctx.fireChannelRead(chatMessage);
            }
        } else {
            LogUtil.log("收到消息类型未知");
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
                    LogUtil.log("客户端读超时，关闭通道：" + ctx.channel().remoteAddress().toString());
                    ctx.close();
                    return;
                }

                User user = CacheManager.getInstance().getCurrentUser();
                if (user != null) {
                    ChatMessage<String> heartbeatMessage = new ChatMessage<>();
                    heartbeatMessage.setFromUserId(user.getId());
                    heartbeatMessage.setToUserId("server");
                    heartbeatMessage.setId(UUID.randomUUID().toString());
                    heartbeatMessage.setType(ChatMessage.ChatType.HEARTBEAT);
                    ChatClient.getInstance().sendMessage(heartbeatMessage);
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

}
