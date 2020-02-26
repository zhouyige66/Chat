package cn.kk20.chat.core.main.client.initializer;

import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.CommonInitializer;
import cn.kk20.chat.core.main.client.handler.common.CenterClientHeartbeatHandler;
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
@ClientComponent
public class CenterClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    CommonInitializer commonInitializer;
    @Autowired
    CenterClientHeartbeatHandler centerClientHeartbeatHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        commonInitializer.initCommon(pipeline);
        pipeline.addLast(new IdleStateHandler(0, 5, 0));
        pipeline.addLast(centerClientHeartbeatHandler);
    }
}
