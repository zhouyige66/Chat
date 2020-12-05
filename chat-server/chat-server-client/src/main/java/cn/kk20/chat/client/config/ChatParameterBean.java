package cn.kk20.chat.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/12/5 7:51 下午
 * @Version: v1.0
 */
@ConfigurationProperties(prefix = "chat")
public class ChatParameterBean {
    private CenterServerParameter centerServer;
    private CommonServerParameter commonServer;
    private WebServerParameter webServer;

    public CenterServerParameter getCenterServer() {
        return centerServer;
    }

    public void setCenterServer(CenterServerParameter centerServer) {
        this.centerServer = centerServer;
    }

    public CommonServerParameter getCommonServer() {
        return commonServer;
    }

    public void setCommonServer(CommonServerParameter commonServer) {
        this.commonServer = commonServer;
    }

    public WebServerParameter getWebServer() {
        return webServer;
    }

    public void setWebServer(WebServerParameter webServer) {
        this.webServer = webServer;
    }

    public static class CenterServerParameter {
        private String host;
        private int port;
        private int autoRestartTimeInterval;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getAutoRestartTimeInterval() {
            return autoRestartTimeInterval;
        }

        public void setAutoRestartTimeInterval(int autoRestartTimeInterval) {
            this.autoRestartTimeInterval = autoRestartTimeInterval;
        }
    }

    public static class CommonServerParameter {
        private int port;
        private int autoRestartTimeInterval;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getAutoRestartTimeInterval() {
            return autoRestartTimeInterval;
        }

        public void setAutoRestartTimeInterval(int autoRestartTimeInterval) {
            this.autoRestartTimeInterval = autoRestartTimeInterval;
        }
    }

    public static class WebServerParameter {
        private int port;
        private int autoRestartTimeInterval;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getAutoRestartTimeInterval() {
            return autoRestartTimeInterval;
        }

        public void setAutoRestartTimeInterval(int autoRestartTimeInterval) {
            this.autoRestartTimeInterval = autoRestartTimeInterval;
        }
    }
}
