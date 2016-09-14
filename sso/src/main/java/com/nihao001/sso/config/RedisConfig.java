package com.nihao001.sso.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

@Configuration
public class RedisConfig implements EnvironmentAware{
    
    private RelaxedPropertyResolver propertyResolver;
    @Bean
    public StringRedisTemplate getStringRedisTemplate(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(propertyResolver.getProperty("pool.max-active")));
        poolConfig.setMaxWaitMillis(Long.parseLong(propertyResolver.getProperty("pool.max-wait")));
        poolConfig.setMaxIdle(Integer.parseInt(propertyResolver.getProperty("pool.max-idle")));
        poolConfig.setMinIdle(Integer.parseInt(propertyResolver.getProperty("pool.min-idle")));
        
        String host = propertyResolver.getProperty("host");
        String password = propertyResolver.getProperty("password");
        password = password.equals("") ? null : password;
        int port = Integer.parseInt(propertyResolver.getProperty("port"));
        int connectionTimeout = Integer.parseInt(propertyResolver.getProperty("timeout"));
        int databaseIndex = Integer.parseInt(propertyResolver.getProperty("database"));
        
        JedisShardInfo shardInfo = new JedisShardInfo(host, port, connectionTimeout);
        shardInfo.setPassword(password);
        
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setShardInfo(shardInfo);
        factory.setDatabase(databaseIndex);
        factory.setUsePool(true);
        
        return new StringRedisTemplate(factory);
    }

    @Override
    public void setEnvironment(Environment arg0) {
        propertyResolver = new RelaxedPropertyResolver(arg0, "spring.redis.");
    }
}
