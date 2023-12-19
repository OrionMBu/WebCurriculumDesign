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
     * 更新指定用户信息
     *
     * @param data 用户信息
     */
    @RequiredLogin
    @PatchMapping("/updateAppointedUser")
    public Result updateAppointedUser(@RequestBody Map<String, Object> data) {
        return userService.updateAppointedUser(data);
    }

    /**
     * 获取评价信息
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getToEvaluate")
    public Result getToEvaluate() {
        return userService.getToEvaluate();
    }

    /**
     * 获取被评价信息
     *
     */
    @RequiredLogin(roles = "ALL")
    @GetMapping("/getEvaluated")
    public Result getEvaluated() {
        return userService.getEvaluated();
    }

    /**
     * 评价
     *
     * @param evaluateId 评价id
     * @param moral 思想道德
     * @param attitude 学习态度
     * @param practice 实践能力
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/evaluate")
    public Result evaluate(@RequestParam int evaluateId,
                           @RequestParam(required = false, defaultValue = "-1") int moral,
                           @RequestParam(required = false, defaultValue = "-1") int attitude,
                           @RequestParam(required = false, defaultValue = "-1") int practice) {
        return userService.evaluate(evaluateId, moral, attitude, practice);
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
    public void insertInfo(@RequestParam Integer userId, @RequestParam Integer role, @RequestParam String name) {
        userService.insertInfo(userId, role, name);
    }

    /**
     * 通过用户id获取用户信息
     *
     * @param userId 用户id
     */
    @GetMapping("/getUserData/{userId}")
    public Map<String, Object> getUserData(@PathVariable("userId") Integer userId) throws IllegalAccessException {
        return userService.getUserData(userId);
    }
}
