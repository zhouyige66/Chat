package cn.roy.chat.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/27 08:47
 * @Version: v1.0
 */
public class NotifyManager {
    private static NotifyManager instance;

    public static NotifyManager getInstance() {
        if (instance == null) {
            synchronized (NotifyManager.class) {
                if (instance == null) {
                    instance = new NotifyManager();
                }
            }
        }
        return instance;
    }

    private Map<String, List<NotifyReceiver>> receiverMap = new HashMap<>();

    public void register(NotifyReceiver receiver) {
        final String receiveEventType = receiver.getReceiveEventType();
        List<NotifyReceiver> notifyReceivers = receiverMap.get(receiveEventType);
        if (notifyReceivers == null) {
            notifyReceivers = new ArrayList<>();
            receiverMap.put(receiveEventType, notifyReceivers);
        }
        if (!notifyReceivers.contains(receiver)) {
            notifyReceivers.add(receiver);
        }
    }

    public void unRegister(NotifyReceiver receiver) {
        final String receiveEventType = receiver.getReceiveEventType();
        final List<NotifyReceiver> notifyReceivers = receiverMap.get(receiveEventType);
        if (notifyReceivers != null && notifyReceivers.contains(receiver)) {
            notifyReceivers.remove(receiver);
        }
    }

    public void notifyEvent(NotifyEvent event) {
        final String type = event.getType();
        final List<NotifyReceiver> notifyReceivers = receiverMap.get(type);
        if (notifyReceivers != null && notifyReceivers.size() > 0) {
            for (NotifyReceiver receiver : notifyReceivers) {
                receiver.onReceiveEvent(event);
            }
        }
    }

}
