package webcurriculumdesign.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webcurriculumdesign.backend.data.constant.CurrentUser;
import webcurriculumdesign.backend.data.constant.Role;
import webcurriculumdesign.backend.data.po.Blog;
import webcurriculumdesign.backend.data.vo.Result;
import webcurriculumdesign.backend.mapper.BlogMapper;
import webcurriculumdesign.backend.util.ImageUtil;
import webcurriculumdesign.backend.util.MapUtil;

import java.util.*;

@Service
public class BlogService {
    @Value("${blog.default-preview-image}")
    public String defaultImage;
    @Resource
    ImageUtil imageUtil;
    @Resource
    BlogMapper blogMapper;

    /**
     * 上传新博客
     *
     * @param file 预览图片
     * @param title 标题
     * @param digest 摘要
     * @param content 正文
     */
    public Result publishBlog(MultipartFile file, String title, String digest, String content) {
        try {
            // 判断是否提供预览图片，没有则使用默认图片
            if (file == null) {
                blogMapper.insertBlog(CurrentUser.id, title, digest, defaultImage, content);
            } else {
                UUID uuid = UUID.randomUUID();
                String shortUUID16 = uuid.toString().substring(0, 16);
                String imagePath = imageUtil.uploadImage(file, "blog-preview-image", shortUUID16, false);
                blogMapper.insertBlog(CurrentUser.id, title, digest, imagePath, content);
            }
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.ok();
    }

    // 更新博客
    public Result updateBlog(MultipartFile file, String title, String digest, String content, int blogId) {
        // 判断是否能修改
        Blog blog = blogMapper.getBlog(blogId);
        if (blog == null) return Result.error(Response.SC_BAD_REQUEST, "修改的博客不存在");
        if (!blog.getUserId().equals(CurrentUser.id) || !CurrentUser.role.equals(Role.ADMIN.role)) return Result.error(Response.SC_FORBIDDEN, "不要修改别人的博客哦");

        try {
            // 判断是否提供预览图片，没有则使用默认图片
            if (file == null) {
                blogMapper.updateBlog(blogId, title, digest, defaultImage, content);
            } else {
                UUID uuid = UUID.randomUUID();
                String shortUUID16 = uuid.toString().substring(0, 16);
                String imagePath = imageUtil.uploadImage(file, "blog-preview-image", shortUUID16, false);
                blogMapper.updateBlog(blogId, title, digest, imagePath, content);
            }
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.ok();
    }

    // 撤销博客
    @Transactional
    public Result revokeBlog(int blogId) {
        // 获取博客信息
        Blog blog = blogMapper.getBlog(blogId);
        if (blog == null) return Result.ok();

        // 只允许管理员和博客作者删除
        if (!blog.getUserId().equals(CurrentUser.id) && !CurrentUser.role.equals(Role.ADMIN.role)) return Result.error(Response.SC_FORBIDDEN, "不要删别人的博客哟");

        // 删除指定博客
        try {
            // 删除浏览记录
            blogMapper.deleteBrowseByBlogId(blogId);

            // 删除点赞记录
            blogMapper.deleteLikeByBlogId(blogId);

            // 删除评论记录
            blogMapper.deleteCommentByBlogId(blogId);

            // 删除博客
            blogMapper.revokeBlog(blogId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }

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
        if (blog == null) return Result.error(Response.SC_BAD_REQUEST, "博客走丢了");

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
        // 判断是否为自己
        boolean flag;
        if (userId != 0) {
            flag = CurrentUser.id == userId;
            CurrentUser.id = userId;
        } else {
            flag = true;
        }

        // 获取所有博客
        List<Blog> blogList = blogMapper.getBlogByUserId(CurrentUser.id);
        List<Map<String, Object>> result = new ArrayList<>();

        // 添加是否为自己发布
        for (Blog blog : blogList) {
            try {
                Map<String, Object> blogMap = MapUtil.convertObjectToMap(blog);
                blogMap.put("selfPublish", flag);
                result.add(blogMap);
            } catch (Exception e) {
                return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
            }
        }
        return Result.success(result);
    }

    // 查询博客
    public Result searchBlog(String title, String digest, String content, String author, int order, boolean isAsc, int page, int pageSize) {
        // 模糊查询
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper
                .like("title", "%" + title + "%")
                .like("title", "%" + digest + "%")
                .like("title", "%" + content + "%")
                .like("name", "%" + author + "%");

        // 排序逻辑
        switch (order) {
            case 1 -> blogQueryWrapper.orderBy(true, isAsc, "like");
            case 2 -> blogQueryWrapper.orderBy(true, isAsc, "browse");
            case 4 -> blogQueryWrapper.orderBy(true, isAsc, "comment");
            default -> blogQueryWrapper.orderBy(true, isAsc, "publish_time");
        }

        // 分页查询
        IPage<Blog> iPage = new Page<>(page, pageSize);
        IPage<Blog> blogIPage = blogMapper.selectBlogList(blogQueryWrapper, iPage);

        List<Map<String, Object>> result = new ArrayList<>();
        List<Blog> blogList = blogIPage.getRecords();

        // 判断是否为自己发布的博客
        try {
            for (Blog blog : blogList) {
                Map<String, Object> blogMap = MapUtil.convertObjectToMap(blog);
                blogMap.put("selfPublish", blog.getUserId().equals(CurrentUser.id));
                result.add(blogMap);
            }
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }


        return Result.success(result);
    }

    // 添加点赞
    public Result like(int blogId) {
        // 获取博客信息
        Blog blog = blogMapper.getBlog(blogId);
        if (blog == null) return Result.error(Response.SC_BAD_REQUEST, "博客走丢了");

        // 判断点赞人是否为自己
        if (blog.getUserId().equals(CurrentUser.id)) return Result.error(Response.SC_BAD_REQUEST, "无法给自己点赞");

        try {
            // 插入点赞记录
            blogMapper.addLike(blogId, CurrentUser.id);
            blogMapper.updateLike(blogId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_BAD_REQUEST, "已经赞过了哦");
        }
    }

    // 撤销点赞
    public Result revokeLike(int blogId) {
        blogMapper.revokeLike(blogId, CurrentUser.id);
        blogMapper.updateLike(blogId);
        return Result.ok();
    }

    // 更新博客评论
    public Result comment(int blogId, String comment) {
        // 获取博客信息
        Blog blog = blogMapper.getBlog(blogId);
        if (blog == null) return Result.error(Response.SC_BAD_REQUEST, "博客走丢了");

        // 插入评论
        try {
            blogMapper.insertComment(blogId, CurrentUser.id, comment);
            blogMapper.updateComment(blogId);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(Response.SC_INTERNAL_SERVER_ERROR, "错误");
        }
    }

    // 删除评论
    public Result deleteComment(int commentId) {
        Map<String, Object> commentMap = blogMapper.getUserIdByCommentId(commentId);
        if (commentMap.get("user_id").equals(CurrentUser.id) || blogMapper.getAuthorIdByCommentId(commentId) == CurrentUser.id || CurrentUser.role.equals(Role.ADMIN.role)) {
            blogMapper.deleteCommentByCommentId(commentId);
            blogMapper.updateComment((Integer) commentMap.get("blog_id"));
            return Result.ok();
        } else {
            return Result.error(Response.SC_FORBIDDEN, "不能删除这个评论哦");
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
