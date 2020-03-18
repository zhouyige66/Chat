package cn.roy.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.chat.ChatConfig;
import cn.roy.demo.model.Group;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        chatConfig.setAutoReconnectTime(10);
        chatConfig.setHeartbeatFailCount(5);
        chatClient.setConfig(chatConfig);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chatClient.connectServer();
        getGroups();
        getFriends();

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

    private void getGroups() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", CacheManager.getInstance().getCurrentUserId());
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                JSONArray list = jsonObject.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    List<Group> groups = JSON.parseObject(list.toJSONString(), new TypeReference<List<Group>>() {
                    });

                    CacheManager.getInstance().cacheGroupList(groups);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_GROUP_LIST,
                params, observer);
    }

    private void getFriends() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", CacheManager.getInstance().getCurrentUserId());
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                JSONArray list = jsonObject.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    List<User> users = JSON.parseObject(list.toJSONString(), new TypeReference<List<User>>() {
                    });
                    CacheManager.getInstance().cacheFriendList(users);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_FRIEND_LIST,
                params, observer);
    }

}
