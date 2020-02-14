package cn.roy.demo.chat.message;

public class LoginBody {
    private String userId;
    private String userName;
    private boolean login;

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

    @Override
    public String toString() {
        return "LoginBody{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", login=" + login +
                '}';
    }
}
