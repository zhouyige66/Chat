package cn.roy.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/17 14:22
 * @Version: v1.0
 */
public class IdGenerator {

    private IdGenerator() {

    }

    public static Long generate() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssS", Locale.CHINA);
        Date date = new Date();
        System.out.println("当前时间：" + date.getTime());
        String str = format.format(date);
        int i = new Random().nextInt(1000);
        String str2 = String.format("%04d", i);
        StringBuffer append = new StringBuffer(str).append(str2);
        Long id = Long.valueOf(append.toString());
        return id;
    }
}
