package cn.kk20.chat.base.message;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/13 08:59
 * @Version: v1.0
 */
public class ApplyMessage extends Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.APPLY;
    }

}
