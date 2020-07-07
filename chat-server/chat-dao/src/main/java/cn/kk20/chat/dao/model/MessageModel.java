package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class MessageModel implements Serializable {
    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private Integer contentType;

    private Boolean received;

    private Boolean fromUserDelete;

    private Boolean toUserDelete;

    private Boolean isDelete;

    private Date createDate;

    private Date modifyDate;

    private String content;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public MessageModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public MessageModel withFromUserId(Long fromUserId) {
        this.setFromUserId(fromUserId);
        return this;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public MessageModel withToUserId(Long toUserId) {
        this.setToUserId(toUserId);
        return this;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getContentType() {
        return contentType;
    }

    public MessageModel withContentType(Integer contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Boolean getReceived() {
        return received;
    }

    public MessageModel withReceived(Boolean received) {
        this.setReceived(received);
        return this;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean getFromUserDelete() {
        return fromUserDelete;
    }

    public MessageModel withFromUserDelete(Boolean fromUserDelete) {
        this.setFromUserDelete(fromUserDelete);
        return this;
    }

    public void setFromUserDelete(Boolean fromUserDelete) {
        this.fromUserDelete = fromUserDelete;
    }

    public Boolean getToUserDelete() {
        return toUserDelete;
    }

    public MessageModel withToUserDelete(Boolean toUserDelete) {
        this.setToUserDelete(toUserDelete);
        return this;
    }

    public void setToUserDelete(Boolean toUserDelete) {
        this.toUserDelete = toUserDelete;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public MessageModel withIsDelete(Boolean isDelete) {
        this.setIsDelete(isDelete);
        return this;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public MessageModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public MessageModel withModifyDate(Date modifyDate) {
        this.setModifyDate(modifyDate);
        return this;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getContent() {
        return content;
    }

    public MessageModel withContent(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", contentType=").append(contentType);
        sb.append(", received=").append(received);
        sb.append(", fromUserDelete=").append(fromUserDelete);
        sb.append(", toUserDelete=").append(toUserDelete);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}