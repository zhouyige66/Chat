package cn.roy.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.chat.ChatConfig;

/**
 * @Description
 * @Author kk20
 * @Date 2020-02-15
 * @Version V1.0.0
 */
public class ChatService extends Service {
    private ChatClient chatClient;

    @Override
    public void onCreate() {
        super.onCreate();

        chatClient = ChatClient.getInstance();
        ChatConfig chatConfig = new ChatConfig();
        chatConfig.setHost(ApplicationConfig.NettyConfig.CHAT_SERVER_HOST);
        chatConfig.setPort(10001);
        chatConfig.setAutoReconnectTime(10);
        chatConfig.setHeartbeatFailCount(5);
        chatClient.setConfig(chatConfig);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chatClient.connectServer();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
