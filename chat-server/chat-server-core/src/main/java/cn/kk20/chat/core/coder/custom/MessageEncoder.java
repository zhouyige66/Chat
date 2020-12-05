package cn.kk20.chat.core.coder.custom;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.kk20.chat.core.common.ConstantValue;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 自定义消息编码器
 * @Author: Roy
 * @Date: 2019/1/21 10:21
 * @Version: v1.0
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
            throws Exception {
        String msgStr = JSON.toJSONString(message);
        if (message.getMessageType() != MessageType.HEARTBEAT.HEARTBEAT) {
            logger.debug("发送消息：" + msgStr);
        }
        byte[] data = msgStr.getBytes(ConstantValue.CHARSET);
        // 1.写入头部标志信息
        byteBuf.writeInt(ConstantValue.HEAD_DATA);
        // 2.写入消息长度
        byteBuf.writeInt(data.length);
        // 3.写入消息内容
        byteBuf.writeBytes(data);
    }

}
