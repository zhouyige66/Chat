package cn.roy.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 * @Description: 关键数据缓存
 * @Author: Roy Z
 * @Date: 2018/8/14 下午1:39
 * @Version: v1.0
 */
public class SPUtil {
    private static final String DEFAULT_NAME = "ChatDemo";
    private static Context context;

    /**
     * 常量配置
     */
    public static final String USER_INFO = "userInfo";
    public static final String LOGIN_NAME = "loginName";
    public static final String LOGIN_PASSWORD = "loginPassword";

    private SPUtil() {

    }

    public static void inject(Context applicationContext) {
        context = applicationContext;
    }

    public static SharedPreferences getSP() {
        return context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    }

    public static void saveParam(@NonNull String key, @NonNull Object value) {
        SharedPreferences.Editor editor = getSP().edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.apply();
    }

    public static String getString(@NonNull String key, @NonNull String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    public static int getInt(@NonNull String key, @NonNull int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    public static long getLong(@NonNull String key, @NonNull long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    public static float getFloat(@NonNull String key, @NonNull float defaultValue) {
        return getSP().getFloat(key, defaultValue);
    }

    public static boolean getBoolean(@NonNull String key, @NonNull boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }

    public static Set<String> getStringSet(@NonNull String key, @NonNull Set<String> defaultValue) {
        return getSP().getStringSet(key, defaultValue);
    }

    public static void delete(String key) {
        getSP().edit().remove(key).apply();
    }

    public static void clean() {
        getSP().edit().clear().apply();
    }

}
