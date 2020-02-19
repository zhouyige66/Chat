package cn.kk20.chat.core;

import cn.kk20.chat.core.common.CommonUtil;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.common.RedisUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:09 下午
 * @Version: v1.0
 */
public class ClientManager {
    private static ClientManager instance = null;
    private ConcurrentHashMap<String, ClientWrapper> clientList;

    @Autowired
    RedisUtil redisUtil;

    private ClientManager() {
        clientList = new ConcurrentHashMap<>(16);
    }

    public static ClientManager getInstance() {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new ClientManager();
                }
            }
        }
        return instance;
    }

    public void addClient(ClientWrapper clientWrapper) {
        String userId = clientWrapper.getUserModel().getId();
        ClientWrapper existClientWrapper = clientList.get(userId);
        if (null != existClientWrapper) {
            // 已经存在且不是当前通道，则关闭
            Channel channel = existClientWrapper.getChannel();
            if (channel != null && channel.isActive() && channel != clientWrapper.getChannel()) {
                channel.closeFuture();
            }
        }
        // 添加到通道容器
        clientList.put(userId, clientWrapper);
        // 存储到redis
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, CommonUtil.getHostIp());
    }

    public void removeClient(String userId) {
        clientList.remove(userId);
        redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
    }

    public ClientWrapper getClient(String userId) {
        return clientList.get(userId);
    }

}
