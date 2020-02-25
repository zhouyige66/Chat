package cn.kk20.chat.core.main.client.channelhandler;

import cn.kk20.chat.core.main.client.handler.web.TextWebSocketFrameHandler;
import cn.kk20.chat.core.main.client.handler.web.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.context.ApplicationContext;

/**
 * @Description: 初始化Web Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public class WebClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ApplicationContext context;

    public WebClientChannelInitializer(ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        WebSocketHandler webSocketHandler = context.getBean(WebSocketHandler.class);
        TextWebSocketFrameHandler textWebSocketFrameHandler = context.getBean(TextWebSocketFrameHandler.class);

        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                webSocketHandler,
                textWebSocketFrameHandler);
    }

}
