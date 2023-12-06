package webcurriculumdesign.backend.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 获取过期时间
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    // 获取value
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 存入
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 存入
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }
}
