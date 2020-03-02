package cn.roy.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.adapter.CommonAdapter;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.model.User;
import cn.roy.demo.service.ChatService;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    private TextView tv_left, tv_title, tv_right;
    private ListView lv;
    private CommonAdapter<User> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_left = findViewById(R.id.tv_left);
        tv_left.setVisibility(View.GONE);

        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动聊天服务器
                Intent intent = new Intent(MainActivity.this, ChatService.class);
                startService(intent);
            }
        });

//        adapter = new CommonAdapter<User>(this, R.layout.item_user_list,
//                MessageManager.getInstance().getUserList()) {
//            @Override
//            public void convert(AdapterViewHolder holder, User user) {
//                TextView tv = holder.getView(R.id.tv_user);
//                tv.setText(user.getName());
//            }
//        };
//        lv = findViewById(R.id.lv);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                User user = MessageManager.getInstance().getUserList().get(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("userId", user.getId());
//                bundle.putString("userName", user.getName());
//                jump(ChatActivity.class, false, bundle);
//            }
//        });

    }

    private void getFriendList() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1L);
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                dismissProgressDialog();

            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                toast(e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.d(MainActivity.this, "结束");
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_FRIEND_LIST,
                params, observer);
    }

}
