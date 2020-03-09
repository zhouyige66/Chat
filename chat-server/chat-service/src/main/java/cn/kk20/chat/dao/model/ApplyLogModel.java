package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class ApplyLogModel implements Serializable {
    public static final int APPLY_TYPE_ADD_FRIEND = 0;
    public static final int APPLY_TYPE_ADD_GROUP = 1;

    private Long id;

    private Integer type;

    private Long applyUserId;

    private Long targetUserId;

    private Long verifyUserId;

    private Boolean isAgree;

    private String applyRemark;

    private String verifyRemark;

    private Boolean isDelete;

    private Date createDate;

    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public ApplyLogModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public ApplyLogModel withType(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public ApplyLogModel withApplyUserId(Long applyUserId) {
        this.setApplyUserId(applyUserId);
        return this;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public ApplyLogModel withTargetUserId(Long targetUserId) {
        this.setTargetUserId(targetUserId);
        return this;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Long getVerifyUserId() {
        return verifyUserId;
    }

    public ApplyLogModel withVerifyUserId(Long verifyUserId) {
        this.setVerifyUserId(verifyUserId);
        return this;
    }

    public void setVerifyUserId(Long verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public Boolean getIsAgree() {
        return isAgree;
    }

    public ApplyLogModel withIsAgree(Boolean isAgree) {
        this.setIsAgree(isAgree);
        return this;
    }

    public void setIsAgree(Boolean isAgree) {
        this.isAgree = isAgree;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public ApplyLogModel withApplyRemark(String applyRemark) {
        this.setApplyRemark(applyRemark);
        return this;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public ApplyLogModel withVerifyRemark(String verifyRemark) {
        this.setVerifyRemark(verifyRemark);
        return this;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public ApplyLogModel withIsDelete(Boolean isDelete) {
        this.setIsDelete(isDelete);
        return this;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public ApplyLogModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public ApplyLogModel withModifyDate(Date modifyDate) {
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
        sb.append(", type=").append(type);
        sb.append(", applyUserId=").append(applyUserId);
        sb.append(", targetUserId=").append(targetUserId);
        sb.append(", verifyUserId=").append(verifyUserId);
        sb.append(", isAgree=").append(isAgree);
        sb.append(", applyRemark=").append(applyRemark);
        sb.append(", verifyRemark=").append(verifyRemark);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}