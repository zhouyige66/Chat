package cn.kk20.chat.core.coder;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/13 10:27
 * @Version: v1.0
 */
public class ObjectToStringEncoder extends MessageToMessageEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(ObjectToStringEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        String msgStr = JSON.toJSONString(message);
        if (message.getMessageType() != MessageType.HEARTBEAT.HEARTBEAT) {
            logger.debug("发送消息：" + msgStr);
        }
        list.add(msgStr);
    }

}
