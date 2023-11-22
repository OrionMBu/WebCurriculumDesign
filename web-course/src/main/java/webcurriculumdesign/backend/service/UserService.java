package webcurriculumdesign.backend.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "web-user", path = "/user")
public interface UserService {
    // 获取用户信息
    @GetMapping("/getUserData/{userId}")
    Map<String, Object> getUserData(@PathVariable("userId") Integer userId) throws IllegalAccessException;
}
