package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.BasicService;

@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    BasicService basicService;

    /**
     * 获取主目录
     *
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/getMainMenu")
    public Result getMainMenu() {
        return basicService.getMainMenu();
    }

    /**
     * 添加新的目录
     *
     * @param name 目录名称
     * @param role 用户组
     * @param route 目录路径
     * @param parent_id 父节点
     */
    @RequiredLogin(roles = "ALL")
    @PostMapping("/addMenu")
    public Result addMenu(@RequestParam String name, @RequestParam Integer parent_id, @RequestParam String role, @RequestParam String route) {
        return basicService.addMenu(name, parent_id, role, route);
    }
}
