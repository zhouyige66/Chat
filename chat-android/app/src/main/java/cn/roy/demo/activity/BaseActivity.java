package cn.roy.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 17:09
 * @Version: v1.0
 */
public class BaseActivity extends AppCompatActivity {
    public static final String BUNDLE_DATA = "data";
    private ProgressDialog progressDialog;
    // 订阅管理器
    protected CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.dispose();
        compositeDisposable = null;
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String msg) {
        progressDialog.setMessage(msg);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    protected void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 跳转
     *
     * @param target
     * @param autoFinish
     * @param data
     */
    protected void jump(Class<?> target, boolean autoFinish, Bundle data) {
        Intent intent = new Intent(this, target);
        if (data != null) {
            intent.putExtra(BUNDLE_DATA, data);
        }
        startActivity(intent);
        if (autoFinish) {
            finish();
        }
    }

    /**
     * 获取传递数据
     *
     * @return
     */
    protected Bundle getData() {
        return getIntent().getBundleExtra(BUNDLE_DATA);
    }

}
