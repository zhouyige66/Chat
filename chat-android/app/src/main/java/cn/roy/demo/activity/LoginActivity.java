package cn.roy.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.SPUtil;
import cn.roy.demo.util.http.HttpResponseException;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 15:47
 * @Version: v1.0
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user_name, et_user_password;
    private TextView tv_login, tv_register, tv_forget_password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_user_name = findViewById(R.id.et_user_name);
        et_user_password = findViewById(R.id.et_user_password);
        tv_login = findViewById(R.id.tv_login);
        tv_register = findViewById(R.id.tv_register);
        tv_forget_password = findViewById(R.id.tv_forget_password);

        et_user_name.setText(SPUtil.getString(SPUtil.LOGIN_NAME, ""));
        et_user_password.setText(SPUtil.getString(SPUtil.LOGIN_PASSWORD, ""));
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_register:
                jump(RegisterActivity.class, false, null);
                break;
            case R.id.tv_forget_password:
                jump(ForgetPasswordActivity.class, false, null);
                break;
            default:
                break;
        }
    }

    private void login() {
        String userName = et_user_name.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            toast("用户名不能为空");
            return;
        }
        String userPassword = et_user_password.getText().toString().trim();
        if (TextUtils.isEmpty(userPassword)) {
            toast("密码不能为空");
            return;
        }

        User user = new User();
        user.setName(userName);
        user.setPassword(userPassword);

        hideSoftKeyboard();
        showProgressDialog("正在登录...");
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                dismissProgressDialog();
                User user = JSON.parseObject(jsonObject.getJSONObject("value").toJSONString(), User.class);
                CacheManager.getInstance().cacheCurrentUser(user);
                SPUtil.saveParam(SPUtil.LOGIN_NAME,
                        et_user_name.getText().toString().trim());
                SPUtil.saveParam(SPUtil.LOGIN_PASSWORD,
                        et_user_password.getText().toString().trim());
                SPUtil.saveParam(SPUtil.USER_INFO, jsonObject.toString());
                jump(MainActivity.class, true, null);
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                if (e instanceof HttpResponseException) {
                    LogUtil.e(LoginActivity.this,
                            "错误码：" + ((HttpResponseException) e).getCode());
                }
                toast(e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.d(LoginActivity.this, "结束");
            }
        };
        HttpUtil.getInstance().post(ApplicationConfig.HttpConfig.API_LOGIN, user, observer);
    }

}
