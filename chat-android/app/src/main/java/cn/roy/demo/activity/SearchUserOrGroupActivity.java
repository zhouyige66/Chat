package cn.roy.demo.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import cn.roy.demo.R;

/**
 * @Description: 搜索联系人或群
 * @Author: zhouzongyi@cpic.com.cn
 * @Date: 12/7/20 9:16 AM
 * @Version: v1.0
 */
public class SearchUserOrGroupActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_or_group);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
