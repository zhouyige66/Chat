package cn.kk20.chat.core.main;

import cn.kk20.chat.core.coder.custom.MessageDecoder;
import cn.kk20.chat.core.coder.custom.MessageEncoder;
import cn.kk20.chat.core.coder.delimiter.DelimiterBasedFrameEncoder;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/25 10:58
 * @Version: v1.0
 */
@Component
public class CommonInitializer {

    @Autowired
    ChatConfigBean chatConfigBean;

    public void initCommon(ChannelPipeline pipeline) {
        switch (chatConfigBean.getCoderType()) {
            case STRING:// 字符串方式
                pipeline.addLast(
                        new StringEncoder(CharsetUtil.UTF_8),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case DELIMITER:// 分隔符方式
                ByteBuf delimiterByteBuf = Unpooled.copiedBuffer(ConstantValue.DELIMITER.getBytes());
                pipeline.addLast(
                        new DelimiterBasedFrameEncoder(),
                        new DelimiterBasedFrameDecoder(2048, delimiterByteBuf),
                        new StringDecoder(CharsetUtil.UTF_8));
                break;
            case CUSTOM:// 自定义编解码器方式
                pipeline.addLast(
                        new MessageDecoder(),
                        new MessageEncoder());
                break;
            default:
                break;
        }
    }

}
