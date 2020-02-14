package cn.roy.demo.chat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.roy.demo.chat.MessageManager;
import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.message.LoginBody;
import cn.roy.demo.model.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 消息处理器（处理来至Android、IOS的消息）
 * @Author: Roy
 * @Date: 2019/1/21 15:55
 * @Version: v1.0
 */
public class MessageHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                ChatMessage chatMessage) throws Exception {
        int type = chatMessage.getType();
        switch (type) {
            case ChatMessage.ChatType.LOGIN:
                LoginBody loginBody = JSON.parseObject(chatMessage.getBody().toString(),
                        LoginBody.class);
                String userId = loginBody.getUserId();
                boolean login = loginBody.isLogin();
                User user = new User();
                user.setId(userId);
                user.setName(loginBody.getUserName());
                MessageManager.getInstance().receiveLoginMessage(user, login);
                break;
            case ChatMessage.ChatType.SINGLE:
                MessageManager.getInstance().receiveSingleMessage(chatMessage);
                break;
            case ChatMessage.ChatType.GROUP:
                break;
            case ChatMessage.ChatType.LOGIN_REPLY:
                JSONArray array = JSON.parseArray(chatMessage.getBody().toString());
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        User u = new User();
                        u.setId(jsonObject.getString("id"));
                        u.setName(jsonObject.getString("name"));
                        MessageManager.getInstance().receiveLoginMessage(u, true);
                    }
                }
                break;
            default:
                break;
        }
    }

}
