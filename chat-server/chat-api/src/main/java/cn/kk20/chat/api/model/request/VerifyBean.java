package cn.kk20.chat.api.model.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/5 13:13
 * @Version: v1.0
 */
public class VerifyBean {
    @ApiModelProperty(value = "申请ID")
    private Long applyId;
    @ApiModelProperty(value = "审批人")
    private Long verifyUserId;
    @ApiModelProperty(value = "是否同意")
    private Boolean isAgree;
    @ApiModelProperty(value = "备注")
    private String remark;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(Long verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
