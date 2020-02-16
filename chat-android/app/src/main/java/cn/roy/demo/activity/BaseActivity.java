package cn.roy.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/1/30 17:09
 * @Version: v1.0
 */
public class BaseActivity extends AppCompatActivity {
    public static final String BUNDLE_DATA = "data";

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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

    protected Bundle getData() {
        return getIntent().getBundleExtra(BUNDLE_DATA);
    }

}
