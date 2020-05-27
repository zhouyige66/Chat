package cn.roy.chat.core.coder.delimiter;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.roy.chat.core.Constants;
import cn.roy.chat.core.util.LogUtil;
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
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
            throws Exception {
        // 写入内容
        String msgStr = JSON.toJSONString(message);
        if (message.getMessageType() != MessageType.HEARTBEAT) {
            LogUtil.d(this, "发送消息：" + msgStr);
        }
        String data = msgStr + Constants.DELIMITER;
        byteBuf.writeBytes(data.getBytes(Constants.CHARSET));
    }

}
