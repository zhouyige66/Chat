package cn.kk20.chat.base.message;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/13 08:47
 * @Version: v1.0
 */
public abstract class Message implements Serializable {
    private MessageType messageType;

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public abstract MessageType getMessageType();

}
