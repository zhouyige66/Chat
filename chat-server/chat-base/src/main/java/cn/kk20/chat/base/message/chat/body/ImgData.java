package cn.kk20.chat.base.message.chat.body;

import cn.kk20.chat.base.message.chat.BodyType;

/**
 * @Description: 图片消息体
 * @Author: Roy
 * @Date: 2020/2/17 15:57
 * @Version: v1.0
 */
public class ImgData extends BodyData {
    private String fileName;// 文件名称
    private String filePath;// 本地路径
    private String url;// 上传后，远程路径
    private String thumbnailUrl;// 缩略图路径
    private Long fileSize;// 文件大小

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    @Override
    public BodyType getMessageBodyType() {
        return BodyType.IMG;
    }

}
