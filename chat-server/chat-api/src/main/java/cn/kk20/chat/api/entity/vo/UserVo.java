package cn.kk20.chat.api.entity.vo;

import java.util.Date;

/**
 * @Description: 用户信息vo
 * @Author: Roy Z
 * @Date: 7/7/2020 4:44 PM
 * @Version: v1.0
 */
public class UserVo {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String head;
    private Boolean isDelete;
    private Date createDate;
    private Date modifyDate;
    private String friendList;
    private String groupList;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean delete) {
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

    public String getFriendList() {
        return friendList;
    }

    public void setFriendList(String friendList) {
        this.friendList = friendList;
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }
}
