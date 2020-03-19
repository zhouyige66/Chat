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

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Integer> channelHeartbeatFailCountMap = new HashMap<>(12);

    @Autowired
    ClientChannelManager clientChannelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage heartbeatMessage) throws Exception {
        Channel channel = ctx.channel();
        // 收到任何消息，均把心跳失败数置零
        String channelId = channel.id().asShortText();
        channelHeartbeatFailCountMap.put(channelId, 0);
        // 取出传递的参数
        String clientId = heartbeatMessage.getData();
        clientChannelManager.cacheChannel(clientId, channel);
        messageSender.sendMessage(channel, heartbeatMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                String channelId = channel.id().asShortText();
                Integer heartFailCount = channelHeartbeatFailCountMap.get(channelId);
                if (heartFailCount == null) {
                    heartFailCount = 0;
                }
                heartFailCount++;
                logger.info("客户端通道：{}，心跳消息读失败：{}次", channelId, heartFailCount);
                hasDeal = true;
                if (heartFailCount > 5) {
                    cleanWork(ctx);
                } else {
                    channelHeartbeatFailCountMap.put(channelId, heartFailCount);
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("通道不可用了");
        cleanWork(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("通道发生异常了");
        cleanWork(ctx);
    }

    private void cleanWork(ChannelHandlerContext ctx) {
        logger.info("执行清理工作");
        String channelId = ctx.channel().id().asShortText();
        channelHeartbeatFailCountMap.remove(channelId);
        clientChannelManager.removeChannel(channelId);
        ctx.close();
    }

}
