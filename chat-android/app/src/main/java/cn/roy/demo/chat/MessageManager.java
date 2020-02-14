package cn.roy.demo.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.util.LogUtil;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 消息管理器
 * @Author: Roy
 * @Date: 2019/1/14 15:51
 * @Version: v1.0
 */
public class MessageManager {
    private static MessageManager instance;

    private List<String> userIdList;
    private List<User> userList;
    private HashMap<String, List<ChatMessage>> messageContainer;
    // 被观察者
    private Observable<Integer> userListObservable;
    private ObservableEmitter<Integer> userListEmitter;
    private Observable<ChatMessage> messageListObservable;
    private List<ObservableEmitter<ChatMessage>> messageListEmitterList;

    private MessageManager() {
        userIdList = new ArrayList<>(16);
        userList = new ArrayList<>(16);

        userListObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                userListEmitter = emitter;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        messageListObservable = Observable.create(new ObservableOnSubscribe<ChatMessage>() {
            @Override
            public void subscribe(ObservableEmitter<ChatMessage> emitter) throws Exception {
                LogUtil.log("监听到订阅");
                if (messageListEmitterList == null) {
                    messageListEmitterList = new ArrayList<>();
                }
                messageListEmitterList.add(emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        messageContainer = new HashMap<>();
    }

    public static MessageManager getInstance() {
        if (instance == null) {
            synchronized (ChatClient.class) {
                if (instance == null) {
                    instance = new MessageManager();
                }
            }
        }
        return instance;
    }

    public Observable<Integer> getUserListObservable() {
        return userListObservable;
    }

    public Observable<ChatMessage> getMessageListObservable() {
        return messageListObservable;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void clearUserList() {
        userIdList.clear();
        userList.clear();
        userListEmitter.onNext(0);
    }

    public List<ChatMessage> getMessageList(String chatUserId) {
        List<ChatMessage> list = messageContainer.get(chatUserId);
        if (list == null) {
            list = new ArrayList<>();
            messageContainer.put(chatUserId, list);
        }
        return list;
    }

    public void receiveLoginMessage(User user, boolean login) {
        // 收到自己登录信息，过滤掉
        if (user.getId().equals(CacheManager.getInstance().getCurrentUserId())) {
            return;
        }
        String loginUserId = user.getId();
        if (login) {
            if (!userIdList.contains(loginUserId)) {
                userIdList.add(loginUserId);
                userList.add(user);
            }
        } else {
            for (User u : userList) {
                if (u.getId().equals(loginUserId)) {
                    userIdList.remove(loginUserId);
                    userList.remove(u);
                    break;
                }
            }
        }
        userListEmitter.onNext(1);
    }

    public void receiveSingleMessage(ChatMessage chatMessage) {
        String key;
        String fromUserId = chatMessage.getFromUserId();
        String toUserId = chatMessage.getToUserId();
        if (fromUserId.equals(CacheManager.getInstance().getCurrentUserId())) {
            key = toUserId;
        } else {
            key = fromUserId;
        }
        List<ChatMessage> list = messageContainer.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(chatMessage);
        messageContainer.put(key, list);

        List<ObservableEmitter<ChatMessage>> disposedList = null;
        for (ObservableEmitter<ChatMessage> emitter : messageListEmitterList) {
            if (emitter.isDisposed()) {
                if (disposedList == null) {
                    disposedList = new ArrayList<>();
                }
                disposedList.add(emitter);
                continue;
            }
            emitter.onNext(chatMessage);
        }
        if (disposedList != null) {
            messageListEmitterList.removeAll(disposedList);
        }
    }

}
