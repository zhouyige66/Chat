package cn.kk20.chat.core.initializer;

import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.handler.HeartbeatForReadHandler;
import cn.kk20.chat.core.handler.HeartbeatForWriteHandler;
import cn.kk20.chat.core.handler.MessageHandler;
import cn.kk20.chat.core.main.ChatConfigBean;
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
    private ChatConfigBean chatConfigBean;
    private boolean needWrite = false;

    public ServerChannelInitializer(ChatConfigBean chatConfigBean) {
        this.chatConfigBean = chatConfigBean;
    }

    public ServerChannelInitializer(ChatConfigBean chatConfigBean, boolean needWrite) {
        this.chatConfigBean = chatConfigBean;
        this.needWrite = needWrite;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        switch (chatConfigBean.getCoderType()) {
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
        boolean registerAsServer = chatConfigBean.isRegisterAsServer();
        if (registerAsServer) {// 中心服务器，只需要读心跳消息的handler
            channelPipeline.addLast(
                    new IdleStateHandler(5, 0, 0))
                    .addLast(new HeartbeatForReadHandler(chatConfigBean, true));
        } else {// 子服务器，需要配置写心跳和读心跳
            if (needWrite) {
                channelPipeline.addLast(
                        new IdleStateHandler(0, 5, 0))
                        .addLast(new HeartbeatForWriteHandler(chatConfigBean));
            } else {
                channelPipeline.addLast(
                        new IdleStateHandler(5, 0, 0))
                        .addLast(new HeartbeatForReadHandler(chatConfigBean, false),
                                new MessageHandler());
            }
        }
    }

}
