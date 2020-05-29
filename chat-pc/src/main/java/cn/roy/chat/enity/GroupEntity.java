package cn.roy.chat.enity;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/29 09:42
 * @Version: v1.0
 */
public class GroupEntity {
    /**
     * id : 1
     * name : 研发一组
     * description : 研发一组讨论群，晨会群
     * creatorId : 1
     * managerList :
     * isDelete : false
     * createDate : 2020-03-09T17:20:47.000+0800
     * modifyDate : 2020-03-12T17:45:31.000+0800
     * memberList :
     */

    private int id;
    private String name;
    private String description;
    private int creatorId;
    private String managerList;
    private boolean isDelete;
    private String createDate;
    private String modifyDate;
    private String memberList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getManagerList() {
        return managerList;
    }

    public void setManagerList(String managerList) {
        this.managerList = managerList;
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

    public String getMemberList() {
        return memberList;
    }

    public void setMemberList(String memberList) {
        this.memberList = memberList;
    }
}
