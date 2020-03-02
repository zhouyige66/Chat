package cn.kk20.chat.base;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.ChatMessageType;
import cn.kk20.chat.base.message.MessageBody;
import cn.kk20.chat.base.message.data.TextData;
import com.alibaba.fastjson.JSON;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/26 10:10
 * @Version: v1.0
 */
public class Test {

    public static void main(String[] args) {
        TextData textData = new TextData("哈哈哈哈哈哈");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageType(ChatMessageType.HEARTBEAT);
        chatMessage.setId(1L);
        chatMessage.setBodyData(textData);
        String str = JSON.toJSONString(chatMessage);
        ChatMessage message = JSON.parseObject(str, ChatMessage.class);
        int i=0;
    }

}
