package cn.kk20.chat.center;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:09 下午
 * @Version: v1.0
 */
@Component
public class ClientChannelManager {
    private final Logger logger = LoggerFactory.getLogger(ClientChannelManager.class);
    private ConcurrentHashMap<String, Channel> clientMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<String, String> channelMap = new ConcurrentHashMap<>(12);

    @Autowired
    RedisUtil redisUtil;

    public void cacheChannel(String clientId, Channel channel) {
        Channel existChannel = clientMap.get(clientId);
        if (existChannel == channel) {
            return;
        }
        // 通道不一致，关闭原通道
        if (existChannel != null && existChannel.isActive()) {
            channel.closeFuture();
        }
        // 添加到通道容器
        clientMap.put(clientId, channel);
        channelMap.put(channel.id().asShortText(), clientId);
        updateNettyServer();
    }

    public void removeChannel(String channelId) {
        String clientId = channelMap.get(channelId);
        if (!StringUtils.isEmpty(clientId)) {
            clientMap.remove(clientId);
        }
        channelMap.remove(channelId);
        updateNettyServer();
    }

    public Channel getChannel(String clientId) {
        return clientMap.get(clientId);
    }

    private void updateNettyServer() {
        // 更新当前可服务主机地址列表
        redisUtil.saveParam(ConstantValue.LIST_OF_SERVER, JSON.toJSONString(clientMap.keys()));
        logger.info("客户端：{}", JSON.toJSONString(clientMap));
        logger.info("通道：{}", JSON.toJSONString(channelMap));
    }

}
