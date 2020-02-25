package cn.kk20.chat.core.main;

import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 初始化通用Server
 * @Author: Roy
 * @Date: 2019-01-28 16:24
 * @Version: v1.0
 */
public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected ApplicationContext context;
    protected List<ChannelHandler> channelHandlerList;

    public AbstractChannelInitializer(ApplicationContext context) {
        this.context = context;
        channelHandlerList = new ArrayList<>();
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChatConfigBean chatConfigBean = context.getBean(ChatConfigBean.class);
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

        channelHandlerList.clear();
        addChannelHandler(channelHandlerList);
        if(CollectionUtils.isEmpty(channelHandlerList)){
            return;
        }
        // 添加自定义的handle
        for(ChannelHandler handler:channelHandlerList){
            channelPipeline.addLast(handler);
        }
    }

    public abstract void addChannelHandler(List<ChannelHandler> list);

}
