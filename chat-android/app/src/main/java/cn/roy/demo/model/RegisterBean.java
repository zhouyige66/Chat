package cn.roy.demo.model;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-09 10:44
 * @Version: v1.0
 */
public class RegisterBean {
    /**
     * email : string
     * name : string
     * password : string
     * phone : string
     */

    private String email;
    private String name;
    private String password;
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
