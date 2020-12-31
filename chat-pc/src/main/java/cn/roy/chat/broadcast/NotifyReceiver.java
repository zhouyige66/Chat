package cn.roy.chat.broadcast;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/5/27 09:11
 * @Version: v1.0
 */
public interface NotifyReceiver {

    String getReceiveEventType();

    void onReceiveEvent(NotifyEvent event);

}
