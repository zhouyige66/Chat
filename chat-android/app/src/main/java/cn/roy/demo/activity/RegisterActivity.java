package cn.roy.demo.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.model.RegisterBean;
import cn.roy.demo.model.User;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 15:47
 * @Version: v1.0
 */
public class RegisterActivity extends BaseActivity {
    private LinearLayout ll_main;
    private Toolbar toolbar;
    private Button btn_submit;
    private List<EditText> editTextList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ll_main = findViewById(R.id.ll_main);
        toolbar = findViewById(R.id.toolbar);
        btn_submit = findViewById(R.id.btn_submit);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editTextList = new ArrayList<>();
        List<String> list = new ArrayList<>(6);
        list.add("确认密码");
        list.add("密码");
        list.add("邮箱");
        list.add("电话");
        list.add("用户名");
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_input_form, ll_main, false);
            TextView tv = view.findViewById(R.id.tv_name);
            EditText et = view.findViewById(R.id.et_value);
            String str = list.get(i);
            tv.setText(str);
            et.setHint(i == 0 ? "请再次输入密码" : "请输入" + str);
            if (str.equals("电话")) {
                et.setInputType(InputType.TYPE_CLASS_PHONE);
            } else if (str.equals("邮箱")) {
                et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            } else if (str.contains("密码")) {
                et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            et.setTag(str);
            editTextList.add(et);
            ll_main.addView(view, 1);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterBean registerBean = new RegisterBean();
                // 校验输入是否合法
                String passwordConfirm = null;
                for (EditText et : editTextList) {
                    String tag = (String) et.getTag();
                    String value = et.getText().toString().trim();
                    if (TextUtils.isEmpty(value)) {
                        toast(String.format("\"%s\"不能为空", tag));
                        return;
                    }
                    if (tag.equals("用户名")) {
                        registerBean.setName(value);
                    } else if (tag.equals("电话")) {
                        registerBean.setPhone(value);
                    } else if (tag.equals("邮箱")) {
                        registerBean.setEmail(value);
                    } else if (tag.equals("密码")) {
                        registerBean.setPassword(value);
                    } else if (tag.equals("确认密码")) {
                        passwordConfirm = value;
                    }
                }
                if (registerBean.getName().length() > 10) {
                    toast("用户名长度不能大于10");
                    return;
                }
                if (registerBean.getPhone().length() > 11) {
                    toast("电话长度不能大于11");
                    return;
                }
                if (!passwordConfirm.equals(registerBean.getPassword())) {
                    toast("两次输入密码不一致，请检查后重试");
                    return;
                }
                register(registerBean);
            }
        });
    }

    private void register(RegisterBean registerBean) {
        hideSoftKeyboard();

        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                dismissProgressDialog();
                toast("注册成功");
                LogUtil.d(RegisterActivity.this, "注册成功：" + jsonObject.toJSONString());
                finish();
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                toast(e.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.d(RegisterActivity.this, "执行完成");
            }
        };
        showProgressDialog("正在提交注册信息...");
        HttpUtil.getInstance().post(ApplicationConfig.HttpConfig.API_REGISTER, registerBean, observer);
    }

}
