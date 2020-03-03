package cn.kk20.chat.core.coder.delimiter;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.core.common.ConstantValue;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 以指定分隔符结尾的编码器
 * @Author: Roy
 * @Date: 2019/1/21 15:08
 * @Version: v1.0
 */
public class DelimiterBasedFrameEncoder extends MessageToByteEncoder<ChatMessage> {
    private final Logger logger = LoggerFactory.getLogger(DelimiterBasedFrameEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf)
            throws Exception {
        // 写入内容
        String msgStr = JSON.toJSONString(chatMessage);
        if(chatMessage.getMessageType() != ChatMessageType.HEARTBEAT){
            logger.debug("发送消息：" + msgStr);
        }
        String data = msgStr + ConstantValue.DELIMITER;
        byteBuf.writeBytes(data.getBytes(ConstantValue.CHARSET));
    }

}
