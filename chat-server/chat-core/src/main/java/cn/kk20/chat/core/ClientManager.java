package cn.kk20.chat.core;

import io.netty.channel.Channel;

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
            // 已经存在
            Channel channel = existClientWrapper.getChannel();
            if (channel != null && channel.isActive()) {
                channel.closeFuture();
            }
        }

        // 添加到通道容器
        clientList.put(userId, clientWrapper);
    }

    public void removeClient(String userId) {
        clientList.remove(userId);
    }

    public ClientWrapper getClient(String userId) {
        return clientList.get(userId);
    }

}
