package cn.roy.demo.chat.message;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/11 10:54
 * @Version: v1.0
 */
public class ChatMessage<T> {

    @Target(value = {ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {ChatType.HEARTBEAT,ChatType.LOGIN, ChatType.SINGLE, ChatType.GROUP, ChatType.LOGIN_REPLY})
    public @interface ChatType {
        int HEARTBEAT = -1;
        int LOGIN = 0;
        int SINGLE = 1;
        int GROUP = 2;
        int LOGIN_REPLY = 3;
    }

    private String fromUserId;
    private String toUserId;
    private String id;
    private int type;
    private T body;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(@ChatType int type) {
        this.type = type;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", body=" + body +
                '}';
    }
}
