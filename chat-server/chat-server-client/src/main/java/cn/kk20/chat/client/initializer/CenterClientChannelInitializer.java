package cn.kk20.chat.client.initializer;

import cn.kk20.chat.core.main.CommonInitializer;
import cn.kk20.chat.client.handler.ForwardMessageHandler;
import cn.kk20.chat.client.handler.heartbeat.WriteHeartbeatMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@Component
@ChannelHandler.Sharable
public class CenterClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    CommonInitializer commonInitializer;
    @Autowired
    WriteHeartbeatMessageHandler writeHeartbeatMessageHandler;
    @Autowired
    ForwardMessageHandler forwardMessageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        commonInitializer.initCommon(pipeline);
        pipeline.addLast(new IdleStateHandler(0, 5, 0));
        pipeline.addLast(writeHeartbeatMessageHandler);
        pipeline.addLast(forwardMessageHandler);
    }

}
