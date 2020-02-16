package cn.kk20.chat.core;

import cn.kk20.chat.core.bean.ChatMessage;
import cn.kk20.chat.core.bean.ChatMessageType;
import cn.kk20.chat.core.bean.body.LoginBody;
import cn.kk20.chat.core.bean.body.TextBody;
import cn.kk20.chat.core.util.IdGeneratorUtil;
import cn.kk20.chat.core.util.LogUtil;
import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.service.MessageService;
import cn.kk20.chat.service.impl.MessageServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @Description: 默认消息处理器
 * @Author: Roy
 * @Date: 2020/2/16 6:00 下午
 * @Version: v1.0
 */
public class MessageProcessor {
    private static MessageProcessor instance;
    private ClientManager clientManager = ClientManager.getInstance();

    @Autowired
    MessageService messageService;

    private MessageProcessor() {

    }

    public static MessageProcessor getInstance() {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new MessageProcessor();
                }
            }
        }
        return instance;
    }

    public void processMessage(ChannelHandlerContext ctx, ChatMessage chatMessage, boolean isFromWeb) {
        String fromUserId = chatMessage.getFromUserId();
        String toUserId = chatMessage.getToUserId();
        // 消息id统一变更为系统生成
        chatMessage.setId(IdGeneratorUtil.generateId());
        ChatMessageType chatMessageTypeEnum = ChatMessageType.convertCode2ChatMessageTypeEnum(
                chatMessage.getType());
        switch (chatMessageTypeEnum) {
            case ChatMessageType.LOGIN:// 暂定为登录登出
                LoginBody loginBody = JSON.parseObject(chatMessage.getBody().toString(), LoginBody.class);
                UserModel userModel = new UserModel();
                userModel.setId(loginBody.getUserId());
                userModel.setName(loginBody.getUserName());
                if (loginBody.isLogin()) {
                    ClientWrapper wrapper = new ClientWrapper();
                    wrapper.setChannel(ctx.channel());
                    wrapper.setUserModel(userModel);
                    wrapper.setIsWebUser(isFromWeb);
                    clientManager.addClient(wrapper);

                    // 回复当前登录用户，在线名单
                    ChatMessage replyMessage = new ChatMessage();
                    replyMessage.setFromUserId("server");
                    replyMessage.setToUserId(fromUserId);
                    replyMessage.setId(IdGeneratorUtil.generateId());
                    replyMessage.setType(ChatMessageType.LOGIN_REPLY.getCode());
                    JSONArray jsonArray = new JSONArray();
                    for (String key : clientList.keySet()) {
                        jsonArray.add(userList.get(key));
                    }
                    replyMessage.setBody(new TextBody(jsonArray.toJSONString()));
                    sendMessage(fromUserId, replyMessage);
                    // 通知其他用户，有人登录
                    notifyOtherClient(fromUserId, true);
                } else {
                    // 通知其他人，有人下线了
                    notifyOtherClient(fromUserId, false);
                    // 从容器中移除
                    clientManager.removeClient(loginBody.getUserId(), isFromWeb);
                }
                break;
            case ChatMessageType.SINGLE:// 点对点消息
                // 发送消息给指定人
                sendMessage(toUserId, chatMessage);
                // 存储到数据库
                MessageModel messageModel = new MessageModel();
                messageModel.setId(chatMessage.getId());
                messageModel.setFromUserId(chatMessage.getFromUserId());
                messageModel.setToUserId(chatMessage.getToUserId());
                messageModel.setContent(JSON.toJSONString(chatMessage));
                if (messageService == null) {
                    ApplicationContext context = ChatServer.getInstance().getContext();
                    messageService = context.getAutowireCapableBeanFactory().createBean(MessageServiceImpl.class);
                }
                int result = messageService.save(messageModel);
                LogUtil.log("存储消息：" + result);
                break;
            case ChatMessageType.GROUP:// TODO 群消息

                break;
            default:
                LogUtil.log("无此消息类型");
                break;
        }
    }

    /**
     * 发送消息给指定用户（客户端为：android、ios、web）
     *
     * @param toUserId
     * @param msg
     */
    private void sendMessage(String toUserId, ChatMessage msg) {
        ClientWrapper clientWrapper = clientManager.getClient(toUserId);
        if (null == clientWrapper) {
            LogUtil.log("指定的消息接收者未登录");
            return;
        }

        Channel channel = clientWrapper.getChannel();
        if (channel == null || !channel.isActive()) {
            LogUtil.log("指定的消息接收者已断开连接");
            return;
        }

        if (clientWrapper.isWebUser()) {
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(msg));
            channel.writeAndFlush(textWebSocketFrame);
            return;
        }

        // 方式一：发送字符串
//        channel.writeAndFlush(JSON.toJSONString(msg));
        // 方式二或三：发送数据经过自定义编码器
        channel.writeAndFlush(msg);
    }

    /**
     * 通知在线用户，有人登录或登出了
     *
     * @param loginUserId
     * @param isLogin
     */
    private void notifyOtherClient(String loginUserId, boolean isLogin) {
        UserModel userModel = userList.get(loginUserId);

        for (String toUserId : clientList.keySet()) {
            if (toUserId.equals(loginUserId)) {
                continue;
            }
            // 构建登录（登出）消息
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFromUserId("server");
            chatMessage.setToUserId(toUserId);
            chatMessage.setId(IdGeneratorUtil.generateId());
            chatMessage.setType(ChatMessageType.LOGIN.getCode());
            LoginBody loginBody = new LoginBody();
            loginBody.setUserId(userModel.getId());
            loginBody.setUserName(userModel.getName());
            loginBody.setLogin(isLogin);
            chatMessage.setBody(loginBody);
            // 发送消息
            sendMessage(toUserId, chatMessage);
        }
    }

}
