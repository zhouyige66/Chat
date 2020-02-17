package cn.kk20.chat.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 日志工具类
 * @Author: Roy
 * @Date: 2019/1/21 16:32
 * @Version: v1.0
 */
final public class LogUtil {
    private static Logger logger = LoggerFactory.getLogger("roy");

    private LogUtil() {

    }

    public static void log(String log) {
        logger.info(log);
    }

    public static void log(Object obj, String log) {
        System.out.println(obj + "/" + log);
    }

}
