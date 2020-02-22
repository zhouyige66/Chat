package cn.kk20.chat.core.main;

import cn.kk20.chat.core.coder.CoderType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 配置类
 * @Author: Roy Z
 * @Date: 2020/2/20 20:00
 * @Version: v1.0
 */
@ConfigurationProperties(prefix = "chat")
public class ChatConfigBean {
    private boolean registerAsServer = false;
    private String coderType;
    private BaseConfig server;
    private Client client;

    public boolean isRegisterAsServer() {
        return registerAsServer;
    }

    public void setRegisterAsServer(boolean registerAsServer) {
        this.registerAsServer = registerAsServer;
    }

    public CoderType getCoderType() {
        return CoderType.valueOf(coderType);
    }

    public void setCoderType(String coderType) {
        this.coderType = coderType;
    }

    public BaseConfig getServer() {
        return server;
    }

    public void setServer(BaseConfig server) {
        this.server = server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class BaseConfig{
        private int port;// 监听端口
        private int autoRestartTimeInterval;// 单位：秒

        public void setPort(int port) {
            this.port = port;
        }

        public void setAutoRestartTimeInterval(int autoRestartTimeInterval) {
            this.autoRestartTimeInterval = autoRestartTimeInterval;
        }

        public int getPort() {
            return port;
        }

        public int getAutoRestartTimeInterval() {
            return autoRestartTimeInterval;
        }
    }

    public static class CenterConfig extends BaseConfig{
        private String host;

        public void setHost(String host) {
            this.host = host;
        }

        public String getHost() {
            return host;
        }
    }

    public static class Client {
        private CenterConfig center;
        private BaseConfig commonServer;
        private BaseConfig webServer;

        public CenterConfig getCenter() {
            return center;
        }

        public BaseConfig getCommonServer() {
            return commonServer;
        }

        public BaseConfig getWebServer() {
            return webServer;
        }

        public void setCenter(CenterConfig center) {
            this.center = center;
        }

        public void setCommonServer(BaseConfig commonServer) {
            this.commonServer = commonServer;
        }

        public void setWebServer(BaseConfig webServer) {
            this.webServer = webServer;
        }
    }

}
