package webcurriculumdesign.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.annotation.RequiredLogin;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.service.BlogService;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    BlogService blogService;

    /**
     * 上传新博客
     *
     * @param file 预览图片，可无
     * @param title 标题
     * @param digest 摘要，可无
     * @param content 正文
     */
    @RequiredLogin(roles = "STUDENT")
    @PostMapping("/publishBlog")
    public Result publishBlog(@RequestParam(name = "previewImage", required = false) MultipartFile file,
                              @RequestParam String title,
                              @RequestParam(required = false, defaultValue = "") String digest,
                              @RequestParam String content) {
        return blogService.publishBlog(file, title, digest, content);
    }

    /**
     * 删除博客
     *
     * @param blogId 博客id
     */
    @RequiredLogin(roles = "STUDENT")
    @DeleteMapping("/revokeBlog")
    public Result revokeBlog(@RequestParam int blogId) {
        return blogService.revokeBlog(blogId);
    }

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

    /**
     * 获取用户所有博客信息
     *
     * @param userId 用户id，默认为自己
     */
    @RequiredLogin(roles = "STUDENT")
    @GetMapping("/getPersonalBlog")
    public Result getPersonalBlog(@RequestParam(required = false, defaultValue = "0") int userId) {
        return blogService.getPersonalBlog(userId);
    }

    /**
     * 添加点赞
     *
     * @param blogId 博客id
     */
    @RequiredLogin(roles = "STUDENT")
    @PostMapping("/like")
    public Result like(@RequestParam int blogId) {
        return blogService.like(blogId);
    }

    /**
     * 撤销点赞
     *
     * @param blogId 博客id
     */
    @RequiredLogin(roles = "STUDENT")
    @DeleteMapping("/revokeLike")
    public Result revokeLike(@RequestParam int blogId) {
        return blogService.revokeLike(blogId);
    }

    /**
     * 上传博客图片
     *
     * @param file 图片
     */
    @RequiredLogin(roles = "STUDENT")
    @PostMapping("/uploadBlogImage")
    public Result uploadBlogImage(@RequestParam("fileToUpload") MultipartFile file) {
        return blogService.uploadBlogImage(file);
    }
}
