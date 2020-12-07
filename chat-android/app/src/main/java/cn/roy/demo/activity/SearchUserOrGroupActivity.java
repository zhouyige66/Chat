package cn.roy.demo.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.roy.demo.R;

/**
 * @Description: 搜索联系人或群
 * @Author: zhouzongyi@cpic.com.cn
 * @Date: 12/7/20 9:16 AM
 * @Version: v1.0
 */
public class SearchUserOrGroupActivity extends BaseActivity {
    private Toolbar toolbar;
    private EditText et_key;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_or_group);

        toolbar = findViewById(R.id.toolbar);
        et_key = findViewById(R.id.et_key);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
