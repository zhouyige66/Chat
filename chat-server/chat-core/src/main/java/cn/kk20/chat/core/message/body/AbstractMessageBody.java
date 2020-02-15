package cn.kk20.chat.core.message.body;

import cn.kk20.chat.core.message.MessageBodyType;

public abstract class AbstractMessageBody implements MessageBody {
    private MessageBodyType bodyType;

    protected AbstractMessageBody(MessageBodyType messageBodyType) {
        this.bodyType = messageBodyType;
    }

    @Override
    public MessageBodyType getMessageType() {
        return bodyType;
    }

}
