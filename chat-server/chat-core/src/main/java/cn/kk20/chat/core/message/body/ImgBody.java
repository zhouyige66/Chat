package cn.kk20.chat.core.message.body;

import cn.kk20.chat.core.message.MessageBodyType;

public class ImgBody extends AbstractMessageBody {
    private String url;
    private String thumbnailUrl;
    private Long fileSize;

    public ImgBody() {
        super(MessageBodyType.IMG);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
