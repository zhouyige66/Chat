package cn.roy.demo.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.demo.model.Group;
import cn.roy.demo.model.RecentContact;
import cn.roy.demo.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:53
 * @Version: v1.0
 */
public class CacheManager {
    private static CacheManager instance = null;

    private CacheManager() {
        observableEmitterList = new ArrayList<>();
        observable = Observable.create(new ObservableOnSubscribe<ChatMessage>() {
            @Override
            public void subscribe(ObservableEmitter<ChatMessage> emitter) throws Exception {
                observableEmitterList.add(emitter);
            }
        });

        baseInfoObservableEmitterList = new ArrayList<>();
        baseInfoObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                baseInfoObservableEmitterList.add(emitter);
            }
        });
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
    private Map<String, List<ChatMessage>> messageCache = new HashMap<>();

    public List<ChatMessage> getCacheMessageList(ChatMessageType type, Long chatUserId) {
        String prefix = type == ChatMessageType.GROUP ? "group_" : "friend_";
        String key = prefix + chatUserId;
        List<ChatMessage> chatMessages = messageCache.get(key);
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            messageCache.put(key, chatMessages);
        }
        return chatMessages;
    }

    public void cacheMessage(ChatMessage message) {
        Long fromUserId = message.getFromUserId();
        Long toUserId = message.getToUserId();
        ChatMessageType chatMessageType = message.getChatMessageType();
        Long contactId;
        if (chatMessageType == ChatMessageType.GROUP) {// 群消息
            contactId = toUserId;
        } else {
            contactId = currentUserId == fromUserId ? toUserId : fromUserId;
        }
        String key = getMessageCacheKey(chatMessageType, contactId);
        List<ChatMessage> chatMessages = messageCache.get(key);
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            messageCache.put(key, chatMessages);
        }
        chatMessages.add(message);
        LogUtil.d(CacheManager.this, "消息条数：" + chatMessages.size());

        // 更新最近联系人列表
        RecentContact recentContact = contactMap.get(key);
        if (recentContact == null) {
            recentContact = new RecentContact();
            recentContact.setContact(key);
            if (chatMessageType == ChatMessageType.GROUP) {
                recentContact.setGroup(groupMap.get(contactId));
            } else {
                recentContact.setUser(userMap.get(contactId));
            }
            recentContact.setNotReadCount(0);
            contactMap.put(key, recentContact);
            contactList.add(recentContact);
        }
        recentContact.setChatMessage(message);
        int notReadCount = 0;
        if (this.recentContact != null && this.recentContact.getContact().equals(key)) {
            notReadCount = 0;
        } else {
            notReadCount = recentContact.getNotReadCount() + 1;
        }
        recentContact.setNotReadCount(notReadCount);
        sortContactList();
        // 发送通知
        if (observableEmitterList.size() == 0) {
            return;
        }
        List<ObservableEmitter> disposedList = null;
        for (ObservableEmitter emitter : observableEmitterList) {
            if (emitter.isDisposed()) {
                if (disposedList == null) {
                    disposedList = new ArrayList<>();
                }
                disposedList.add(emitter);
                continue;
            }
            emitter.onNext(message);
        }
        if (disposedList != null && !disposedList.isEmpty()) {
            observableEmitterList.removeAll(disposedList);
        }
    }

    public void deleteMessage(ChatMessageType chatMessageType, Long contactId, ChatMessage... messages) {
        int length = messages.length;
        if (length == 0) {
            return;
        }
        String key = getMessageCacheKey(chatMessageType, contactId);
        List<ChatMessage> messageList = messageCache.get(key);
        messageList.removeAll(Arrays.asList(messages));

        // 更新最近联系人
        RecentContact recentContact = contactMap.get(key);
        recentContact.setNotReadCount(0);
        if (messageList.isEmpty()) {
            contactList.remove(recentContact);
        } else {
            recentContact.setChatMessage(messageList.get(messageList.size() - 1));
        }
        sortContactList();
    }

    public static String getMessageCacheKey(ChatMessageType chatMessageType, Long contactId) {
        String key = (chatMessageType == ChatMessageType.GROUP ? "group_" : "friend_") + contactId;
        return key;
    }

    /**********功能：缓存最近联系人、好友列表、群组列表**********/
    private Map<Long, Group> groupMap = new HashMap<>();
    private Map<Long, User> userMap = new HashMap<>();
    private RecentContact recentContact;
    private List<RecentContact> contactList = new ArrayList<>();
    private Map<String, RecentContact> contactMap = new HashMap<>();

    public void cacheGroupList(List<Group> groupList) {
        if (groupList == null || groupList.isEmpty()) {
            return;
        }
        groupMap.clear();
        for (Group group : groupList) {
            groupMap.put(group.getId(), group);
        }
        if (baseInfoObservableEmitterList.isEmpty()) {
            return;
        }
        for (ObservableEmitter emitter : baseInfoObservableEmitterList) {
            if (!emitter.isDisposed()) {
                emitter.onNext(0);
            }
        }
    }

    public void cacheFriendList(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            return;
        }
        userMap.clear();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
        if (baseInfoObservableEmitterList.isEmpty()) {
            return;
        }
        for (ObservableEmitter emitter : baseInfoObservableEmitterList) {
            if (!emitter.isDisposed()) {
                emitter.onNext(1);
            }
        }
    }

    public void cacheRecentContact(RecentContact recentContact) {
        this.recentContact = recentContact;
    }

    public List<Group> getGroupList() {
        return new ArrayList<>(groupMap.values());
    }

    public List<User> getFriendList() {
        return new ArrayList<>(userMap.values());
    }

    public List<RecentContact> getRecentContactList() {
        return contactList;
    }

    private void sortContactList() {
        Collections.sort(contactList, new Comparator<RecentContact>() {
            @Override
            public int compare(RecentContact o1, RecentContact o2) {
                Long sendTimestamp = o1.getChatMessage().getSendTimestamp();
                Long sendTimestamp2 = o2.getChatMessage().getSendTimestamp();

                if (sendTimestamp > sendTimestamp2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        LogUtil.d(this, "最近联系人列表：" + JSON.toJSONString(contactList));

        if (baseInfoObservableEmitterList.isEmpty()) {
            return;
        }
        for (ObservableEmitter emitter : baseInfoObservableEmitterList) {
            if (!emitter.isDisposed()) {
                emitter.onNext(2);
            }
        }
    }

    /**********功能：观察者**********/
    private Observable<Integer> baseInfoObservable;
    private List<ObservableEmitter<Integer>> baseInfoObservableEmitterList;
    private Observable<ChatMessage> observable;
    private List<ObservableEmitter<ChatMessage>> observableEmitterList;

    public Observable<ChatMessage> getObservable() {
        return observable;
    }

    public Observable<Integer> getBaseInfoObservable() {
        return baseInfoObservable;
    }

}
