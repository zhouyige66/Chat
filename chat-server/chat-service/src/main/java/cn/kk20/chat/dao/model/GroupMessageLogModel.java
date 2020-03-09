package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class GroupMessageLogModel implements Serializable {
    private Long id;

    private Long msgId;

    private Long operatorId;

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

    public Long getMsgId() {
        return msgId;
    }

    public GroupMessageLogModel withMsgId(Long msgId) {
        this.setMsgId(msgId);
        return this;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public GroupMessageLogModel withOperatorId(Long operatorId) {
        this.setOperatorId(operatorId);
        return this;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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
        sb.append(", msgId=").append(msgId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}