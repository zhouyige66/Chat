package cn.kk20.chat.base.message.chat.body;

import cn.kk20.chat.base.message.chat.BodyType;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/2/25 21:30
 * @Version: v1.0
 */
public class TextData extends BodyData {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public BodyType getMessageBodyType() {
        return BodyType.TEXT;
    }

}
