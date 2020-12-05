package cn.kk20.chat.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/12/5 7:54 下午
 * @Version: v1.0
 */
@ConfigurationProperties(prefix = "chat.server")
public class ChatParameterBean {
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
