package cn.roy.chat.core.util;

import java.util.Random;
import java.util.UUID;

/**
 * @Description: id生成器
 * @Author: Roy
 * @Date: 2019-02-14 10:28
 * @Version: v1.0
 */
public class IdGenerateUtil {
    final static Random random = new Random(1000000);

    private static Long currentIp = null;

    private IdGenerateUtil() {

    }

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 产生唯一Long uid
     * 系统时间(秒)+毫秒+(IP+当前进程)+一个1百万中的随机数Long
     *
     * @return
     */
    public static Long generateLongId(Long factor) {
        return Math.abs(System.currentTimeMillis() + System.nanoTime() + factor + random.nextLong());
    }

    public static String generateUid(long mostSigBits, long leastSigBits) {
        return new UUID(mostSigBits, leastSigBits).toString().replaceAll("-", "");
    }

    public static UUID parseUid(String uid) {
        StringBuffer buf = new StringBuffer();
        buf.append(uid.substring(0, 8)).append("-").append(uid.substring(8, 12)).append("-")
                .append(uid.substring(12, 16)).append("-").append(uid.substring(16, 20)).append("-")
                .append(uid.substring(20, 32));
        return UUID.fromString(buf.toString());
    }

}
