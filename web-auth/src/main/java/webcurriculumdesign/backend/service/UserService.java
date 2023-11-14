package webcurriculumdesign.backend.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "web-user", path = "/user")
public interface UserService {
    // 插入用户信息
    @Transactional
    @PostMapping("/insertInfo")
    void insertInfo(@RequestParam Integer userId, @RequestParam Integer role);
}
