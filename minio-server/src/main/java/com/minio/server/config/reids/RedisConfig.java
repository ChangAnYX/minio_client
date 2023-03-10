package com.minio.server.config.reids;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @program: minio
 * @description：redis配置类
 * @author: bin
 * @create: 2022-03-09 17:25
 **/
@Configuration
public class RedisConfig {
    @Bean(name="redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //value序列化
        template.setKeySerializer(new StringRedisSerializer());
        //value序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //value haspmap序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        //key haspmap序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
