package cn.roy.chat.broadcast;

import javafx.application.Platform;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private Map<String, CopyOnWriteArrayList<NotifyReceiver>> receiverMap = new HashMap<>();

    public void register(NotifyReceiver receiver) {
        final String receiveEventType = receiver.getReceiveEventType();
        CopyOnWriteArrayList<NotifyReceiver> notifyReceivers = receiverMap.get(receiveEventType);
        if (notifyReceivers == null) {
            notifyReceivers = new CopyOnWriteArrayList<>();
            receiverMap.put(receiveEventType, notifyReceivers);
        }
        if (!notifyReceivers.contains(receiver)) {
            notifyReceivers.add(receiver);
        }
    }

    public void unRegister(NotifyReceiver receiver) {
        final String receiveEventType = receiver.getReceiveEventType();
        CopyOnWriteArrayList<NotifyReceiver> notifyReceivers = receiverMap.get(receiveEventType);
        if (notifyReceivers != null && notifyReceivers.contains(receiver)) {
            notifyReceivers.remove(receiver);
        }
    }

    public void notifyEvent(NotifyEvent event) {
        final String type = event.getType();
        CopyOnWriteArrayList<NotifyReceiver> notifyReceivers = receiverMap.get(type);
        if (notifyReceivers != null && notifyReceivers.size() > 0) {
            notifyReceivers.stream().forEach(e -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        e.onReceiveEvent(event);
                    }
                });
            });
        }
    }

}
