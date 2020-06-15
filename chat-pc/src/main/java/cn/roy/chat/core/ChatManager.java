package cn.roy.chat.core;

import cn.kk20.chat.base.message.Message;
import cn.roy.chat.broadcast.NotifyEvent;
import cn.roy.chat.broadcast.NotifyManager;
import cn.roy.chat.controller.ChatController;
import cn.roy.chat.enity.RecentContactEntity;
import cn.roy.chat.util.FXMLUtil;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ChatController chatController;
    private ContactManager contactManager = new ContactManager();
    private MessageManager messageManager = new MessageManager();

    private ChatManager() {

    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    /**********功能：最近联系人相关**********/
    public void addRecentContact(RecentContactEntity entity) {
        validAndOpenChat();
        chatController.setRecentContactEntity(entity);
        contactManager.add(entity);
    }

    public void removeRecentContact(RecentContactEntity entity) {
        contactManager.remove(entity);
    }

    public void clearRecentContact() {
        contactManager.clear();
    }

    private void validAndOpenChat() {
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
    }

    /**********功能：消息相关**********/
    public void sendMessage(Message message) {

    }

    public void receiveMessage(Message message) {

    }

    private static class ContactManager {
        private Map<String, RecentContactEntity> contactEntityMap = new HashMap<>();

        public void add(RecentContactEntity entity) {
            final String key = getKey(entity);
            if (contactEntityMap.containsKey(key)) {
                return;
            }
            contactEntityMap.put(key, entity);

            NotifyEvent notifyEvent = new NotifyEvent();
            notifyEvent.setEventType("update_recent_contact");
            notifyEvent.put("data", entity);
            NotifyManager.getInstance().notifyEvent(notifyEvent);
        }

        public void remove(RecentContactEntity entity) {
            final String key = getKey(entity);
            if (contactEntityMap.containsKey(key)) {
                contactEntityMap.remove(key);
            }
        }

        public void clear() {
            contactEntityMap.clear();
        }

        private String getKey(RecentContactEntity entity) {
            return entity.getType() == 0 ? ("friend_" + entity.getFriendEntity().getId())
                    : ("group_" + entity.getGroupEntity().getId());
        }
    }

    private static class MessageManager {
        private Map<String, List<Message>> messageMap = new HashMap<>();

        public void addMessage(Message message) {

        }

        public void removeMessage(Message message) {

        }

        public void removeMessage(String contactId) {

        }

        public List<Message> getMessages(String contactId) {
            return messageMap.get(contactId);
        }
    }

}
