package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.scheduling.annotation.Async;
import webcurriculumdesign.backend.data.po.Blog;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    /**
     * 插入新博客
     *
     * @param userId 用户id
     * @param title 标题
     * @param digest 摘要
     * @param image 图片地址
     * @param content 正文
     */
    @Insert("INSERT INTO blog(user_id, title, digest, image, content) VALUES (#{userId}, #{title}, #{digest}, #{image}, #{content})")
    void insertBlog(@Param("userId") Integer userId, @Param("title") String title, @Param("digest") String digest, @Param("image") String image, @Param("content") String content);

    /**
     * 获取个人博客基本信息
     *
     * @param userId 用户id
     */
    @Select("SELECT SUM(browse) AS browse, SUM(`like`) AS likes, COUNT(id) AS blogNumber FROM blog WHERE user_id = #{userId}")
    Map<String, Object> getPersonalBlogInfo(Integer userId);

    /**
     * 获取指定用户的部分（signature, speciality, profile）信息
     *
     * @param userId 用户id
     */
    @Select("SELECT signature, speciality, profile FROM info_student JOIN info_user ON info_student.user_id = info_user.id WHERE user_id = #{userId}")
    Map<String, Object> getPersonalInfo(Integer userId);

    /**
     * 获取指定博客
     *
     * @param blogId 博客id
     */
    @Select("SELECT *, name AS author FROM blog JOIN info_student ON blog.user_id = info_student.user_id WHERE blog.id = #{blogId}")
    Blog getBlog(Integer blogId);

    /**
     * 通过用户id获取博客列表
     *
     * @param userId 用户id
     */
    @Select("SELECT *, name AS author FROM blog JOIN info_student ON blog.user_id = info_student.user_id WHERE blog.user_id = #{userId}")
    List<Blog> getBlogByUserId(Integer userId);

    /**
     * 获取指定博客的评论
     *
     * @param blogId 博客id
     */
    @Select("SELECT comment, name, profile, time FROM blog_comment JOIN info_user ON blog_comment.user_id = info_user.id JOIN info_student ON info_user.id = info_student.user_id WHERE blog_id = #{blogId}")
    List<Map<String, Object>> getComments(Integer blogId);

    /**
     * 获取用户对指定博客的点赞数
     *
     * @param blogId 博客id
     * @param userId 用户id
     * @return 正常为0/1
     */
    @Select("SELECT COUNT(*) FROM blog_like WHERE blog_id = #{blogId} AND user_id = #{userId}")
    int alreadyLiked(@Param("blogId") Integer blogId, @Param("userId") Integer userId);

    /**
     * 插入点赞记录
     *
     * @param blogId 博客id
     * @param userId 用户id
     */
    @Insert("INSERT INTO blog_like VALUES (NULL, #{blogId}, #{userId})")
    void addLike(@Param("blogId") Integer blogId, @Param("userId") Integer userId);

    /**
     * 删除点赞记录
     *
     * @param blogId 博客id
     * @param userId 用户id
     */
    @Delete("DELETE FROM blog_like WHERE blog_id = #{blogId} AND user_id = #{userId}")
    void revokeLike(@Param("blogId") Integer blogId, @Param("userId") Integer userId);

    /**
     * 更新博客点赞
     *
     * @param blogId 博客id
     */
    @Async
    @Update("UPDATE blog SET `like` = (SELECT COUNT(id) FROM blog_like WHERE blog_id = blog.id) WHERE id = #{blogId}")
    void updateLike(@Param("blogId") Integer blogId);

    /**
     * 插入浏览记录，重复则更新
     *
     * @param blogId 博客id
     * @param userId 用户id
     */
    @Insert("INSERT INTO blog_browse(blog_id, user_id) VALUES (#{blogId}, #{userId}) ON DUPLICATE KEY UPDATE times = times + 1, last_time = CURRENT_TIMESTAMP")
    void addBrowse(@Param("blogId") Integer blogId, @Param("userId") Integer userId);

    /**
     * 更新博客浏览量
     *
     * @param blogId 博客id
     */
    @Async
    @Update("UPDATE blog SET browse = (SELECT SUM(times) FROM blog_browse WHERE blog_browse.blog_id = blog.id) WHERE id = #{blogId}")
    void updateBrowse(@Param("blogId") Integer blogId);
}
