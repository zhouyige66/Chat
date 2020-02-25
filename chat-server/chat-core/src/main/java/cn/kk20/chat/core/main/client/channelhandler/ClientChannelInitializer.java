package cn.kk20.chat.core.main.client.channelhandler;

import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.handler.common.ClientMessageHandler;
import cn.kk20.chat.core.main.client.handler.common.HeartbeatForReadHandler;
import cn.kk20.chat.core.main.client.handler.common.HeartbeatForWriteHandler;
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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
@ClientComponent
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    HeartbeatForReadHandler heartbeatForReadHandler;
    @Autowired
    ClientMessageHandler clientMessageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        switch (chatConfigBean.getCoderType()) {
            case STRING:// 字符串方式
                pipeline.addLast(
                        new StringEncoder(CharsetUtil.UTF_8),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case DELIMITER:// 分隔符方式
                ByteBuf delimiterByteBuf = Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
                pipeline.addLast(
                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
                        new DelimiterBasedFrameEncoder(),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case CUSTOM:// 自定义编解码器方式
                pipeline.addLast(
                        new MessageDecoder(),
                        new MessageEncoder());
                break;
            default:
                throw new Exception("该方式暂无实现");
        }

        pipeline.addLast(new IdleStateHandler(5, 0, 0));
        pipeline.addLast(heartbeatForReadHandler);
        pipeline.addLast(clientMessageHandler);
    }
}
