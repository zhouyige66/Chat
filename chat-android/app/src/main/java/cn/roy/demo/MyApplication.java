package cn.roy.demo;

import android.app.Application;

import cn.roy.demo.util.SPUtil;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2019/2/13 13:47
 * @Version: v1.0
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SPUtil.inject(this);
    }


}
