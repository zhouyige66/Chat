package cn.kk20.chat.base.message;

/**
 * @Description: 通知消息
 * @Author: Roy Z
 * @Date: 2020/3/13 08:59
 * @Version: v1.0
 */
public class NotifyMessage extends Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.NOTIFY;
    }

}
