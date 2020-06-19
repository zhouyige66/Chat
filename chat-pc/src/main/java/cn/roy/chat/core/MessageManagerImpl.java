package cn.roy.chat.core;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 消息管理器
 * @Author: Roy Z
 * @Date: 2020/6/17 17:11
 * @Version: v1.0
 */
public class MessageManagerImpl implements MessageManager {
    private Map<String, List<ChatMessage>> messageMap = new HashMap<>();

    @Override
    public void addMessage(ChatMessage message, boolean isSend) {
        final Long fromUserId = message.getFromUserId();
        final Long toUserId = message.getToUserId();
        final ChatMessageType chatMessageType = message.getChatMessageType();

        final String keyPrefix = getKeyPrefix(chatMessageType);
        String key;
        if (!isSend && chatMessageType == ChatMessageType.SINGLE) {
            key = keyPrefix + fromUserId;
        } else {
            key = keyPrefix + toUserId;
        }
        if (!messageMap.containsKey(key)) {
            List<ChatMessage> list = new ArrayList<>();
            messageMap.put(key, list);
        }
        messageMap.get(key).add(message);

        // 广播
        NotifyEvent notifyEvent = new NotifyEvent();
        notifyEvent.setEventType("update_message_list");
        notifyEvent.put("data", message);
        notifyEvent.put("key", key);
        NotifyManager.getInstance().notifyEvent(notifyEvent);
    }

    @Override
    public void removeMessage(ChatMessage message) {
        final ChatMessageType chatMessageType = message.getChatMessageType();
        final String keyPrefix = getKeyPrefix(chatMessageType);
        String contactKey;
        if (chatMessageType == ChatMessageType.SINGLE) {
            contactKey = keyPrefix + message.getToUserId();
            if (messageMap.containsKey(contactKey)) {
                messageMap.get(contactKey).remove(message);
            } else {
                contactKey = keyPrefix + message.getFromUserId();
                if (messageMap.containsKey(contactKey)) {
                    messageMap.get(contactKey).remove(message);
                }
            }
        } else {
            contactKey = keyPrefix + message.getToUserId();
            if (messageMap.containsKey(contactKey)) {
                messageMap.get(contactKey).remove(message);
            }
        }
    }

    @Override
    public void removeMessage(String contactKey) {
        if (messageMap.containsKey(contactKey)) {
            messageMap.get(contactKey).clear();
        }
    }

    @Override
    public List<ChatMessage> getMessages(String contactKey) {
        return messageMap.get(contactKey);
    }

    @Override
    public ChatMessage getLatestChatMessage(String contactKey) {
        final List<ChatMessage> list = messageMap.get(contactKey);
        if (list == null || list.size() == 0) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    private String getKeyPrefix(ChatMessageType chatMessageType) {
        return chatMessageType == ChatMessageType.SINGLE ? "friend_" : "group_";
    }
}
