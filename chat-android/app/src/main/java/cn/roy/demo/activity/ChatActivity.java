package cn.roy.demo.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.roy.demo.R;
import cn.roy.demo.adapter.AdapterViewHolder;
import cn.roy.demo.adapter.CommonAdapter;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.chat.MessageManager;
import cn.roy.demo.chat.message.ChatMessage;
import cn.roy.demo.chat.message.TextBody;
import cn.roy.demo.util.CacheManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChatActivity extends BaseActivity {
    private String toUserId;
    private Disposable disposable;
    private List<ChatMessage> messageList = new ArrayList<>();
    private CommonAdapter<ChatMessage> adapter;

    private TextView tv_left, tv_title, tv_right;
    private ListView lv;
    private EditText et_input;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tv_left = findViewById(R.id.tv_left);
        tv_left.setText("返回");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_left_enter, R.anim.anim_right_exit);
            }
        });

        tv_title = findViewById(R.id.tv_title);
        Bundle bundle = getData();
        if (bundle != null) {
            toUserId = bundle.getString("userId", "");
            tv_title.setText(bundle.getString("userName", ""));
        }
        tv_right = findViewById(R.id.tv_right);
        tv_right.setVisibility(View.GONE);

        lv = findViewById(R.id.lv);
        messageList = MessageManager.getInstance().getMessageList(toUserId);
        adapter = new CommonAdapter<ChatMessage>(this, R.layout.item_message_list, messageList) {
            @Override
            public void convert(AdapterViewHolder holder, ChatMessage message) {
                View v_left = holder.getView(R.id.rl_left);
                View v_right = holder.getView(R.id.rl_right);
                String fromUserId = message.getFromUserId();
                JSONObject bodyJson = JSON.parseObject(JSON.toJSONString(message.getBody()));
                if (fromUserId.equals(CacheManager.getInstance().getCurrentUserId())) {
                    v_left.setVisibility(View.GONE);
                    v_right.setVisibility(View.VISIBLE);

                    TextView tv_msg_right = holder.getView(R.id.tv_msg_right);
                    tv_msg_right.setText(bodyJson.getString("text"));

                } else {
                    v_left.setVisibility(View.VISIBLE);
                    v_right.setVisibility(View.GONE);

                    TextView tv_msg_left = holder.getView(R.id.tv_msg_left);
                    tv_msg_left.setText(bodyJson.getString("text"));
                }
            }
        };
        lv.setAdapter(adapter);

        btn_send = findViewById(R.id.btn_send);
        et_input = findViewById(R.id.et_input);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ChatActivity.this, "请输入内容", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!ChatClient.getInstance().isConnectSuccess()) {
                    Toast.makeText(ChatActivity.this, "服务已中断，请重新连接server",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                et_input.setText("");

                String userId = CacheManager.getInstance().getCurrentUser().getId();
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setFromUserId(userId);
                chatMessage.setToUserId(toUserId);
                chatMessage.setId(UUID.randomUUID().toString());
                chatMessage.setType(ChatMessage.ChatType.SINGLE);
                TextBody textBody = new TextBody(msg);
                chatMessage.setBody(textBody);

                MessageManager.getInstance().receiveSingleMessage(chatMessage);
                ChatClient.getInstance().sendMessage(chatMessage);
            }
        });

        // 添加观察者
        disposable = MessageManager.getInstance().getMessageListObservable()
                .subscribe(new Consumer<ChatMessage>() {
                    @Override
                    public void accept(ChatMessage chatMessage) throws Exception {
                        if (chatMessage.getFromUserId().equals(toUserId)
                                || chatMessage.getToUserId().equals(toUserId)) {
                            adapter.notifyDataSetChanged();
                        }

                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(ChatActivity.this);
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentText("消息通知");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            builder.setSubText(chatMessage.getBody().toString());
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
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
    }

}
