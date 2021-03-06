package cn.kk20.chat.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: Redis操作工具
 * @Author: Roy
 * @Date: 2020/2/18 10:03
 * @Version: v1.0
 */
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void delete(String key) {
        redisTemplate.delete(key);
    }

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

    public Set<Long> getLongSetValue(String key) {
        Set<Object> members = redisTemplate.opsForSet().members(key);
        Set<Long> longSet = members.stream()
                .filter(e -> (e instanceof Number))// 过滤
                .map(e -> ((Number) e).longValue())// 装换
                .collect(Collectors.toSet());
        return longSet;
    }

    public void saveParam(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void getParam(String key) {
        redisTemplate.opsForValue().get(key);
    }

}
