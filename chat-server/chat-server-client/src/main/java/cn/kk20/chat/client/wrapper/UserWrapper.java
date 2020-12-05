package cn.kk20.chat.client.wrapper;

import cn.kk20.chat.base.message.login.ClientType;
import cn.kk20.chat.dao.model.UserModel;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 客户端相关信息
 * @Author: Roy
 * @Date: 2020/2/16 11:18 上午
 * @Version: v1.0
 */
public class UserWrapper {
    private UserModel userModel;
    private Map<ClientType, Channel> channelMap;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Long getUserId() {
        if (userModel == null) {
            return null;
        }
        return userModel.getId();
    }

    public void addChannel(ClientType clientType, Channel channel) {
        if (channelMap == null) {
            channelMap = new HashMap<>(4);
        }
        channelMap.put(clientType, channel);
    }

    public ClientType removeChannel(Channel channel) {
        if (channelMap == null) {
            return null;
        }

        ClientType clientType = null;
        final Set<Map.Entry<ClientType, Channel>> entries = channelMap.entrySet();
        for (Map.Entry<ClientType, Channel> entry : entries) {
            if (entry.getValue() == channel) {
                clientType = entry.getKey();
                channelMap.remove(entry.getKey());
                break;
            }
        }
        return clientType;
    }

    public Channel getChannel(ClientType clientType) {
        if (channelMap != null) {
            return channelMap.get(clientType);
        }
        return null;
    }

    public Map<ClientType, Channel> getChannelMap() {
        return channelMap;
    }

    public boolean isOnline() {
        if (channelMap == null) {
            return false;
        }
        return channelMap.keySet().size() > 0;
    }
}
