package cn.roy.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.BodyType;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.kk20.chat.base.message.chat.body.TextData;
import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.adapter.AdapterViewHolder;
import cn.roy.demo.adapter.CommonAdapter;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.model.Group;
import cn.roy.demo.model.RecentContact;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.IdGenerator;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_left, tv_title, tv_right;
    private ListView lv;
    private EditText et_input;
    private Button btn_send;

    private ChatMessageType chatMessageType;
    private Long toUserId;
    private String contactName = "";
    private List<ChatMessage> messageList = new ArrayList<>();
    private CommonAdapter<ChatMessage> adapter;

    private static String CHAT_TYPE = "chat_type";
    private static String DATA = "data";

    public static void launch(Context context, ChatMessageType chatMessageType, Serializable data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CHAT_TYPE, chatMessageType.name());
        intent.putExtra(DATA, data);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getInitData();
        tv_left = findViewById(R.id.tv_left);
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        lv = findViewById(R.id.lv);
        btn_send = findViewById(R.id.btn_send);
        et_input = findViewById(R.id.et_input);

        tv_left.setText("返回");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_left_enter, R.anim.anim_right_exit);
            }
        });
        tv_title.setText(contactName);
        tv_right.setVisibility(View.GONE);
        messageList = CacheManager.getInstance().getCacheMessageList(chatMessageType, toUserId);
        adapter = new CommonAdapter<ChatMessage>(this, R.layout.item_message_list,
                messageList) {
            @Override
            public void convert(AdapterViewHolder holder, ChatMessage message) {
                View v_left = holder.getView(R.id.rl_left);
                View v_right = holder.getView(R.id.rl_right);
                Long fromUserId = message.getFromUserId();
                BodyType bodyType = message.getBodyType();
                if (bodyType == BodyType.TEXT) {
                    TextView tv;
                    if (fromUserId == CacheManager.getInstance().getCurrentUserId()) {
                        // 我发送的消息
                        v_left.setVisibility(View.GONE);
                        v_right.setVisibility(View.VISIBLE);
                        tv = holder.getView(R.id.tv_msg_right);
                    } else {
                        // 其他人发送的消息
                        v_left.setVisibility(View.VISIBLE);
                        v_right.setVisibility(View.GONE);
                        tv = holder.getView(R.id.tv_msg_left);
                    }
                    TextData textData = JSON.parseObject(message.getBody(), TextData.class);
                    tv.setText(textData.getText());
                }
            }
        };
        lv.setAdapter(adapter);
        btn_send.setOnClickListener(this);

        // 添加观察者
        Observable<ChatMessage> observable = CacheManager.getInstance().getObservable();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChatMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChatMessage message) {
                        updateUI();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        // TODO 获取历史聊天记录（分页查询）
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getInitData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CacheManager.getInstance().cacheRecentContact(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String msg = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ChatActivity.this, "请输入内容", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                // 构建发送实体
                TextData textData = new TextData();
                textData.setText(msg);
                Long currentUserId = CacheManager.getInstance().getCurrentUserId();
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setChatMessageType(chatMessageType);
                chatMessage.setFromUserId(currentUserId);
                chatMessage.setToUserId(toUserId);
                chatMessage.setSendTimestamp(System.currentTimeMillis());
                chatMessage.setBody(textData);
                CacheManager.getInstance().cacheMessage(chatMessage);
                // 发送消息
                if (ChatClient.getInstance().isConnectSuccess()) {
                    chatMessage.setId(IdGenerator.generate());
                    ChatClient.getInstance().sendMessage(chatMessage);
                } else {
                    // TODO 即时通讯未连接，通过http方式请求接口发送
                    sendMessage(chatMessage);
                }
                et_input.setText("");
                updateUI();
                break;
            default:
                break;
        }
    }

    private void getInitData() {
        String chatType = getIntent().getStringExtra(CHAT_TYPE);
        Serializable data = getIntent().getSerializableExtra(DATA);
        chatMessageType = ChatMessageType.valueOf(chatType);

        RecentContact recentContact = new RecentContact();
        if (chatMessageType == ChatMessageType.GROUP) {
            Group group = (Group) data;
            recentContact.setGroup(group);
            toUserId = group.getId();
            contactName = group.getName();
        } else {
            User user = (User) data;
            recentContact.setUser(user);
            toUserId = user.getId();
            contactName = user.getName();
        }
        recentContact.setContact(CacheManager.getMessageCacheKey(chatMessageType, toUserId));
        recentContact.setNotReadCount(0);
        CacheManager.getInstance().cacheRecentContact(recentContact);
    }

    private void updateUI() {
        LogUtil.d(this, "消息目前长度：" + messageList.size());
        adapter.notifyDataSetChanged();
        lv.smoothScrollToPosition(messageList.size());
    }

    private void sendMessage(ChatMessage chatMessage) {
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().post(ApplicationConfig.HttpConfig.API_SEND_MESSAGE,
                chatMessage, observer);
    }

}
