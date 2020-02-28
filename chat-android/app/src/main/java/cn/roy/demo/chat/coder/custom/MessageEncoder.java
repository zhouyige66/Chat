package cn.roy.demo.chat.coder.custom;

import com.alibaba.fastjson.JSON;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.roy.demo.chat.coder.ConstantValue;
import cn.roy.demo.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 自定义消息编码器
 * @Author: Roy
 * @Date: 2019/1/21 10:21
 * @Version: v1.0
 */
public class MessageEncoder extends MessageToByteEncoder<ChatMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf)
            throws Exception {
        if (chatMessage.getMessageType() != ChatMessageType.HEARTBEAT) {
            LogUtil.d(this, "发送消息：" + chatMessage.toString());
        }

        String msgStr = JSON.toJSONString(chatMessage);
        byte[] data = msgStr.getBytes(ConstantValue.CHARSET);
        // 1.写入头部标志信息
        byteBuf.writeInt(ConstantValue.HEAD_DATA);
        // 2.写入消息长度
        byteBuf.writeInt(data.length);
        // 3.写入消息内容
        byteBuf.writeBytes(data);
    }

}
