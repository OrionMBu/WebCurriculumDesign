package webcurriculumdesign.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.data.vo.Result;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @GetMapping("/test")
    public Result test() {
        return Result.success("Yes");
    }
}
