package cn.kk20.chat.core.coder.delimiter;

import cn.kk20.chat.core.coder.ConstantValue;
import cn.kk20.chat.core.message.Message;
import cn.kk20.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 以指定分隔符结尾的编码器
 * @Author: Roy
 * @Date: 2019/1/21 15:08
 * @Version: v1.0
 */
public class DelimiterBasedFrameEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        // 写入内容
        LogUtil.log("发送消息：" + message.toString());
        String data = JSON.toJSONString(message) + ConstantValue.DELIMITER;
        byteBuf.writeBytes(data.getBytes(ConstantValue.CHARSET));
    }

}
