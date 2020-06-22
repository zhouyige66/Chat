package cn.kk20.chat.core.main.client;

import cn.kk20.chat.base.message.login.ClientType;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.CommonUtil;
import cn.kk20.chat.core.util.RedisUtil;
import cn.kk20.chat.dao.model.UserModel;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端管理器
 * @Author: Roy
 * @Date: 2020/2/16 6:09 下午
 * @Version: v1.0
 */
@ClientComponent
public class ChannelManager {
    // 客户端通道
    private ConcurrentHashMap<Long, UserWrapper> userWrapperMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<String, Long> channelIdMap = new ConcurrentHashMap<>(12);
    // 中心服务器通道
    private Channel centerChannel;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    RedisUtil redisUtil;

    public void cache(UserModel userModel, ClientType clientType, Channel channel) {
        Long userId = userModel.getId();
        UserWrapper userWrapper;
        if (userWrapperMap.contains(userId)) {
            userWrapper = userWrapperMap.get(userId);
            // 已经存在且不是当前通道，则关闭
            Channel clientChannel = userWrapper.getChannel(clientType);
            if (clientChannel != null && clientChannel != channel) {
                clientChannel.closeFuture();
                String channelId = clientChannel.id().asShortText();
                channelIdMap.remove(channelId);
            }
            userWrapper.addChannel(clientType, channel);
        } else {
            userWrapper = new UserWrapper();
            userWrapper.setUserModel(userModel);
            userWrapper.addChannel(clientType, channel);
            userWrapperMap.put(userId, userWrapper);
        }
        String channelId = channel.id().asShortText();
        channelIdMap.put(channelId, userId);
        // 存储到redis，更新当前主机的连接数据量
        String host = getHost();
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, host);
        redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + host, userWrapperMap.size());
    }

    public Long remove(Channel channel) {
        String channelId = channel.id().asShortText();
        Long userId = channelIdMap.get(channelId);
        if (userId != null) {
            UserWrapper userWrapper = userWrapperMap.remove(userId);
            if (userWrapper != null) {
                userWrapper.removeChannel(channel);
            }
            // 清除引用
            userWrapperMap.remove(channelId);
            redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
            redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + getHost(), userWrapperMap.size());
            return userId;
        }
        return null;
    }

    public UserWrapper getClient(Long userId) {
        return userWrapperMap.get(userId);
    }

    public void clear() {
        userWrapperMap.clear();
        channelIdMap.clear();
        // 清除当前主机上连接数
        redisUtil.delete(ConstantValue.STATISTIC_OF_HOST + getHost());
    }

    private String getHost() {
        String host = CommonUtil.getTargetAddress(CommonUtil.getHostIp(),
                chatConfigBean.getClient().getCommonServer().getPort(),
                chatConfigBean.getClient().getWebServer().getPort());
        return host;
    }

    /**********功能：中心服务器通道**********/
    public Channel getCenterChannel() {
        return centerChannel;
    }

    public void setCenterChannel(Channel centerChannel) {
        this.centerChannel = centerChannel;
    }

}
