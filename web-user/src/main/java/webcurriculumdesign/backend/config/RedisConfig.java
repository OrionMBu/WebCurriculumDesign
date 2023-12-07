package webcurriculumdesign.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.password}")
    private String pwd;
    @Value("${spring.redis.database}")
    private Integer database;

    // Redis 连接配置
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(host); // Redis 服务器地址
        redisStandaloneConfiguration.setPort(port); // Redis 服务器端口
        redisStandaloneConfiguration.setPassword(pwd); // Redis 服务器密码
        redisStandaloneConfiguration.setDatabase(database); // Redis 数据库

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    // 配置RedisTemplate
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory());

        // 设置序列化规则
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.string());

        return template;
    }
}
