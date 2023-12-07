package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.BlogService;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    BlogService blogService;

    /**
     * 获取用户博客基础信息
     *
     * @param userId 用户id，非必须，默认为自己
     */
    @RequiredLogin(roles = "STUDENT")
    @GetMapping("/getPersonalBlogInfo")
    public Result getPersonalBlogInfo(@RequestParam(required = false, defaultValue = "0") int userId) {
        return blogService.getPersonalBlogInfo(userId);
    }

    /**
     * 获取博客信息
     *
     * @param blogId 博客id
     */
    @RequiredLogin(roles = "STUDENT")
    @GetMapping("/getBlog")
    public Result getBlog(@RequestParam int blogId) {
        return blogService.getBlog(blogId);
    }

}
