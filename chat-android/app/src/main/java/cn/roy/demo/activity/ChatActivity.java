package cn.roy.demo.activity;

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
import cn.kk20.chat.base.message.ChatMessageType;
import cn.roy.demo.R;
import cn.roy.demo.adapter.AdapterViewHolder;
import cn.roy.demo.adapter.CommonAdapter;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.model.Group;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_left, tv_title, tv_right;
    private ListView lv;
    private EditText et_input;
    private Button btn_send;

    private Long toUserId;
    private List<ChatMessage> messageList = new ArrayList<>();
    private CommonAdapter<ChatMessage> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

        int chatType = getIntent().getIntExtra("chatType", 1);
        Serializable data = getIntent().getSerializableExtra("data");
        if (chatType == 1) {
            Group group = (Group) data;
            toUserId = group.getId();
            tv_title.setText(group.getName());
        } else {
            User user = (User) data;
            toUserId = user.getId();
            tv_title.setText(user.getName());
        }
        tv_right.setVisibility(View.GONE);

        messageList = CacheManager.getInstance().getCacheMessageList(toUserId);
        adapter = new CommonAdapter<ChatMessage>(this, R.layout.item_message_list,
                messageList) {
            @Override
            public void convert(AdapterViewHolder holder, ChatMessage message) {
                View v_left = holder.getView(R.id.rl_left);
                View v_right = holder.getView(R.id.rl_right);
                Long fromUserId = message.getFromUserId();
                JSONObject bodyJson = JSON.parseObject(JSON.toJSONString(message.getBody()));
                if (fromUserId == CacheManager.getInstance().getCurrentUserId()) {
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
        btn_send.setOnClickListener(this);

        // 添加观察者
//        disposable = MessageManager.getInstance().getMessageListObservable()
//                .subscribe(new Consumer<ChatMessage>() {
//                    @Override
//                    public void accept(ChatMessage chatMessage) throws Exception {
//                        if (chatMessage.getFromUserId().equals(toUserId)
//                                || chatMessage.getToUserId().equals(toUserId)) {
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        NotificationCompat.Builder builder =
//                                new NotificationCompat.Builder(ChatActivity.this);
//                        builder.setSmallIcon(R.mipmap.ic_launcher);
//                        builder.setContentText("消息通知");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            builder.setSubText(chatMessage.getBody().toString());
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            NotificationManager nm = (NotificationManager)
//                                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationChannel channel = new NotificationChannel("dymamic",
//                                        "test",
//                                        NotificationManager.IMPORTANCE_DEFAULT);
//                                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                                builder.setChannelId("dymamic");
//                                nm.createNotificationChannel(channel);
//                            } else {
//                                builder.setPriority(Notification.PRIORITY_MAX);
//                                builder.setDefaults(Notification.DEFAULT_ALL);
//                            }
//                            nm.notify(1, builder.build());
//                        }
//                    }
//                });

        // TODO 获取历史聊天记录（分页查询）

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
                Long currentUserId = CacheManager.getInstance().getCurrentUserId();
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessageType(ChatMessageType.SINGLE);
                chatMessage.setFromUserId(currentUserId);
                chatMessage.setToUserId(toUserId);
                // 发送消息
                if (ChatClient.getInstance().isConnectSuccess()) {
                    ChatClient.getInstance().sendMessage(chatMessage);
                } else {
                    // TODO 即时通讯未连接，通过http方式请求接口发送
                    Toast.makeText(ChatActivity.this, "服务已中断，请重新连接server",
                            Toast.LENGTH_SHORT).show();
                }
                et_input.setText("");
                break;
            default:
                break;
        }
    }
}
