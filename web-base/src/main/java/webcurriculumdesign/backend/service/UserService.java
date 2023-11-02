package webcurriculumdesign.backend.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import webcurriculumdesign.backend.data.po.User;

@FeignClient(value = "web-user", path = "/user")
public interface UserService {

    @PostMapping("/getUser/{account}")
    User getUser(@PathVariable("account") String account);
}
