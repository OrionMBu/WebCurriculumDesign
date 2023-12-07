package webcurriculumdesign.backend.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 获取value
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
