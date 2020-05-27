package cn.roy.chat.core.coder.custom;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.roy.chat.core.Constants;
import cn.roy.chat.core.util.LogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 自定义消息编码器
 * @Author: Roy
 * @Date: 2019/1/21 10:21
 * @Version: v1.0
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
            throws Exception {
        String msgStr = JSON.toJSONString(message);
        if (message.getMessageType() != MessageType.HEARTBEAT.HEARTBEAT) {
            LogUtil.d(this, "发送消息：" + msgStr);
        }
        byte[] data = msgStr.getBytes(Constants.CHARSET);
        // 1.写入头部标志信息
        byteBuf.writeInt(Constants.HEAD_DATA);
        // 2.写入消息长度
        byteBuf.writeInt(data.length);
        // 3.写入消息内容
        byteBuf.writeBytes(data);
    }

}
