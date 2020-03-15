package cn.kk20.chat.core.main.server.handler;

import cn.kk20.chat.base.message.HeartbeatMessage;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.ClientChannelManager;
import cn.kk20.chat.core.main.server.MessageSender;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 中央Server读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ServerComponent
@ChannelHandler.Sharable
public class HeartbeatMessageHandler extends SimpleChannelInboundHandler<HeartbeatMessage> {
    private Logger logger = LoggerFactory.getLogger(HeartbeatMessageHandler.class);
    private final int heartFailMax = 5;
    private volatile int heartFailCount = 0;

    @Autowired
    ClientChannelManager clientChannelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage heartbeatMessage) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        heartFailCount = 0;
        // 取出传递的参数
        String clientId = heartbeatMessage.getData();
        Channel channel = ctx.channel();
        ChannelId id = channel.id();
        logger.debug("ChannelId=={}", id.asShortText());
        clientChannelManager.addClient(clientId, channel);
        messageSender.sendMessage(channel, heartbeatMessage);
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
