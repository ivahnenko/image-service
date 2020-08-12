package com.example.demo.config;

import com.example.demo.service.CacheService;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class CacheServiceConfiguration {

    @Value("${cache.name}")
    private String name;
    @Value("${cache.host}")
    private String host;
    @Value("${cache.port}")
    private int port;
    @Value("${cache.ttl}")
    private int ttl;

    private static RedisCacheConfiguration createCacheConfiguration(final long timeoutInSeconds) {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
            redisStandaloneConfiguration.setPort(port);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return createCacheConfiguration(ttl);
    }

    @Bean
    public CacheManager cacheManager() {
        final Map<String, RedisCacheConfiguration> configuration = new HashMap<>();
        configuration.put(name, createCacheConfiguration(ttl));

        return RedisCacheManager
                .builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .withInitialCacheConfigurations(configuration)
                .build();
    }

    @Bean
    public CacheService cacheService() {
        return new CacheService(name, cacheManager());
    }

}
