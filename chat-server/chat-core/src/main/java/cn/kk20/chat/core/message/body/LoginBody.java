package cn.kk20.chat.core.message.body;

import cn.kk20.chat.core.message.MessageBodyType;

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
