package cn.kk20.chat.api.model.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/5 13:13
 * @Version: v1.0
 */
public class ApplyBean {
    @ApiModelProperty(value = "申请类型：0-加好友，1-加群")
    private Integer type;
    @ApiModelProperty(value = "申请人")
    private Long applyUserId;
    @ApiModelProperty(value = "添加目标（好友或群）")
    private Long targetUserId;
    @ApiModelProperty(value = "备注")
    private String remark;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
