package cn.kk20.chat.base.message.login;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/6/19 17:44
 * @Version: v1.0
 */
public enum ClientType {
    ANDROID(0, "android"),
    IOS(1, "ios"),
    PC(2, "pc"),
    WEB(3, "web");

    private static Map<Integer, ClientType> clientTypeMap;
    private int code;
    private String identify;

    static {
        ClientType[] clientTypes = ClientType.values();
        clientTypeMap = new HashMap<>(clientTypes.length);
        for (ClientType type : clientTypes) {
            clientTypeMap.put(type.code, type);
        }
    }

    ClientType(int code, String identify) {
        this.code = code;
        this.identify = identify;
    }

    public int getCode() {
        return code;
    }

    public String getIdentify() {
        return identify;
    }

    public static ClientType getMessageBodyTypeByCode(int code) {
        return clientTypeMap.get(code);
    }

}
