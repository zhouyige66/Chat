package cn.kk20.chat.core.main.server.channelhandler;

import cn.kk20.chat.core.main.AbstractChannelInitializer;
import cn.kk20.chat.core.main.client.handler.common.HeartbeatForWriteHandler;
import cn.kk20.chat.core.main.server.handler.ServerMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public class ServerChannelInitializer extends AbstractChannelInitializer {

    public ServerChannelInitializer(ApplicationContext context) {
        super(context);
    }

    @Override
    public void addChannelHandler(List<ChannelHandler> list) {
        list.add(new IdleStateHandler(5, 0, 0));
        list.add(new HeartbeatForWriteHandler(context));
        list.add(new ServerMessageHandler());
    }

}
