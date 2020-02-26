package cn.kk20.chat.base.message.data;

import cn.kk20.chat.base.message.MessageBodyType;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/25 21:30
 * @Version: v1.0
 */
public class TextData extends BodyData {
    private String text;

    public TextData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MessageBodyType getMessageBodyType() {
        return MessageBodyType.TEXT;
    }

}
