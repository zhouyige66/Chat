package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.main.client.UserChannelManager;
import cn.kk20.chat.core.main.client.UserWrapper;
import cn.kk20.chat.core.main.MessageSender;
import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.body.LoginBody;
import cn.kk20.chat.base.message.body.MessageBody;
import cn.kk20.chat.base.message.body.TextBody;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.core.common.RedisUtil;
import cn.kk20.chat.dao.model.UserModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;

/**
 * @Description: 登录登出处理器
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgProcessor(messageType = ChatMessageType.LOGIN)
public class LoginMsgProcessor implements MessageProcessor {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MessageSender messageSender;

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        MessageBody body = chatMessage.getBody();
        if (!(body instanceof LoginBody)) {
            LogUtil.log("解析登录消息出错");
            return;
        }

        String fromUserId = chatMessage.getFromUserId();
        LoginBody loginBody = (LoginBody) body;
        boolean login = loginBody.isLogin();
        UserModel userModel = new UserModel();
        userModel.setId(loginBody.getUserId());
        userModel.setName(loginBody.getUserName());

        // 交由客户端管理器处理
        if (login) {
            UserWrapper wrapper = new UserWrapper();
            wrapper.setChannel(channelHandlerContext.channel());
            wrapper.setUserModel(userModel);
            wrapper.setIsWebUser(isFromWeb);
            UserChannelManager.getInstance().addClient(wrapper);
        } else {
            UserChannelManager.getInstance().removeClient(fromUserId);
        }

        // 查询好友列表
        Set<String> userIdSet = redisUtil.getSetValue(ConstantValue.FRIEND_OF_USER + fromUserId);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        HashMap<String, String> onlineFriendConnectHostMap = new HashMap<>(userIdSet.size());
        // 查询在线好友
        for (String id : userIdSet) {
            String host = redisUtil.getStringValue(ConstantValue.HOST_OF_USER + id);
            if (!StringUtils.isEmpty(host)) {
                onlineFriendConnectHostMap.put(id, host);
            }
        }
        if (login) {
            // 回复当前登录用户，好友在线名单
            ChatMessage replyMessage = new ChatMessage();
            replyMessage.setFromUserId(ConstantValue.SERVER_ID);
            replyMessage.setToUserId(fromUserId);
            replyMessage.setId(IdGenerator.generateId());
            replyMessage.setType(ChatMessageType.LOGIN_NOTIFY.getCode());
            replyMessage.setBody(new TextBody(JSON.toJSONString(onlineFriendConnectHostMap.keySet())));
            messageSender.sendMessage(channelHandlerContext.channel(), replyMessage);
        }

        // 通知好友，用户登录或登出了，这里仅通知在线好友，因为不在线好友没必要通知
        ChatMessage notifyMsg = new ChatMessage();
        BeanUtils.copyProperties(chatMessage, notifyMsg);
        notifyMsg.setFromUserId(ConstantValue.SERVER_ID);
        notifyMsg.setType(ChatMessageType.LOGIN_NOTIFY.getCode());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", fromUserId);
        map.put("login", login);
        notifyMsg.setBody(new TextBody(JSON.toJSONString(map)));
        for (String id : userIdSet) {
            messageSender.sendMessage(id, notifyMsg);
        }
    }

}
