package cn.kk20.chat.core.message.body;

import cn.kk20.chat.core.message.MessageBodyType;

public class TextBody extends AbstractMessageBody {
    private String text;

    public TextBody(String text) {
        super(MessageBodyType.TEXT);

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
