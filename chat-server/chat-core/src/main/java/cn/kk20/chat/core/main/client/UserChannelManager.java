package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.CommonUtil;
import cn.kk20.chat.core.util.RedisUtil;
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
public class UserChannelManager {
    // 客户端通道
    private ConcurrentHashMap<Long, UserWrapper> userWrapperMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<String, Long> channelIdMap = new ConcurrentHashMap<>(12);
    // 中心服务器通道
    private Channel centerChannel;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    RedisUtil redisUtil;

    public void cache(UserWrapper userWrapper) {
        Long userId = userWrapper.getUserModel().getId();
        if (userWrapperMap.contains(userId)) {
            UserWrapper existUserWrapper = userWrapperMap.get(userId);
            // 已经存在且不是当前通道，则关闭
            Channel channel = existUserWrapper.getChannel();
            if (channel != null && channel.isActive() && channel != userWrapper.getChannel()) {
                channel.closeFuture();
            }
            String channelId = channel.id().asShortText();
            channelIdMap.remove(channelId);
        }
        // 添加到通道容器
        userWrapperMap.put(userId, userWrapper);
        Channel channel = userWrapper.getChannel();
        String channelId = channel.id().asShortText();
        channelIdMap.put(channelId, userId);
        // 存储到redis，更新当前主机的连接数据量
        String host = getHost();
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, host);
        redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + host, userWrapperMap.size());
    }

    private String getHost() {
        String host = CommonUtil.getTargetAddress(CommonUtil.getHostIp(),
                chatConfigBean.getClient().getCommonServer().getPort(),
                chatConfigBean.getClient().getWebServer().getPort());
        return host;
    }

    public void remove(Long userId) {
        if(userId==null){
            return;
        }
        if (!userWrapperMap.contains(userId)) {
            return;
        }
        UserWrapper remove = userWrapperMap.remove(userId);
        channelIdMap.remove(remove.getChannel().id().asShortText());
        redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
        redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + getHost(), userWrapperMap.size());
    }

    public UserWrapper getClient(Long userId) {
        return userWrapperMap.get(userId);
    }

    public Long getUserId(Channel channel) {
        String channelId = channel.id().asShortText();
        return channelIdMap.get(channelId);
    }

    public void clear() {
        userWrapperMap.clear();
        channelIdMap.clear();
        // 清除当前主机上连接数
        redisUtil.delete(ConstantValue.STATISTIC_OF_HOST + getHost());
    }

    /**********功能：中心服务器通道**********/
    public Channel getCenterChannel() {
        return centerChannel;
    }

    public void setCenterChannel(Channel centerChannel) {
        this.centerChannel = centerChannel;
    }

    public void removeCenterChannel() {
        this.centerChannel = null;
    }

}
