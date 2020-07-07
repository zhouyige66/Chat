package cn.kk20.chat.api.entity.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/5 13:48
 * @Version: v1.0
 */
public class RegisterBean {
    /**
     * email : string
     * name : string
     * password : string
     * phone : string
     */
    @ApiModelProperty(value = "用户名",required = true)
    private String name;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "注册电话")
    private String phone;
    @ApiModelProperty(value = "注册邮箱")
    private String email;

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
