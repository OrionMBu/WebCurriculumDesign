package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;


    //----------------------内部微服务接口----------------------//
    @PostMapping("/getUser/{account}")
    public User getUser(@PathVariable("account") String account) {
        return userService.getUser(account);
    }
}
