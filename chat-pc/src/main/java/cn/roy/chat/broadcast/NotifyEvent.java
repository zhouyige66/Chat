package cn.roy.chat.broadcast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/5/27 08:57
 * @Version: v1.0
 */
public class NotifyEvent implements Serializable {
    private  String type;

    private Map<String, Object> map = new HashMap<>();

    public void setEventType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void put(String key, int value) {
        map.put(key, value);
    }

    public void put(String key, long value) {
        map.put(key, value);
    }

    public void put(String key, boolean value) {
        map.put(key, value);
    }

    public void put(String key, Serializable serializable) {
        map.put(key, serializable);
    }

    public String getStringValue(String key) {
        return (String) map.get(key);
    }

    public int getIntValue(String key) {
        return (int) map.get(key);
    }

    public long getLongValue(String key) {
        return (long) map.get(key);
    }

    public boolean getBooleanValue(String key) {
        return (boolean) map.get(key);
    }

    public Serializable getSerializableValue(String key) {
        return (Serializable) map.get(key);
    }

}
