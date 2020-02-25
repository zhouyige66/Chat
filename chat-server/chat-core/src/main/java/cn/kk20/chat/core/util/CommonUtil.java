package cn.kk20.chat.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: 通用工具类
 * @Author: Roy Z
 * @Date: 2020/2/18 13:57
 * @Version: v1.0
 */
public class CommonUtil {

    private CommonUtil(){

    }

    public static String getHostIp(){
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostAddress;
    }

}
