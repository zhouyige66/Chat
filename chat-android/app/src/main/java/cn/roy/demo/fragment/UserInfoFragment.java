package cn.roy.demo.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;

import cn.roy.demo.R;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-04 13:07
 * @Version: v1.0
 */
public class UserInfoFragment extends BaseFragment {
    private ImageView iv_user_head;
    private TextView tv_user_name, tv_user_phone, tv_user_email, tv_user_register_time;
    private Button btn_logout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        iv_user_head = view.findViewById(R.id.iv_head);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_user_phone = view.findViewById(R.id.tv_user_phone);
        tv_user_email = view.findViewById(R.id.tv_user_email);
        tv_user_register_time = view.findViewById(R.id.tv_user_register_time);
        btn_logout = view.findViewById(R.id.btn_logout);

        User user = CacheManager.getInstance().getCurrentUser();
        tv_user_name.setText(user.getName());
        tv_user_phone.setText(TextUtils.isEmpty(user.getPassword())?"未绑定":user.getPhone());
        tv_user_email.setText(TextUtils.isEmpty(user.getEmail())?"未绑定":user.getEmail());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tv_user_register_time.setText(simpleDateFormat.format(user.getRegisterTime()));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        return view;
    }
}

