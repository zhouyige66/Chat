package cn.kk20.chat.core.initializer;

import cn.kk20.chat.core.coder.CoderType;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.kk20.chat.core.handler.HeartbeatHandler;
import cn.kk20.chat.core.handler.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private CoderType coderType;

    public ServerChannelInitializer(CoderType coderType) {
        this.coderType = coderType;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        switch (coderType) {
            case STRING:// 字符串方式
                channelPipeline.addLast(
                        new StringEncoder(CharsetUtil.UTF_8),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case DELIMITER:// 分隔符方式
                ByteBuf delimiterByteBuf = Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
                channelPipeline.addLast(
                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
                        new DelimiterBasedFrameEncoder(),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case CUSTOM:// 自定义编解码器方式
                channelPipeline.addLast(
                        new MessageDecoder(),
                        new MessageEncoder());
                break;
            default:
                throw new Exception("该方式暂无实现");
        }
        // 添加心跳处理，消息处理器
        channelPipeline.addLast(new IdleStateHandler(5, 0, 0),
                new HeartbeatHandler(),
                new MessageHandler());
    }

}
