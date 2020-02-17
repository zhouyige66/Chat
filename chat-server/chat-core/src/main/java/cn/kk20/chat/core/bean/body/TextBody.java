package cn.kk20.chat.core.bean.body;

import cn.kk20.chat.core.bean.MessageBodyType;

/**
 * @Description: 普通文本消息体
 * @Author: Roy Z
 * @Date: 2020/2/17 15:56
 * @Version: v1.0
 */
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
