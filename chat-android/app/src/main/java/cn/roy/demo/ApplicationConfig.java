package cn.roy.demo;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-02-28 10:03
 * @Version: v1.0
 */
public final class ApplicationConfig {

    public static final class HttpConfig {
        public static String API_HOST = "192.168.1.0";
        public static int API_PORT = 9001;
        public static final int TIMEOUT = 10;
        public static final String API_REGISTER = "/auth/register";
        public static final String API_LOGIN = "/auth/login";
        public static final String API_SEARCH_USER_GROUP = "/user/search";
        public static final String API_GET_VERIFY_LIST = "/verify/list";
        public static final String API_GET_GROUP_LIST = "/group/list";
        public static final String API_GET_FRIEND_LIST = "/friend/list";
        public static final String API_GET_NETTY_HOST = "/netty/getHost";
        public static final String API_SEND_MESSAGE = "/netty/sendMessage";

        private static String API_BASE_URL = null;

        public static void resetApiBaseUrl(String host, int port) {
            API_HOST = host;
            API_PORT = port;
            API_BASE_URL = API_HOST.startsWith("http") ? (API_HOST + ":" + API_PORT)
                    : ("http://" + API_HOST + ":" + API_PORT);
        }

        public static String getApiBaseUrl() {
            if (API_BASE_URL == null) {
                API_BASE_URL = "http://" + API_HOST + ":" + API_PORT;
            }
            return API_BASE_URL;
        }

    }

    public static final class ServerConfig {
        public static final String HOST_OF_API = "host_of_api";
        public static final String PORT_OF_API = "port_of_api";
        public static final String IS_CUSTOM_SOCKET = "is_custom_socket";
        public static final String HOST_OF_SOCKET = "host_of_socket";
        public static final String PORT_OF_SOCKET = "port_of_socket";
    }

}
