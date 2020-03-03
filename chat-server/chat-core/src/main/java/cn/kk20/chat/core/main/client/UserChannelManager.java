package cn.kk20.chat.core.main.client;

import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.config.ChatConfigBean;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.CommonUtil;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;
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
    private ConcurrentHashMap<Long, UserWrapper> userMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<SocketAddress, Long> hostUserMap = new ConcurrentHashMap<>(12);
    // 中心服务器通道
    private Channel centerChannel;

    @Autowired
    ChatConfigBean chatConfigBean;
    @Autowired
    RedisUtil redisUtil;

    public void addClient(UserWrapper userWrapper) {
        Long userId = userWrapper.getUserModel().getId();
        UserWrapper existUserWrapper = userMap.get(userId);
        if (null != existUserWrapper) {
            // 已经存在且不是当前通道，则关闭
            Channel channel = existUserWrapper.getChannel();
            if (channel != null && channel.isActive() && channel != userWrapper.getChannel()) {
                channel.closeFuture();
            }
            hostUserMap.remove(channel.remoteAddress());
        }
        // 添加到通道容器
        userMap.put(userId, userWrapper);
        SocketAddress socketAddress = userWrapper.getChannel().remoteAddress();
        hostUserMap.put(socketAddress,userId);
        // 存储到redis
        String host = CommonUtil.getTargetAddress(CommonUtil.getHostIp(),
                chatConfigBean.getClient().getCommonServer().getPort(),
                chatConfigBean.getClient().getWebServer().getPort());
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, host);
    }

    public void removeClient(Long userId) {
        UserWrapper remove = userMap.remove(userId);
        hostUserMap.remove(remove.getChannel().remoteAddress());
        redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
    }

    public UserWrapper getClient(Long userId) {
        return userMap.get(userId);
    }

    public Long getUserId(SocketAddress socketAddress){
        return hostUserMap.get(socketAddress);
    }
    
    /**********功能：服务器通道**********/
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
