package cn.kk20.chat.core.message.body;

public class TextBody extends AbsBody {
    private String text;

    public TextBody(String text) {
        super(MsgType.TEXT);

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
