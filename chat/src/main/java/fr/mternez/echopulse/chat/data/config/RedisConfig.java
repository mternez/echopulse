package fr.mternez.echopulse.chat.data.config;

import fr.mternez.echopulse.chat.data.model.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    private final RedisProperties properties;

    RedisConfig(final RedisProperties properties) {
        this.properties = properties;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(this.properties.getDatabase());
        config.setHostName(this.properties.getHost());
        config.setPort(this.properties.getPort());
        config.setPassword(this.properties.getPassword());
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Post> redisTemplate() {
        RedisTemplate<String, Post> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
