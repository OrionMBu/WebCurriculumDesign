package webcurriculumdesign.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import webcurriculumdesign.backend.data.po.Blog;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

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
}
