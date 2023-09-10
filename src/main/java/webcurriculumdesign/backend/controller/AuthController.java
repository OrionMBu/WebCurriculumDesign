package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    AuthService authService;

    /**
     * 注册接口
     *
     * @param userMail 用户邮箱
     * @param password 密码
     * @param mailVerificationCode 邮箱验证码
     */
    @PostMapping("/signUp")
    public Result signUp(@RequestParam String userMail, @RequestParam String password, @RequestParam String mailVerificationCode) {
        return authService.signUp(userMail, password, mailVerificationCode);
    }

    /**
     * 获取邮箱验证码
     *
     * @param userMail 用户邮箱
     */
    @PostMapping("/getMailVerificationCode")
    public Result getMailVerificationCode(@RequestParam String userMail) {
        return authService.getMailVerificationCode(userMail);
    }

    /**
     * 通过邮箱（userMail）或昵称(userName)登录
     *
     * @param account 邮箱或昵称
     * @param password 密码
     */
    @PostMapping("/login")
    public Result login(@RequestParam String account, String password) {
        return authService.login(account, password);
    }
}
