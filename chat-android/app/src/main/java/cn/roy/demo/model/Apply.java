package cn.roy.demo.model;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/10 16:27
 * @Version: v1.0
 */
public class Apply {
    /**
     * id : 7
     * type : 0
     * applyUserId : 1
     * targetUserId : 5
     * verifyUserId : 5
     * isAgree : null
     * applyRemark : 你好
     * verifyRemark : null
     * isDelete : false
     * createDate : 2020-03-10T16:27:24.000+0800
     * modifyDate : 2020-03-10T16:27:24.000+0800
     */

    private int id;
    private int type;
    private int applyUserId;
    private int targetUserId;
    private int verifyUserId;
    private Object isAgree;
    private String applyRemark;
    private Object verifyRemark;
    private boolean isDelete;
    private String createDate;
    private String modifyDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(int applyUserId) {
        this.applyUserId = applyUserId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(int verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public Object getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(Object isAgree) {
        this.isAgree = isAgree;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public Object getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(Object verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
}
