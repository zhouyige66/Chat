package cn.kk20.chat.core.main.client.channelhandler;

import cn.kk20.chat.core.main.AbstractChannelInitializer;
import cn.kk20.chat.core.main.client.handler.common.HeartbeatForWriteHandler;
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
public class CenterClientChannelInitializer extends AbstractChannelInitializer {

    public CenterClientChannelInitializer(ApplicationContext context) {
        super(context);
    }

    @Override
    public void addChannelHandler(List<ChannelHandler> list) {
        list.add(new IdleStateHandler(0, 5, 0));
        list.add(new HeartbeatForWriteHandler(context));
    }

}
