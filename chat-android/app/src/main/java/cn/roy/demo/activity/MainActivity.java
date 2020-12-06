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
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.roy.demo.R;
import cn.roy.demo.adapter.HomePagePagerAdapter;
import cn.roy.demo.chat.ChatClient;
import cn.roy.demo.fragment.ChatListFragment;
import cn.roy.demo.fragment.ContactListFragment;
import cn.roy.demo.fragment.UserInfoFragment;
import cn.roy.demo.model.ChatServerStatus;
import cn.roy.demo.service.ChatService;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private View v_title;
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

        v_title = findViewById(R.id.v_title);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_chat_status = findViewById(R.id.tv_chat_status);
        iv_add = findViewById(R.id.iv_add);
        vp_content = findViewById(R.id.vp_content);
        v_menu = findViewById(R.id.v_menu);

        tv_user_name.setText(CacheManager.getInstance().getCurrentUserName());
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
        HomePagePagerAdapter adapter = new HomePagePagerAdapter(fragmentManager, fragmentList);
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
                        if (v_title.getVisibility() == View.GONE) {
                            v_title.setVisibility(View.VISIBLE);
                        }
                        index = 0;
                        break;
                    case R.id.rb_menu2:
                        if (v_title.getVisibility() == View.GONE) {
                            v_title.setVisibility(View.VISIBLE);
                        }
                        index = 1;
                        break;
                    case R.id.rb_menu3:
                        if (v_title.getVisibility() == View.VISIBLE) {
                            v_title.setVisibility(View.GONE);
                        }
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

        ChatServerStatus status = ChatClient.getInstance().getStatus();
        tv_chat_status.setText(status.getDes());
        Observable<ChatServerStatus> observable = ChatClient.getInstance().getObservable();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChatServerStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChatServerStatus status) {
                        LogUtil.d(MainActivity.this, "收到状态变更通知：" + status.getDes());
                        tv_chat_status.setText(status.getDes());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        // 启动聊天服务器
        Intent intent = new Intent(MainActivity.this, ChatService.class);
        startService(intent);
    }

}
