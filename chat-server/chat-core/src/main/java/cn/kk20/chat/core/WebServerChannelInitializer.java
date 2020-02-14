package cn.kk20.chat.core;

import cn.kk20.chat.core.handler.TextWebSocketFrameHandler;
import cn.kk20.chat.core.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public class WebServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                new WebSocketHandler(),
                new TextWebSocketFrameHandler());
    }
}
