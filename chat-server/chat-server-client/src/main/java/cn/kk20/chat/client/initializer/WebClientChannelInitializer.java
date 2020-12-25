package cn.kk20.chat.client.initializer;

import cn.kk20.chat.client.handler.MsgHandler;
import cn.kk20.chat.client.handler.web.TextWebSocketFrameHandler;
import cn.kk20.chat.client.handler.web.WebSocketHandler;
import cn.kk20.chat.core.coder.ObjectToStringEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 初始化Web Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@Component
public class WebClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    ApplicationContext applicationContext;
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
                new ObjectToStringEncoder(),
                webSocketHandler,
                textWebSocketFrameHandler);
        // 获取所有消息处理器
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MsgHandler.class);
        for (Object object : beansWithAnnotation.values()) {
            if (object instanceof SimpleChannelInboundHandler) {
                channelPipeline.addLast((SimpleChannelInboundHandler) object);
            }
        }
    }

}
