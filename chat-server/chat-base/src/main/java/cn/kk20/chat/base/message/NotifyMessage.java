package cn.kk20.chat.base.message;

import cn.kk20.chat.base.message.notify.NotifyMessageType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: 通知消息
 * @Author: Roy
 * @Date: 2020/3/13 08:59
 * @Version: v1.0
 */
public class NotifyMessage extends Message {
    private NotifyMessageType notifyMessageType;
    private String data;

    @Override
    public MessageType getMessageType() {
        return MessageType.NOTIFY;
    }

    public NotifyMessageType getNotifyMessageType() {
        return notifyMessageType;
    }

    public void setNotifyMessageType(NotifyMessageType notifyMessageType) {
        this.notifyMessageType = notifyMessageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JSONField(serialize = false, deserialize = false)
    public void setData(Object object) {
        this.setData(JSON.toJSONString(object));
    }

}
