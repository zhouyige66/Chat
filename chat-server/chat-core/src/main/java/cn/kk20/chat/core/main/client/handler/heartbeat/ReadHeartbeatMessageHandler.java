package cn.kk20.chat.core.main.client.handler.heartbeat;

import cn.kk20.chat.base.message.HeartbeatMessage;
import cn.kk20.chat.base.message.NotifyMessage;
import cn.kk20.chat.base.message.notify.NotifyMessageType;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.main.client.ChannelManager;
import cn.kk20.chat.core.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ClientComponent
@ChannelHandler.Sharable
public class ReadHeartbeatMessageHandler extends SimpleChannelInboundHandler<HeartbeatMessage> {
    private Logger logger = LoggerFactory.getLogger(ReadHeartbeatMessageHandler.class);
    private final int heartFailMax = 5;
    private Map<String, Integer> heartFailMap = new HashMap<>(12);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ChannelManager channelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage heartbeatMessage) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.id().asShortText();
        // 收到任何消息，均把心跳失败数置零
        heartFailMap.put(channelId, 0);
        // 原样返回心跳信息
        messageSender.sendMessage(channel, heartbeatMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                String channelId = channel.id().asShortText();
                Integer integer = heartFailMap.get(channelId);
                if (integer == null) {
                    integer = 0;
                }
                integer++;
                hasDeal = true;
                if (integer > heartFailMax) {
                    logger.info("客户端：{}，心跳失败，关闭通道", channelId);
                    cleanWork(channel);
                } else {
                    heartFailMap.put(channelId, integer);
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cleanWork(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 客户端异常退出，清理数据
        logger.error("通信通道出现异常：{}", cause);
        cleanWork(ctx.channel());
    }

    private void cleanWork(Channel channel) {
        heartFailMap.remove(channel.id().asShortText());
        Long userId = channelManager.remove(channel);
        if (userId == null) {
            return;
        }
        // 通知好友，该用户下线了
        notifyFriend(userId);
        channel.close();
    }

    private void notifyFriend(Long userId) {
        if (null == userId) {
            return;
        }
        // 查询好友列表
        Set<Long> userIdSet = redisUtil.getLongSetValue(ConstantValue.FRIEND_OF_USER + userId);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        Map<Long, String> onlineFriendMap = new HashMap<>(userIdSet.size());
        // 查询在线好友
        for (Long id : userIdSet) {
            String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + id);
            if (!StringUtils.isEmpty(host)) {
                onlineFriendMap.put(id, host);
            }
        }
        // 通知在线好友，用户掉线了
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("login", false);
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setNotifyMessageType(NotifyMessageType.LOGIN_NOTIFY);
        notifyMessage.setData(map);
        for (Long friendId : onlineFriendMap.keySet()) {
            messageSender.sendMessage(friendId, notifyMessage);
        }
    }

}
