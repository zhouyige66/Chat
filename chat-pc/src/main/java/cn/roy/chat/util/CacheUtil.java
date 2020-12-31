package cn.roy.chat.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/5/25 15:27
 * @Version: v1.0
 */
public class CacheUtil {
    private CacheUtil() {

    }

    private static Map<String, String> cacheMap = new HashMap<>();

    public static <T> void cacheData(String key, T t) {
        // TODO 目前暂时使用内存缓存，以后更换为其他方案
        if (t instanceof String) {
            cacheMap.put(key, (String) t);
        } else {
            cacheMap.put(key, JSON.toJSONString(t));
        }
    }

    public static <T> T getCache(String key, Class<T> clazz) {
        final String s = cacheMap.get(key);
        if (StringUtils.isEmpty(s)) {
            return null;
        } else {
            return JSON.parseObject(s, clazz);
        }
    }

    private static String propPath;

    static {
        try {
            propPath = URLDecoder.decode(
                    CacheUtil.class.getClassLoader().getResource("config.properties").getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static Properties getDefaultProperties() {
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static <T> T cacheToProp(String key, T t) {
        try {
            final Properties properties = getDefaultProperties();
            properties.setProperty(key, JSON.toJSONString(t));
            properties.save(new BufferedOutputStream(new FileOutputStream(propPath)), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T getCacheFromProp(String key, Class<T> clazz) {
        final Properties properties = getDefaultProperties();
        final String property = properties.getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        final T t = JSON.parseObject(property, clazz);
        return t;
    }

}
