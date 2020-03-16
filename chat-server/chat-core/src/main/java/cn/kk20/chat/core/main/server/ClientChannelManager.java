package cn.kk20.chat.core.main.server;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.ServerComponent;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ConcurrentHashMap<String, String> channelMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<String, Integer> channelFailCountMap = new ConcurrentHashMap<>(12);

    @Autowired
    RedisUtil redisUtil;

    public void addClient(String clientId, Channel channel) {
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

        cacheServer();
    }

    public void removeClient(Channel channel) {
        String channelId = channel.id().asShortText();
        String clientId = channelMap.get(channelId);
        if (!StringUtils.isEmpty(clientId)) {
            clientMap.remove(clientId);
        }
        channelMap.remove(channelId);
        cacheServer();
    }

    public Channel getClient(String clientId) {
        return clientMap.get(clientId);
    }

    private void cacheServer() {
        // 更新当前可服务主机地址列表
        redisUtil.saveParam(ConstantValue.LIST_OF_SERVER, JSON.toJSONString(clientMap.keys()));
        logger.info("客户端：{}", JSON.toJSONString(clientMap));
        logger.info("通道：{}", JSON.toJSONString(channelMap));
    }

    public void channelHeartFailReset(Channel channel) {
        String channelId = channel.id().asShortText();
        Integer integer = 0;
        channelFailCountMap.put(channelId, integer);
    }

    public int channelHeartFail(Channel channel) {
        String channelId = channel.id().asShortText();
        Integer integer = channelFailCountMap.get(channelId);
        if (integer == null) {
            integer = 0;
        }
        integer++;
        channelFailCountMap.put(channelId, integer);
        return integer;
    }

}
