package cn.roy.chat.enity;

import cn.kk20.chat.base.message.Message;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 13:46
 * @Version: v1.0
 */
public class RecentContactEntity {
    private int type;// 0-好友，1-群组
    private FriendEntity friendEntity;
    private GroupEntity groupEntity;
    private Message lastChatMessage;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FriendEntity getFriendEntity() {
        return friendEntity;
    }

    public void setFriendEntity(FriendEntity friendEntity) {
        this.friendEntity = friendEntity;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public Message getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(Message lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }
}
