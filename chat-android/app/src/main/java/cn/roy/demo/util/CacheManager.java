package cn.roy.demo.util;

import androidx.collection.LruCache;

import java.util.ArrayList;
import java.util.List;

import cn.kk20.chat.base.message.ChatMessage;
import cn.roy.demo.model.User;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:53
 * @Version: v1.0
 */
public class CacheManager {
    private static CacheManager instance = null;

    private CacheManager() {
        // 最多缓存50个聊天记录
        messageCache = new LruCache<>(50);
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    /**********功能：缓存用户信息**********/
    private User currentUser;
    private Long currentUserId;
    private String currentUserName;

    public void cacheCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        this.currentUserId = currentUser.getId();
        this.currentUserName = currentUser.getName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    /**********功能：缓存聊天信息**********/
    private LruCache<Long, List<ChatMessage>> messageCache;

    public void cacheMessage(ChatMessage message) {
        Long fromUserId = message.getFromUserId();
        Long toUserId = message.getToUserId();
        Long cacheKey = currentUserId == fromUserId ? toUserId : fromUserId;
        List<ChatMessage> chatMessages = messageCache.get(cacheKey);
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            messageCache.put(cacheKey, chatMessages);
        }
        chatMessages.add(message);
    }

    public List<ChatMessage> getCacheMessageList(Long chatUserId) {
        List<ChatMessage> chatMessages = messageCache.get(chatUserId);
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            messageCache.put(chatUserId, chatMessages);
        }
        return chatMessages;
    }

}
