package webcurriculumdesign.backend.service;

import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.po.Blog;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.BlogMapper;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    @Resource
    BlogMapper blogMapper;

    public Result getPersonalBlogInfo(int userId) {
        // 判断是否查询指定用户信息
        if (userId != 0) CurrentUser.id = userId;

        // 获取个人博客信息
        Map<String, Object> result = blogMapper.getPersonalBlogInfo(CurrentUser.id);

        // 获取个人信息
        result.putAll(blogMapper.getPersonalInfo(CurrentUser.id));
        return Result.success(result);
    }

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

        // 判断是否为本人博客
        result.put("selfPublish", blog.getUserId().equals(CurrentUser.id));

        // 判断是否已点赞
        result.put("alreadyLiked", blogMapper.alreadyLiked(blog.getId(), CurrentUser.id) > 0);

        // 获取博客评论
        List<Map<String, Object>> commentList = new ArrayList<>(blogMapper.getComments(blog.getId()));

        result.put("comments", commentList);

        return Result.success(result);
    }

    public Result getPersonalBlog(int userId) {
        if (userId != 0) CurrentUser.id = userId;
        return Result.success(blogMapper.getBlogByUserId(CurrentUser.id));
    }
}
