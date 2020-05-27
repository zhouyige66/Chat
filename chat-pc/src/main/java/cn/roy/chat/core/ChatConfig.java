package cn.roy.chat.core;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-02-27 14:26
 * @Version: v1.0
 */
public class ChatConfig {
    //模拟器连接本地主机："10.0.2.2";
    private String host;
    private int port;
    private int heartbeatFailCount;
    private int autoReconnectTime;

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

    public int getHeartbeatFailCount() {
        return heartbeatFailCount;
    }

    public void setHeartbeatFailCount(int heartbeatFailCount) {
        this.heartbeatFailCount = heartbeatFailCount;
    }

    public int getAutoReconnectTime() {
        return autoReconnectTime;
    }

    public void setAutoReconnectTime(int autoReconnectTime) {
        this.autoReconnectTime = autoReconnectTime;
    }

}
