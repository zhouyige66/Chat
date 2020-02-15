package cn.kk20.chat.core;

import cn.kk20.chat.core.message.Message;
import cn.kk20.chat.core.message.MessageType;
import cn.kk20.chat.core.message.body.LoginBody;
import cn.kk20.chat.core.message.body.TextBody;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static ClientManager instance = null;

    private ConcurrentHashMap<String, UserModel> userList;
    private ConcurrentHashMap<String, Channel> clientList;
    private Set<String> webClientList;

    @Autowired
    MessageService messageService;

    private ClientManager() {
        userList = new ConcurrentHashMap<>(16);
        clientList = new ConcurrentHashMap<>(16);
        webClientList = new HashSet<>(16);
    }

    public static ClientManager getInstance() {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new ClientManager();
                }
            }
        }
        return instance;
    }

    /**
     * 消息处理
     *
     * @param ctx
     * @param message
     */
    public void handleMessage(ChannelHandlerContext ctx, Message message, boolean isWebMessage) {
        // 消息id统一变更为系统生成
        message.setId(IdGeneratorUtil.generateId());

        String fromUserId = message.getFromUserId();
        String toUserId = message.getToUserId();
        switch (message.getType()) {
            case 0:// 暂定为登录登出
                LoginBody loginBody = JSON.parseObject(message.getBody().toString(), LoginBody.class);
                UserModel userModel = new UserModel();
                userModel.setId(loginBody.getUserId());
                userModel.setName(loginBody.getUserName());
                if (loginBody.isLogin()) {
                    // 添加到容器
                    addClient(userModel, ctx.channel(), isWebMessage);
                    // 回复当前登录用户，在线名单
                    Message replyMessage = new Message();
                    replyMessage.setFromUserId("server");
                    replyMessage.setToUserId(fromUserId);
                    replyMessage.setId(IdGeneratorUtil.generateId());
                    replyMessage.setType(MessageType.LOGIN_REPLY.getCode());
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
                    removeClient(loginBody.getUserId(), isWebMessage);
                }
                break;
            case 1:// 点对点消息
                // 发送消息给指定人
                sendMessage(toUserId, message);
                // 存储到数据库
                MessageModel messageModel = new MessageModel();
                messageModel.setId(message.getId());
                messageModel.setFromUserId(message.getFromUserId());
                messageModel.setToUserId(message.getToUserId());
                messageModel.setContent(JSON.toJSONString(message));
                if (messageService == null) {
                    ApplicationContext context = ChatServer.getInstance().getContext();
                    messageService = context.getAutowireCapableBeanFactory().createBean(MessageServiceImpl.class);
                }
                int result = messageService.save(messageModel);
                LogUtil.log("存储消息：" + result);
                break;
            case 2:// TODO 群消息

                break;
            default:
                LogUtil.log("无此消息类型");
                break;
        }
    }

    private void addClient(UserModel userModel, Channel channel, boolean isWeb) {
        String loginUserId = userModel.getId();
        // 判断添加的用户是否已登录，且为同一设备
        Channel indexChannel = clientList.get(loginUserId);
        if (indexChannel != null && indexChannel.isActive()) {
            indexChannel.closeFuture();
        }
        // 添加到用户列表
        userList.put(loginUserId, userModel);
        if (isWeb) {
            webClientList.add(loginUserId);
        }
        // 添加到通道容器
        clientList.put(loginUserId, channel);
    }

    private void removeClient(String logoutUserId, boolean isWeb) {
        userList.remove(logoutUserId);
        if (isWeb) {
            webClientList.remove(logoutUserId);
        }
        clientList.remove(logoutUserId);
    }

    /**
     * 发送消息给指定用户（客户端为：android、ios、web）
     *
     * @param toUserId
     * @param msg
     */
    public void sendMessage(String toUserId, Message msg) {
        Channel channel = clientList.get(toUserId);
        if (channel == null || !channel.isActive()) {
            LogUtil.log("指定的消息接收者未登录或已关闭连接");
            return;
        }

        if (webClientList.contains(toUserId)) {
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(msg));
            channel.writeAndFlush(textWebSocketFrame);
            return;
        }

        // 方式一：发送字符串
//            channel.writeAndFlush(JSON.toJSONString(msg));
        // 方式二或三：发送数据经过自定义编码器
        channel.writeAndFlush(msg);
    }

    /**
     * 通知在线用户，有人登录或登出了
     *
     * @param loginUserId
     * @param isLogin
     */
    public void notifyOtherClient(String loginUserId, boolean isLogin) {
        UserModel userModel = userList.get(loginUserId);

        for (String toUserId : clientList.keySet()) {
            if (toUserId.equals(loginUserId)) {
                continue;
            }
            // 构建登录（登出）消息
            Message message = new Message();
            message.setFromUserId("server");
            message.setToUserId(toUserId);
            message.setId(IdGeneratorUtil.generateId());
            message.setType(MessageType.LOGIN.getCode());
            LoginBody loginBody = new LoginBody();
            loginBody.setUserId(userModel.getId());
            loginBody.setUserName(userModel.getName());
            loginBody.setLogin(isLogin);
            message.setBody(loginBody);
            // 发送消息
            sendMessage(toUserId, message);
        }
    }

}
