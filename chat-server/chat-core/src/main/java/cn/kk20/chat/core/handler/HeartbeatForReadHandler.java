package cn.kk20.chat.core.handler;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.core.main.ChatConfigBean;
import com.alibaba.fastjson.JSON;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @Description: 读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
public class HeartbeatForReadHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(HeartbeatForReadHandler.class);

    private ChatConfigBean chatConfigBean;
    private boolean isServer;
    // 发送心跳未收到回复的指令次数
    private final int heartFailMax = 5;
    private volatile int heartFailCount = 0;

    public HeartbeatForReadHandler(ChatConfigBean chatConfigBean, boolean isServer) {
        super();
        this.chatConfigBean = chatConfigBean;
        this.isServer = isServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        logger.debug("收到消息");
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

            if (chatMessage.getType() == ChatMessageType.HEARTBEAT.getCode()) {
                ChatMessage heartbeatMessage = new ChatMessage();
                heartbeatMessage.setFromUserId("server");
                heartbeatMessage.setToUserId(ctx.channel().toString());
                heartbeatMessage.setId(IdGenerator.generateId());
                heartbeatMessage.setType(ChatMessageType.HEARTBEAT.getCode());
                String msg = JSON.toJSONString(heartbeatMessage);
                logger.debug("回复心跳消息：{}", msg);
                ChannelFuture channelFuture;
                if(chatConfigBean.getCoderType() == CoderType.STRING){
                    channelFuture = ctx.writeAndFlush(msg);
                }else {
                    channelFuture = ctx.writeAndFlush(heartbeatMessage);
                }
                logger.debug("回复心跳消息：{}", channelFuture.isSuccess() ? "成功" : "失败");
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

    private void heartbeatFail(ChannelHandlerContext ctx) {
        logger.debug("客户端读超时，关闭通道");

        // 中心server读超时，表示子服务server断开连接
        if (isServer) {
            Channel channel = ctx.channel();
            String name = ctx.name();
            ChannelPipeline pipeline = ctx.pipeline();
            SocketAddress socketAddress = channel.localAddress();
            SocketAddress socketAddress1 = channel.remoteAddress();
        }
    }

}
