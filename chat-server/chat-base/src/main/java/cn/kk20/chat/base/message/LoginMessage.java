package cn.kk20.chat.base.message;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/13 09:06
 * @Version: v1.0
 */
public class LoginMessage extends Message {
    private Long userId;
    private String userName;
    private String device;
    private String location;
    private boolean login;

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
