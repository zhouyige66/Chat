package cn.roy.demo.chat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;
import java.util.Set;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.data.TextData;
import cn.roy.demo.util.CacheManager;
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
        switch (chatMessage.getMessageType()) {
            case LOGIN:
                TextData textData = JSON.parseObject(chatMessage.getBody().getData().toString(),
                        TextData.class);
                Set<Long> onlineFriendSet = JSON.parseObject(textData.getText(),
                        new TypeReference<Set<Long>>() {
                        });
                // TODO 更新在线好友列表
                break;
            case LOGIN_NOTIFY:
                // TODO 更新好友列表
                TextData textData2 = JSON.parseObject(chatMessage.getBody().getData().toString(),
                        TextData.class);
                Map<String, Object> map = JSON.parseObject(textData2.getText(),
                        new TypeReference<Map<String, Object>>() {
                        });
                Long id = (Long) map.get("id");
                Boolean login = (Boolean) map.get("login");
                break;
            case NOTIFY:
                break;
            default:
                CacheManager.getInstance().cacheMessage(chatMessage);
                break;
        }
    }

}
