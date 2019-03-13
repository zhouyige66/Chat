package cn.roy.demo.chat.message;

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

    @Override
    public String toString() {
        return "TextBody{" +
                "text='" + text + '\'' +
                '}';
    }
}
