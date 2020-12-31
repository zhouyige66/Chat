package cn.kk20.chat.api.entity.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/3/5 11:29
 * @Version: v1.0
 */
public class LoginBean {
    @ApiModelProperty(value = "用户名\\电话\\邮箱")
    private String userName;
    @ApiModelProperty(value = "用户密码")
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
