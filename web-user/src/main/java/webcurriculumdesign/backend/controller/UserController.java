package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 修改头像
     *
     * @param file 新头像
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/changeProfile")
    public Result changeProfile(@RequestParam("fileToUpload") MultipartFile file) {
        return userService.changeProfile(file);
    }

    /**
     * 获取用户申请结果
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getAuditResult")
    public Result getAuditResult() {
        return userService.getAuditResult();
    }


    //----------------------内部微服务接口----------------------//

    /**
     * 通过邮箱或用户名获取用户信息
     *
     * @param account 邮箱或用户名
     */
    @PostMapping("/getUser/{account}")
    public User getUser(@PathVariable("account") String account) {
        return userService.getUser(account);
    }
}
