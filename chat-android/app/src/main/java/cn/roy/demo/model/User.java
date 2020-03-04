package cn.roy.demo.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/12 16:55
 * @Version: v1.0
 */
public class User {
    private String id;

    private String name;

    private String password;

    private String phone;

    private String email;

    @JSONField(name = "createDate", format = "yyyy-MM-dd HH:mm:ss")
    private String registerTime;

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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
