package cn.kk20.chat.core.common;

import cn.kk20.chat.core.coder.CoderType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/20 20:00
 * @Version: v1.0
 */
@ConfigurationProperties(prefix = "chat")
public class ChatConfigBean {
    private boolean registerAsServer = false;
    private CoderType coderType;
    private Server server;
    private Client client;

    public boolean isRegisterAsServer() {
        return registerAsServer;
    }

    public CoderType getCoderType() {
        return coderType;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public static class Server {
        private int port;// 监听端口
        private int autoRestartTimeInterval;// 单位：秒

        public int getPort() {
            return port;
        }

        public int getAutoRestartTimeInterval() {
            return autoRestartTimeInterval;
        }
    }

    public static class Client {

    }

}
