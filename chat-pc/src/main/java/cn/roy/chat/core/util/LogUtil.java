package cn.roy.chat.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/5/26 17:26
 * @Version: v1.0
 */
public class LogUtil {
    private LogUtil() {
    }

    public static void d(Object o, String log) {
        final Logger logger = LoggerFactory.getLogger(o.getClass());
        logger.info(log);
    }
}
