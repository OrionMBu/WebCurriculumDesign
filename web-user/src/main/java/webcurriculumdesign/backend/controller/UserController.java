package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.po.Admin;
import webcurriculumdesign.backend.data.po.User;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService<Admin> userService;

    /**
     * 修改头像
     *
     * @param file 新头像
     */
    @RequiredLogin(roles = "ALL")
    @PatchMapping("/changeProfile")
    public Result changeProfile(@RequestParam("fileToUpload") MultipartFile file) {
        return userService.changeProfile(file);
    }

    /**
     * 获取用户列表
     *
     * @param type 用户类型（0 -> ADMIN，1 -> 教师，2 -> 学生，其他 -> 全部）
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getUserList")
    public Result getUserList(@RequestParam(required = false, defaultValue = "3") int type) {
        return userService.getUserList(type);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam(required = false) String userId) {
        return Result.success(userService.getUserInfo(userId == null ? String.valueOf(CurrentUser.id) : userId));
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

    /**
     * 更新指定用户信息
     *
     * @param data 用户信息
     */
    @RequiredLogin
    @PatchMapping("/updateAppointedUser")
    public Result updateAppointedUser(@RequestBody Map<String, Object> data) {
        return userService.updateAppointedUser(data);
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

    /**
     * 插入信息（初始化）
     *
     * @param userId 用户id
     * @param role 用户身份（1 -> 教师，2 -> 学生）
     */
    @Transactional
    @PostMapping("/insertInfo")
    public void insertInfo(@RequestParam Integer userId, @RequestParam Integer role) {
        userService.insertInfo(userId, role);
    }

    /**
     * 通过用户id获取用户名
     *
     * @param userId 用户id
     * @return userName 用户真实姓名
     */
    @GetMapping("/getUserName/{userId}")
    public String getUserName(@PathVariable("userId") String userId) {
        return userService.getUserInfo(userId).getName();
    }
}
