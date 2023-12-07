package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    /**
     * 获取登录统计
     *
     * @param pastDay 获取前面几天的数据，默认值为7
     * @param containToday 是否包括今天，默认值为false
     */
    @RequiredLogin
    @GetMapping("/getLoginData")
    public Result getLoginData(@RequestParam(required = false, defaultValue = "7") int pastDay,
                               @RequestParam(required = false, defaultValue = "false") boolean containToday) {
        return adminService.getLoginData(pastDay, containToday);
    }

    /**
     * 获取学生人数统计
     *
     */
    @RequiredLogin
    @GetMapping("/getStudentNumber")
    public Result getStudentNumber() {
        return adminService.getStudentNumber();
    }
}
