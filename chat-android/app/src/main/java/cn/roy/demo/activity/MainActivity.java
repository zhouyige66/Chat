package cn.roy.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.adapter.HomePagePagerAdapter;
import cn.roy.demo.fragment.ChatListFragment;
import cn.roy.demo.fragment.ContactListFragment;
import cn.roy.demo.fragment.UserInfoFragment;
import cn.roy.demo.service.ChatService;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    private TextView tv_user_name, tv_chat_status;
    private ImageView iv_add;
    private ViewPager vp_content;
    private RadioGroup v_menu;

    // 内容页面
    ChatListFragment chatListFragment;
    ContactListFragment contactListFragment;
    UserInfoFragment userInfoFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_user_name = findViewById(R.id.tv_user_name);
        tv_chat_status = findViewById(R.id.tv_chat_status);
        iv_add = findViewById(R.id.iv_add);
        vp_content = findViewById(R.id.vp_content);
        v_menu = findViewById(R.id.v_menu);

        // 赋值或添加监听器
        tv_user_name.setText("Roy");
        tv_chat_status.setText("未连接");
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("添加联系人");
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        chatListFragment = new ChatListFragment();
        contactListFragment = new ContactListFragment();
        userInfoFragment = new UserInfoFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(chatListFragment);
        fragmentList.add(contactListFragment);
        fragmentList.add(userInfoFragment);
        HomePagePagerAdapter adapter = new HomePagePagerAdapter(fragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton) v_menu.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        v_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int currentItem = vp_content.getCurrentItem();
                int index = 0;
                switch (checkedId) {
                    case R.id.rb_menu1:
                        index = 0;
                        break;
                    case R.id.rb_menu2:
                        index = 1;
                        break;
                    case R.id.rb_menu3:
                        index = 2;
                        break;
                    default:
                        break;
                }
                if (index == currentItem) {
                    return;
                }
                vp_content.setCurrentItem(index);
            }
        });

        // 启动聊天服务器
        Intent intent = new Intent(MainActivity.this, ChatService.class);
        startService(intent);

        // 获取最近聊天列表记录

        // 获取好友列表
        getFriendList();
    }

    private void getFriendList() {
        showProgressDialog("加载中...");
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
