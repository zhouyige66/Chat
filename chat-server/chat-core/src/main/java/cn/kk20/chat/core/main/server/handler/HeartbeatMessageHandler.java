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

    @Autowired
    ClientChannelManager clientChannelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage heartbeatMessage) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        Channel channel = ctx.channel();
        clientChannelManager.channelHeartFailReset(channel);
        // 取出传递的参数
        String clientId = heartbeatMessage.getData();
        clientChannelManager.addClient(clientId, channel);
        messageSender.sendMessage(channel, heartbeatMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("当前线程为：{}", Thread.currentThread().getId());
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                int heartFail = clientChannelManager.channelHeartFail(ctx.channel());
                logger.info("心跳消息读失败：{}次", heartFail);
                hasDeal = true;
                if (heartFail > 5) {
                    heartbeatFail(ctx);
                    ctx.close();
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        clientChannelManager.channelHeartFailReset(ctx.channel());
        clientChannelManager.removeClient(ctx.channel());
    }

}
