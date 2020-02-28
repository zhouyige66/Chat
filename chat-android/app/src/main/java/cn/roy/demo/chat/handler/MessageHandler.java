package cn.roy.demo.chat.handler;

import cn.kk20.chat.base.message.ChatMessage;
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
//        switch (chatMessage.getMessageType()) {
//            case ChatMessageType.LOGIN:
//                LoginBody loginBody = JSON.parseObject(chatMessage.getBody().toString(),
//                        LoginBody.class);
//                String userId = loginBody.getUserId();
//                boolean login = loginBody.isLogin();
//                User user = new User();
//                user.setId(userId);
//                user.setName(loginBody.getUserName());
//                MessageManager.getInstance().receiveLoginMessage(user, login);
//                break;
//            case ChatMessageType.SINGLE:
//                MessageManager.getInstance().receiveSingleMessage(chatMessage);
//                break;
//            case ChatMessageType.GROUP:
//                break;
//            case ChatMessageType.LOGIN_REPLY:
//                JSONArray array = JSON.parseArray(chatMessage.getBody().toString());
//                if (array != null && array.size() > 0) {
//                    for (int i = 0; i < array.size(); i++) {
//                        JSONObject jsonObject = array.getJSONObject(i);
//                        User u = new User();
//                        u.setId(jsonObject.getString("id"));
//                        u.setName(jsonObject.getString("name"));
//                        MessageManager.getInstance().receiveLoginMessage(u, true);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
    }

}
