package cn.roy.demo;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-02-28 10:03
 * @Version: v1.0
 */
public final class ApplicationConfig {

    public static final class HttpConfig {
        public static final int TIMEOUT = 10;
//        public static final String API_BASE_URL = "http://10.0.2.2:8081";
        public static final String API_BASE_URL = "http://192.168.43.133:8081";
        public static final String API_REGISTER = "/user/register";
        public static final String API_LOGIN = "/user/login";
        public static final String API_GET_VERIFY_LIST = "/verify/list";
        public static final String API_GET_GROUP_LIST = "/group/list";
        public static final String API_GET_FRIEND_LIST = "/friend/list";
        public static final String API_GET_NETTY_HOST = "/netty/getHost";
    }

}
