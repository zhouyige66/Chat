package cn.kk20.chat.core.bean.body;

import cn.kk20.chat.core.bean.MessageBodyType;

/**
 * @Description: 消息体基类
 * @Author: Roy
 * @Date: 2020/2/16 8:58 下午
 * @Version: v1.0
 */
public abstract class AbstractMessageBody implements MessageBody {
    private MessageBodyType bodyType;

    protected AbstractMessageBody(MessageBodyType messageBodyTypeEnum) {
        this.bodyType = messageBodyTypeEnum;
    }

    @Override
    public MessageBodyType getMessageType() {
        return bodyType;
    }

}
