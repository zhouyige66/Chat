package cn.kk20.chat.client.initializer;

import cn.kk20.chat.client.handler.MsgHandler;
import cn.kk20.chat.client.handler.heartbeat.ReadHeartbeatMessageHandler;
import cn.kk20.chat.core.main.CommonInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    CommonInitializer commonInitializer;
    @Autowired
    ReadHeartbeatMessageHandler readHeartbeatMessageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        commonInitializer.initCommon(pipeline);
        pipeline.addLast(new IdleStateHandler(5, 0, 0));
        pipeline.addLast(readHeartbeatMessageHandler);

        // 获取所有消息处理器
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MsgHandler.class);
        for (Object object : beansWithAnnotation.values()) {
//            MsgHandler msgHandler = object.getClass().getAnnotation(MsgHandler.class);
            if (object instanceof SimpleChannelInboundHandler) {
                pipeline.addLast((SimpleChannelInboundHandler) object);
            }
        }
    }
}
