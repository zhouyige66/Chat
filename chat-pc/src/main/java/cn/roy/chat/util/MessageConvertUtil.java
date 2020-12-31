package cn.roy.chat.util;

import cn.kk20.chat.base.message.Message;
import cn.kk20.chat.base.message.MessageType;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/17 11:22
 * @Version: v1.0
 */
public class MessageConvertUtil {

    public static Message convert(Message message) {
        final MessageType messageType = message.getMessageType();
        return message;
    }

}
