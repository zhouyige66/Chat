package cn.kk20.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.common.CommonUtil;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.core.main.ChatConfigBean;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 写心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
public class HeartbeatForWriteHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(HeartbeatForWriteHandler.class);

    private final int heartFailMax = 5;
    // 发送心跳未收到回复的指令次数
    private volatile int heartFailCount = 0;
    private ChatConfigBean chatConfigBean;

    public HeartbeatForWriteHandler(ChatConfigBean chatConfigBean) {
        this.chatConfigBean = chatConfigBean;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        logger.debug("收到消息");
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

            if (chatMessage.getType() == ChatMessageType.HEARTBEAT.getCode()) {
                logger.debug("收到中心服务器的心跳消息");
            } else {
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
                if (heartFailCount > heartFailMax) {
                    heartbeatFail(ctx);
                    ctx.close();
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
        String centerServerHost = chatConfigBean.getClient().getCenter().getHost();
        String hostIp = CommonUtil.getHostIp();
        int commonServerPort = chatConfigBean.getClient().getCommonServer().getPort();
        int webServerPort = chatConfigBean.getClient().getWebServer().getPort();

        ChatMessage heartbeatMessage = new ChatMessage();
        heartbeatMessage.setFromUserId(hostIp + ":" + commonServerPort + "&" + webServerPort);
        heartbeatMessage.setToUserId(centerServerHost);
        heartbeatMessage.setId(IdGenerator.generateId());
        heartbeatMessage.setType(ChatMessageType.HEARTBEAT.getCode());
        String msg = JSON.toJSONString(heartbeatMessage);
        logger.debug("发送心跳消息：{}", msg);
        ChannelFuture channelFuture;
        if (chatConfigBean.getCoderType() == CoderType.STRING) {
            channelFuture = ctx.writeAndFlush(msg);
        } else {
            channelFuture = ctx.writeAndFlush(heartbeatMessage);
        }
        if (channelFuture.isSuccess()) {
            logger.debug("发送心跳消息：{}", channelFuture.isSuccess() ? "成功" : "失败");
        }
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        logger.debug("心跳失败，中心服务器无法连接");
    }

}
