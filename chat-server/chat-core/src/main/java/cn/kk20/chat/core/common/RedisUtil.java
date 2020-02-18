package cn.kk20.chat.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description: Redis操作工具
 * @Author: Roy Z
 * @Date: 2020/2/18 10:03
 * @Version: v1.0
 */
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void setStringValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public boolean removeStringValue(String key) {
        return stringRedisTemplate.delete(key);
    }

    public String getStringValue(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    public boolean addSetValue(String key, Object... objects) {
        Long add = redisTemplate.opsForSet().add(key, objects);
        return add > 0;
    }

    public boolean removeSetValue(String key, Object... objects) {
        Long add = redisTemplate.opsForSet().remove(key, objects);
        return add > 0;
    }

    public boolean setSetValue(String key, Set set) {
        Boolean delete = redisTemplate.delete(key);
        Long add = redisTemplate.opsForSet().add(key, set);
        return add > 0;
    }

    public Set getSetValue(String key) {
        Set<Object> members = redisTemplate.opsForSet().members(key);
        return members;
    }

}
