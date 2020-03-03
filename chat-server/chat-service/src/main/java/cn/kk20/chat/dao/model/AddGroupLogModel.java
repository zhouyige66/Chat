package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class AddGroupLogModel implements Serializable {
    private Long id;

    private Long groupId;

    private Long fromUserId;

    private Long toUserId;

    private Boolean isAgree;

    private Date createDate;

    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public AddGroupLogModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public AddGroupLogModel withGroupId(Long groupId) {
        this.setGroupId(groupId);
        return this;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public AddGroupLogModel withFromUserId(Long fromUserId) {
        this.setFromUserId(fromUserId);
        return this;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public AddGroupLogModel withToUserId(Long toUserId) {
        this.setToUserId(toUserId);
        return this;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Boolean getIsAgree() {
        return isAgree;
    }

    public AddGroupLogModel withIsAgree(Boolean isAgree) {
        this.setIsAgree(isAgree);
        return this;
    }

    public void setIsAgree(Boolean isAgree) {
        this.isAgree = isAgree;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public AddGroupLogModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public AddGroupLogModel withModifyDate(Date modifyDate) {
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
        sb.append(", groupId=").append(groupId);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", isAgree=").append(isAgree);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}