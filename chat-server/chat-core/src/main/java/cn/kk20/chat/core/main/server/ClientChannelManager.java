package cn.kk20.chat.core.main.server;

import cn.kk20.chat.core.main.ServerComponent;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:09 下午
 * @Version: v1.0
 */
@ServerComponent
public class ClientChannelManager {
    private final Logger logger = LoggerFactory.getLogger(ClientChannelManager.class);

    private ConcurrentHashMap<String, Channel> clientMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<Channel, String> channelMap = new ConcurrentHashMap<>(12);

    public void addClient(String clientId, Channel channel) {
        Channel existChannel = clientMap.get(clientId);
        if (channel == existChannel) {
            return;
        }
        // 通道不一致，关闭原通道
        if (existChannel != null && existChannel.isActive()) {
            channel.closeFuture();
        }
        // 添加到通道容器
        clientMap.put(clientId, channel);
        channelMap.put(channel, clientId);
        log();
    }

    public void removeClient(Channel channel) {
        String clientId = channelMap.get(channel);
        if (!StringUtils.isEmpty(clientId)) {
            clientMap.remove(clientId);
        }
        channelMap.remove(channel);
        log();
    }

    public Channel getClient(String clientId) {
        return clientMap.get(clientId);
    }

    private void log() {
        logger.debug("客户端：{}", JSON.toJSONString(clientMap));
        logger.debug("通道：{}", JSON.toJSONString(channelMap));
    }

}
