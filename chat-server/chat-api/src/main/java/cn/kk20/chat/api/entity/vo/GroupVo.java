package cn.kk20.chat.api.entity.vo;

import java.util.Date;
import java.util.List;

/**
 * @Description: 群组vo
 * @Author: Roy Z
 * @Date: 7/7/2020 4:25 PM
 * @Version: v1.0
 */
public class GroupVo {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private String managerList;
    private Boolean isDelete;
    private Date createDate;
    private Date modifyDate;
    private List<UserVo> memberList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getManagerList() {
        return managerList;
    }

    public void setManagerList(String managerList) {
        this.managerList = managerList;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public List<UserVo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<UserVo> memberList) {
        this.memberList = memberList;
    }

}
