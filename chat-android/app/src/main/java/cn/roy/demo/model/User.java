package cn.roy.demo.model;

import java.util.Date;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/12 16:55
 * @Version: v1.0
 */
public class User {
    private String id;

    private String password;

    private String name;

    private String nickName;

    private String phone;

    private String email;

    private Date createTime;

    private Date modifyTime;

    private Boolean del;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public User withId(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public User withPassword(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public User withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public User withNickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public User withPhone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public User withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public User withCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public User withModifyTime(Date modifyTime) {
        this.setModifyTime(modifyTime);
        return this;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Boolean getDel() {
        return del;
    }

    public User withDel(Boolean del) {
        this.setDel(del);
        return this;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }
}
