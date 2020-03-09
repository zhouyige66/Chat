package cn.kk20.chat.dao.model;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable {
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String password;

    private String head;

    private Boolean isDelete;

    private Date createDate;

    private Date modifyDate;

    private String friendList;

    private String groupList;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public UserModel withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserModel withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public UserModel withPhone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public UserModel withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public UserModel withPassword(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHead() {
        return head;
    }

    public UserModel withHead(String head) {
        this.setHead(head);
        return this;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public UserModel withIsDelete(Boolean isDelete) {
        this.setIsDelete(isDelete);
        return this;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public UserModel withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public UserModel withModifyDate(Date modifyDate) {
        this.setModifyDate(modifyDate);
        return this;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getFriendList() {
        return friendList;
    }

    public UserModel withFriendList(String friendList) {
        this.setFriendList(friendList);
        return this;
    }

    public void setFriendList(String friendList) {
        this.friendList = friendList;
    }

    public String getGroupList() {
        return groupList;
    }

    public UserModel withGroupList(String groupList) {
        this.setGroupList(groupList);
        return this;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", head=").append(head);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", friendList=").append(friendList);
        sb.append(", groupList=").append(groupList);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}