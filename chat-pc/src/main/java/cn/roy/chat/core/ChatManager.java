package cn.roy.chat.core;

import cn.kk20.chat.base.message.Message;
import cn.roy.chat.controller.ChatController;
import cn.roy.chat.enity.FriendEntity;
import cn.roy.chat.enity.GroupEntity;
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
    private ContactManager contactManager;
    private MessageManager messageManager;

    private ChatManager() {

    }

    public ChatController getChatController() {
        return chatController;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void chatWithFriend(FriendEntity friendEntity) {
        validAndOpenChat();
        RecentContactEntity recentContactEntity = new RecentContactEntity();
        recentContactEntity.setType(0);
        recentContactEntity.setFriendEntity(friendEntity);
        chatController.setRecentContactEntity(recentContactEntity);
    }

    public void chatWithGroup(GroupEntity groupEntity) {
        validAndOpenChat();
        RecentContactEntity recentContactEntity = new RecentContactEntity();
        recentContactEntity.setType(1);
        recentContactEntity.setGroupEntity(groupEntity);
        chatController.setRecentContactEntity(recentContactEntity);
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

    public void sendMessage(Message message) {

    }

    public void receiveMessage(Message message) {

    }

    private static class ContactManager {
        private Map<String, RecentContactEntity> contactEntityMap = new HashMap<>();
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
