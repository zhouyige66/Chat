package cn.roy.demo;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import cn.roy.demo.chat.MessageManager;
import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.util.LogUtil;
import cn.roy.demo.util.SPUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019/2/13 13:47
 * @Version: v1.0
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SPUtil.inject(this);

        MessageManager.getInstance().getMessageListObservable()
                .subscribe(new Observer<ChatMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChatMessage chatMessage) {
                        LogUtil.log("Application收到通知");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
