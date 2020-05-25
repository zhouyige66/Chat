package cn.roy.chat.enity;

/**
 * @Description: 登录实体
 * @Author: Roy Z
 * @Date: 5/25/2020 10:39 AM
 * @Version: v1.0
 */
public class LoginEntity {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
