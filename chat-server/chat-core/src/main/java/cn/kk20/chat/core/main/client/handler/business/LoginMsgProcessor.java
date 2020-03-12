package cn.kk20.chat.core.main.client.handler.business;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.MessageBody;
import cn.kk20.chat.base.message.data.LoginData;
import cn.kk20.chat.base.message.data.TextData;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.main.client.MessageSender;
import cn.kk20.chat.core.main.client.UserChannelManager;
import cn.kk20.chat.core.main.client.wrapper.UserWrapper;
import cn.kk20.chat.core.util.RedisUtil;
import cn.kk20.chat.dao.model.UserModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 登录登出处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgProcessor(messageType = ChatMessageType.LOGIN)
public class LoginMsgProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LoginMsgProcessor.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserChannelManager userChannelManager;
    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        MessageBody body = chatMessage.getBody();
        Serializable data = body.getData();
        LoginData loginData = null;
        try {
            loginData = JSON.parseObject(JSON.toJSONString(data), LoginData.class);
        } catch (Exception e) {
            logger.error("解析登录消息出错:{}", channelHandlerContext.channel().remoteAddress());
            return;
        }

        Long fromUserId = chatMessage.getFromUserId();
        UserModel userModel = new UserModel();
        userModel.setId(loginData.getUserId());
        userModel.setName(loginData.getUserName());
        // 交由客户端管理器处理
        boolean login = loginData.isLogin();
        if (login) {
            UserWrapper wrapper = new UserWrapper();
            wrapper.setChannel(channelHandlerContext.channel());
            wrapper.setUserModel(userModel);
            wrapper.setIsWebUser(isFromWeb);
            userChannelManager.addClient(wrapper);
        } else {
            userChannelManager.removeClient(fromUserId);
        }

        // 查询好友列表
        Set userIdSet = redisUtil.getSetValue(ConstantValue.FRIEND_OF_USER + fromUserId);
        if (userIdSet.isEmpty()) {
            return;
        }
        HashMap<Long, String> onlineFriendConnectHostMap = new HashMap<>(userIdSet.size());
        // 查询在线好友
        for (Object id : userIdSet) {
            String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + id);
            if (!StringUtils.isEmpty(host)) {
                if (id instanceof Integer) {
                    Integer i = (Integer) id;
                    onlineFriendConnectHostMap.put(i.longValue(), host);
                } else {
                    Long l = (Long) id;
                    onlineFriendConnectHostMap.put(l, host);
                }
            }
        }
        if (login) {
            // 回复当前登录用户，好友在线名单
            TextData textData = new TextData(JSON.toJSONString(onlineFriendConnectHostMap.keySet()));
            ChatMessage replyMessage = new ChatMessage();
            replyMessage.setMessageType(ChatMessageType.LOGIN);
            replyMessage.setFromUserId(ConstantValue.SERVER_ID);
            replyMessage.setToUserId(fromUserId);
            replyMessage.setBodyData(textData);
            messageSender.sendMessage(channelHandlerContext.channel(), replyMessage);
        }

        // 通知好友，用户登录或登出了，这里仅通知在线好友，因为不在线的好友没必要通知
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", fromUserId);
        map.put("login", login);
        TextData textData = new TextData(JSON.toJSONString(map));

        ChatMessage notifyMsg = new ChatMessage();
        BeanUtils.copyProperties(chatMessage, notifyMsg);
        notifyMsg.setMessageType(ChatMessageType.LOGIN_NOTIFY);
        notifyMsg.setFromUserId(ConstantValue.SERVER_ID);
        notifyMsg.setBodyData(textData);
        for (Long id : onlineFriendConnectHostMap.keySet()) {
            messageSender.sendMessage(id, notifyMsg);
        }
    }

}
