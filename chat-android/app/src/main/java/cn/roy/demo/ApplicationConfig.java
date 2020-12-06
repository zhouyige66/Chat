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
//        public static final String API_BASE_URL = "http://10.0.2.2:9001";
        public static String API_BASE_URL = "http://192.168.43.133:9001";
        public static final String API_REGISTER = "/auth/register";
        public static final String API_LOGIN = "/auth/login";
        public static final String API_GET_VERIFY_LIST = "/verify/list";
        public static final String API_GET_GROUP_LIST = "/group/list";
        public static final String API_GET_FRIEND_LIST = "/friend/list";
        public static final String API_GET_NETTY_HOST = "/netty/getHost";
        public static final String API_SEND_MESSAGE = "/netty/sendMessage";
    }

    public static final class ServerConfig{
        public static final String HOST_OF_API = "port_of_api";
        public static final String PORT_OF_API = "port_of_api";
        public static final String IS_CUSTOM_SOCKET = "is_custom_socket";
        public static final String HOST_OF_SOCKET = "host_of_socket";
        public static final String PORT_OF_SOCKET = "port_of_socket";
    }

}
