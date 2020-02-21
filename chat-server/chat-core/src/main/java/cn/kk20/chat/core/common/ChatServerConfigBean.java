package cn.kk20.chat.core.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/2/20 20:00
 * @Version: v1.0
 */
@ConfigurationProperties(prefix = "chat.server")
public class ChatServerConfigBean {

    private String host;
    private int port;

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

}
