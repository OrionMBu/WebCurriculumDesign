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

    @RequiredLogin
    @GetMapping("/getLoginData")
    public Result getLoginData(@RequestParam(required = false, defaultValue = "7") int pastDay,
                               @RequestParam(required = false, defaultValue = "false") boolean containToday) {
        return adminService.getLoginData(pastDay, containToday);
    }
}
