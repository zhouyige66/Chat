package cn.roy.chat.core;

import cn.kk20.chat.base.message.ChatMessage;

import java.util.List;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/17 17:11
 * @Version: v1.0
 */
public interface MessageManager {
    void addMessage(ChatMessage message, boolean isSend);

    void removeMessage(ChatMessage message);

    void removeMessage(String contactKey);

    List<ChatMessage> getMessages(String contactKey);

    ChatMessage getLatestChatMessage(String contactKey);
}
