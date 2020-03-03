package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class AddFriendLogModel implements Serializable {
    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private Boolean isAgree;

    private Date createDate;

    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public AddFriendLogModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public AddFriendLogModel withFromUserId(Long fromUserId) {
        this.setFromUserId(fromUserId);
        return this;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public AddFriendLogModel withToUserId(Long toUserId) {
        this.setToUserId(toUserId);
        return this;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Boolean getIsAgree() {
        return isAgree;
    }

    public AddFriendLogModel withIsAgree(Boolean isAgree) {
        this.setIsAgree(isAgree);
        return this;
    }

    public void setIsAgree(Boolean isAgree) {
        this.isAgree = isAgree;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public AddFriendLogModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public AddFriendLogModel withModifyDate(Date modifyDate) {
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