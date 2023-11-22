package webcurriculumdesign.backend.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "web-user", path = "/user")
public interface UserService {
    // 插入用户信息
    @Transactional
    @PostMapping("/insertInfo")
    void insertInfo(@RequestParam("userId") Integer userId, @RequestParam("role") Integer role);

    // 通过用户id获取用户信息
    @GetMapping("/getUserData/{userId}")
    Map<String, Object> getUserData(@PathVariable("userId") Integer userId) throws IllegalAccessException;
}
