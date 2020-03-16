package cn.kk20.chat.core.main.client.handler.heartbeat;

import cn.kk20.chat.base.message.HeartbeatMessage;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.util.CommonUtil;
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
public class WriteHeartbeatMessageHandler extends SimpleChannelInboundHandler<HeartbeatMessage> {
    private Logger logger = LoggerFactory.getLogger(WriteHeartbeatMessageHandler.class);
    private final int heartFailMax = 5;
    private int heartFailCount = 0;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartbeatMessage heartbeatMessage)
            throws Exception {
        // 重置心跳失败次数
        heartFailCount = 0;
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
        String clientId = CommonUtil.getTargetAddress(CommonUtil.getHostIp(),
                chatConfigBean.getClient().getCommonServer().getPort(),
                chatConfigBean.getClient().getWebServer().getPort());

        HeartbeatMessage heartbeatMessage = new HeartbeatMessage();
        heartbeatMessage.setData(clientId);
        messageSender.sendMessage(ctx.channel(), heartbeatMessage);
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        heartFailCount = 0;
        logger.debug("心跳失败，中心服务器无法连接，通道为：{}", ctx.channel());
        ctx.close();
    }

}
