package cn.kk20.chat.api.base.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2019/10/29 11:17
 * @Version: v1.0
 */
public class SimpleMapDto extends BaseDto {
    private Map<String,Serializable> map = new HashMap<>();

    public Map<String, Serializable> getMap() {
        return map;
    }

    public SimpleMapDto add(String key,Serializable value){
        map.put(key,value);
        return this;
    }

}
