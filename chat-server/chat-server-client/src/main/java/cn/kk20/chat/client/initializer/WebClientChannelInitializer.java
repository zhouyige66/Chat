package cn.kk20.chat.client.initializer;

import cn.kk20.chat.client.handler.web.TextWebSocketFrameHandler;
import cn.kk20.chat.client.handler.web.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 初始化Web Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@Component
public class WebClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    WebSocketHandler webSocketHandler;
    @Autowired
    TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                webSocketHandler,
                textWebSocketFrameHandler);
    }

}