package cn.roy.demo.chat.coder.delimiter;

import com.alibaba.fastjson.JSON;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.roy.demo.chat.coder.ConstantValue;
import cn.roy.demo.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 以指定分隔符结尾的编码器
 * @Author: Roy
 * @Date: 2019/1/21 15:08
 * @Version: v1.0
 */
public class DelimiterBasedFrameEncoder extends MessageToByteEncoder<ChatMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage,
                          ByteBuf byteBuf) throws Exception {
        if (chatMessage.getMessageType() != ChatMessageType.HEARTBEAT) {
            LogUtil.d(this, "发送消息：" + JSON.toJSONString(chatMessage));
        }
        // 写入内容
        String data = JSON.toJSONString(chatMessage) + ConstantValue.DELIMITER;
        byteBuf.writeBytes(data.getBytes(ConstantValue.CHARSET));
    }

}
