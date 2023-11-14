package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import webcurriculumdesign.backend.service.AuthService;
import webcurriculumdesign.backend.data.vo.Result;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    AuthService authService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    /**
     * 注册接口
     *
     * @param userMail 用户邮箱
     * @param password 密码
     * @param mailVerificationCode 邮箱验证码
     * @param signUpRole 注册时的身份选择（0 -> 管理员，1 -> 教师，2 -> 学生），默认为学生
     */
    @PostMapping("/signUp")
    public Result signUp(@RequestParam String userMail, @RequestParam String password, @RequestParam String mailVerificationCode, @RequestParam(required = false, defaultValue = "2") String signUpRole) {
        return authService.signUp(userMail, password, mailVerificationCode, signUpRole);
    }

    /**
     * 获取邮箱验证码
     *
     * @param userMail 用户邮箱
     * @param flag 是否需要判断邮箱有效性
     *
     */
    @PostMapping("/getMailVerificationCode")
    public Result getMailVerificationCode(@RequestParam String userMail,
                                          @RequestParam(required = false, defaultValue = "true") String flag) {
        return authService.getMailVerificationCode(userMail, flag);
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

    /**
     * 通过邮箱更新用户密码
     *
     * @param verificationCode 邮件验证码
     * @param userMail 用户邮箱
     * @param newPassword 新密码
     */
    @PostMapping("updatePassword")
    public Result updatePassword(@RequestParam String verificationCode, @RequestParam String userMail, @RequestParam String newPassword) {
        return authService.updatePassword(verificationCode, userMail, newPassword);
    }

    /**
     * 更新accessToken
     *
     * @param refreshToken refreshToken
     */
    @PostMapping("/refresh")
    public Result refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
