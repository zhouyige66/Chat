package cn.kk20.chat.core.main.client.handler.common;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.data.TextData;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.ClientComponent;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.main.client.UserChannelManager;
import cn.kk20.chat.core.util.RedisUtil;
import com.alibaba.fastjson.JSON;
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
import java.util.Set;

/**
 * @Description: 读心跳处理器
 * @Author: Roy
 * @Date: 2019/1/21 15:31
 * @Version: v1.0
 */
@ClientComponent
@ChannelHandler.Sharable
public class ClientHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(ClientHeartbeatHandler.class);
    private final int heartFailMax = 5;
    private volatile int heartFailCount = 0;

    @Autowired
    UserChannelManager userChannelManager;
    @Autowired
    MessageSender messageSender;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        // 收到任何消息，均把心跳失败数置零
        heartFailCount = 0;

        if (obj instanceof ChatMessage || obj instanceof String) {
            ChatMessage chatMessage;
            if (obj instanceof ChatMessage) {
                chatMessage = (ChatMessage) obj;
            } else {
                try {
                    chatMessage = JSON.parseObject((String) obj, ChatMessage.class);
                } catch (Exception e) {
                    logger.error("客户端：{}，数据转换出错", ctx.channel().remoteAddress());
                    return;
                }
            }

            if (chatMessage.getMessageType() == ChatMessageType.HEARTBEAT) {
                messageSender.sendMessage(ctx.channel(), chatMessage);
            } else {// 向下层传递
                ctx.fireChannelRead(chatMessage);
            }
        } else {
            ctx.fireChannelRead(obj);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        boolean hasDeal = false;
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                heartFailCount++;
                logger.debug("客户端：{}，心跳消息读失败：{}次", ctx.channel().remoteAddress(), heartFailCount);
                hasDeal = true;
                if (heartFailCount > heartFailMax) {
                    heartFailCount = 0;
                    heartbeatFail(ctx);
                }
            }
        }

        if (!hasDeal) {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void heartbeatFail(ChannelHandlerContext ctx) {
        logger.debug("客户端：{}，读超时，关闭通道", ctx.channel().remoteAddress());
        Channel channel = ctx.channel();
        Long userId = userChannelManager.getUserId(channel.remoteAddress());
        userChannelManager.removeClient(userId);
        // 通知好友，该用户下线了
        notifyFriend(userId);
        // 关闭通道
        ctx.close();
    }

    private void notifyFriend(Long userId) {
        if (null == userId) {
            return;
        }
        // 查询好友列表
        Set<Long> userIdSet = redisUtil.getSetValue(ConstantValue.FRIEND_OF_USER + userId);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        HashMap<Long, String> onlineFriendMap = new HashMap<>(userIdSet.size());
        // 查询在线好友
        for (Long id : userIdSet) {
            String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + id);
            if (!StringUtils.isEmpty(host)) {
                onlineFriendMap.put(id, host);
            }
        }

        // 通知好友，用户登录或登出了，这里仅通知在线好友，因为不在线的好友没必要通知
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("login", false);
        TextData textData = new TextData();
        textData.setText(JSON.toJSONString(map));
        ChatMessage notifyMsg = new ChatMessage();
        notifyMsg.setFromUserId(ConstantValue.SERVER_ID);
        notifyMsg.setMessageType(ChatMessageType.LOGIN_NOTIFY);
        notifyMsg.setBodyData(textData);
        for (Long targetId : userIdSet) {
            messageSender.sendMessage(targetId, notifyMsg);
        }
    }

}
