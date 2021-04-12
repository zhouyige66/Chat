package cn.kk20.chat.dao.model.plus;

import cn.kk20.chat.dao.model.ApplyLogModel;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2021/04/12
 * @Version: v1.0
 */
public class ApplyLogPlusModel extends ApplyLogModel {
    private String applyUserName;
    private String targetUserName;
    private String verifyUserName;

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getVerifyUserName() {
        return verifyUserName;
    }

    public void setVerifyUserName(String verifyUserName) {
        this.verifyUserName = verifyUserName;
    }
}
