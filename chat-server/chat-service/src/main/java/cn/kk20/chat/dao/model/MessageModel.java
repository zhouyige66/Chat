package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class MessageModel implements Serializable {
    private String id;

    private String fromUserId;

    private String toUserId;

    private String content;

    private Date sendTime;

    private Byte status;

    private Boolean fromUserDelete;

    private Boolean toUserDelete;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public MessageModel withId(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public MessageModel withFromUserId(String fromUserId) {
        this.setFromUserId(fromUserId);
        return this;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public MessageModel withToUserId(String toUserId) {
        this.setToUserId(toUserId);
        return this;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
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

    public Date getSendTime() {
        return sendTime;
    }

    public MessageModel withSendTime(Date sendTime) {
        this.setSendTime(sendTime);
        return this;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getStatus() {
        return status;
    }

    public MessageModel withStatus(Byte status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", content=").append(content);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", status=").append(status);
        sb.append(", fromUserDelete=").append(fromUserDelete);
        sb.append(", toUserDelete=").append(toUserDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}