package cn.kk20.chat.core.main.server.initializer;

import cn.kk20.chat.core.main.CommonInitializer;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.main.server.handler.HeartbeatHandler;
import cn.kk20.chat.core.main.server.handler.ServerMessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@ServerComponent
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    CommonInitializer commonInitializer;
    @Autowired
    HeartbeatHandler heartbeatHandler;
    @Autowired
    ServerMessageHandler serverMessageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        commonInitializer.initCommon(pipeline);
        pipeline.addLast(new IdleStateHandler(5, 0, 0));
        pipeline.addLast(heartbeatHandler);
        pipeline.addLast(serverMessageHandler);
    }
}