package cn.kk20.chat.client;

import cn.kk20.chat.base.message.login.ClientType;
import cn.kk20.chat.client.config.ChatParameterBean;
import cn.kk20.chat.client.wrapper.UserWrapper;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.util.CommonUtil;
import cn.kk20.chat.core.util.RedisUtil;
import cn.kk20.chat.dao.model.UserModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
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
public class ChannelManager {
    // 客户端通道
    private ConcurrentHashMap<Long, UserWrapper> userWrapperMap = new ConcurrentHashMap<>(12);
    private ConcurrentHashMap<String, Long> channelIdMap = new ConcurrentHashMap<>(12);
    // 中心服务器通道
    private Channel centerChannel;

    @Autowired
    ChatParameterBean chatParameterBean;
    @Autowired
    RedisUtil redisUtil;

    public void add(UserModel userModel, ClientType clientType, Channel channel) {
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
        JSONObject hostJson = getCacheFromRedis(userId);
        if (hostJson == null) {
            hostJson = new JSONObject();
        }
        hostJson.put(clientType.getIdentify(), host);
        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, hostJson.toJSONString());
        redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + host, channelIdMap.size());
    }

    public Long remove(Channel channel) {
        String channelId = channel.id().asShortText();
        Long userId = channelIdMap.get(channelId);
        if (userId == null) {// 之前已删除
            return null;
        }

        UserWrapper userWrapper = userWrapperMap.get(userId);
        if (userWrapper != null) {
            ClientType clientType = userWrapper.removeChannel(channel);
            if (clientType != null) {// 拿到该channel对应的客户端类型
                JSONObject hostJson = getCacheFromRedis(userId);
                if (hostJson != null && hostJson.containsKey(clientType.getIdentify())) {
                    hostJson.remove(clientType.getIdentify());
                    final int size = hostJson.size();
                    if (size == 0) {// 登录的客户端全部下线
                        userWrapperMap.remove(userId);
                        redisUtil.removeStringValue(ConstantValue.HOST_OF_USER + userId);
                    } else {
                        redisUtil.setStringValue(ConstantValue.HOST_OF_USER + userId, hostJson.toJSONString());
                    }
                }
            }
        }
        // 清除引用
        channelIdMap.remove(channelId);
        redisUtil.saveParam(ConstantValue.STATISTIC_OF_HOST + getHost(), channelIdMap.size());
        return userId;
    }

    public void clear() {
        userWrapperMap.clear();
        channelIdMap.clear();
        // 清除当前主机上连接数
        redisUtil.delete(ConstantValue.STATISTIC_OF_HOST + getHost());
    }

    public UserWrapper getClient(Long userId) {
        return userWrapperMap.get(userId);
    }

    public Long getUserIdByChannel(Channel channel) {
        return channelIdMap.get(channel.id().asShortText());
    }

    public String getHost() {
        String host = CommonUtil.getTargetAddress(CommonUtil.getHostIp(),
                chatParameterBean.getCommonServer().getPort(),
                chatParameterBean.getWebServer().getPort());
        return host;
    }

    public JSONObject getCacheFromRedis(Long userId) {
        String hostInfo = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + userId);
        if (StringUtils.isEmpty(hostInfo)) {
            return null;
        }
        JSONObject hostJson = JSON.parseObject(hostInfo);
        return hostJson;
    }

    /**********功能：中心服务器通道**********/
    public Channel getCenterChannel() {
        return centerChannel;
    }

    public void setCenterChannel(Channel centerChannel) {
        this.centerChannel = centerChannel;
    }

}
