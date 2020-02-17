package cn.kk20.chat.core.handler.business;

import cn.kk20.chat.core.ClientManager;
import cn.kk20.chat.core.ClientWrapper;
import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.bean.body.LoginBody;
import cn.kk20.chat.core.common.IdGenerator;
import cn.kk20.chat.dao.model.UserModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/17 16:34
 * @Version: v1.0
 */
@MsgProcessor(messageType = ChatMessageType.LOGIN)
public class LoginMsgProcessor implements MessageProcessor {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, boolean isFromWeb) {
        String fromUserId = chatMessage.getFromUserId();
        String toUserId = chatMessage.getToUserId();
        LoginBody loginBody = JSON.parseObject(chatMessage.getBody().toString(), LoginBody.class);
        UserModel userModel = new UserModel();
        userModel.setId(loginBody.getUserId());
        userModel.setName(loginBody.getUserName());
        if (loginBody.isLogin()) {
            ClientWrapper wrapper = new ClientWrapper();
            wrapper.setChannel(channelHandlerContext.channel());
            wrapper.setUserModel(userModel);
            wrapper.setIsWebUser(isFromWeb);
            ClientManager.getInstance().addClient(wrapper);

            // 回复当前登录用户，在线名单
//            ChatMessage replyMessage = new ChatMessage();
//            replyMessage.setFromUserId("server");
//            replyMessage.setToUserId(fromUserId);
//            replyMessage.setId(IdGenerator.generateId());
//            replyMessage.setType(ChatMessageType.LOGIN_REPLY.getCode());
//            JSONArray jsonArray = new JSONArray();
//            for (String key : clientList.keySet()) {
//                jsonArray.add(userList.get(key));
//            }
//            replyMessage.setBody(new TextBody(jsonArray.toJSONString()));
//            sendMessage(fromUserId, replyMessage);
//            // 通知其他用户，有人登录
//            notifyOtherClient(fromUserId, true);
        } else {
            // 通知其他人，有人下线了
//            notifyOtherClient(fromUserId, false);
//            // 从容器中移除
//            clientManager.removeClient(loginBody.getUserId(), isFromWeb);
        }
    }

    /**
     * 通知在线用户，有人登录或登出了
     *
     * @param loginUserId
     * @param isLogin
     */
//    private void notifyOtherClient(String loginUserId, boolean isLogin) {
//        UserModel userModel = userList.get(loginUserId);
//
//        for (String toUserId : clientList.keySet()) {
//            if (toUserId.equals(loginUserId)) {
//                continue;
//            }
//            // 构建登录（登出）消息
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setFromUserId("server");
//            chatMessage.setToUserId(toUserId);
//            chatMessage.setId(IdGenerator.generateId());
//            chatMessage.setType(ChatMessageType.LOGIN.getCode());
//            LoginBody loginBody = new LoginBody();
//            loginBody.setUserId(userModel.getId());
//            loginBody.setUserName(userModel.getName());
//            loginBody.setLogin(isLogin);
//            chatMessage.setBody(loginBody);
//            // 发送消息
//            sendMessage(toUserId, chatMessage);
//        }
//    }

}
