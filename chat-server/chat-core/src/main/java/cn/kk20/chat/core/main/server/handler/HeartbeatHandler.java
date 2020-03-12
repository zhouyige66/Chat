package cn.kk20.chat.core.main.server.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.MessageBody;
import cn.kk20.chat.base.message.data.TextData;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.ClientChannelManager;
import cn.kk20.chat.core.main.server.MessageSender;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ServerComponent
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
    private final int heartFailMax = 5;
    private volatile int heartFailCount = 0;

    @Autowired
    ClientChannelManager clientChannelManager;
    @Autowired
    MessageSender messageSender;

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
                    logger.error("数据转换出错");
                    return;
                }
            }

            logger.debug("收到消息：{}", JSON.toJSONString(chatMessage));
            if (chatMessage.getMessageType() == ChatMessageType.HEARTBEAT) {
                // 取出传递的参数
                MessageBody body = chatMessage.getBody();
                JSONObject data = (JSONObject) body.getData();
                TextData textData = JSON.parseObject(data.toJSONString(),TextData.class);
                String clientId = textData.getText();
                clientChannelManager.addClient(clientId,ctx.channel());
                messageSender.sendMessage(ctx.channel(), chatMessage);
            } else {// 向下层传递
                ctx.fireChannelRead(chatMessage);
            }
        } else {
            ctx.fireChannelRead(obj);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                heartFailCount++;
                logger.debug("心跳消息读失败：{}次", heartFailCount);
                hasDeal = true;
                if (heartFailCount > heartFailMax) {
                    heartbeatFail(ctx);
                    ctx.close();
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelInactive");
        clientChannelManager.removeClient(ctx.channel());
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        clientChannelManager.removeClient(ctx.channel());
    }

}
