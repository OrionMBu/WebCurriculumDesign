package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.po.Blog;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.BlogMapper;
import webcurriculumdesign.backend.util.ImageUtil;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.*;

@Service
public class BlogService {
    @Resource
    ImageUtil imageUtil;
    @Resource
    BlogMapper blogMapper;

    // 获取用户博客基础信息
    public Result getPersonalBlogInfo(int userId) {
        // 判断是否查询指定用户信息
        if (userId != 0) CurrentUser.id = userId;

        // 获取个人博客信息
        Map<String, Object> result = blogMapper.getPersonalBlogInfo(CurrentUser.id);

        // 获取个人信息
        result.putAll(blogMapper.getPersonalInfo(CurrentUser.id));
        return Result.success(result);
    }

    // 获取博客信息
    public Result getBlog(int blogId) {
        // 获取博客
        Blog blog = blogMapper.getBlog(blogId);

        Map<String, Object> result;
        try {
            // 转换成Map形式
            result = new HashMap<>(MapUtil.convertObjectToMap(blog));
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        // 判断是否为本人博客, 不是则添加查看记录
        if (!blog.getUserId().equals(CurrentUser.id)) {
            blogMapper.addBrowse(blogId, CurrentUser.id);
            blogMapper.updateBrowse(blogId);
        }
        result.put("selfPublish", blog.getUserId().equals(CurrentUser.id));

        // 判断是否已点赞
        result.put("alreadyLiked", blogMapper.alreadyLiked(blog.getId(), CurrentUser.id) > 0);

        // 获取博客评论
        List<Map<String, Object>> commentList = new ArrayList<>(blogMapper.getComments(blog.getId()));

        result.put("comments", commentList);

        return Result.success(result);
    }

    // 获取用户所有博客信息
    public Result getPersonalBlog(int userId) {
        if (userId != 0) CurrentUser.id = userId;
        return Result.success(blogMapper.getBlogByUserId(CurrentUser.id));
    }

    // 添加点赞
    public Result like(int blogId) {
        try {
            blogMapper.addLike(blogId, CurrentUser.id);
            blogMapper.updateLike(blogId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_BAD_REQUEST, "已经赞过了哦");
        }

    }

    // 更新博客图片
    public Result uploadBlogImage(MultipartFile file) {
        String imagePath;
        try {
            UUID uuid = UUID.randomUUID();
            String shortUUID16 = uuid.toString().substring(0, 16);
            imagePath = imageUtil.uploadImage(file, "blog-image", shortUUID16, false);
            return Result.success(imagePath);
        } catch (Exception e) {
            return Result.error(Response.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
