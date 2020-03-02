package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class GroupMessageLogModel implements Serializable {
    private Long id;

    private Long msgId;

    private Long operator;

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

    public Long getOperator() {
        return operator;
    }

    public GroupMessageLogModel withOperator(Long operator) {
        this.setOperator(operator);
        return this;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
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
        sb.append(", operator=").append(operator);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}