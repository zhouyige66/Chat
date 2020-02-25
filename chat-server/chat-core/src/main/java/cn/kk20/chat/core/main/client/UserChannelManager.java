package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.CommonUtil;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:09 下午
 * @Version: v1.0
 */
@Component
public class UserChannelManager {
    private ConcurrentHashMap<String, UserWrapper> clientList  = new ConcurrentHashMap<>(12);

    @Autowired
    RedisUtil redisUtil;

    public void addClient(UserWrapper userWrapper) {
        String userId = userWrapper.getUserModel().getId();
        UserWrapper existUserWrapper = clientList.get(userId);
        if (null != existUserWrapper) {
            // 已经存在且不是当前通道，则关闭
            Channel channel = existUserWrapper.getChannel();
            if (channel != null && channel.isActive() && channel != userWrapper.getChannel()) {
                channel.closeFuture();
            }
        }
        // 添加到通道容器
        clientList.put(userId, userWrapper);
        // 存储到redis
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, CommonUtil.getHostIp());
    }

    public void removeClient(String userId) {
        clientList.remove(userId);
        redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
    }

    public UserWrapper getClient(String userId) {
        return clientList.get(userId);
    }

}
