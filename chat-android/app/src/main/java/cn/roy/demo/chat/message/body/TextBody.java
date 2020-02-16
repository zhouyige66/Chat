package cn.roy.demo.chat.message.body;

import cn.kk20.chat.core.message.MessageBodyTypeEnum;

public class TextBody extends AbstractMessageBody {
    private String text;

    public TextBody(String text) {
        super(MessageBodyTypeEnum.TEXT);

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
