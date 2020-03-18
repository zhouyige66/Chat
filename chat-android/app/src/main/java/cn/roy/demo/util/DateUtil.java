package cn.roy.demo.util;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/18 16:58
 * @Version: v1.0
 */
public class DateUtil {
    private DateUtil() {

    }

    private static final Object lock = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = sdfMap.get(pattern);
        if (threadLocal == null) {
            synchronized (lock) {
                threadLocal = new ThreadLocal<SimpleDateFormat>() {
                    @Nullable
                    @Override
                    protected SimpleDateFormat initialValue() {
                        return new SimpleDateFormat(pattern);
                    }
                };
                sdfMap.put(pattern, threadLocal);
            }
        }
        return threadLocal.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

}
