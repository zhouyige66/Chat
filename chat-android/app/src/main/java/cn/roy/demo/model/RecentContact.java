package cn.roy.demo.model;

import cn.kk20.chat.base.message.ChatMessage;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/18 08:36
 * @Version: v1.0
 */
public class RecentContact {
    private String contact;
    private User user;
    private Group group;
    private ChatMessage chatMessage;
    private int notReadCount;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public int getNotReadCount() {
        return notReadCount;
    }

    public void setNotReadCount(int notReadCount) {
        this.notReadCount = notReadCount;
    }
}
