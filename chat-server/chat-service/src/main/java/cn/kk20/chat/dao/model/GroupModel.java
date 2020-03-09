package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class GroupModel implements Serializable {
    private Long id;

    private String name;

    private String description;

    private Long creatorId;

    private String managerList;

    private Boolean isDelete;

    private Date createDate;

    private Date modifyDate;

    private String memberList;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public GroupModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public GroupModel withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public GroupModel withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public GroupModel withCreatorId(Long creatorId) {
        this.setCreatorId(creatorId);
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getManagerList() {
        return managerList;
    }

    public GroupModel withManagerList(String managerList) {
        this.setManagerList(managerList);
        return this;
    }

    public void setManagerList(String managerList) {
        this.managerList = managerList;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public GroupModel withIsDelete(Boolean isDelete) {
        this.setIsDelete(isDelete);
        return this;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public GroupModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public GroupModel withModifyDate(Date modifyDate) {
        this.setModifyDate(modifyDate);
        return this;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getMemberList() {
        return memberList;
    }

    public GroupModel withMemberList(String memberList) {
        this.setMemberList(memberList);
        return this;
    }

    public void setMemberList(String memberList) {
        this.memberList = memberList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", managerList=").append(managerList);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", memberList=").append(memberList);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}