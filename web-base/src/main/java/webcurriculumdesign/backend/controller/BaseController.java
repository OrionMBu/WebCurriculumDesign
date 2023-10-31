package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.BaseService;

@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    BaseService baseService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    /**
     * 获取主目录
     *
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/getMainMenu")
    public Result getMainMenu() {
        return baseService.getMainMenu();
    }

    /**
     * 添加新的目录
     *
     * @param name 目录名称
     * @param role 用户组
     * @param route 目录路径
     * @param parent_id 父节点
     */
    @PostMapping("/addMenu")
    public Result addMenu(@RequestParam String name, @RequestParam Integer parent_id, @RequestParam String role, @RequestParam String route) {
        return baseService.addMenu(name, parent_id, role, route);
    }
}

