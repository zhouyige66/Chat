package cn.kk20.chat.core;

import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.handler.HeartbeatHandler;
import cn.kk20.chat.core.handler.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        // 方式一：字符串方式
//                                    p.addLast(
//                                            new StringEncoder(Charset.forName("UTF-8")),
//                                            new StringDecoder(Charset.forName("UTF-8")),
//                                            new IdleStateHandler(5, 0,
//                                                    0),
//                                            new HeartbeatHandler(),
//                                            new WebSocketHandler());
        // 方式二：分隔符方式
//                                    ByteBuf delimiterByteBuf =
//                                            Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
//                                    p.addLast(new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
//                                            new DelimiterBasedFrameEncoder(),
//                                            new StringDecoder(CharsetUtil.UTF_8),
//                                            new IdleStateHandler(5, 0,
//                                                    0),
//                                            new HeartbeatHandler(),
//                                            new MessageHandler());
        // 方式三：自定义编解码器方式
        channelPipeline.addLast(
                new MessageDecoder(),
                new MessageEncoder(),
                new IdleStateHandler(5, 0,
                        0),
                new HeartbeatHandler(),
                new MessageHandler());
    }
}
