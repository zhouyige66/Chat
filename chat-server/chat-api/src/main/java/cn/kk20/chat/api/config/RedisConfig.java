package cn.kk20.chat.api.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: Redis配置
 * @Author: Roy
 * @Date: 2020/2/18 09:12
 * @Version: v1.0
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置key序列化方式，StringRedisSerializer的序列化方式，如果没有设置，key前面会有一段字节
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化方式，序列化为json
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 设置连接池
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
