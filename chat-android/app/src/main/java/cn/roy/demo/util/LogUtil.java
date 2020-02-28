package cn.roy.demo.util;

import android.util.Log;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-02-27 14:10
 * @Version: v1.0
 */
public class LogUtil {

    private LogUtil() {

    }

    public static void e(Object o, String log) {
        Log.e(o.getClass().getSimpleName(), log);
    }

    public static void w(Object o, String log) {
        Log.w(o.getClass().getSimpleName(), log);
    }

    public static void i(Object o, String log) {
        Log.i(o.getClass().getSimpleName(), log);
    }

    public static void d(Object o, String log) {
        Log.d(o.getClass().getSimpleName(), log);
    }
}
