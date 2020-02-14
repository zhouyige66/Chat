package cn.roy.demo.chat.message;

public class ImgBody extends AbsBody {
    private String url;

    public ImgBody(String url) {
        super(MsgType.IMG);

        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
