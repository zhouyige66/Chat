package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class GroupMessageLogModel implements Serializable {
    private Long id;

    private Long groupMsgId;

    private Long userId;

    private Boolean received;

    private Boolean isDelete;

    private Date createDate;

    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public GroupMessageLogModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupMsgId() {
        return groupMsgId;
    }

    public GroupMessageLogModel withGroupMsgId(Long groupMsgId) {
        this.setGroupMsgId(groupMsgId);
        return this;
    }

    public void setGroupMsgId(Long groupMsgId) {
        this.groupMsgId = groupMsgId;
    }

    public Long getUserId() {
        return userId;
    }

    public GroupMessageLogModel withUserId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getReceived() {
        return received;
    }

    public GroupMessageLogModel withReceived(Boolean received) {
        this.setReceived(received);
        return this;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public GroupMessageLogModel withIsDelete(Boolean isDelete) {
        this.setIsDelete(isDelete);
        return this;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public GroupMessageLogModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public GroupMessageLogModel withModifyDate(Date modifyDate) {
        this.setModifyDate(modifyDate);
        return this;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupMsgId=").append(groupMsgId);
        sb.append(", userId=").append(userId);
        sb.append(", received=").append(received);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}