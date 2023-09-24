package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequiredLogin
    @GetMapping("/getMainMenu")
    public Result getMainMenu() {
        return Result.success(basicService.getMainMenu());
    }
}
