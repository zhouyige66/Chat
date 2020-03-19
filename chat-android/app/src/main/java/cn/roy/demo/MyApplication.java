package cn.roy.demo;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import cn.roy.demo.util.SPUtil;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:47
 * @Version: v1.0
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SPUtil.inject(this);
    }

    private void publish(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"chat");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("消息通知");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setSubText(msg);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationManager nm = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("dymamic",
                        "test",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setChannelId("dymamic");
                nm.createNotificationChannel(channel);
            } else {
                builder.setPriority(Notification.PRIORITY_MAX);
                builder.setDefaults(Notification.DEFAULT_ALL);
            }
            nm.notify(1, builder.build());
        }
    }

}
