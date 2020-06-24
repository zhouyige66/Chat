package cn.roy.chat.enity;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.BodyType;
import cn.kk20.chat.base.message.chat.body.TextData;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 13:46
 * @Version: v1.0
 */
public class ContactEntity implements Serializable {
    private int type;// 0-好友，1-群组
    private FriendEntity friendEntity;
    private GroupEntity groupEntity;
    private boolean isOnline;
    private ChatMessage latestChatMessage;

    private ContactEntity() {
    }

    public ContactEntity(FriendEntity friendEntity) {
        this.type = 0;
        this.isOnline = friendEntity.isOnline();
        this.friendEntity = friendEntity;
    }

    public ContactEntity(GroupEntity groupEntity) {
        this.type = 1;
        this.isOnline = true;
        this.groupEntity = groupEntity;
    }

    public int getType() {
        return type;
    }

    public FriendEntity getFriendEntity() {
        return friendEntity;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        if (type == 0) {
            friendEntity.setOnline(online);
            isOnline = online;
        }
    }

    public ChatMessage getLatestChatMessage() {
        return latestChatMessage;
    }

    public void setLatestChatMessage(ChatMessage latestChatMessage) {
        this.latestChatMessage = latestChatMessage;
    }

    public String getIdentifyKey() {
        return type == 0 ? ("friend_" + friendEntity.getId())
                : ("group_" + groupEntity.getId());
    }

    public long getContactId() {
        return type == 0 ? friendEntity.getId() : groupEntity.getId();
    }

    public String getContactName() {
        return type == 0 ? friendEntity.getName() : groupEntity.getName();
    }

    public String getContactHead() {
        return type == 0 ? friendEntity.getHead() : null;
    }

    public String getLastContactTime() {
        if (latestChatMessage == null) {
            return "";
        }

        return getTimeStr(latestChatMessage.getSendTimestamp());
    }

    private String getTimeStr(long timestamp) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(timestamp);
    }

    public String getLatestMessage() {
        if (latestChatMessage == null) {
            return "";
        }

        String msg = "";
        final BodyType bodyType = latestChatMessage.getBodyType();
        switch (bodyType) {
            case TEXT:
                TextData textData = JSON.parseObject(latestChatMessage.getBody(), TextData.class);
                msg = textData.getText();
                break;
            case HYBRID:
            case LINK:
                msg = "[link]";
                break;
            case IMG:
                msg = "[图片]";
                break;
            case AUDIO:
                msg = "[音频]";
                break;
            case VIDEO:
                msg = "[视频]";
                break;
            case FILE:
                msg = "[文件]";
                break;
            default:
                break;
        }

        return msg;
    }

}
