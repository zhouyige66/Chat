package cn.roy.chat.core.handler;

import cn.kk20.chat.base.message.NotifyMessage;
import cn.kk20.chat.base.message.notify.NotifyMessageType;
import cn.roy.chat.core.ChatManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/17 13:51
 * @Version: v1.0
 */
public class NotifyMessageHandler extends SimpleChannelInboundHandler<NotifyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NotifyMessage notifyMessage)
            throws Exception {
        final NotifyMessageType notifyMessageType = notifyMessage.getNotifyMessageType();
        switch (notifyMessageType) {
            case LOGIN_REPLY:
                final List<Long> ids = JSONArray.parseArray(notifyMessage.getData(), Long.class);
                if (ids != null && ids.size() > 0) {
                    for (Long id : ids) {
                        ChatManager.getInstance().addOnlineIds(id);
                    }
                }
                break;
            case LOGIN_NOTIFY:
                final Map map = JSON.parseObject(notifyMessage.getData(), new TypeReference<Map>() {
                });
                if (map != null) {
                    final boolean login = (boolean) map.get("login");
                    final long id = (Long) map.get("login");
                    if (login) {
                        ChatManager.getInstance().addOnlineIds(id);
                    } else {
                        ChatManager.getInstance().removeOnlineIds(id);
                    }
                }
                break;
            case IMPORTANT:
                break;
            case CHAT_MESSAGE_ID:// 消息发送成功，返回数据库ID值
                break;
            default:
                break;
        }
    }

}
