package cn.roy.chat.core;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.chat.Main;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.controller.ChatController;
import cn.roy.chat.core.util.LogUtil;
import cn.roy.chat.enity.ContactEntity;
import cn.roy.chat.enity.FriendEntity;
import cn.roy.chat.enity.GroupEntity;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.util.FXMLUtil;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/6/1 09:24
 * @Version: v1.0
 */
public class ChatManager {
    private static ChatManager instance;

    public static ChatManager getInstance() {
        if (instance == null) {
            synchronized (ChatManager.class) {
                instance = new ChatManager();
            }
        }
        return instance;
    }

    private ChatClient chatClient = ChatClient.getInstance();
    private ChatController chatController;
    private Set<Long> onlineSet = new CopyOnWriteArraySet<>();
    private ContactManager contactManager = new ContactManagerImpl();
    private MessageManager messageManager = new MessageManagerImpl();

    private ChatManager() {

    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    /**********功能：在线好友列表**********/
    public void addOnlineIds(Long... ids) {
        LogUtil.d(this, "有人上线了：" + ids);
        boolean needNotify = false;
        if (ids.length > 0) {
            for (Long id : ids) {
                onlineSet.add(id);
                final ContactEntity contactEntity = contactManager.get("friend_" + id);
                if (contactEntity != null) {
                    needNotify = true;
                    contactEntity.setOnline(true);
                    contactManager.update(contactEntity);
                }
            }
        }
        if (needNotify) {
            notifyLoginEvent();
        }
    }

    public void removeOnlineIds(Long... ids) {
        LogUtil.d(this, "有人下线了：" + ids);
        if (ids.length > 0) {
            boolean needNotify = false;
            for (Long id : ids) {
                if (onlineSet.contains(id)) {
                    onlineSet.remove(id);
                }
                final ContactEntity contactEntity = contactManager.get("friend_" + id);
                if (contactEntity != null) {
                    needNotify = true;
                    contactEntity.setOnline(false);
                    contactManager.update(contactEntity);
                }
            }
            if (needNotify) {
                notifyLoginEvent();
            }
        }
    }

    public void bindFriendList(List<FriendEntity> friendEntityList) {
        LogUtil.d(this, "绑定好友列表");
        for (FriendEntity friendEntity : friendEntityList) {
            if (onlineSet.contains(friendEntity.getId())) {
                friendEntity.setOnline(true);
            }
            final ContactEntity contactEntity = new ContactEntity(friendEntity);
            contactManager.add(contactEntity);
        }
    }

    public void bindGroupList(List<GroupEntity> groupEntityList) {
        LogUtil.d(this, "绑定群组列表");
        for (GroupEntity groupEntity : groupEntityList) {
            final ContactEntity contactEntity = new ContactEntity(groupEntity);
            contactManager.add(contactEntity);
        }
    }

    public UserEntity searchContact(Long userId, Long groupId) {
        if (userId == null) {
            return null;
        }
        ContactEntity contactEntity = contactManager.get("friend_" + userId);
        if (contactEntity != null) {
            UserEntity entity = new UserEntity();
            entity.setId(contactEntity.getFriendEntity().getId());
            entity.setName(contactEntity.getFriendEntity().getName());
            entity.setHead(contactEntity.getFriendEntity().getHead());
            return entity;
        }
        if (groupId == null) {
            return null;
        }
        ContactEntity group = contactManager.get("group_" + groupId);
        if (group != null) {
            GroupEntity groupEntity = group.getGroupEntity();
            List<UserEntity> members = groupEntity.getMembers();
            if (members == null) {
                return null;
            } else {
                UserEntity userEntity = null;
                for (UserEntity entity : members) {
                    if (entity.getId() == userId) {
                        userEntity = entity;
                        break;
                    }
                }
                return userEntity;
            }
        } else {
            return null;
        }
    }

    private void notifyLoginEvent() {
        NotifyEvent notifyEvent = new NotifyEvent();
        notifyEvent.setEventType("update_online_friend");
        NotifyManager.getInstance().notifyEvent(notifyEvent);
    }

    /**********功能：最近联系人相关**********/
    public void openChatScene(ContactEntity entity) {
        if (chatController == null) {
            Stage stage = new Stage();
            stage.setTitle("聊天");
            stage.setMinWidth(800.0);
            stage.setMaxWidth(1200.0);
            stage.setMinHeight(600.0);
            stage.setMaxHeight(900.0);
            chatController = FXMLUtil.startNewScene("chat", stage);
            chatController.setStage(stage);
        }

        final String contactKey = entity.getIdentifyKey();
        final ContactEntity contactEntity = contactManager.get(contactKey);
        notifyContactEvent(contactEntity);
        chatController.setContactEntity(contactEntity);
    }

    public void removeRecentContact(ContactEntity entity) {
        contactManager.remove(entity);
    }

    public void clearRecentContact() {
        contactManager.clear();
    }

    private void notifyContactEvent(ContactEntity entity) {
        NotifyEvent notifyEvent = new NotifyEvent();
        notifyEvent.setEventType("update_recent_contact");
        notifyEvent.put("data", entity);
        NotifyManager.getInstance().notifyEvent(notifyEvent);
    }

    /**********功能：消息相关**********/
    public void sendMessage(ChatMessage message) {
        message.setFromUserId((long) Main.currentUser.getId());
        message.setSendTimestamp(System.currentTimeMillis());
        // 调用发送器发送消息
        if (chatClient.isConnected()) {
            chatClient.sendMessage(message);
        } else {
            // TODO 调用http发送离线消息

        }
        // 发送通知事件，更新UI等
        preDealChatMessage(message);
        messageManager.addMessage(message, true);
    }

    public void receiveMessage(ChatMessage message) {
        preDealChatMessage(message);
        messageManager.addMessage(message, false);
    }

    public List<ChatMessage> getChatMessageList(String contactKey) {
        return messageManager.getMessages(contactKey);
    }

    private void preDealChatMessage(ChatMessage message) {
        // 关联最近联系人->最新一条消息
        final Long fromUserId = message.getFromUserId();
        final Long toUserId = message.getToUserId();
        final ChatMessageType chatMessageType = message.getChatMessageType();
        String contactKey;
        if (chatMessageType == ChatMessageType.GROUP) {
            contactKey = "group_" + toUserId;
        } else {
            contactKey = "friend_" + fromUserId;
            if (contactManager.get(contactKey) == null) {
                contactKey = "friend_" + toUserId;
            }
        }
        final ContactEntity contactEntity = contactManager.get(contactKey);
        if (contactEntity != null) {
            contactEntity.setLatestChatMessage(message);
            notifyContactEvent(contactEntity);
        }
    }

}
