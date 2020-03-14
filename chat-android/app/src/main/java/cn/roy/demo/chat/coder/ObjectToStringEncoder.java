package cn.roy.demo.chat.coder;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;
import cn.roy.demo.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/13 10:27
 * @Version: v1.0
 */
public class ObjectToStringEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message,
                          List<Object> list) throws Exception {
        String msgStr = JSON.toJSONString(message);
        if (message.getMessageType() != MessageType.HEARTBEAT.HEARTBEAT) {
            LogUtil.d(this, "发送消息：" + msgStr);
        }
        list.add(msgStr);
    }

}
