package cn.roy.demo.chat.message.body;


import cn.roy.demo.chat.message.MessageBodyTypeEnum;

public abstract class AbstractMessageBody implements MessageBody {
    private MessageBodyTypeEnum bodyType;

    protected AbstractMessageBody(MessageBodyTypeEnum messageBodyTypeEnum) {
        this.bodyType = messageBodyTypeEnum;
    }

    @Override
    public MessageBodyTypeEnum getMessageType() {
        return bodyType;
    }

}
