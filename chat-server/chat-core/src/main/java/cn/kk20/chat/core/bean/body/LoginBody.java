package cn.kk20.chat.core.bean.body;

import cn.kk20.chat.core.bean.MessageBodyType;

/**
 * @Description: 登录消息体
 * @Author: Roy Z
 * @Date: 2020/2/17 15:57
 * @Version: v1.0
 */
public class LoginBody extends AbstractMessageBody {
    private String userId;
    private String userName;
    private boolean login;

    public LoginBody() {
        super(MessageBodyType.TEXT);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

}
