package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.ClientWrapper;
import cn.kk20.chat.core.MessageSender;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.bean.body.LoginBody;
import cn.kk20.chat.core.bean.body.MessageBody;
import cn.kk20.chat.core.bean.body.TextBody;
import cn.kk20.chat.core.common.ConstantValue;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.core.common.LogUtil;
import cn.kk20.chat.core.common.RedisUtil;
import cn.kk20.chat.dao.model.UserModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
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
            LogUtil.log("收到消息的消息体不能解析为登录消息体");
            return;
        }

        String fromUserId = chatMessage.getFromUserId();
        LoginBody loginBody = (LoginBody) body;
        boolean login = loginBody.isLogin();
        UserModel userModel = new UserModel();
        userModel.setId(loginBody.getUserId());
        userModel.setName(loginBody.getUserName());

        // 查询登录用户的联系人列表
        Set<String> userIdSet = redisUtil.getSetValue(ConstantValue.FRIEND_OF_USER + fromUserId);
        HashMap<String, String> hostMap = new HashMap<>(userIdSet.size());
        for (String userId : userIdSet) {
            // 查询在线好友
            String userHost = redisUtil.getStringValue(userId);
            if (!StringUtils.isEmpty(userHost)) {
                hostMap.put(userId, userHost);
            }
        }
        if (login) {
            ClientWrapper wrapper = new ClientWrapper();
            wrapper.setChannel(channelHandlerContext.channel());
            wrapper.setUserModel(userModel);
            wrapper.setIsWebUser(isFromWeb);
            ClientManager.getInstance().addClient(wrapper);

            // 回复当前登录用户，在线名单
            ChatMessage replyMessage = new ChatMessage();
            replyMessage.setFromUserId(ConstantValue.SERVER_ID);
            replyMessage.setToUserId(fromUserId);
            replyMessage.setId(IdGenerator.generateId());
            replyMessage.setType(ChatMessageType.LOGIN_NOTIFY.getCode());
            replyMessage.setBody(new TextBody(JSON.toJSONString(hostMap.keySet())));
            messageSender.sendMessage(channelHandlerContext.channel(), replyMessage);
        } else {
            ClientManager.getInstance().removeClient(fromUserId);
        }

        // 通知联系人，用户登录或登出了
        String hostAddress = getHostAddress();
        for (Map.Entry<String, String> entity : hostMap.entrySet()) {
            String userId = entity.getKey();
            String host = entity.getValue();
            if (host.equals(hostAddress)) {
                // 在本机上连接用户
                messageSender.sendMessage(userId,chatMessage);
            }else {
                // 由中心server转发

            }
        }
    }

    private String getHostAddress() {
        String hostAddress = null;
        try {
            hostAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostAddress;
    }

}
