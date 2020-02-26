package cn.kk20.chat.core.main.client.handler.common;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.data.TextData;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 写心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ClientComponent
@ChannelHandler.Sharable
public class CenterClientHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(CenterClientHeartbeatHandler.class);
    private final int heartFailMax = 5;
    private int heartFailCount = 0;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        heartFailCount = 0;

        if (obj instanceof ChatMessage || obj instanceof String) {
            ChatMessage chatMessage = null;
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
            if (chatMessage.getMessageType() != ChatMessageType.HEARTBEAT) {
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
            if (event.state() == IdleState.WRITER_IDLE) {
                hasDeal = true;
                heartFailCount++;
                if (heartFailCount >= heartFailMax) {
                    heartbeatFail(ctx);
                } else {
                    sendHeartbeatMessage(ctx);
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void sendHeartbeatMessage(ChannelHandlerContext ctx) {
        int port = chatConfigBean.getClient().getCommonServer().getPort();
        int port2 = chatConfigBean.getClient().getWebServer().getPort();
        String clientId = CommonUtil.getHostIp() + ":" + port + "&" + port2;
        TextData textData = new TextData(clientId);
        ChatMessage heartbeatMessage = new ChatMessage();
        heartbeatMessage.setMessageType(ChatMessageType.HEARTBEAT);
        heartbeatMessage.setBodyData(textData);
        messageSender.sendMessage(ctx.channel(), heartbeatMessage);
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        logger.debug("心跳失败，中心服务器无法连接");
        ctx.close();
    }

}
