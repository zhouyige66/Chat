package cn.kk20.chat.core.main.client;

import cn.kk20.chat.dao.model.UserModel;
import io.netty.channel.Channel;

/**
 * @Description: 客户端相关信息
 * @Author: Roy
 * @Date: 2020/2/16 11:18 上午
 * @Version: v1.0
 */
public class UserWrapper {
    private UserModel userModel;
    private Channel channel;
    private boolean isWebUser;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isWebUser() {
        return isWebUser;
    }

    public void setIsWebUser(boolean webUser) {
        isWebUser = webUser;
    }

}
